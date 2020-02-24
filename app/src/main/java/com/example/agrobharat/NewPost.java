package com.example.agrobharat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.firebase.client.Firebase;
import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import 	androidx.appcompat.widget.Toolbar;

public class NewPost extends AppCompatActivity {

    Toolbar toolbar;
    Button submit;
    ImageView postimage;
    EditText postdesc;
    TextView desctext;
    static String langcheck,username;

    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference firebaseStoragedown;

    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    String imageUrl;

    String millisInString;
    public static String uniqueID;
    private static final Random random = new Random();
    private static final String CHARS = "1234567890";

    private FirebaseFirestore firestore;
    String description;
    StorageReference refurl;


    private static final String KEY_USERNAME = "username";
    private static final  String KEY_TIME = "timeStamp";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGEURL = "imageURL";

    Map<String,Object> note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        submit =  (Button) findViewById(R.id.addpost_submit);
        postimage = (ImageView)findViewById(R.id.addpost_image);
        postdesc = (EditText)findViewById(R.id.addpost_desc);
        desctext = (TextView) findViewById(R.id.addpost_textview);
        langcheck = MainActivity.language;
        username = LoginActivity.username_handle;
        setSupportActionBar(toolbar);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseStoragedown = FirebaseStorage.getInstance().getReference();

        firestore = FirebaseFirestore.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        millisInString  = dateFormat.format(new Date());

        switch (langcheck){

            case "tamil":
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_tamil_newpost));
                desctext.setText(getResources().getString(R.string.adddesc_tamil));
                submit.setText(getResources().getString(R.string.addpost_tamil));
                break;

            case "hindi":
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_hindi_newpost));
                desctext.setText(getResources().getString(R.string.adddesc_hindi));
                submit.setText(getResources().getString(R.string.addpost_hindi));
                break;

            case "english":
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_english_newpost));
                desctext.setText(getResources().getString(R.string.adddesc_english));
                submit.setText(getResources().getString(R.string.addpost_english));
                break;

        }

        postimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getToken(6);
                chooseImage();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                description = postdesc.getText().toString();
                refurl = firebaseStoragedown.child(langcheck).child(uniqueID);
                refurl.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUrl = uri.toString();

                    }
                });

                String url = imageUrl;
                System.out.println(url);
                note = new HashMap<>();
                note.put(KEY_USERNAME,LoginActivity.username_handle);
                note.put(KEY_TIME,millisInString);
                note.put(KEY_DESCRIPTION,description);
                note.put(KEY_IMAGEURL,url);


                if(uniqueID!=null)
                {
                    uniqueID = uniqueID;
                }
                else
                {
                    getToken(6);
                }

                firestore.collection(langcheck).document(uniqueID).set(note)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                switch (langcheck){

                                    case "tamil":
                                        Toast.makeText(NewPost.this,getResources().getString(R.string.addpost_tamil_success),Toast.LENGTH_SHORT).show();
                                        break;

                                    case "hindi":
                                        Toast.makeText(NewPost.this,getResources().getString(R.string.addpost_hindi_success),Toast.LENGTH_SHORT).show();
                                        break;

                                    case "english":
                                        Toast.makeText(NewPost.this,getResources().getString(R.string.addpost_english_success),Toast.LENGTH_SHORT).show();
                                        break;

                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                switch (langcheck){

                                    case "tamil":
                                        Toast.makeText(NewPost.this,getResources().getString(R.string.addpost_tamil_failure),Toast.LENGTH_SHORT).show();
                                        break;

                                    case "hindi":
                                        Toast.makeText(NewPost.this,getResources().getString(R.string.addpost_hindi_failure),Toast.LENGTH_SHORT).show();
                                        break;

                                    case "english":
                                        Toast.makeText(NewPost.this,getResources().getString(R.string.addpost_english_failure),Toast.LENGTH_SHORT).show();
                                        break;

                                }
                            }
                        });


            }


        });



    }


    public void getToken(int length) {
        StringBuilder token = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            token.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        uniqueID = token.toString();
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
                postimage.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child(langcheck+"/"+uniqueID);

            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    progressDialog.dismiss();
                    Toast.makeText(NewPost.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(NewPost.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());

                        }
                    });



        }
    }



}
