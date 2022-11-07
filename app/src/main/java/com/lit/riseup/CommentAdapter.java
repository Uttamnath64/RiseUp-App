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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHoleder> {

    ArrayList<CommentModal> listData;
    Context context;
    public CommentAdapter(ArrayList<CommentModal> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHoleder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.comment,parent,false);
        return new ViewHoleder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHoleder holder, int position) {
        CommentModal commentModal = listData.get(position);
        holder.Channel_Name.setText(commentModal.getChannel_name());
        holder.Date.setText(commentModal.getComment_date());
        holder.Comment.setText(commentModal.getComment());

        Picasso.with(context).load(commentModal.getChannel_logo()).into(holder.Channel_Logo);


        holder.Channel_Logo.setOnClickListener(View->{
            gotoChannel(commentModal.getChannel_Url());
        });
    }

    private void gotoChannel(String channel_url) {
        context.startActivity(new Intent(context,Channel.class).putExtra("Url",channel_url));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHoleder extends RecyclerView.ViewHolder {
        ImageFilterView Channel_Logo;
        TextView Channel_Name, Date, Comment;
        public ViewHoleder(@NonNull View itemView) {
            super(itemView);
            Channel_Logo = itemView.findViewById(R.id.channel_Image);
            Channel_Name = itemView.findViewById(R.id.channel_Name);
            Date = itemView.findViewById(R.id.comment_data);
            Comment = itemView.findViewById(R.id.comment);
        }
    }
}
