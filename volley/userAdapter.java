package com.jaydip.volley;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class userAdapter extends RecyclerView.Adapter<userAdapter.userHolder> {

    MainActivity mainActivity;
    List<userModel> allUsersList;

    public userAdapter(MainActivity mainActivity,List<userModel> allUsersList){
        this.mainActivity = mainActivity;
        this.allUsersList = allUsersList;
    }

    @NonNull
    @Override
    public userAdapter.userHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new userHolder(LayoutInflater.from(mainActivity).inflate(R.layout.item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull userAdapter.userHolder holder, int position) {
        holder.itemTxt.setText(allUsersList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return allUsersList.size();
    }

    public class userHolder extends RecyclerView.ViewHolder {

        TextView itemTxt;

        public userHolder(@NonNull View itemView) {
            super(itemView);
            itemTxt = itemView.findViewById(R.id.itemTxt);
        }
    }
}
