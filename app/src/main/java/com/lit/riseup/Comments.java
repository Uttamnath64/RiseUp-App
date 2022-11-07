package com.lit.riseup;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Comments extends AppCompatActivity {

    RecyclerView RV;
    Conn conn = new Conn();
    String WEB_URL = conn.getUrl();
    ArrayList<CommentModal> card;
    ImageFilterView User_Image;
    EditText Comment;
    LinearLayout LoadData, DataNotFound;

    String Post_Url;

    JSONParser jsonParser = new JSONParser();
    SharedPreference sharedPreference = new SharedPreference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        RV = findViewById(R.id.recyclerView);
        LoadData = findViewById(R.id.loadData);
        User_Image = findViewById(R.id.user_img);
        Comment = findViewById(R.id.comment_box);
        DataNotFound = findViewById(R.id.dataNotFound);

        RV.setVisibility(View.GONE);
        LoadData.setVisibility(View.VISIBLE);
        DataNotFound.setVisibility(View.GONE);

        card = new ArrayList<>();

        Intent intent = getIntent();
        Post_Url = intent.getStringExtra("Url");

        new GetComment().execute(Post_Url,sharedPreference.getURL(getApplication()));

        // back button click
        findViewById(R.id.back_btn).setOnClickListener(View->{
            finish();
        });
        Comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String CommentText = Comment.getText().toString().trim();
                if (!CommentText.equals("")){
                    card.clear();
                    Comment.setText("");
                    new AddComment().execute(Post_Url,CommentText,sharedPreference.getURL(getApplication()));
                    return true;
                }else{
                    return false;
                }
            }
        });
    }

    class AddComment extends AsyncTask<String,String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {

            ArrayList param = new ArrayList();
            param.add(new BasicNameValuePair("Post_Url",args[0]));
            param.add(new BasicNameValuePair("Comment",args[1]));
            param.add(new BasicNameValuePair("User_Url",args[2]));

            JSONObject object = jsonParser.makeHttpRequest(WEB_URL+"/android/addComment","POST",param);
            return object;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RV.setVisibility(View.GONE);
            LoadData.setVisibility(View.VISIBLE);
            DataNotFound.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            try {

                if (result != null) {
                    if (Integer.parseInt(result.getString("success")) == 1) {

                        RV.setVisibility(View.VISIBLE);
                        LoadData.setVisibility(View.GONE);
                        DataNotFound.setVisibility(View.GONE);
                        Toast.makeText(Comments.this, "Comment added!", Toast.LENGTH_SHORT).show();
                        setHomeCard(result.getString("Data"));

                    } else {
                        Toast.makeText(Comments.this, "Something want wrong! Retry!", Toast.LENGTH_SHORT).show();
                        RV.setVisibility(View.GONE);
                        LoadData.setVisibility(View.GONE);
                        DataNotFound.setVisibility(View.VISIBLE);

                    }
                }else{

                    RV.setVisibility(View.GONE);
                    LoadData.setVisibility(View.GONE);
                    DataNotFound.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server!", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class GetComment extends AsyncTask<String,String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {

            ArrayList param = new ArrayList();
            param.add(new BasicNameValuePair("Post_Url",args[0]));
            param.add(new BasicNameValuePair("User_Url",args[1]));

            JSONObject object = jsonParser.makeHttpRequest(WEB_URL+"/android/getComments","POST",param);
            return object;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RV.setVisibility(View.GONE);
            LoadData.setVisibility(View.VISIBLE);
            DataNotFound.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            try {

                if (result != null) {
                    Picasso.with(Comments.this).load(WEB_URL+"/images/"+result.getString("User_Img")).into(User_Image);
                    if (Integer.parseInt(result.getString("success")) == 1) {

                        RV.setVisibility(View.VISIBLE);
                        LoadData.setVisibility(View.GONE);
                        DataNotFound.setVisibility(View.GONE);
                        setHomeCard(result.getString("Data"));

                    } else {

                        RV.setVisibility(View.GONE);
                        LoadData.setVisibility(View.GONE);
                        DataNotFound.setVisibility(View.VISIBLE);

                    }
                }else{

                    RV.setVisibility(View.GONE);
                    LoadData.setVisibility(View.GONE);
                    DataNotFound.setVisibility(View.VISIBLE);
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
            card.add(new CommentModal(
                    WEB_URL+"/images/"+obj.getString("Channel_Logo"),
                    obj.getString("Channel_Name"),
                    obj.getString("Comment_Date"),
                    obj.getString("Comment"),
                    obj.getString("Channel_Url")));

        }

        CommentAdapter commentAdapter = new CommentAdapter(card, Comments.this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        RV.setLayoutManager(linearLayoutManager);

        RV.setAdapter(commentAdapter);
    }
}