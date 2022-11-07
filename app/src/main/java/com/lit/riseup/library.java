package com.lit.riseup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

public class library extends Fragment {

    View rootView;

    SharedPreference sharedPreference = new SharedPreference();

    TextView ChannelName, ChannelJoiner, Description, Articles, Likes;
    ImageFilterView img;
    Conn conn = new Conn();
    String WEB_URL = conn.getUrl();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_library, container, false);

        ChannelName = rootView.findViewById(R.id.channelName);
        ChannelJoiner = rootView.findViewById(R.id.channelJoiner);
        Description = rootView.findViewById(R.id.description);
        Articles = rootView.findViewById(R.id.articles);
        Likes = rootView.findViewById(R.id.likes);
        img = rootView.findViewById(R.id.ChannelImage);

        Bundle bundle = getArguments();
            ChannelName.setText(bundle.getString("Account_Data_ChannelName",""));
            ChannelJoiner.setText(bundle.getString("Account_Data_ChannelJoiner",""));
            Description.setText(bundle.getString("Account_Data_Channel_Description",""));
            Articles.setText(bundle.getString("Account_Data_Channel_Articles",""));
            Likes.setText(bundle.getString("Account_Data_Channel_Likes",""));
            Picasso.with(getContext()).load(WEB_URL+"/images/"+bundle.getString("Account_Data_Channel_Image","")).into(img);

        //logout button click
        rootView.findViewById(R.id.logoutButton).setOnClickListener(View->{
            sharedPreference.removeURL(getContext());
            Intent intent = new Intent(getContext(),SignIn.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });


        rootView.findViewById(R.id.history_Button).setOnClickListener(View->{
            Intent intent = new Intent(getContext(),ArticleView.class);
            intent.putExtra("Title","History");
            intent.putExtra("Url","/Android/getLibraryHistory");
            startActivity(intent);
        });

        rootView.findViewById(R.id.article_Button).setOnClickListener(View->{
            Intent intent = new Intent(getContext(),ArticleView.class);
            intent.putExtra("Title","Your Article");
            intent.putExtra("Url","/Android/getLibraryArticle");
            startActivity(intent);
        });


        rootView.findViewById(R.id.favorite_Button).setOnClickListener(View->{
            Intent intent = new Intent(getContext(),ArticleView.class);
            intent.putExtra("Title","Favorite");
            intent.putExtra("Url","/Android/getLibraryLiked");
            startActivity(intent);
        });

        rootView.findViewById(R.id.bookmark_Button).setOnClickListener(View->{
            Intent intent = new Intent(getContext(),ArticleView.class);
            intent.putExtra("Title","Bookmark");
            intent.putExtra("Url","/Android/getLibraryBookmark");
            startActivity(intent);
        });
        return rootView;
    }
}