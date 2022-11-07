package com.lit.riseup;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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

public class SearchPage extends AppCompatActivity {

    EditText SearchText;

    JSONParser jsonParser  = new JSONParser();
    ArrayList<SearchModal> card;
    RecyclerView RV;
    LinearLayout Process, DataNotFound;
    Conn conn = new Conn();
    String WEB_URL = conn.getUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        card = new ArrayList<>();
        SearchText = findViewById(R.id.searchBox);
        RV = findViewById(R.id.recyclerView);
        Process = findViewById(R.id.process);
        DataNotFound = findViewById(R.id.dataNotFound);
        Process.setVisibility(View.GONE);
        DataNotFound.setVisibility(View.GONE);

        SearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_DONE){
                    String  text = SearchText.getText().toString().trim();
                    if (!text.isEmpty()){
                        card.clear();
                        new GetSearchData().execute(text);
                        return true;
                    }
                }
                return false;
            }
        });

        findViewById(R.id.back_btn).setOnClickListener(View->{
            finish();
        });

    }
    class GetSearchData extends AsyncTask<String,String,JSONObject>{

        @Override
        protected JSONObject doInBackground(String... args) {

            ArrayList param = new ArrayList();

            param.add(new BasicNameValuePair("Query",args[0]));
            String Url = String.valueOf(Uri.parse(WEB_URL+"/Android/getSearchResult"));

            JSONObject jsonObject = jsonParser.makeHttpRequest(Url,"POST",param);

            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Process.setVisibility(View.VISIBLE);
            DataNotFound.setVisibility(View.GONE);
            RV.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            Process.setVisibility(View.GONE);
            RV.setVisibility(View.VISIBLE);

            try {

                if (result != null) {
                    if (Integer.parseInt(result.getString("success")) == 1) {

                        setHomeCard(result.getString("Data"));

                    } else {
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
    private void setHomeCard(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            card.add(new SearchModal(
                    obj.getString("Search_Url"),
                    obj.getString("Search_Image"),
                    obj.getString("Search_Title"),
                    obj.getString("Search_Description"),
                    obj.getString("Search_Channel_Image"),
                    obj.getString("Search_Channel_Name")+" • "+obj.getString("Search_Views")+" Views"));

        }

        SearchAdapter searchAdapter = new SearchAdapter(card, SearchPage.this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        RV.setLayoutManager(linearLayoutManager);

        RV.setAdapter(searchAdapter);
    }
}