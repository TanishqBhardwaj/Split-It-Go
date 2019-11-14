package com.example.SplitItGo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.SplitItGo.Model.GroupItem;
import com.example.SplitItGo.R;

import java.util.ArrayList;

public class AllMemberAdapter extends ArrayAdapter<GroupItem> {

    public AllMemberAdapter(Context context, ArrayList<GroupItem> allGroupList) {
        super(context, 0, allGroupList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.group_spinner_row, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageViewGroupMemberImage);
        TextView textView = convertView.findViewById(R.id.textViewGroupMemberName);

        GroupItem groupItem = getItem(position);

        if(groupItem != null) {
            imageView.setImageResource(groupItem.getImageView());
            textView.setText(groupItem.getmGroupMemberName());
        }
        return convertView;
    }
}