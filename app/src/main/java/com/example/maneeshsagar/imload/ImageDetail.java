package com.example.maneeshsagar.imload;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageDetail extends AppCompatActivity {



    ImageView Picture;
    @BindView(R.id.delete)
    Button button;
    String key;
    Intent intent;
    String foldername;
    int k=0;
    String url="";
    DatabaseReference mDatabaserefrence;
    StorageReference photoRef ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        ButterKnife.bind(this);
        foldername=getIntent().getStringExtra("foldername");
        photoRef=FirebaseStorage.getInstance().getReferenceFromUrl(getIntent().getStringExtra("url"));
        mDatabaserefrence=FirebaseDatabase.getInstance().getReference(getIntent().getStringExtra("foldername"));
        Picture=findViewById(R.id.imageView2);
        key=getIntent().getStringExtra("key");

        Picasso.get().load(getIntent().getStringExtra("url")).into(Picture);

    }

    @OnClick(R.id.delete)
    public void deletePhoto()
    {
      //  Toast.makeText(this,"Tapped",Toast.LENGTH_LONG).show();
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                mDatabaserefrence.child(key).removeValue();
                k=1;
                backGo();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!

            }
        });
        if(k==1)
        {
            Toast.makeText(this,"Deleted",Toast.LENGTH_LONG).show();
        }
        else if(k==0)
        {
            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
        }


    }

    private void backGo() {
        Intent intent=new Intent(this,AdminShowImage.class);
        intent.putExtra("foldername",foldername);
        startActivity(intent);
        finish();
    }
}
