package com.lit.riseup;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Article extends AppCompatActivity {


    Conn conn = new Conn();
    String WEB_URL = conn.getUrl();

    ProgressDialog progressDialog;

    CardView backBtn,channelBtn,likeBtn,dislikeBtn,shareBtn,bookmarkBtn,Comment_Btn;
    ImageView ChannelLogo;
    LinearLayout ArticleImage;
    TextView Title,ArticleAbout,ChannelName,ChannelJoiner,likeCount,DislikeCount;

    ImageView like, dislike, bookmark;

    TextView LT, DT, BT;

    WebView webView;

    TextView ChannelJoinBtn;

    String Channel_Url;

    JSONParser jsonParser = new JSONParser();
    SharedPreference sharedPreference = new SharedPreference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        ArticleImage = findViewById(R.id.articleImage);
        ChannelLogo = findViewById(R.id.channelLogo);

        backBtn = findViewById(R.id.articleBackBtn);
        channelBtn = findViewById(R.id.channelButton);
        likeBtn = findViewById(R.id.likeButton);
        dislikeBtn = findViewById(R.id.dislikeButton);
        shareBtn = findViewById(R.id.shareButton);
        bookmarkBtn = findViewById(R.id.bookmarkButton);

        Title = findViewById(R.id.articleTitle);
        ArticleAbout = findViewById(R.id.articleAbout);
        ChannelName = findViewById(R.id.channelName);
        ChannelJoiner = findViewById(R.id.channelJoiner);
        likeCount = findViewById(R.id.text_like);
        DislikeCount = findViewById(R.id.text_dislike);

        webView = findViewById(R.id.web);

        like = findViewById(R.id.id_IC_LIKE);
         dislike = findViewById(R.id.id_IC_DISLIKE);
         bookmark = findViewById(R.id.id_IC_BOOKMARK);

         LT = findViewById(R.id.text_like);
         DT = findViewById(R.id.text_dislike);
         BT = findViewById(R.id.bookmark_text);

        Comment_Btn = findViewById(R.id.comment);

        webView.setVerticalScrollBarEnabled(true);

         ChannelJoinBtn = findViewById(R.id.channelJoinButton);

        Intent intent = getIntent();

        GetArticleData getArticleData = new GetArticleData();
        getArticleData.execute(intent.getStringExtra("Url"),sharedPreference.getURL(getApplication()));

        findViewById(R.id.articleBackBtn).setOnClickListener(View ->{
            finish();
        });

        findViewById(R.id.articleReportBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View alertCustomDialog = getLayoutInflater().from(Article.this).inflate(R.layout.report_dailog,null);

                AlertDialog.Builder alert = new AlertDialog.Builder(Article.this);

                alert.setView(alertCustomDialog);

                CardView cancel = alertCustomDialog.findViewById(R.id.cancel_button);

                RadioGroup group = alertCustomDialog.findViewById(R.id.radio_report);

                CardView Btn = alertCustomDialog.findViewById(R.id.report_now);

                final AlertDialog dialog = alert.create();

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                cancel.setOnClickListener(View->{
                    dialog.dismiss();
                });

                Btn.setOnClickListener(View->{
                    if(group.getCheckedRadioButtonId() != -1) {
                        int getId = group.getCheckedRadioButtonId();
                        RadioButton RB = alertCustomDialog.findViewById(getId);
                        String reportVal = RB.getText().toString();
                        String reportText = "";
                        if (reportVal.equals("Sexual content")) {
                            reportText = "R1";
                        } else if (reportVal.equals("Violent or repulsive content")) {
                            reportText = "R2";
                        } else if (reportVal.equals("Hateful or abusive content")) {
                            reportText = "R3";
                        } else if (reportVal.equals("Harmful or dangerous acts")) {
                            reportText = "R4";
                        } else if (reportVal.equals("Spam or misleading")) {
                            reportText = "R5";
                        }
                        new ReportArticle(reportText,intent.getStringExtra("Url"),Channel_Url).execute();
                        dialog.dismiss();
                    }else{
                        Toast.makeText(Article.this, "Select any article report!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        // share button click
        shareBtn.setOnClickListener(View->{
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_SUBJECT,"Share Article");
            share.putExtra(Intent.EXTRA_TEXT,WEB_URL+"/feed/article/"+intent.getStringExtra("Url"));
            startActivity(share);
        });

        //commetes click
        Comment_Btn.setOnClickListener(View->{
            startActivity(new Intent(Article.this,Comments.class).putExtra("Url",intent.getStringExtra("Url")));
        });

        //like button
        findViewById(R.id.likeButton).setOnClickListener(View->{
            if(sharedPreference.getURL(Article.this).length() == 0){
                startActivity(new Intent(Article.this, SignIn.class));
            }else{
                new DoActionOnArticle("1",intent.getStringExtra("Url"),"/Android/likeAndDislike/").execute();
            }
        });

        //Dislike button click
        findViewById(R.id.dislikeButton).setOnClickListener(View->{
            if(sharedPreference.getURL(Article.this).length() == 0){
                startActivity(new Intent(Article.this, SignIn.class));
            }else{
                new DoActionOnArticle("0",intent.getStringExtra("Url"),"/Android/likeAndDislike/").execute();
            }
        });

        //Bookmark button click
        findViewById(R.id.bookmarkButton).setOnClickListener(View->{
            if(sharedPreference.getURL(Article.this).length() == 0){
                startActivity(new Intent(Article.this, SignIn.class));
            }else{
                new DoActionOnArticle("1",intent.getStringExtra("Url"),"/Android/articlsBookmar/").execute();
            }
        });

        // channel join and left button click
        ChannelJoinBtn.setOnClickListener(View->{
            if(sharedPreference.getURL(Article.this).length() == 0){
                startActivity(new Intent(Article.this, SignIn.class));
            }else{
                new DoActionOnArticle("1",Channel_Url,"/Android/channelJoinAndLeft/").execute();
            }
        });

        // channel button click
        findViewById(R.id.channelButton).setOnClickListener(View->{
            Intent i = new Intent(Article.this,Channel.class);
            i.putExtra("Url", Channel_Url);
            startActivity(i);
        });


    }
    @SuppressLint("StaticFieldLeak")
    private class DoActionOnArticle extends AsyncTask<String, String, JSONObject>{

        String Url, Post_Url;
        String Fun_Value;

        public DoActionOnArticle(String Fun_Value, String Post_Url, String Url) {
            this.Fun_Value = Fun_Value;
            this.Url = Url;
            this.Post_Url = Post_Url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressLint("ResourceAsColor")
        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                if (result != null) {
                    if (Integer.parseInt(result.getString("success")) == 1) {

                        if (result.getString("msg").trim().equals("LIKE")){
                            like.setColorFilter(ContextCompat.getColor(Article.this,R.color.App_Color),PorterDuff.Mode.MULTIPLY);
                            LT.setTextColor(ContextCompat.getColor(Article.this,R.color.App_Color));
                            LT.setText(result.getString("Like")+" Likes");
                            DT.setText(result.getString("Dislike")+" Dislike");
                        }else if(result.getString("msg").trim().equals("LIKENOW")){
                            like.setColorFilter(ContextCompat.getColor(Article.this,R.color.App_Color),PorterDuff.Mode.MULTIPLY);
                            LT.setTextColor(ContextCompat.getColor(Article.this,R.color.App_Color));
                            dislike.setColorFilter(ContextCompat.getColor(Article.this,R.color.Border_Txt),PorterDuff.Mode.MULTIPLY);
                            DT.setTextColor(ContextCompat.getColor(Article.this,R.color.Border_Txt));
                            LT.setText(result.getString("Like")+" Likes");
                            DT.setText(result.getString("Dislike")+" Dislike");
                        }else if(result.getString("msg").trim().equals("UNSET1")){
                            like.setColorFilter(ContextCompat.getColor(Article.this,R.color.Border_Txt),PorterDuff.Mode.MULTIPLY);
                            LT.setTextColor(ContextCompat.getColor(Article.this,R.color.Border_Txt));
                            LT.setText(result.getString("Like")+" Likes");
                            DT.setText(result.getString("Dislike")+" Dislike");
                        }else if(result.getString("msg").trim().equals("DISLIKE")){
                            dislike.setColorFilter(ContextCompat.getColor(Article.this,R.color.App_Color),PorterDuff.Mode.MULTIPLY);
                            DT.setTextColor(ContextCompat.getColor(Article.this,R.color.App_Color));
                            LT.setText(result.getString("Like")+" Likes");
                            DT.setText(result.getString("Dislike")+" Dislike");
                        }else if(result.getString("msg").trim().equals("DISLIKENOW")){
                            dislike.setColorFilter(ContextCompat.getColor(Article.this,R.color.App_Color),PorterDuff.Mode.MULTIPLY);
                            DT.setTextColor(ContextCompat.getColor(Article.this,R.color.App_Color));
                            like.setColorFilter(ContextCompat.getColor(Article.this,R.color.Border_Txt),PorterDuff.Mode.MULTIPLY);
                            LT.setTextColor(ContextCompat.getColor(Article.this,R.color.Border_Txt));
                            LT.setText(result.getString("Like")+" Likes");
                            DT.setText(result.getString("Dislike")+" Dislike");
                        }else if(result.getString("msg").trim().equals("UNSET")){
                            dislike.setColorFilter(ContextCompat.getColor(Article.this,R.color.Border_Txt),PorterDuff.Mode.MULTIPLY);
                            DT.setTextColor(ContextCompat.getColor(Article.this,R.color.Border_Txt));
                            LT.setText(result.getString("Like")+" Likes");
                            DT.setText(result.getString("Dislike")+" Dislike");
                        }else if(result.getString("msg").trim().equals("BOOKMARK")){
                            bookmark.setColorFilter(ContextCompat.getColor(Article.this,R.color.App_Color),PorterDuff.Mode.MULTIPLY);
                            BT.setTextColor(ContextCompat.getColor(Article.this,R.color.App_Color));
                        }else if (result.getString("msg").trim().equals("REMOVE")){
                            bookmark.setColorFilter(ContextCompat.getColor(Article.this,R.color.Border_Txt),PorterDuff.Mode.MULTIPLY);
                            BT.setTextColor(ContextCompat.getColor(Article.this,R.color.Border_Txt));
                        }else if(result.getString("msg").trim().equals("JOIN")){
                            ChannelJoinBtn.setBackgroundResource(R.drawable.edittext_back);
                            ChannelJoinBtn.setText("LEFT");
                            ChannelJoinBtn.setTextColor(ContextCompat.getColor(Article.this,R.color.Border_Txt));
                            ChannelJoiner.setText(result.getString("Joiner")+" joiner");
                        }else if(result.getString("msg").trim().equals("LEFT")){
                            ChannelJoinBtn.setBackgroundResource(R.drawable.button_back);
                            ChannelJoinBtn.setText("Join");
                            ChannelJoinBtn.setTextColor(ContextCompat.getColor(Article.this, R.color.white));
                            ChannelJoiner.setText(result.getString("Joiner")+" joiner");
                        }
                    } else {
                        Toast.makeText(Article.this, result.getString("success_msg"), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Article.this, "Unable to connect. try again!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            ArrayList param = new ArrayList();
            param.add(new BasicNameValuePair("Fun_Value",Fun_Value));
            param.add(new BasicNameValuePair("Post_Url",Post_Url));
            param.add(new BasicNameValuePair("User_Url",sharedPreference.getURL(getApplication())));

            String Web_url = String.valueOf(Uri.parse(WEB_URL+Url));
            JSONObject jsonObject = jsonParser.makeHttpRequest(Web_url,"POST",param);
            return jsonObject;
        }
    }


    private class ReportArticle extends AsyncTask<String, String, JSONObject>{

        String Post_Url, Report, Channel_Url;

        public ReportArticle(String Report, String Post_Url, String Channel_Url) {
            this.Report = Report;
            this.Post_Url = Post_Url;
            this.Channel_Url = Channel_Url;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                if (result != null) {
                    if (Integer.parseInt(result.getString("success")) == 1) {
                        Toast.makeText(Article.this, "Thank you for article Report!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Article.this, result.getString("success_msg"), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Article.this, "Unable to connect. try again!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            ArrayList param = new ArrayList();
            param.add(new BasicNameValuePair("Report",Report));
            param.add(new BasicNameValuePair("Post_Url",Post_Url));
            param.add(new BasicNameValuePair("Channel_Url",Channel_Url));
            param.add(new BasicNameValuePair("User_Url",sharedPreference.getURL(getApplication())));

            String Web_url = String.valueOf(Uri.parse(WEB_URL+"/android/Report"));
            JSONObject jsonObject = jsonParser.makeHttpRequest(Web_url,"POST",param);
            return jsonObject;
        }
    }



    private class GetArticleData extends AsyncTask<String, String, JSONObject>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Article.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            progressDialog.hide();
            try {
                if (result != null) {
                    if (Integer.parseInt(result.getString("success")) == 1) {
                        setData(result);
                    } else {
                        Toast.makeText(getApplicationContext(), result.getString("success_msg"), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server!"+result, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected JSONObject doInBackground(String... arg) {
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("Article_Url",arg[0]));
            params.add(new BasicNameValuePair("User_Url",arg[1]));
            String var = String.valueOf(Uri.parse(WEB_URL+"/Android/getArticleViewData/"));
            JSONObject json = jsonParser.makeHttpRequest(var, "POST",params);
            return json;
        }
    }

    private void setData(JSONObject result) {

        try {

            Picasso.with(getApplicationContext()).load(WEB_URL+"/images/"+result.getString("Post_Image").trim()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    ArticleImage.setBackground(new BitmapDrawable(bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });

            Picasso.with(getApplicationContext()).load(WEB_URL+"/images/"+result.getString("Channel_Logo").trim()).into(ChannelLogo);

            webView.loadUrl(WEB_URL+"/Android/getArticleContent/"+result.getString("Post_Url"));
            Title.setText(result.getString("Post_Title"));
            ArticleAbout.setText(result.getString("Post_Views")+" Views • "+result.getString("Post_Date_Time"));
            ChannelName.setText(result.getString("Channel_Name"));
            ChannelJoiner.setText(result.getString("Channel_Joiner")+" joiner");
            likeCount.setText(result.getString("Post_Like")+" Likes");
            DislikeCount.setText(result.getString("Post_Dislike")+" Dislike");
            Channel_Url = result.getString("Channel_Url").trim();

            if (result.getString("Post_Comments").trim().equals("0")){
                Comment_Btn.setVisibility(View.GONE);
            }

            if (result.getString("LikeAndDislike").trim().equals("LIKE")){
                like.setColorFilter(ContextCompat.getColor(Article.this,R.color.App_Color),PorterDuff.Mode.MULTIPLY);
                LT.setTextColor(ContextCompat.getColor(Article.this,R.color.App_Color));
            }

            if(result.getString("LikeAndDislike").trim().equals("DISLIKE")){
                dislike.setColorFilter(ContextCompat.getColor(Article.this,R.color.App_Color),PorterDuff.Mode.MULTIPLY);
                DT.setTextColor(ContextCompat.getColor(Article.this,R.color.App_Color));
            }

            if(result.getString("ArticleBookmark").trim().equals("BOOKMARK")){
                bookmark.setColorFilter(ContextCompat.getColor(Article.this,R.color.App_Color),PorterDuff.Mode.MULTIPLY);
                BT.setTextColor(ContextCompat.getColor(Article.this,R.color.App_Color));
            }

            if(result.getString("ChannelJoined").trim().equals("JOIN")){
                ChannelJoinBtn.setBackgroundResource(R.drawable.edittext_back);
                ChannelJoinBtn.setText("LEFT");
                ChannelJoinBtn.setTextColor(ContextCompat.getColor(Article.this,R.color.Border_Txt));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}