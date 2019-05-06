package com.example.maneeshsagar.imload;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SpalshScreen extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_spalsh_screen);
        SharedPreferences sharedPreferences=getSharedPreferences(Login.MyPREFERENCES,Context.MODE_PRIVATE);
        String username=sharedPreferences.getString(Login.Email,null);
        String password=sharedPreferences.getString(Login.Password,null);
        if(username==null)
        {
           intent=new Intent(this,Login.class);
        }
        else if(username.equals("admin") && password.equals("1234") ){
            intent =new Intent(this,AdminSuccess.class);
        }
        else {
             intent=new Intent(this,MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
