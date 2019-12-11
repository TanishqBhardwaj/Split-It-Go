package com.example.SplitItGo.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.SplitItGo.Adapter.FriendsAdapter;
import com.example.SplitItGo.Interface.JsonPlaceHolderApi;
import com.example.SplitItGo.Model.GetUsersResponse;
import com.example.SplitItGo.Model.GroupItem;
import com.example.SplitItGo.R;
import com.example.SplitItGo.Utils.PreferenceUtils;
import com.example.SplitItGo.Utils.RetrofitInstance;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    FriendsAdapter friendsAdapter;
    ArrayList<GroupItem> friendList;
    ArrayList<GroupItem> groupItemArrayList;
    View view;

    JsonPlaceHolderApi jsonPlaceHolderApi;
    PreferenceUtils pref;

    ProgressBar progressBar;
    FloatingActionButton fabAddContact;

    String phoneNumber="";

    public final static  int RESULT_PICK_CONTACT = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        if(!isConnected(getActivity())){ buildDialog(getActivity()).show();}
        else {
            pref = new PreferenceUtils(getContext());

            friendList = new ArrayList<>();
            groupItemArrayList = new ArrayList<>();
            recyclerView = view.findViewById(R.id.recyclerViewHome);
            progressBar = view.findViewById(R.id.progressBarHome);
            fabAddContact = view.findViewById(R.id.fab_add_contact);
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false));
            friendsAdapter = new FriendsAdapter(getContext(), friendList);
            recyclerView.setAdapter(friendsAdapter);

            initList();

            fabAddContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
                }
            });
        }

        return view;
    }

    public boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Dismiss.");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        return builder;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    Cursor cursor = null;
                    try {
                        String phoneNo = null ;
                        int count=0;
                        Uri uri = data.getData();
                        cursor = getActivity().getContentResolver().query(uri, null, null,
                                null, null);
                        cursor.moveToFirst();
                        int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        phoneNo = cursor.getString(phoneIndex);
                        if(phoneNo.length()>10) {
                            count = phoneNo.length() - 10 -1;
                        }
                        for(int i=count; i<phoneNo.length(); i++) {
                            if(phoneNo.charAt(i)!=' ') {
                                phoneNumber += phoneNo.charAt(i);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    jsonPlaceHolderApi = RetrofitInstance.getRetrofit().create(JsonPlaceHolderApi.class);
                    final String token = "JWT " + pref.getToken();

                    Call<GetUsersResponse> call = jsonPlaceHolderApi.getGroupMembers(token);
                    call.enqueue(new Callback<GetUsersResponse>() {
                        @Override
                        public void onResponse(Call<GetUsersResponse> call, Response<GetUsersResponse> response) {

                            try {
                                if (!response.isSuccessful()) {
//                                    Toast.makeText(getContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                                    return;
                                }
                                GetUsersResponse posts = response.body();

                                String content = "";
                                content += "Code: " + response.code() + "\n";

                                ArrayList<GetUsersResponse.UserData> allMemberList = new ArrayList<>(posts.getResults());
                                for (int i = 0; i < allMemberList.size(); i++) {

                                    if (!allMemberList.get(i).getUsername().equals("admin")) {
                                        if (!pref.getKeyUsername().equals(allMemberList.get(i).getUsername())) {
                                            String url = allMemberList.get(i).getUrl();
                                            String user_id = "";
                                            int count = 0;
                                            for (int j = 0; j < url.length(); j++) {
                                                if (url.charAt(j) == '/') {
                                                    count++;
                                                }
                                                if (count == 4) {
                                                    if (url.charAt(j) != '/') {
                                                        user_id = user_id + url.charAt(j);
                                                    }
                                                }
                                            }

                                            Log.d(phoneNumber, "onResponse: ");

                                            if (phoneNumber.equals(allMemberList.get(i).getPhone_number())) {
                                                Call<String> call3 = jsonPlaceHolderApi.addFriend(user_id, token);
                                                call3.enqueue(new Callback<String>() {
                                                    @Override
                                                    public void onResponse(Call<String> call, Response<String> response) {
                                                        if (!response.isSuccessful()) {
//                                                            Toast.makeText(getContext(), "Code: " + response.code(),
//                                                                    Toast.LENGTH_LONG).show();
                                                            return;
                                                        }
                                                        String content = response.body();

                                                        Toast.makeText(getContext(), content, Toast.LENGTH_LONG).show();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<String> call, Throwable t) {
                                                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                            groupItemArrayList.add(new GroupItem(allMemberList.get(i).getUsername(), R.drawable.ic_home, user_id));
                                        }
                                    }
                                }
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<GetUsersResponse> call, Throwable t) {
                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
            }
    }
        else {
//            Log.e("MainActivity", "Failed to pick contact");
            Toast.makeText(getActivity(), "Failed to pick contact!", Toast.LENGTH_LONG).show();
        }
    }

    public void initList() {

        jsonPlaceHolderApi = RetrofitInstance.getRetrofit().create(JsonPlaceHolderApi.class);
        final String token = "JWT " + pref.getToken();

        Call<GetUsersResponse> call = jsonPlaceHolderApi.getGroupMembers(token);
        call.enqueue(new Callback<GetUsersResponse>() {
            @Override
            public void onResponse(Call<GetUsersResponse> call, Response<GetUsersResponse> response) {

                try {
                    if(!response.isSuccessful()) {
//                        Toast.makeText(getContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
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
                    Call<ArrayList<Integer>> call2 = jsonPlaceHolderApi.getFriendList(token);
                    call2.enqueue(new Callback<ArrayList<Integer>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Integer>> call, Response<ArrayList<Integer>> response) {
                            try {
                                if(response.code()==429 ) {
                                    Toast.makeText(getActivity(), "No response from server", Toast.LENGTH_LONG).show();
                                }
                                if(!response.isSuccessful()) {
//                                    Toast.makeText(getContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                                    return;
                                }
                                ArrayList<Integer> friendResponse = response.body();
                                if(friendResponse!=null) {
                                    for(int i=0; i<friendResponse.size(); i++) {
                                        for(int j=0; j<groupItemArrayList.size(); j++) {
                                            if(friendResponse.get(i) == Integer.parseInt(groupItemArrayList.get(j).getUser_id())) {
                                                friendList.add(new GroupItem(groupItemArrayList.get(j).getmGroupMemberName(),
                                                        R.drawable.ic_home, groupItemArrayList.get(j).getUser_id()));
                                            }
                                        }
                                    }
                                }
                                friendsAdapter = new FriendsAdapter(getContext(), friendList);
                                recyclerView.setAdapter(friendsAdapter);
                                progressBar.setVisibility(View.GONE);
//                                Toast.makeText(getContext(), String.valueOf(content), Toast.LENGTH_LONG).show();
                            }
                            catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Integer>> call, Throwable t) {
                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
               catch (NullPointerException e) {
                    e.printStackTrace();
               }
            }

            @Override
            public void onFailure(Call<GetUsersResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }
}
