package com.lit.riseup;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArticleView extends AppCompatActivity {

    TextView title;
    Intent intent;
    Conn conn = new Conn();
    String WEB_URL = conn.getUrl();
    JSONParser jsonParser = new JSONParser();
    ArrayList<ListHomeCardData> card;
    LinearLayout DataNotFound;
    RecyclerView RV;
    SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view);

        intent = getIntent();
        RV = findViewById(R.id.recyclerView);
        title = findViewById(R.id.screen_name);
        DataNotFound = findViewById(R.id.dataNotFound);
        title.setText(intent.getStringExtra("Title"));
        card = new ArrayList<>();
        DataNotFound.setVisibility(View.GONE);
        sharedPreference = new SharedPreference();

        findViewById(R.id.back_btn).setOnClickListener(View->{
            finish();
        });

        new GetArticle().execute(sharedPreference.getURL(ArticleView.this),intent.getStringExtra("Url"));

    }

    private class GetArticle extends AsyncTask<String, String, JSONObject> {

        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("User_Url",args[0]));

            String var = String.valueOf(Uri.parse(WEB_URL+args[1]));
            JSONObject json = jsonParser.makeHttpRequest(var, "POST",params);

            return json;
        }
        protected void onPostExecute(JSONObject result){
            try {

                if (result != null) {
                    if (Integer.parseInt(result.getString("success")) == 1) {
                        setCard(result.getString("Data"));
                    } else {
                        RV.setVisibility(View.GONE);
                        DataNotFound.setVisibility(View.VISIBLE);
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void setCard(String json) throws JSONException {
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

        ListHomeCardAdapter listHomeCardAdapter = new ListHomeCardAdapter(card, ArticleView.this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ArticleView.this, LinearLayoutManager.VERTICAL, false);

        RV.setLayoutManager(linearLayoutManager);

        RV.setAdapter(listHomeCardAdapter);
    }
}