package com.example.SplitItGo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SplitItGo.Model.GroupItem;
import com.example.SplitItGo.R;

import java.util.ArrayList;

public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.ModelViewHolder> {

    Context context;
    ArrayList<GroupItem> groupMemberList;

    public GroupMemberAdapter(Context context, ArrayList<GroupItem> groupItemArrayList) {
        this.context = context;
        groupMemberList = groupItemArrayList;
    }

    @NonNull
    @Override
    public ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_view, parent, false);
        return new ModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModelViewHolder holder, int position) {
        GroupItem groupItem = groupMemberList.get(position);
        String username = groupItem.getmGroupMemberName();

        holder.textView.setText(username);
    }

    @Override
    public int getItemCount() {
        return groupMemberList.size();
    }

    public class ModelViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView;

        public ModelViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.imageViewCardView);
            textView = view.findViewById(R.id.textViewCardView);
        }
    }
}
