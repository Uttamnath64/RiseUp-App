package com.lit.riseup;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListHomeCardAdapter extends RecyclerView.Adapter<ListHomeCardAdapter.ViewHoleder> {
    ArrayList<ListHomeCardData> listCardData;
    Context context;
    public ListHomeCardAdapter(ArrayList<ListHomeCardData> listCardData, Context context) {
        this.listCardData = listCardData;
        this.context = context;
    }

    @NonNull
    @Override
    public ListHomeCardAdapter.ViewHoleder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_home_card,parent,false);
        return new ViewHoleder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHomeCardAdapter.ViewHoleder holder, int position) {
        ListHomeCardData listHomeCardData = listCardData.get(position);
        holder.Title.setText(listHomeCardData.getTitle());
        holder.Description.setText(listHomeCardData.getDescription());
        holder.ChannelNameAndViewAndTime.setText(listHomeCardData.getChannelNameAndViewAndTime());

        Picasso.with(context).load(listHomeCardData.getImage()).into(holder.Image);
        Picasso.with(context).load(listHomeCardData.getChannelLogo()).into(holder.ChannelLogo);

        holder.itemView.setOnClickListener(View -> {
            Intent intent = new Intent(context.getApplicationContext(),Article.class);
            intent.putExtra("Url", listHomeCardData.getUrl());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return listCardData.size();
    }

    public class ViewHoleder extends RecyclerView.ViewHolder{
        ImageView Image, ChannelLogo;
        TextView Title, Description,ChannelNameAndViewAndTime;
        public ViewHoleder(@NonNull View itemView) {
            super(itemView);
            Image = itemView.findViewById(R.id.IdListImage);
            ChannelLogo = itemView.findViewById(R.id.IdListChannelLogo);
            Title = itemView.findViewById(R.id.IdListTitle);
            Description = itemView.findViewById(R.id.IdListDescription);
            ChannelNameAndViewAndTime = itemView.findViewById(R.id.IdListChannelName);
        }
    }
}
