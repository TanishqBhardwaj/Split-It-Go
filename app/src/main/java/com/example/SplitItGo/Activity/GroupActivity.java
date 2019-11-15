package com.example.SplitItGo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.SplitItGo.Adapter.AllMemberAdapter;
import com.example.SplitItGo.Adapter.GroupMemberAdapter;
import com.example.SplitItGo.Interface.JsonPlaceHolderApi;
import com.example.SplitItGo.Model.GetUsersResponse;
import com.example.SplitItGo.Model.GroupItem;
import com.example.SplitItGo.Model.GroupResponse;
import com.example.SplitItGo.Model.PostGroup;
import com.example.SplitItGo.R;
import com.example.SplitItGo.Utils.PreferenceUtils;
import com.example.SplitItGo.Utils.RetrofitInstance;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupActivity extends AppCompatActivity {

    private ArrayList<GroupItem> groupItemArrayList;
    private ArrayList<GroupItem> selectedMemberList;
    private ArrayList<Integer> groupMemberUserId = new ArrayList<>();
    private AllMemberAdapter allMemberAdapter;

    TextView textView;
    EditText editText;

    String groupName;

    JsonPlaceHolderApi jsonPlaceHolderApi;
    PreferenceUtils pref;
    RecyclerView recyclerView;
    GroupMemberAdapter groupMemberAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        pref = new PreferenceUtils(GroupActivity.this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(GroupActivity.this,
                LinearLayoutManager.VERTICAL, false));
        selectedMemberList = new ArrayList<>();
        selectedMemberList.add(new GroupItem(pref.getKeyUsername(), R.drawable.ic_home, pref.getKeyUserId()));
        groupMemberUserId.add(Integer.parseInt(pref.getKeyUserId()));
        groupMemberAdapter = new GroupMemberAdapter(GroupActivity.this, selectedMemberList);
        recyclerView.setAdapter(groupMemberAdapter);

        textView = findViewById(R.id.textViewSave);
        editText = findViewById(R.id.editTextGroupName);

        initList();

        Spinner spinner = findViewById(R.id.spinner);
        allMemberAdapter = new AllMemberAdapter(this, groupItemArrayList);
        spinner.setAdapter(allMemberAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                GroupItem groupItem = (GroupItem) adapterView.getItemAtPosition(i);
                String clickedGroupMember = groupItem.getmGroupMemberName();
                String clickedUserId = groupItem.getUser_id();
                Toast.makeText(GroupActivity.this, clickedGroupMember, Toast.LENGTH_LONG).show();
                if(!clickedGroupMember.equals("Add Members")) {
                    selectedMemberList.add(new GroupItem(clickedGroupMember, R.drawable.ic_home, clickedUserId));
                    groupMemberAdapter = new GroupMemberAdapter(GroupActivity.this, selectedMemberList);
                    recyclerView.setAdapter(groupMemberAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ImageView imageView = findViewById(R.id.imageViewBackButton);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupName = editText.getText().toString();
                createGroup();
                finish();
            }
        });
    }

    private void initList() {
        groupItemArrayList = new ArrayList<>();
        groupItemArrayList.add(new GroupItem("Add Members", R.drawable.ic_home, "0"));

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        jsonPlaceHolderApi = RetrofitInstance.getRetrofit(okHttpClient).create(JsonPlaceHolderApi.class);

        Call<GetUsersResponse> call = jsonPlaceHolderApi.getGroupMembers();
        call.enqueue(new Callback<GetUsersResponse>() {
            @Override
            public void onResponse(Call<GetUsersResponse> call, Response<GetUsersResponse> response) {

                if(!response.isSuccessful()) {
                    Toast.makeText(GroupActivity.this, "Code: " + response.code(), Toast.LENGTH_LONG).show();
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
                            Log.d("User_id", "onResponse:" + user_id);
                            groupItemArrayList.add(new GroupItem(allMemberList.get(i).getUsername(), R.drawable.ic_home, user_id));
                        }
                    }
                }
                Toast.makeText(GroupActivity.this, content, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<GetUsersResponse> call, Throwable t) {
                Toast.makeText(GroupActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createGroup() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        jsonPlaceHolderApi = RetrofitInstance.getRetrofit(okHttpClient).create(JsonPlaceHolderApi.class);

        for(int i=0; i<groupItemArrayList.size(); i++) {
            for(int j=0; j<selectedMemberList.size(); j++) {
                if(selectedMemberList.get(j).getmGroupMemberName()
                        .equals(groupItemArrayList.get(i).getmGroupMemberName())) {
                    groupMemberUserId.add(Integer.parseInt(groupItemArrayList.get(i).getUser_id()));
                    break;
                }
            }
        }

        PostGroup postGroup = new PostGroup(groupName, pref.getKeyUserId(), groupMemberUserId, "TRIP");
            String token = "Bearer " + pref.getToken();
            Call<GroupResponse> call = jsonPlaceHolderApi.createGroup(postGroup, token);
            call.enqueue(new Callback<GroupResponse>() {
                @Override
                public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                    if(!response.isSuccessful()) {
                        Toast.makeText(GroupActivity.this, "Code: " + response.code(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    GroupResponse posts = response.body();
                    String content = posts.getDetails();
                    Toast.makeText(GroupActivity.this, content, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<GroupResponse> call, Throwable t) {

                }
            });
    }
}