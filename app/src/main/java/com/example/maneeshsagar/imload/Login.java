package com.example.maneeshsagar.imload;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
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

public class Login extends AppCompatActivity {




    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Password = "passwordKey";
    public static final String Email = "emailKey";
    public static final String FOLDERNAME="foldername";
    SharedPreferences sharedpreferences;


    @BindView(R.id.email)
    EditText mEmail;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.login)
    Button mLogin;
    @BindView(R.id.option)
    CheckBox UserType;
    DatabaseReference mDatabaserefrence;
    List<User> mUser;
    List<String> email;
    List<String> password;
    String cEmail;
    String cPassword;
    int k=0;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setToolbar();
        mDatabaserefrence=FirebaseDatabase.getInstance().getReference("user/");
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


    }


    void setToolbar()
    {
        android.support.v7.widget.Toolbar toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.t1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Imload");
    }
    @OnClick(R.id.login)
    public void login()
    {
        cEmail=mEmail.getText().toString();
        cPassword=mPassword.getText().toString();
        mUser=new ArrayList<>();
        email=new ArrayList<>();
        password=new ArrayList<>();

        if(UserType.isChecked())
        {
        //   if(mEmail.getText().toString().equals("admin")&&mPassword.getText().toString().equals("12345"))
        //   {
               Intent intent=new Intent(this,AdminSuccess.class);
               startActivity(intent);
               finish();
         //  }
       }
       else {
     count++;
            mDatabaserefrence.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //dismissing the progress dialog
                     System.out.println("Helooo its me www.google.com");
                    //iterating through all the values in database
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        User upload = postSnapshot.getValue(User.class);
                        mUser.add(upload);
                        email.add(upload.getEmail());
                        password.add(upload.getPassword());
                        if(upload.getEmail().equals(cEmail) && upload.getPassword().equals(cPassword))
                        {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Email,upload.getEmail());
                            editor.putString(Password,upload.getPassword());
                            editor.commit();
                            k=1;
                            System.out.println("Helooo its me www.google.com");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


           if(k==1 && count>=2)
           {
               Toast.makeText(this, "Welcome", Toast.LENGTH_LONG).show();
               Intent intent=new Intent(this,MainActivity.class);
               startActivity(intent);
                finish();

           }
           else if(count>=2){
               Toast.makeText(this, "Enter Valid Credentials", Toast.LENGTH_SHORT).show();
           }
        }
    }
}
