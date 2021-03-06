package com.example.maneeshsagar.imload;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminShowImage extends AppCompatActivity {
    //recyclerview object
    private RecyclerView recyclerView;

    //adapter object
    private RecyclerView.Adapter adapter;

    //database reference
    private DatabaseReference mDatabase;

    //progress dialog
    private ProgressDialog progressDialog;

    //list to hold all the uploaded images
    private List<Upload> uploads;

    String foldername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        setToolbar();
        foldername=getIntent().getStringExtra(Login.FOLDERNAME);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

       /* SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        foldername=sharedpreferences.getString(Login.Email,"")+"/";*/
        //  SharedPreferences.Editor editor = sharedpreferences.edit();
        progressDialog = new ProgressDialog(this);

        uploads = new ArrayList<>();

        //displaying progress dialog while fetching images
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        mDatabase = FirebaseDatabase.getInstance().getReference("user-image/").child(foldername);

        //adding an event listener to fetch values
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //dismissing the progress dialog
                progressDialog.dismiss();

                //iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploads.add(upload);
                }
                //creating adapter
                adapter = new AdminImageAdapter(getApplicationContext(), uploads,foldername);

                //adding adapter to recyclerview
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

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

    void setToolbar()
    {
        android.support.v7.widget.Toolbar toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.include2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Imload");
    }
}

