package com.example.SplitItGo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.SplitItGo.Model.GroupResponse;
import com.example.SplitItGo.R;

import java.util.ArrayList;
import java.util.List;

public class AllGroupAdapter extends RecyclerView.Adapter<AllGroupAdapter.ModelViewHolder> {

        Context context;
        ArrayList<GroupResponse> groupList;

        public AllGroupAdapter(Context context, ArrayList<GroupResponse> groupItemArrayList) {
            this.context = context;
            groupList = groupItemArrayList;
        }

        @NonNull
        @Override
        public ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(context).inflate(R.layout.card_view, parent, false);
                return new ModelViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ModelViewHolder holder, int position) {
                GroupResponse groupItem = groupList.get(position);
                String username = groupItem.getName();
                holder.textView.setText(username);
        }

        @Override
        public int getItemCount() {
                return groupList.size();
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
