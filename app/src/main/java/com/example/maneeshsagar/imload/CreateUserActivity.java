package com.example.maneeshsagar.imload;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateUserActivity extends AppCompatActivity {


    public static final String USER_PATH="user/";
    @BindView(R.id.name_creation) EditText Name;
    @BindView(R.id.email_creation) EditText Email;
    @BindView(R.id.password_creation) EditText Password;
    @BindView(R.id.create) Button Create;
    private String mName;
    private String mEmail;
    private String mPassword;
    DatabaseReference mDatabaserefrence;
    List<User> mUser;
    List<String > mEmailList;
    int k=0;
    int count=0;
    String gotEmial="Registered Successfully";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        setToolbar();
        ButterKnife.bind(this);
        mUser=new ArrayList<>();
        mEmailList=new ArrayList<>();
        mDatabaserefrence=FirebaseDatabase.getInstance().getReference("user/");
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
        android.support.v7.widget.Toolbar toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.tcu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Imload");
    }
    @OnClick(R.id.create)
    public void create() {
        count++;
        mName=Name.getText().toString();
        mEmail=Email.getText().toString();
        mPassword=Password.getText().toString();
        if(mName!=null && mEmail !=null & mPassword!=null)
        {
         /*   mDatabaserefrence.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //dismissing the progress dialog

                    //iterating through all the values in database
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        User upload = postSnapshot.getValue(User.class);
                        mUser.add(upload);
                        mEmailList.add(upload.getEmail());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/

            mDatabaserefrence.orderByChild("email").equalTo(mEmail).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists())
                    {
                        k=1;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        /*    if(mEmailList.contains(mEmail))
            {
                Toast.makeText(this,"Already Registred",Toast.LENGTH_LONG).show();
            }
            else {
                register(mName,mEmail,mPassword);

                System.out.println(mEmailList);

            }*/
          if(k==1 && count>=2)
          {
              Toast.makeText(this,"Already Registred",Toast.LENGTH_LONG).show();
          }
          else if(k==0 && count>=2){
              register(mName,mEmail,mPassword);
              Toast.makeText(this,"Succesfully  Registred",Toast.LENGTH_LONG).show();
          }
        }
        else
        {
            Toast.makeText(this,"Every field Must Filled",Toast.LENGTH_LONG).show();
        }

    }


    public void register(String name,String email,String password) {
        String uploadId = mDatabaserefrence.push().getKey();
        mDatabaserefrence.child(uploadId).setValue(new User(name,email,password,uploadId));
        Toast.makeText(this,gotEmial,Toast.LENGTH_LONG).show();
    }
}
