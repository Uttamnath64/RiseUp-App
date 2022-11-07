package com.lit.riseup;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SignIn extends AppCompatActivity {

    ImageButton Back_Btn;
    Button SignIn_Btn, SignUp_Btn, Forgot_Btn;
    EditText Mobile_Number, Password;
    TextView VT;
    ImageView VI;
    Conn conn = new Conn();
    String WEB_URL = conn.getUrl();
    private  String password;
    ProgressDialog progressDialog;
    JSONParser jsonParser = new JSONParser();
    private  boolean is8char=false, hasUpper=false, hasnum=false, hasSpecialSymbol =false, isSignupClickable = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        createObject();


        // password on change
        passwordChange();
        // SignUp Button OnClick
        SignUp_Btn.setOnClickListener(View ->{
            startActivity(new Intent(SignIn.this, SignUp.class));
        });

        // SignIn Button OnClick
        SignIn_Btn.setOnClickListener(View -> {
            onClickSignInB();
        });



    }

    private void createObject() {
        // image button
        Back_Btn = findViewById(R.id.back_btn);

        // button
        SignIn_Btn = findViewById(R.id.signin_btn);
        SignUp_Btn = findViewById(R.id.signup_btn);
        Forgot_Btn = findViewById(R.id.forgot_btn);

        // valid data
        VI = findViewById(R.id.valid_icon);
        VT = findViewById(R.id.valid_text);

        // edit text
        Mobile_Number = findViewById(R.id.mobile_number);
        Password = findViewById(R.id.password);
    }

    protected void onClickSignInB(){
        String TMobile = Mobile_Number.getText().toString();
        String TPassword = Password.getText().toString();
        if (!Patterns.PHONE.matcher(TMobile).matches() || TMobile.length() != 10){
            Toast.makeText(SignIn.this, "Enter valid Mobile Number!", Toast.LENGTH_SHORT).show();
        }else if(!is8char || !hasnum || !hasSpecialSymbol || !hasUpper){
            Toast.makeText(SignIn.this, "Enter valid Password!", Toast.LENGTH_SHORT).show();
        }else{
            AttemptSignIn attemptSignIn = new AttemptSignIn();
            attemptSignIn.execute(TMobile,TPassword);
        }
    }

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

    private class AttemptSignIn extends AsyncTask<String, String, JSONObject> {

        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(SignIn.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("Mobile_Number",args[0]));
            params.add(new BasicNameValuePair("Password",args[1]));


            String var = String.valueOf(Uri.parse(WEB_URL+"/Android/SignInActivity/"));
            JSONObject json = jsonParser.makeHttpRequest(var, "POST",params);

            return json;
        }
        protected void onPostExecute(JSONObject result){
            progressDialog.hide();
            try {

                if (result != null) {

                    if (Integer.parseInt(result.getString("success")) == 1) {
                        SharedPreference sharedPreference = new SharedPreference();
                        sharedPreference.setURL(SignIn.this, result.getString("URL"));
                        Toast.makeText(SignIn.this, result.getString("success_msg"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignIn.this, Home.class));
                        finish();
                    } else {
                        Toast.makeText(SignIn.this, result.getString("success_msg"), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignIn.this, "Unable to retrieve any data from server!"+result, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
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
                    if(password.length()>= 6)
                    {
                        is8char = true;

                        VT.setTextColor(Color.parseColor(getString(R.color.validCR_Color)));
                        VI.setImageResource(R.drawable.ic_valid);
                        VT.setText("All done!");
                    }else{
                        is8char = false;
                        VT.setTextColor(Color.parseColor(getString(R.color.invalidCR_Color)));
                        VI.setImageResource(R.drawable.ic_invalid);
                        VT.setText("6 character minimum!");
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