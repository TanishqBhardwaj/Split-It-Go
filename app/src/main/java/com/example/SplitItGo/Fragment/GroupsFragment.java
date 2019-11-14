package com.example.SplitItGo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.SplitItGo.Activity.GroupActivity;
import com.example.SplitItGo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GroupsFragment extends Fragment {

    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        fab = view.findViewById(R.id.floatingActionButton2);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), GroupActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}