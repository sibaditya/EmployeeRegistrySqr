package com.example.squareemployeeregistry.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.squareemployeeregistry.R;
import com.example.squareemployeeregistry.db.Employee;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.EmployeeViewHolder> {

  public interface EmployeeItemClickListener {
    void onEmployeeItemClickListener(Employee employee);
  }

  public interface EmployeePhoneNumberClickListener {
    void onEmployeePhoneNumberClickListener(Long phoneNumber);
  }

  private Context mContext;
  private List<Employee> mEmployeeList;
  private EmployeeItemClickListener mEmployeeItemClickListener;
  private EmployeePhoneNumberClickListener mEmployeePhoneNumberClickListener;

  public EmployeeListAdapter(Context context,
                             EmployeeItemClickListener employeeItemClickListener,
                             EmployeePhoneNumberClickListener employeePhoneNumberClickListener,
                             List<Employee> employeeList) {
    mContext = context;
    mEmployeeItemClickListener = employeeItemClickListener;
    mEmployeePhoneNumberClickListener = employeePhoneNumberClickListener;
    mEmployeeList = employeeList;
  }

  public void setEmployeeList(List<Employee> employeeList) {
    mEmployeeList.clear();
    mEmployeeList.addAll(employeeList);
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.layout_employee_list, parent, false);
    return new EmployeeViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
    Employee employee = mEmployeeList.get(position);
    holder.bind(employee);
  }

  @Override
  public int getItemCount() {
    return mEmployeeList.size();
  }

  class EmployeeViewHolder extends RecyclerView.ViewHolder {

    private CardView mCardView;
    private ImageView mEmployeeImageView;
    private TextView mEmployeeName;
    private TextView mEmployeePhoneNumber;
    private TextView mEmployeeTeam;

    public EmployeeViewHolder(@NonNull View itemView) {
      super(itemView);
      mCardView = itemView.findViewById(R.id.card_view);
      mEmployeeImageView = itemView.findViewById(R.id.employee_image);
      mEmployeeName = itemView.findViewById(R.id.employee_name);
      mEmployeePhoneNumber = itemView.findViewById(R.id.employee_phone_number);
      mEmployeeTeam = itemView.findViewById(R.id.employee_team);
    }

    public void bind(final Employee employee) {
      String imageUrl = employee.getPhotoUrlSmall();
      if (!TextUtils.isEmpty(imageUrl)) {
        Picasso.with(mContext).load(imageUrl)
                .placeholder(R.drawable.user_icon)
                .into(mEmployeeImageView);
      }
      mEmployeeName.setText(employee.getFullName());
      mEmployeePhoneNumber.setText(String.valueOf(employee.getPhoneNumber()));
      String team = mContext.getString(R.string.team,employee.getTeam());
      mEmployeeTeam.setText(team);
      mCardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mEmployeeItemClickListener.onEmployeeItemClickListener(employee);
        }
      });

      mEmployeePhoneNumber.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mEmployeePhoneNumberClickListener.onEmployeePhoneNumberClickListener(employee.getPhoneNumber());
        }
      });
    }
  }
}
