package com.lit.riseup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SignUp extends AppCompatActivity {
    Conn conn = new Conn();
    String WEB_URL = conn.getUrl();
    JSONParser jsonParser = new JSONParser();
    String[] country;
    final Calendar myCalendar= Calendar.getInstance();
    Spinner countrySpinner, GenderSpinner;
    String[] GenderList = {"-- Select Gender --","Male", "Female"};
    TextView DOB;
    ProgressDialog progressDialog;
    TextView VT, FileBtn;
    ImageView VI;
    Button SignUp_Btn, SignIn_Btn;
    int IdOfCountry;
    EditText F_Name, L_Name, Mobile_Number,Email,Password;
    private String password;
    private  boolean is8char=false, hasUpper=false, hasnum=false, hasSpecialSymbol =false, isSignupClickable = false;
    private Uri Iamge_Path;
    Bitmap image_data;
    ImageView ShowImage;
    int imageSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // set Object
        countrySpinner = findViewById(R.id.country);
        GenderSpinner = findViewById(R.id.gender);
        DOB = findViewById(R.id.dob);
        F_Name = findViewById(R.id.first_name);
        L_Name = findViewById(R.id.last_name);
        Mobile_Number = findViewById(R.id.mobile_number);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        SignUp_Btn = findViewById(R.id.signup_btn);
        SignIn_Btn = findViewById(R.id.signin_btn);
        ShowImage = findViewById(R.id.ChannelImage);

        VI = findViewById(R.id.valid_icon);
        VT = findViewById(R.id.valid_text);
        FileBtn = findViewById(R.id.file);

        //get county id
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                IdOfCountry = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        FileBtn.setOnClickListener(View ->{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Image"),1);
        });

        SignIn_Btn.setOnClickListener(View ->{
            startActivity(new Intent(SignUp.this, SignIn.class));
        });
        // Set Password Change

        passwordChange();

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        // onClick DOB TextView

        DOB.setOnClickListener( View ->{
            new DatePickerDialog(SignUp.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        });

        //get/set country list
        getCountryList();


        // onClick SignUp Button
        SignUp_Btn.setOnClickListener(View ->{
            goToSingUp();
        });

        //set Gender

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, GenderList);
        GenderSpinner.setAdapter(arrayAdapter);
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
    //Update date in TextView
    private String convertImage(Bitmap bmp){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    private void updateLabel(){
        String myFormat="YYYY-MM-DD";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        DOB.setText(dateFormat.format(myCalendar.getTime()));
    }

    // get Country data form mysql DB

    protected void getCountryList(){
        class GetCountryList extends AsyncTask<Void, Void, String> {

            protected void onPreExecute(){
                super.onPreExecute();
                progressDialog = new ProgressDialog(SignUp.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            protected  void onPostExecute(String s){
                super.onPostExecute(s);
                progressDialog.hide();
                if (s!=null){
                    try {
                        setCountryList(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(SignUp.this, "Internet Error!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try{
                    URL Url = new URL(WEB_URL+"/Android/getCountryList");
                    HttpURLConnection conn = (HttpURLConnection) Url.openConnection();

                    StringBuilder stringBuilder = new StringBuilder();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    String json;

                    while ((json = bufferedReader.readLine()) != null){
                        stringBuilder.append(json).append("\n");
                    }
                    return stringBuilder.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetCountryList getCountryList = new GetCountryList();
        getCountryList.execute();
    }

    //set Country list in Spinner

    private void setCountryList(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        country = new String[jsonArray.length()+1];
        country[0] = "-- Select Country --";
        for (int i = 1; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            country[i] = obj.getString("country_name");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, country);
        countrySpinner.setAdapter(arrayAdapter);
    }

    // onClick signup button

    protected void goToSingUp(){
        String TF_Name = F_Name.getText().toString().trim();
        String TL_Name = L_Name.getText().toString().trim();
        String TMobile = Mobile_Number.getText().toString().trim();
        String TEmail = Email.getText().toString().trim();
        String TPassword = Password.getText().toString().trim();
        String TGendar = GenderSpinner.getSelectedItem().toString();
        String TCountry = countrySpinner.getSelectedItem().toString();
        String TDOB =  DOB.getText().toString().trim();
        String Image = convertImage(image_data);

        if (TF_Name.length() < 3){
            Toast.makeText(SignUp.this, "Enter valid First Name!", Toast.LENGTH_SHORT).show();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(TEmail).matches()){
            Toast.makeText(SignUp.this, "Enter valid Email Address!", Toast.LENGTH_SHORT).show();
        }else if((!Patterns.PHONE.matcher(TMobile).matches()) || (TMobile.length() != 10)){
            Toast.makeText(SignUp.this, "Enter valid Mobile Number!", Toast.LENGTH_SHORT).show();
        }else if(TGendar.equals("-- Select Gender --")){
            Toast.makeText(SignUp.this, "Select Gender!", Toast.LENGTH_SHORT).show();
        }else if(TCountry.equals("-- Select Country --")){
            Toast.makeText(SignUp.this, "Select Country!", Toast.LENGTH_SHORT).show();
        }else if(TDOB.equals("Date of Birth")){
            Toast.makeText(SignUp.this, "Select Date of Birth!", Toast.LENGTH_SHORT).show();
        }else if(!is8char || !hasnum || !hasSpecialSymbol || !hasUpper){
            Toast.makeText(SignUp.this, "Enter valid Password!", Toast.LENGTH_SHORT).show();
        }else if(imageSelected != 1) {
            Toast.makeText(SignUp.this, "Select Image!", Toast.LENGTH_SHORT).show();
        }else {
            AttemptSignUP attemptSignUP = new AttemptSignUP();
            attemptSignUP.execute(TF_Name,TL_Name,TEmail,TMobile,TGendar, String.valueOf(IdOfCountry),TDOB,TPassword,Image);
        }
    }
   private class AttemptSignUP extends AsyncTask<String, String, JSONObject>{

        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(SignUp.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            if(args[4] == "Male"){
                args[4] = "M";
            }else{
                args[4] = "F";
            }

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("F_Name",args[0]));
            params.add(new BasicNameValuePair("L_Name",args[1]));
            params.add(new BasicNameValuePair("Email",args[2]));
            params.add(new BasicNameValuePair("Mobile_Number",args[3]));
            params.add(new BasicNameValuePair("Gender",args[4]));
            params.add(new BasicNameValuePair("Country",args[5]));
            params.add(new BasicNameValuePair("DOB",args[6]));
            params.add(new BasicNameValuePair("Password",args[7]));
            params.add(new BasicNameValuePair("Image",args[8]));

            String var = String.valueOf(Uri.parse(WEB_URL+"/Android/SignUpActivity/"));
            JSONObject json = jsonParser.makeHttpRequest(var, "POST",params);

            return json;
        }
         protected void onPostExecute(JSONObject result){
            progressDialog.hide();
            try {
                if (result != null) {

                    if (Integer.parseInt(result.getString("success")) == 1) {
                        SharedPreference sharedPreference = new SharedPreference();
                        sharedPreference.setURL(SignUp.this, result.getString("URL"));
                        Toast.makeText(SignUp.this, result.getString("success_msg"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUp.this, Home.class));
                        finish();
                    } else {
                        Toast.makeText(SignUp.this, result.getString("success_msg"), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignUp.this, "Unable to retrieve any data from server!"+result, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
         }
    }

    // on Password Change method

    private void passwordChange() {
        Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password = Password.getText().toString().trim();
                cardViewColor();
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //Show the message of Password Validation

    @SuppressLint("ResourceType")
    private void cardViewColor() {
        //upper case
        if(password.matches("(.*[A-Z].*)"))
        {
            hasUpper = true;
            //number
            if(password.matches("(.*[0-9].*)"))
            {
                hasnum = true;
                //symbol
                if(password.matches("^(?=.*[_.()$&@]).*$"))
                {
                    hasSpecialSymbol = true;
                    // 6 character
                    if(password.length()>= 8)
                    {
                        is8char = true;

                        VT.setTextColor(Color.parseColor(getString(R.color.validCR_Color)));
                        VI.setImageResource(R.drawable.ic_valid);
                        VT.setText("All done!");
                    }else{
                        is8char = false;
                        VT.setTextColor(Color.parseColor(getString(R.color.invalidCR_Color)));
                        VI.setImageResource(R.drawable.ic_invalid);
                        VT.setText("8 character minimum!");
                    }
                }else{
                    hasSpecialSymbol = false;
                    VT.setTextColor(Color.parseColor(getString(R.color.invalidCR_Color)));
                    VI.setImageResource(R.drawable.ic_invalid);
                    VT.setText("One special number!");
                }
            }else{
                hasUpper = false;
                VT.setTextColor(Color.parseColor(getString(R.color.invalidCR_Color)));
                VI.setImageResource(R.drawable.ic_invalid);
                VT.setText("One number!");
            }
        }else{
            hasUpper = false;
            VT.setTextColor(Color.parseColor(getString(R.color.invalidCR_Color)));
            VI.setImageResource(R.drawable.ic_invalid);
            VT.setText("One uppercase character!");
        }

    }
}