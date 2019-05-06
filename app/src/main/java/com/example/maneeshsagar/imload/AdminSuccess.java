package com.example.maneeshsagar.imload;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdminSuccess extends AppCompatActivity {

    @BindView(R.id.user_creation) Button CreateUser;
    @BindView(R.id.view_images) Button ViewImages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_success);
        SharedPreferences sharedPreferences=getSharedPreferences(Login.MyPREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(Login.Email,"admin");
        editor.putString(Login.Password,"1234");
        editor.commit();
        setToolbar();
        ButterKnife.bind(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int d=item.getItemId();
        if(d==R.id.logout1)
        {
            SharedPreferences sharedPreferences=getSharedPreferences(Login.MyPREFERENCES,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent=new Intent(this,Login.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.user_creation)
    public void createUser()
    {
        Intent intent=new Intent(this,CreateUserActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.view_images)
    public void ViewImages()
    {
        Intent intent=new Intent(this,ShowUser.class);
        startActivity(intent);
    }
    void setToolbar()
    {
        android.support.v7.widget.Toolbar toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.include4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Imload");
    }
}
