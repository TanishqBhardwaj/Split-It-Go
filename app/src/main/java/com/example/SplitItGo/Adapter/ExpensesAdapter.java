package com.example.SplitItGo.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.SplitItGo.Model.ExpensesResponse;
import com.example.SplitItGo.Model.GroupItem;
import com.example.SplitItGo.Model.GroupResponse;
import com.example.SplitItGo.R;
import com.example.SplitItGo.Utils.PreferenceUtils;

import org.w3c.dom.Text;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ModelViewHolder>{

    Context context;
    static List<ExpensesResponse> groupMemberList;
    static List<ExpensesResponse> transactionsList;
    View view;

    static String groupName;

    static ArrayList<GroupItem> groupItemArrayList;
    PreferenceUtils pref;

    public ExpensesAdapter(String name) {
        groupName = name;
    }

    public ExpensesAdapter(Context context, List<ExpensesResponse> groupItemArrayList, ArrayList<GroupItem> allUsersList) {
        this.context = context;
        groupMemberList = groupItemArrayList;
        transactionsList = groupItemArrayList;
        this.groupItemArrayList = allUsersList;
    }

    @NonNull
    @Override
    public ExpensesAdapter.ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        pref = new PreferenceUtils(context);
        view = LayoutInflater.from(context).inflate(R.layout.card_view_expenses, parent, false);
        return new ExpensesAdapter.ModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesAdapter.ModelViewHolder holder, int position) {
        ExpensesResponse expensesResponse = groupMemberList.get(position);
        String time = expensesResponse.getCreated_at();
        String day="", month="", year="";
        int count = 0;
        for(int i=0; i<time.length(); i++) {
            if(time.charAt(i)!='-') {
                if(count==0) {
                    year = year + time.charAt(i);
                }
                if(count == 1) {
                    month = month + time.charAt(i);
                }
                if(count == 2) {
                    if(time.charAt(i)=='T') {
                        break;
                    }
                    day = day + time.charAt(i);
                }
            }
            else {
                count ++;
            }
        }
        String temp = getMonth(month);
        temp = day + " " + temp + " " + year ;
        if(position == 0) {
            holder.imageView.setVisibility(View.VISIBLE);
            holder.textView.setVisibility(View.VISIBLE);
            holder.textView.setText(temp);
            double expense = expensesResponse.getAmount();
            int payerId = expensesResponse.getPayer();
            if(payerId == Integer.parseInt(pref.getKeyUserId())) {
                holder.textViewExpenseMoney.setText("You paid Rs" + expense);
            }
            else {
                for(int i=0; i<groupItemArrayList.size(); i++) {
                    if(payerId == Integer.parseInt(groupItemArrayList.get(i).getUser_id())) {
                        holder.textViewExpenseMoney.setText(groupItemArrayList.get(i).getmGroupMemberName() + " paid Rs" + expense);
                    }
                }
            }
            holder.textViewExpensePaid.setText(expensesResponse.getDescription());
            holder.textViewExpenseDescription.setText(expensesResponse.getBill_name());
        }
        else {
            ExpensesResponse expensesResponse1 = groupMemberList.get(position-1);
            String time1 = expensesResponse1.getCreated_at();
            String day1="", month1="", year1="";
            int count1 = 0;
            for(int i=0; i<time1.length(); i++) {
                if(time1.charAt(i)!='-') {
                    if(count1==0) {
                        year1 = year1 + time1.charAt(i);
                    }
                    if(count1 == 1) {
                        month1 = month1 + time.charAt(i);
                    }
                    if(count1 == 2) {
                        if(time1.charAt(i)=='T') {
                            break;
                        }
                        day1 = day1 + time1.charAt(i);
                    }
                }
                else {
                    count1 ++;
                }
            }

            if(year.equals(year1) && month.equals(month1) && day.equals(day1)) {
                holder.imageView.setVisibility(View.GONE);
                holder.textView.setVisibility(View.GONE);
            }
            else {
                holder.imageView.setVisibility(View.VISIBLE);
                holder.textView.setVisibility(View.VISIBLE);
                holder.textView.setText(temp);
            }
            double expense = expensesResponse.getAmount();
            int payerId = expensesResponse.getPayer();
            if(payerId == Integer.parseInt(pref.getKeyUserId())) {
                holder.textViewExpenseMoney.setText("You paid Rs" + expense);
            }
            else {
                for(int i=0; i<groupItemArrayList.size(); i++) {
                    if(payerId == Integer.parseInt(groupItemArrayList.get(i).getUser_id())) {
                        holder.textViewExpenseMoney.setText(groupItemArrayList.get(i).getmGroupMemberName() + " paid Rs" + expense);
                    }
                }
            }
            holder.textViewExpensePaid.setText(expensesResponse.getDescription());
            holder.textViewExpenseDescription.setText(expensesResponse.getBill_name());
        }
//        holder.textViewGroupName.setText(expensesResponse.get);
    }

    @Override
    public int getItemCount() {
        return groupMemberList.size();
    }

    public String getMonth(String string) {
        if(string.equals("1")) {
            return "January";
        }
        if(string.equals("2")) {
            return "February";
        }
        if(string.equals("3")) {
            return "March";
        }
        if(string.equals("4")) {
            return "April";
        }
        if(string.equals("5")) {
            return "May";
        }
        if(string.equals("6")) {
            return "June";
        }
        if(string.equals("7")) {
            return "July";
        }
        if(string.equals("8")) {
            return "August";
        }
        if(string.equals("9")) {
            return "September";
        }
        if(string.equals("10")) {
            return "October";
        }
        if(string.equals("11")) {
            return "November";
        }
        if(string.equals("12")) {
            return "December";
        }
        return null;
    }

    public class ModelViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageViewExpense;
        public TextView textViewExpenseMoney;
        public TextView textViewExpensePaid;
        public TextView textViewExpenseDescription;
        public ImageView imageView;
        public TextView textView;
        public TextView textViewGroupName;

        public ModelViewHolder(@NonNull View view) {
            super(view);
            imageViewExpense = view.findViewById(R.id.imageViewExpense);
            textViewExpenseMoney = view.findViewById(R.id.textViewExpenseMoney);
            textViewExpensePaid = view.findViewById(R.id.textViewExpensePaid);
            textViewExpenseDescription = view.findViewById(R.id.textViewExpenseDescription);
            imageView = view.findViewById(R.id.imageViewTime);
            textView = view.findViewById(R.id.textViewTime);
            textViewGroupName = view.findViewById(R.id.textViewGroupName);
        }
    }
}
