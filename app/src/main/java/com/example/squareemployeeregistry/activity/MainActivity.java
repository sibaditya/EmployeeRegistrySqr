package com.example.squareemployeeregistry.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.squareemployeeregistry.R;
import com.example.squareemployeeregistry.adapter.EmployeeListAdapter;
import com.example.squareemployeeregistry.db.Employee;
import com.example.squareemployeeregistry.utils.ResultCode;
import com.example.squareemployeeregistry.viewmodel.MainActivityViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import static com.example.squareemployeeregistry.activity.SplashActivity.RESULT_CODE_BUNDLE_VALUE;

public class MainActivity extends AppCompatActivity
        implements EmployeeListAdapter.EmployeeItemClickListener,
        EmployeeListAdapter.EmployeePhoneNumberClickListener {

  private RecyclerView mEmployeeRecyclerView;
  private ShimmerFrameLayout mShimmerFrameLayout;
  private LinearLayout mNoInternetConnectionLayout;
  private LinearLayout mEmptyListLayout;
  private ResultCode mResultCode;

  private boolean doubleBackToExitPressedOnce = false;
  private EmployeeListAdapter mEmployeeListAdapter;
  private List<Employee> mEmployeeList = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mEmployeeRecyclerView = findViewById(R.id.employee_recylerview);
    mShimmerFrameLayout = findViewById(R.id.shimmer_view_container);
    mNoInternetConnectionLayout = findViewById(R.id.no_internet_connection_layout);
    mEmptyListLayout = findViewById(R.id.empty_list_layout);
    mResultCode = (ResultCode) getIntent().getSerializableExtra(RESULT_CODE_BUNDLE_VALUE);
    switch (mResultCode.getId()) {
      case 0:
        mEmployeeRecyclerView.setVisibility(View.VISIBLE);
        mShimmerFrameLayout.setVisibility(View.VISIBLE);
        mNoInternetConnectionLayout.setVisibility(View.GONE);
        mEmptyListLayout.setVisibility(View.GONE);
        mShimmerFrameLayout.startShimmerAnimation();
      break;

      case 1:
        mNoInternetConnectionLayout.setVisibility(View.VISIBLE);
        mEmployeeRecyclerView.setVisibility(View.GONE);
        mShimmerFrameLayout.setVisibility(View.GONE);
        mEmptyListLayout.setVisibility(View.GONE);
        LottieAnimationView noInternetConnection = findViewById(R.id.no_connection_animation);
        noInternetConnection.playAnimation();
        break;

      case 2:

        break;

      case 3:
        mEmptyListLayout.setVisibility(View.VISIBLE);
        mNoInternetConnectionLayout.setVisibility(View.GONE);
        mEmployeeRecyclerView.setVisibility(View.GONE);
        mShimmerFrameLayout.setVisibility(View.GONE);
        LottieAnimationView emptyListAnimation = findViewById(R.id.empty_list);
        TextView emptyListErrorMessage = findViewById(R.id.empty_list_error_message);
        emptyListAnimation.playAnimation();
        emptyListErrorMessage.setText(mResultCode.getMsg());
        break;

      case 4:

        break;
    }
    setUpEmployeeRecyclerView();
    MainActivityViewModel mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
    mainActivityViewModel.init(this);
    mainActivityViewModel.getEmployeeList();
    mainActivityViewModel.getEmployeeLiveData().observe(this, new Observer<List<Employee>>() {
      @Override
      public void onChanged(List<Employee> employeeList) {
        mEmployeeListAdapter.setEmployeeList(employeeList);
        mShimmerFrameLayout.stopShimmerAnimation();
        mShimmerFrameLayout.setVisibility(View.GONE);
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    mShimmerFrameLayout.startShimmerAnimation();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mShimmerFrameLayout.stopShimmerAnimation();
  }

  @Override
  public void onBackPressed() {
    if (doubleBackToExitPressedOnce) {
      super.onBackPressed();
      return;
    }

    this.doubleBackToExitPressedOnce = true;
    Toast.makeText(this, getString(R.string.press_back_twice), Toast.LENGTH_SHORT).show();

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        doubleBackToExitPressedOnce=false;
      }
    }, 2500);
  }

  private void setUpEmployeeRecyclerView() {
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    mEmployeeRecyclerView.setLayoutManager(linearLayoutManager);
    mEmployeeListAdapter = new EmployeeListAdapter(this,
            this,
            this,
            mEmployeeList);
    mEmployeeRecyclerView.setAdapter(mEmployeeListAdapter);
  }

  @Override
  public void onEmployeeItemClickListener(Employee employee) {

  }

  @Override
  public void onEmployeePhoneNumberClickListener(Long phoneNumber) {
    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_DIAL); // Action for what intent called for
    intent.setData(Uri.parse("tel: " + phoneNumber)); // Data with intent respective action on intent
    startActivity(intent);
  }
}
