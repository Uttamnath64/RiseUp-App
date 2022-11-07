package com.lit.riseup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lit.riseup.databinding.ActivityHomeBinding;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    ProgressDialog progressDialog;
    BottomNavigationView navView;
    JSONParser jsonParser = new JSONParser();
    Conn conn = new Conn();
    String WEB_URL = conn.getUrl();
    String Home_Activity_Data, Achievement_Data, Joined_Data, Account_Data_ChannelName,Account_Data_ChannelJoiner,Account_Data_Channel_Image,Account_Data_Channel_Description,Account_Data_Channel_Articles,Account_Data_Channel_Likes;
    Bundle bundle = new Bundle();
    Fragment fragment;
    SharedPreference sharedPreference;

    private ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        fragment = new Fhome();
        sharedPreference = new SharedPreference();

        loadAllData();
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);


        findViewById(R.id.search_button).setOnClickListener(View->{
            startActivity(new Intent(Home.this,SearchPage.class));
        });


    }


    private void loadAllData() {
        String url = sharedPreference.getURL(Home.this);
        new GetHomeData().execute(url);
        new GetAchievementData().execute(url);
        new GetChannelJoinedData().execute(url);
        new GetAccountData().execute(url);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.navigation_home:
                fragment = new Fhome();
                bundle.putString("Data",Home_Activity_Data);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_home, fragment).commit();
                return true;

            case R.id.navigation_achievement:
                fragment = new Achievement();
                bundle.putString("Data",Achievement_Data);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_home, fragment).commit();
                return true;

            case R.id.navigation_create:
                startActivity(new Intent(Home.this,CrrateArticle.class));
                return true;

            case R.id.navigation_joined:
                fragment = new Joined();
                bundle.putString("Data",Joined_Data);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_home, fragment).commit();
                return true;

            case R.id.navigation_library:
                fragment = new library();
                bundle.putString("Account_Data_ChannelName",Account_Data_ChannelName);
                bundle.putString("Account_Data_ChannelJoiner",Account_Data_ChannelJoiner);
                bundle.putString("Account_Data_Channel_Image",Account_Data_Channel_Image);
                bundle.putString("Account_Data_Channel_Description",Account_Data_Channel_Description);
                bundle.putString("Account_Data_Channel_Articles",Account_Data_Channel_Articles);
                bundle.putString("Account_Data_Channel_Likes",Account_Data_Channel_Likes);

                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_home,fragment).commit();
                return true;
        }
        return false;
    }


    private class GetHomeData extends AsyncTask<String, String, JSONObject> {

        protected void onPreExecute(){
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("URL",args[0]));


            String var = String.valueOf(Uri.parse(WEB_URL+"/Android/getHomeArticleData/"));
            JSONObject json = jsonParser.makeHttpRequest(var, "POST",params);

            return json;
        }
        protected void onPostExecute(JSONObject result){
            try {

                if (result != null) {
                    if (Integer.parseInt(result.getString("success")) == 1) {
                        Home_Activity_Data = result.getString("Data");

                        fragment = new Fhome();
                        bundle.putString("Data",Home_Activity_Data);
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_home, fragment).commit();

                    } else {
                        fragment = new Fhome();
                        bundle.putString("Data",Home_Activity_Data);
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_home, fragment).commit();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class GetAchievementData extends AsyncTask<String, String, JSONObject> {

        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("User_Url",args[0]));


            String var = String.valueOf(Uri.parse(WEB_URL+"/Android/getAchievementData/"));
            JSONObject json = jsonParser.makeHttpRequest(var, "POST",params);

            return json;
        }
        protected void onPostExecute(JSONObject result){
            try {

                if (result != null) {
                    if (Integer.parseInt(result.getString("success")) == 1) {
                        Achievement_Data = result.getString("Data");
                    } else {
                        Achievement_Data = result.getString("Data");
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private class GetChannelJoinedData extends AsyncTask<String, String, JSONObject> {

        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("User_Url",args[0]));


            String var = String.valueOf(Uri.parse(WEB_URL+"/Android/getJoinedChannel"));
            JSONObject json = jsonParser.makeHttpRequest(var, "POST",params);

            return json;
        }
        protected void onPostExecute(JSONObject result){
            try {
                if (result != null) {
                    if (Integer.parseInt(result.getString("success")) == 1) {
                        Joined_Data = result.getString("Data");
                    }else{
                        Joined_Data = result.getString("Data");
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class GetAccountData extends AsyncTask<String, String, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... args) {

            ArrayList param = new ArrayList();
            param.add(new BasicNameValuePair("User_Url",args[0]));

            String url = String.valueOf(Uri.parse(WEB_URL+"/Android/ProfileData"));
            JSONObject jsonObject = jsonParser.makeHttpRequest(url,"POST",param);
            return jsonObject;
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
                        Account_Data_ChannelName = result.getString("Channel_Name");
                        Account_Data_ChannelJoiner = result.getString("Joiner");
                        Account_Data_Channel_Image = result.getString("Channel_Logo");
                        Account_Data_Channel_Description = result.getString("Description");
                        Account_Data_Channel_Articles = result.getString("Articles");
                        Account_Data_Channel_Likes = result.getString("Likes");
                    } else {
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}