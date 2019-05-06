package com.example.maneeshsagar.imload;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    FirebaseStorage storage;
    StorageReference storageReference;
    private DatabaseReference mDatabase,uDatabase,kDatabase;

    private Button btnChoose, btnUpload,uploaded;
    private ImageView imageView;
    private EditText vehilceNo;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;
    String foldername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();

        SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        foldername=sharedpreferences.getString(Login.Email,"")+"/";
        SharedPreferences.Editor editor = sharedpreferences.edit();

        btnChoose = (Button) findViewById(R.id.btnChoose);
        uploaded = (Button) findViewById(R.id.uploaded_images);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        imageView = (ImageView) findViewById(R.id.imgView);
        vehilceNo=(EditText)findViewById(R.id.vehicle_no);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

     //   mDatabase = FirebaseDatabase.getInstance().getReference(foldername);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        uploaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImages();
            }
        });
    }

    private void showImages() {
        Intent intent=new Intent(this,ShowImage.class);
        intent.putExtra(Login.FOLDERNAME,foldername);
        startActivity(intent);
    }


    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void uploadImage() {
        String veh=vehilceNo.getText().toString();
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final File file= new File(filePath.getPath());


            final StorageReference ref = storageReference.child(veh+"/"+ UUID.randomUUID().toString()+"."+getFileExtension(filePath));
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        String url;
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();

                            final String sdownload_url = String.valueOf(downloadUrl);
                            uDatabase=FirebaseDatabase.getInstance()
                                    .getReference("vehicle/");
                            mDatabase=FirebaseDatabase.getInstance()
                                    .getReference("vehicle-image/").child(veh);
                            kDatabase=FirebaseDatabase.getInstance()
                                    .getReference("user-image/").child(foldername);


                            uDatabase.orderByChild("number").equalTo(veh).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(!dataSnapshot.exists())
                                    {
                                        String uplaodIdVeh=uDatabase.push().getKey();
                                        Vehicle vehicle=new Vehicle(veh);
                                        uDatabase.child(uplaodIdVeh).setValue(vehicle);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                          //  System.out.println("URL= "+downloadUrl.toString());
                            String uploadId1 = kDatabase.push().getKey();
                            String uploadId2=mDatabase.push().getKey();

                            Upload upload1 = new Upload(file.getName(),downloadUrl.toString(),uploadId1);
                            Upload upload2=new Upload(file.getName(),downloadUrl.toString(),uploadId2);
                            kDatabase.child(uploadId1).setValue(upload1);
                            mDatabase.child(uploadId2).setValue(upload2);
                            Toast.makeText(MainActivity.this, url, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }


    }
    public String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));

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
        android.support.v7.widget.Toolbar toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.include);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FavMovie");
    }


}
