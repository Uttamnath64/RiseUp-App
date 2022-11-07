package com.lit.riseup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CrrateArticle extends AppCompatActivity {

    EditText Title,Article,Keyword,Paid;
    Spinner Category,Visibility,Comments;
    TextView FileBtn;

    ImageView ShowImage;

    Uri Iamge_Path;
    String T_Title, T_Article, T_Keyword, T_Paid, T_Category, T_Visibility, T_Comments;
    Bitmap image_data;
    Conn conn = new Conn();
    String WEB_URL = conn.getUrl();

    JSONParser jsonParser = new JSONParser();

    String[] Visibility_List = {"-- Select Visibility --","Public","Unlisted","Private"};
    String[] Comments_List = {"Allow all comments","Disable comments"};

    String[] Category_Value,Category_Id;

    int imageSelected, IndexOfCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crrate_article);

        requestStoragePermission();
        // Set All Object fun()
        setObjects();

        setAllData();

        // back button clicked
        findViewById(R.id.back_btn).setOnClickListener(View ->{
            finish();
        });

        // upload button clicked
        findViewById(R.id.uploadButton).setOnClickListener(View ->{

            //call upload
            uploadData();
        });

        FileBtn.setOnClickListener(View ->{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Image"),1);
        });

        Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                IndexOfCategory = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setAllData() {
        Visibility.setAdapter(new ArrayAdapter<>(CrrateArticle.this, android.R.layout.simple_spinner_dropdown_item,Visibility_List));
        Comments.setAdapter(new ArrayAdapter<>(CrrateArticle.this, android.R.layout.simple_spinner_dropdown_item,Comments_List));
        new GetCategoryList().execute();
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
    }

    // when image is selected call this
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null){
            Iamge_Path = data.getData();

            try {
                image_data = MediaStore.Images.Media.getBitmap(getContentResolver(),Iamge_Path);
                imageSelected = 1;
                ShowImage.setImageBitmap(image_data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String convertImage(Bitmap bmp){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // Set All Object
    private void setObjects() {

        Title = findViewById(R.id.title);
        Article = findViewById(R.id.article);
        Keyword = findViewById(R.id.keyword);
        Paid = findViewById(R.id.paid);

        Category = findViewById(R.id.category);
        Visibility = findViewById(R.id.visibility);
        Comments = findViewById(R.id.comment);

        FileBtn = findViewById(R.id.file);

        ShowImage = findViewById(R.id.ChannelImage);

    }


    // check all validation
    private void uploadData() {

        T_Title = Title.getText().toString().trim();
        T_Article = Article.getText().toString().trim();
        T_Keyword = Keyword.getText().toString().trim();
        T_Paid = Paid.getText().toString().trim();

        T_Category = Category.getSelectedItem().toString();
        T_Visibility = Visibility.getSelectedItem().toString();
        T_Comments = Comments.getSelectedItem().toString();
        String Image = convertImage(image_data);


        if (T_Title.length() < 10){
            Toast.makeText(CrrateArticle.this, "Enter valid Title!", Toast.LENGTH_SHORT).show();
        }else if(T_Article.length() < 10){
            Toast.makeText(CrrateArticle.this, "Enter valid Article!", Toast.LENGTH_SHORT).show();
        }else if(T_Category.equals("-- Select Category --")){
            Toast.makeText(CrrateArticle.this, "Select Category!", Toast.LENGTH_SHORT).show();
        }else if(T_Visibility.equals("-- Select Visibility --")){
            Toast.makeText(CrrateArticle.this, "Select Visibility!", Toast.LENGTH_SHORT).show();
        }else if(imageSelected != 1) {
            Toast.makeText(CrrateArticle.this, "Select Image!", Toast.LENGTH_SHORT).show();
        }else{

            if(T_Comments.equals("Allow all comments")){
                T_Comments = "1";
            }else{
                T_Comments = "0";
            }
            if(T_Visibility.equals("Public")){
                T_Visibility = "PU";
            }else if(T_Visibility.equals("Unlisted")){
                T_Visibility = "UN";
            }else{
                T_Visibility = "PR";
            }
            T_Category = Category_Id[IndexOfCategory];

            new CreateArticleData().execute(T_Title,T_Article,T_Keyword,T_Paid,T_Category,T_Visibility,T_Comments,Image);
        }
    }

    private  class CreateArticleData extends AsyncTask<String, String, JSONObject>{

        @Override
        protected void onPostExecute(JSONObject result) {;
            try {
                if (result != null) {
                    if (Integer.parseInt(result.getString("success")) == 1) {
                        Toast.makeText(getApplicationContext(), result.getString("success_msg"), Toast.LENGTH_SHORT).show();
                        finish();
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

        @Override
        protected JSONObject doInBackground(String... args) {

            ArrayList param = new ArrayList();
            SharedPreference sharedPreference = new SharedPreference();
            param.add(new BasicNameValuePair("Title",args[0]));
            param.add(new BasicNameValuePair("Article",args[1]));
            param.add(new BasicNameValuePair("Keyword",args[2]));
            param.add(new BasicNameValuePair("Paid",args[3]));
            param.add(new BasicNameValuePair("Category",args[4]));
            param.add(new BasicNameValuePair("Visibility",args[5]));
            param.add(new BasicNameValuePair("Comments",args[6]));
            param.add(new BasicNameValuePair("Image",args[7]));
            param.add(new BasicNameValuePair("User_Url",sharedPreference.getURL(CrrateArticle.this)));

            JSONObject jsonObject = jsonParser.makeHttpRequest(WEB_URL+"/Android/CreateArticle","POST",param);

            return jsonObject;
        }
    }

    private  class GetCategoryList extends AsyncTask<String, String, JSONObject>{


        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                if (result != null) {
                    if (Integer.parseInt(result.getString("success")) == 1) {
                        JSONArray jsonArray = new JSONArray(result.getString("Data"));
                        Category_Value = new String[jsonArray.length()+1];
                        Category_Id = new String[jsonArray.length()+1];
                        Category_Value[0] = "-- Select Category --";
                        Category_Id[0] = "0";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            Category_Value[i+1] = obj.getString("Category_Name");
                            Category_Id[i+1] = obj.getString("Id");
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CrrateArticle.this, android.R.layout.simple_spinner_dropdown_item, Category_Value);
                        Category.setAdapter(arrayAdapter);
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

        @Override
        protected JSONObject doInBackground(String... args) {

            ArrayList param = new ArrayList();

            JSONObject jsonObject = jsonParser.makeHttpRequest(WEB_URL+"/Android/getCategoryList","POST",param);

            return jsonObject;
        }
    }
}