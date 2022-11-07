package com.lit.riseup;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewDataHolder> {

    ArrayList<SearchModal> list;
    Context rootView;
    Conn conn = new Conn();
    String WEB_URL = conn.getUrl();

    public SearchAdapter(ArrayList<SearchModal> list, Context rootView) {
        this.list = list;
        this.rootView = rootView;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.search_list,parent,false);
        return new ViewDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewDataHolder holder, int position) {

        SearchModal searchModal = list.get(position);

        holder.Title.setText(searchModal.getSearch_title());
        holder.Description.setText(searchModal.getSearch_description());
        holder.Channel_Name.setText(searchModal.getChannel_name());

        Picasso.with(rootView).load(WEB_URL+searchModal.getSearch_image()).into(holder.Image);
        Picasso.with(rootView).load(WEB_URL+searchModal.getSearch_channel_image()).into(holder.Channel_Image);

        holder.itemView.setOnClickListener(View->{
            Intent intent = new Intent(rootView.getApplicationContext(),Article.class);
            intent.putExtra("Url",searchModal.getSearch_url());
            rootView.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewDataHolder extends RecyclerView.ViewHolder {
        ImageFilterView Image,Channel_Image;
        TextView Title,Description,Channel_Name;
        public ViewDataHolder(@NonNull View itemView) {
            super(itemView);
            Image = itemView.findViewById(R.id.image);
            Channel_Image = itemView.findViewById(R.id.channel_logo);
            Title = itemView.findViewById(R.id.title);
            Description = itemView.findViewById(R.id.description);
            Channel_Name = itemView.findViewById(R.id.IdListChannelName);
        }
    }
}
