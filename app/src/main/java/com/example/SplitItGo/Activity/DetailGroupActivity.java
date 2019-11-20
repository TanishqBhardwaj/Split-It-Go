package com.example.SplitItGo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.SplitItGo.Adapter.ExpensesAdapter;
import com.example.SplitItGo.Interface.JsonPlaceHolderApi;
import com.example.SplitItGo.Model.ExpensesResponse;
import com.example.SplitItGo.Model.GetUsersResponse;
import com.example.SplitItGo.Model.GroupItem;
import com.example.SplitItGo.Model.GroupResponse;
import com.example.SplitItGo.R;
import com.example.SplitItGo.Utils.PreferenceUtils;
import com.example.SplitItGo.Utils.RetrofitInstance;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailGroupActivity extends AppCompatActivity {

    public static GroupResponse groupResponse;
    JsonPlaceHolderApi jsonPlaceHolderApi;
    PreferenceUtils pref;
    ExpensesAdapter expensesAdapter;
    RecyclerView recyclerView;
    ArrayList<ExpensesResponse> allExpenses;
    List<ExpensesResponse> temp;
    FloatingActionButton fabAddExpense;
    ArrayList<GroupItem> groupItemArrayList;
    TextView textViewGroupName;
    ProgressBar progressBar;


    public DetailGroupActivity() {

    }

    public DetailGroupActivity(GroupResponse groupResponse) {
        this.groupResponse = groupResponse;
        new AddExpenseActivity(groupResponse);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_group);
        pref = new PreferenceUtils(DetailGroupActivity.this);
        allExpenses = new ArrayList<>();
        groupItemArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewExpenses);
        fabAddExpense = findViewById(R.id.fab_add_expense);
        textViewGroupName = findViewById(R.id.textViewGroupName);
        progressBar = findViewById(R.id.progressBarActivityDetail);
        progressBar.setVisibility(View.VISIBLE);
        textViewGroupName.setText(groupResponse.getName());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailGroupActivity.this,
                LinearLayoutManager.VERTICAL, false));
        expensesAdapter = new ExpensesAdapter(DetailGroupActivity.this, allExpenses, groupItemArrayList);
        expensesAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(expensesAdapter);
        initList();

        fabAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailGroupActivity.this, AddExpenseActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void initList() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        jsonPlaceHolderApi = RetrofitInstance.getRetrofit(okHttpClient).create(JsonPlaceHolderApi.class);
        String token = "JWT " + pref.getToken();

        Call<GetUsersResponse> callUsers = jsonPlaceHolderApi.getGroupMembers(token);
        callUsers.enqueue(new Callback<GetUsersResponse>() {
            @Override
            public void onResponse(Call<GetUsersResponse> call, Response<GetUsersResponse> response) {

                if(!response.isSuccessful()) {
                    Toast.makeText(DetailGroupActivity.this, "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                GetUsersResponse posts = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";

                ArrayList<GetUsersResponse.UserData> allMemberList = new ArrayList<>(posts.getResults());
                for(int i=0; i<allMemberList.size(); i++) {

                    if(!allMemberList.get(i).getUsername().equals("admin")) {
                        if(!pref.getKeyUsername().equals(allMemberList.get(i).getUsername())) {
                            String url = allMemberList.get(i).getUrl();
                            String user_id="";
                            int count=0;
                            for(int j=0; j<url.length(); j++) {
                                if(url.charAt(j) == '/') {
                                    count++;
                                }
                                if(count == 4) {
                                    if(url.charAt(j) != '/') {
                                        user_id = user_id + url.charAt(j);
                                    }
                                }
                            }
                            groupItemArrayList.add(new GroupItem(allMemberList.get(i).getUsername(), R.drawable.ic_home, user_id));
                        }
                    }
                }
//                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(DetailGroupActivity.this, content, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<GetUsersResponse> call, Throwable t) {
                Toast.makeText(DetailGroupActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        Call<List<ExpensesResponse>> call = jsonPlaceHolderApi.getExpenses(String.valueOf(groupResponse.getId()), token);
        temp = new ArrayList<>();
        call.enqueue(new Callback<List<ExpensesResponse>>() {
            @Override
            public void onResponse(Call<List<ExpensesResponse>> call, Response<List<ExpensesResponse>> response) {
                try {
                    if (!response.isSuccessful()) {
                        Toast.makeText(DetailGroupActivity.this, "Code: " + response.code(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    List<ExpensesResponse> expensesList = response.body();
                    for(ExpensesResponse expensesResponse: expensesList) {
                        int id = expensesResponse.getId();
                        String bill_name = expensesResponse.getBill_name();
                        String description = expensesResponse.getDescription();
                        Double amount = expensesResponse.getAmount();
                        String created_at = expensesResponse.getCreated_at();
                        int group_name = expensesResponse.getGroup_name();
                        int payer = expensesResponse.getPayer();
                        ExpensesResponse expensesResponse1 = new ExpensesResponse(id, bill_name, description, amount,
                                created_at, group_name, payer);
                        temp.add(expensesResponse1);
                    }
                    expensesAdapter = new ExpensesAdapter(DetailGroupActivity.this, temp, groupItemArrayList);
                    progressBar.setVisibility(View.GONE);
//                    expensesAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(expensesAdapter);
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<ExpensesResponse>> call, Throwable t) {

            }
        });
    }
}