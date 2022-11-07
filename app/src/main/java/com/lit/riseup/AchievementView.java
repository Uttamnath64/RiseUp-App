package com.lit.riseup;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AchievementView extends AppCompatActivity {


    Conn conn = new Conn();
    String WEB_URL = conn.getUrl();

    JSONParser jsonParser = new JSONParser();
    TextView Icon, Title, Description, Points, Date, LB_Btn;
    Boolean Btn = false;
    String Id;
    Intent intent;
    SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_view);

        Icon = findViewById(R.id.icon);
        Title = findViewById(R.id.title);
        Description = findViewById(R.id.description);
        Points = findViewById(R.id.point);
        Date = findViewById(R.id.date);
        LB_Btn = findViewById(R.id.btn);
        intent = getIntent();

        Id = intent.getStringExtra("Id");
        sharedPreference = new SharedPreference();

        new GetAchievementDataById().execute(Id,sharedPreference.getURL(AchievementView.this));

        LB_Btn.setOnClickListener(View->{
            if(Btn){
                new SetAchievementDataById().execute(Id,sharedPreference.getURL(AchievementView.this));
            }
        });

    }

    private class GetAchievementDataById extends AsyncTask<String, String, JSONObject> {

        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("Id",args[0]));
            params.add(new BasicNameValuePair("User_Url",args[1]));


            String var = String.valueOf(Uri.parse(WEB_URL+"/Android/getAchievementView"));
            JSONObject json = jsonParser.makeHttpRequest(var, "POST",params);

            return json;
        }
        protected void onPostExecute(JSONObject result){
            try {
                if (result != null) {
                    if (Integer.parseInt(result.getString("success")) == 1) {
                        Icon.setText(String.valueOf(result.getString("Achievement_Name").charAt(0)));
                        Title.setText(result.getString("Achievement_Name"));
                        Description.setText(result.getString("Description"));
                        Points.setText(result.getString("Points"));
                    if (Integer.parseInt(result.getString("Status")) == 1){
                        LB_Btn.setText("Remaining");
                        LB_Btn.setTextColor(ContextCompat.getColor(AchievementView.this,R.color.A_S_1T));
                        LB_Btn.setBackgroundColor(ContextCompat.getColor(AchievementView.this, R.color.A_S_1B));
                        Date.setText("-");
                    }else if(Integer.parseInt(result.getString("Status")) == 2){
                        LB_Btn.setText("Collected");
                        LB_Btn.setTextColor(ContextCompat.getColor(AchievementView.this,R.color.A_S_2T));
                        LB_Btn.setBackgroundColor(ContextCompat.getColor(AchievementView.this,R.color.A_S_2B));
                        Date.setText(result.getString("Date"));
                    }else{
                        Btn = true;
                        LB_Btn.setText("Collect");
                        LB_Btn.setTextColor(ContextCompat.getColor(AchievementView.this,R.color.white));
                        LB_Btn.setBackgroundColor(ContextCompat.getColor(AchievementView.this,R.color.App_Color));
                        Date.setText("-");
                    }
                    }else{
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

    private class SetAchievementDataById extends AsyncTask<String, String, JSONObject> {

        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("Id",args[0]));
            params.add(new BasicNameValuePair("User_Url",args[1]));


            String var = String.valueOf(Uri.parse(WEB_URL+"/Android/setAchievementData"));
            JSONObject json = jsonParser.makeHttpRequest(var, "POST",params);

            return json;
        }
        protected void onPostExecute(JSONObject result){
            try {
                if (result != null) {
                    Toast.makeText(getApplicationContext(), result.getString("success_msg"), Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}