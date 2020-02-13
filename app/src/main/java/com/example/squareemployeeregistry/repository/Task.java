package com.example.squareemployeeregistry.repository;

import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Task <Progress, Result> implements Runnable{

  public static final String TAG = Task.class.getSimpleName();

  public static final int DEFAULT_PRIORITY = 0;

  private static final int MESSAGE_POST_STARTED = 1;
  private static final int MESSAGE_POST_PROGRESS = 2;
  private static final int MESSAGE_POST_RESULT = 3;

  private static final Handler sMainHandler =
          new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @SuppressWarnings({"unchecked", "RawUseOfParameterizedType"})
            @Override
            public boolean handleMessage(android.os.Message msg) {
              MessageObject msgObject = (MessageObject) msg.obj;

              switch (msg.what){
                case MESSAGE_POST_STARTED:
                  msgObject.mTask.onStarted();
                  return true;
                case MESSAGE_POST_PROGRESS:
                  msgObject.mTask.onProgressUpdate(msgObject.mData);
                  return true;
                case MESSAGE_POST_RESULT:
                  msgObject.mTask.finish(msgObject.mData);
                  return true;
              }

              return false;
            }
          });

  private static class MessageObject<Data> {
    final Task mTask;
    final Data mData;

    public MessageObject(Task task, Data data){
      mTask = task;
      mData = data;
    }
  }

  public enum Status {
    PENDING,
    RUNNING,
    FINISHED
  }

  private final int mPriority;
  private final boolean mAllowOverride;
  private final String mTag;

  private volatile Status mStatus;

  private final AtomicBoolean mCancelled;
  private final AtomicBoolean mTaskInvoked;
  private final AtomicBoolean mSkipped;

  private final Callable<Result> mWorker;
  private final FutureTask<Result> mMainTask;

  private Task mSubTask;

  protected Task(){
    this(DEFAULT_PRIORITY, false, null);
  }

  protected Task(int priority, boolean allowOverride, String tag){

    mPriority = priority;
    mAllowOverride = allowOverride;
    mTag = tag;

    mStatus = Status.PENDING;
    mCancelled = new AtomicBoolean();
    mTaskInvoked = new AtomicBoolean();
    mSkipped = new AtomicBoolean();

    mWorker = new Callable<Result>() {
      @Override
      public Result call() throws Exception {
        mTaskInvoked.set(true);

        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        return postResult(doInBackground());
      }
    };

    // We override done function to make sure onGetUserLoyaltyProgramsTaskCancelled() is called if
    // cancel() is called early
    // Source: http://stackoverflow.com/questions/25322651/asynctask-source-code-questions
    mMainTask = new FutureTask<Result>(mWorker){
      @Override
      protected void done() {
        try {
          postResultIfNotInvoked(get());
        } catch (InterruptedException e) {
          e.printStackTrace();
        } catch (ExecutionException e) {
          throw new RuntimeException(
                  "An error occurred while executing doInBackground()",
                  e.getCause());
        } catch (CancellationException e) {
          postResultIfNotInvoked(null);
        }
      }
    };
  }

  @Override
  public void run() {
    synchronized (this) {
      // We allow run() to be called after cancel().  Just do nothing
      if (isCancelled()) {
        Log.w(TAG, getClass().getName() +
                " will not run because it has been cancelled");
        return;
      }

      if (mStatus != Status.PENDING) {
        switch (mStatus) {
          case RUNNING:
            throw new IllegalStateException("Cannot execute task:"
                    + " the task is already running.");
          case FINISHED:
            throw new IllegalStateException("Cannot execute task:"
                    + " the task has already been executed "
                    + "(a task can be executed only once)");
        }
      }

      setStatus(Status.RUNNING);
      postStart();
    }

    mMainTask.run();
  }

  /** Get result synchronously and will wait if necessary.  Use with caution. */
  public final Result get() throws InterruptedException, ExecutionException {
    return mMainTask.get();
  }

  public final synchronized boolean cancel(boolean mayInterruptIfRunning) {
    mCancelled.set(true);

    if (mSubTask != null)
      mSubTask.cancel(mayInterruptIfRunning);

    // Cancel this task
    return mMainTask.cancel(mayInterruptIfRunning);
  }

  public final synchronized boolean skip(){
    if (mStatus != Status.PENDING){
      Log.w(TAG, "Cannot skip task: current state " + mStatus);
      return false;
    }

    mSkipped.set(true);

    // Trigger cancel to trigger finish
    cancel(true);

    // Skip sub tasks
    if (mSubTask != null)
      mSubTask.skip();

    return true;
  }

  private synchronized void setStatus(Status status){
    mStatus = status;
  }

  public final synchronized boolean isCancelled() {
    return mCancelled.get();
  }

  public final synchronized boolean isSkipped(){
    return mSkipped.get();
  }

  public final int getPriority(){
    return mPriority;
  }

  public final boolean getAllowOverride(){
    return mAllowOverride;
  }

  public final String getTag(){
    return mTag;
  }

  public final synchronized Status getStatus() {
    return mStatus;
  }

  // Must be implemented
  protected abstract Result doInBackground();

  // Optionally implemented
  protected void onStarted(){}
  protected void onProgressUpdate(Progress progress){}
  protected void onFinished(Result result){}
  protected void onCancelled(Task task){}
  protected void onSkipped(){}

  /** Run and get the result of sub task. */
  protected final Object getSubTaskResult(Task subTask){
    synchronized (this) {
      if (isCancelled())
        return null;

      // Add to sub task list
      mSubTask = subTask;
    }

    // Get result from this subtask right away
    try {
      // Must run first!
      subTask.run();

      // Then get the result!
      Object result = subTask.get();
      synchronized (this) {
        mSubTask = null;
      }

      return result;
    } catch (InterruptedException e){
      e.printStackTrace();
    } catch (ExecutionException e){
      throw new RuntimeException(
              "An error occurred while executing sub task",
              e.getCause());
    } catch (CancellationException e){
      e.printStackTrace();
    }

    return null;
  }

  private void postStart(){
    if (isCancelled())
      return;

    sMainHandler.obtainMessage(MESSAGE_POST_STARTED,
            new MessageObject<Void>(this, null)).sendToTarget();
  }

  /** Call this function if you wish to post progress to UI thread. */
  protected final void postProgress(Progress progress){
    if (isCancelled())
      return;

    sMainHandler.obtainMessage(MESSAGE_POST_PROGRESS,
            new MessageObject<>(this, progress)).sendToTarget();
  }

  private Result postResult(Result result){
    sMainHandler.obtainMessage(MESSAGE_POST_RESULT,
            new MessageObject<>(this, result)).sendToTarget();

    return result;
  }

  private void postResultIfNotInvoked(Result result) {
    final boolean wasTaskInvoked = mTaskInvoked.get();
    if (!wasTaskInvoked) {
      postResult(result);
    }
  }

  private void finish(Result result) {
    Asserts.isOnMainThread();

    if (isSkipped())
      onSkipped();
    else if(isCancelled())
      onCancelled(this);
    else
      onFinished(result);

    setStatus(Status.FINISHED);
  }
}