package com.lit.riseup;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ArchanaData> {

    ArrayList<AchievementModal> JoinedList;
    Context rootView;

    public AchievementAdapter(ArrayList<AchievementModal> JoinedList, Context rootView) {
        this.JoinedList = JoinedList;
        this.rootView = rootView;
    }

    @NonNull
    @Override
    public AchievementAdapter.ArchanaData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.achievement_list,parent,false);
        return new ArchanaData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementAdapter.ArchanaData holder, int position) {

        AchievementModal achievementModal = JoinedList.get(position);

        holder.Icon.setText(String.valueOf(achievementModal.getAchievement_name().charAt(0)));
        holder.Title.setText(achievementModal.getAchievement_name());
        holder.Description.setText(achievementModal.getDescription());
        if (Integer.parseInt(achievementModal.getStatus()) == 1){
            holder.Status.setText("Remaining");
            holder.Status.setTextColor(ContextCompat.getColor(rootView,R.color.A_S_1T));
            holder.Status.setBackgroundColor(ContextCompat.getColor(rootView, R.color.A_S_1B));
        }else if(Integer.parseInt(achievementModal.getStatus()) == 2){
            holder.Status.setText("Collected");
            holder.Status.setTextColor(ContextCompat.getColor(rootView,R.color.A_S_2T));
            holder.Status.setBackgroundColor(ContextCompat.getColor(rootView,R.color.A_S_2B));
        }else{
            holder.Status.setText("Collect");
            holder.Status.setTextColor(ContextCompat.getColor(rootView,R.color.white));
            holder.Status.setBackgroundColor(ContextCompat.getColor(rootView,R.color.App_Color));
        }

        holder.itemView.setOnClickListener(View->{
            Intent intent = new Intent(rootView.getApplicationContext(),AchievementView.class);
            intent.putExtra("Id", achievementModal.getId());
            rootView.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return JoinedList.size();
    }

    public class ArchanaData extends RecyclerView.ViewHolder {
        TextView Icon, Title, Description, Status;
        public ArchanaData(@NonNull View itemView) {
            super(itemView);
            Icon = itemView.findViewById(R.id.icon);
            Title = itemView.findViewById(R.id.title);
            Description = itemView.findViewById(R.id.description);
            Status = itemView.findViewById(R.id.status);
        }
    }
}
