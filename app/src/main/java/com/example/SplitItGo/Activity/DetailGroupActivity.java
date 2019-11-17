package com.example.SplitItGo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.SplitItGo.Adapter.ExpensesAdapter;
import com.example.SplitItGo.Interface.JsonPlaceHolderApi;
import com.example.SplitItGo.Model.ExpensesResponse;
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

    public GroupResponse groupResponse;
    JsonPlaceHolderApi jsonPlaceHolderApi;
    PreferenceUtils pref;
    ExpensesAdapter expensesAdapter;
    RecyclerView recyclerView;
    ArrayList<ExpensesResponse> allExpenses;
    List<ExpensesResponse> temp;
    FloatingActionButton fabAddExpense;
    static int id;

    public DetailGroupActivity() {

    }

    public DetailGroupActivity(GroupResponse groupResponse) {
        this.groupResponse = groupResponse;
        id = groupResponse.getId();
        new AddExpenseActivity(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_group);
        pref = new PreferenceUtils(DetailGroupActivity.this);
        allExpenses = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewExpenses);
        fabAddExpense = findViewById(R.id.fab_add_expense);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailGroupActivity.this,
                LinearLayoutManager.VERTICAL, false));
        expensesAdapter = new ExpensesAdapter(DetailGroupActivity.this, allExpenses);
        recyclerView.setAdapter(expensesAdapter);
        initList();

        fabAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailGroupActivity.this, AddExpenseActivity.class);
                startActivity(intent);
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
        Call<List<ExpensesResponse>> call = jsonPlaceHolderApi.getExpenses(String.valueOf(id), token);
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
                    expensesAdapter = new ExpensesAdapter(DetailGroupActivity.this, temp);
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