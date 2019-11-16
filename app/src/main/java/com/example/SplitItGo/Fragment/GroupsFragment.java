package com.example.SplitItGo.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.SplitItGo.Activity.GroupActivity;
import com.example.SplitItGo.Adapter.AllGroupAdapter;
import com.example.SplitItGo.Interface.JsonPlaceHolderApi;
import com.example.SplitItGo.Model.GroupResponse;
import com.example.SplitItGo.R;
import com.example.SplitItGo.Utils.PreferenceUtils;
import com.example.SplitItGo.Utils.RetrofitInstance;
import com.example.SplitItGo.Utils.ViewAnimation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupsFragment extends Fragment {

    private FloatingActionButton fabAdd;
    private FloatingActionButton fabAddGroup;
    private RecyclerView recyclerView;
    private boolean isRotate = false;

    private ArrayList<GroupResponse> allGroups;
    private AllGroupAdapter allGroupAdapter;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private PreferenceUtils pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        pref = new PreferenceUtils(getContext());
        allGroups = new ArrayList<>();
        fabAdd = view.findViewById(R.id.fab_add);
        fabAddGroup = view.findViewById(R.id.fab_add_group);
        recyclerView = view.findViewById(R.id.recyclerViewGroup);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        allGroupAdapter = new AllGroupAdapter(getContext(), allGroups);
        recyclerView.setAdapter(allGroupAdapter);

        initList();



        ViewAnimation.init(view.findViewById(R.id.fab_add_group));

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRotate = ViewAnimation.rotateFab(view, !isRotate);
                if(isRotate){
                    ViewAnimation.showIn(fabAddGroup);
                }else{
                    ViewAnimation.showOut(fabAddGroup);
                }
            }
        });

        fabAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), GroupActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void initList() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        jsonPlaceHolderApi = RetrofitInstance.getRetrofit(okHttpClient).create(JsonPlaceHolderApi.class);
        String token = "JWT " + pref.getToken();
        Call<List<GroupResponse>> call = jsonPlaceHolderApi.getGroups(token);
        call.enqueue(new Callback<List<GroupResponse>>() {
            @Override
            public void onResponse(Call<List<GroupResponse>> call, Response<List<GroupResponse>> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                List<GroupResponse> groupResponse = response.body();
                for(GroupResponse groupResponse1: groupResponse) {
                    int id = groupResponse1.getId();
                    String name = groupResponse1.getName();
                    int admin = groupResponse1.getAdmin();
                    ArrayList<Integer> users = groupResponse1.getUsers();
                    String type = groupResponse1.getType();
                    String created_at = groupResponse1.getCreated_at();
                    Log.d(name, "onResponse: ");
                    GroupResponse groupResponse2 = new GroupResponse(id, name, admin, users, type, created_at);
                    allGroups.add(groupResponse2);
                }
                allGroupAdapter = new AllGroupAdapter(getContext(), allGroups);
                recyclerView.setAdapter(allGroupAdapter);
            }

            @Override
            public void onFailure(Call<List<GroupResponse>> call, Throwable t) {

            }
        });
    }
}