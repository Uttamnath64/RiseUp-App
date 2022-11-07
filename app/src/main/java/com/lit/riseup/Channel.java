package com.lit.riseup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Channel extends AppCompatActivity {

    JSONParser jsonParser = new JSONParser();
    Conn conn = new Conn();
    String WEB_URL = conn.getUrl();
    ArrayList<ListHomeCardData> card;
    SharedPreference sharedPreference = new SharedPreference();
    View rootView;
    RecyclerView RV;
    Intent intent;

    TextView Title,Name,Joiner,About, ChannelJoinBtn;
    ImageView Logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

        RV = findViewById(R.id.recyclerView);

        Title = findViewById(R.id.Title_Name);
        Name = findViewById(R.id.channel_Name);
        Joiner = findViewById(R.id.channel_Joiner);
        About = findViewById(R.id.channel_about);

        Logo = findViewById(R.id.channel_Image);

        RV.setHasFixedSize(true);
        RV.setLayoutManager(new LinearLayoutManager(Channel.this));
        intent = getIntent();

        ChannelJoinBtn = findViewById(R.id.channelJoinButton);

        card = new ArrayList<>();
        new GetChannelArticleData().execute(intent.getStringExtra("Url"),sharedPreference.getURL(getApplication()));

        findViewById(R.id.back_btn).setOnClickListener(View->{
            finish();
        });

        // channel join and left button click
        ChannelJoinBtn.setOnClickListener(View->{
            if(sharedPreference.getURL(Channel.this).length() == 0){
                startActivity(new Intent(Channel.this, SignIn.class));
            }else{
                new DoActionOnArticle("1",intent.getStringExtra("Url"),"/Android/channelJoinAndLeft/").execute();
            }
        });
    }
    private class GetChannelArticleData extends AsyncTask<String, String, JSONObject> {

        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("Channel_Url",args[0]));
            params.add(new BasicNameValuePair("User_Url",args[1]));

            String var = String.valueOf(Uri.parse(WEB_URL+"/Android/getChannelData/"));
            JSONObject json = jsonParser.makeHttpRequest(var, "POST",params);

            return json;
        }
        protected void onPostExecute(JSONObject result){
            try {

                if (result != null) {
                    if (Integer.parseInt(result.getString("success")) == 1) {
                        setHomeCard(result.getString("Data"));
                        Title.setText(result.getString("Channel_Name"));
                        Name.setText(result.getString("Channel_Name"));
                        About.setText(result.getString("Description"));
                        Joiner.setText(result.getString("Joiner")+" Joiner");
                        if(result.getString("ChannelJoined").trim().equals("JOIN")){
                            ChannelJoinBtn.setBackgroundResource(R.drawable.edittext_back);
                            ChannelJoinBtn.setText("LEFT");
                            ChannelJoinBtn.setTextColor(ContextCompat.getColor(Channel.this,R.color.Border_Txt));
                        }
                        Picasso.with(getApplicationContext()).load(WEB_URL+"/images/"+result.getString("Channel_Logo").trim()).into(Logo);
                    } else {
                        Toast.makeText(getApplicationContext(), result.getString("success_msg"), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setHomeCard(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            card.add(new ListHomeCardData(
                    obj.getString("Url"),
                    WEB_URL+obj.getString("Image_Url"),
                    obj.getString("Title"),
                    obj.getString("Description"),
                    WEB_URL+obj.getString("Channel_Logo"),
                    obj.getString("Channel_Name")+" • "+ obj.getString("Views")+" View • "+ obj.getString("Date_Time")));

        }

        ListHomeCardAdapter listHomeCardAdapter = new ListHomeCardAdapter(card, Channel.this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Channel.this, LinearLayoutManager.VERTICAL, false);

        RV.setLayoutManager(linearLayoutManager);

        RV.setAdapter(listHomeCardAdapter);
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
                        Toast.makeText(getApplicationContext(), result.getString("msg"), Toast.LENGTH_SHORT).show();
                       if(result.getString("msg").trim().equals("JOIN")){
                            ChannelJoinBtn.setBackgroundResource(R.drawable.edittext_back);
                            ChannelJoinBtn.setText("LEFT");
                            ChannelJoinBtn.setTextColor(ContextCompat.getColor(Channel.this,R.color.Border_Txt));
                            Joiner.setText(result.getString("Joiner")+" joiner");
                        }else if(result.getString("msg").trim().equals("LEFT")){
                            ChannelJoinBtn.setBackgroundResource(R.drawable.button_back);
                            ChannelJoinBtn.setText("Join");
                            ChannelJoinBtn.setTextColor(ContextCompat.getColor(Channel.this, R.color.white));
                            Joiner.setText(result.getString("Joiner")+" joiner");
                        }
                    } else {
                        Toast.makeText(Channel.this, result.getString("success_msg"), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Channel.this, "Unable to connect. try again!", Toast.LENGTH_SHORT).show();
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
}