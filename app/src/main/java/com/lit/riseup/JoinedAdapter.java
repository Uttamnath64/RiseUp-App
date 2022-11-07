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

public class JoinedAdapter extends RecyclerView.Adapter<JoinedAdapter.ArchanaData> {

    ArrayList<JoinedModal> card;
    Context rootView;
    Conn conn = new Conn();
    String WEB_URL = conn.getUrl();

    public JoinedAdapter(ArrayList<JoinedModal> card, Context rootView) {
        this.card =card;
        this.rootView = rootView;
    }

    @NonNull
    @Override
    public JoinedAdapter.ArchanaData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.channel_list,parent,false);
        return new ArchanaData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JoinedAdapter.ArchanaData holder, int position) {
        JoinedModal joinedModal = card.get(position);
        holder.Channel_Name.setText(joinedModal.getChannel_name());
        holder.Channel_Joiner.setText(joinedModal.getChannel_joiner()+" Joiner");
        Picasso.with(rootView).load(WEB_URL+joinedModal.getChannel_image()).into(holder.Channel_Logo);
        Picasso.with(rootView).load(WEB_URL+joinedModal.getArticle_image()).into(holder.Channel_Article);

        holder.itemView.setOnClickListener(View->{
            Intent intent = new Intent(rootView,Channel.class);
            intent.putExtra("Url",joinedModal.getChannel_url());
            rootView.startActivity(intent);
        });

        holder.Channel_Article.setOnClickListener(View->{
            Intent intent = new Intent(rootView,Article.class);
            intent.putExtra("Url",joinedModal.getArticle_url());
            rootView.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return card.size();
    }

    public class ArchanaData extends RecyclerView.ViewHolder {
        TextView Channel_Name, Channel_Joiner;
        ImageView Channel_Logo, Channel_Article;
        public ArchanaData(@NonNull View itemView) {
            super(itemView);
            Channel_Name = itemView.findViewById(R.id.channelName);
            Channel_Joiner = itemView.findViewById(R.id.channelJoiner);
            Channel_Logo = itemView.findViewById(R.id.channelLogo);
            Channel_Article = itemView.findViewById(R.id.channelArticle);
        }
    }
}
