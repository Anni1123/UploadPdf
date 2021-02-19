package com.anni.uploadpdf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ImageView upload;
    Uri imageuri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        upload=findViewById(R.id.uploadpdf);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("application/pdf");
                startActivityForResult(galleryIntent, 1);
            }
        });
    }

    ProgressDialog dialog;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            dialog=new ProgressDialog(this);
            dialog.setMessage("Uploading");
            dialog.show();
                imageuri=data.getData();
                final String timestamp=""+System.currentTimeMillis();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                final String messagePushID = timestamp;
                Toast.makeText(MainActivity.this,imageuri.toString(),Toast.LENGTH_SHORT).show();
                final StorageReference filepath = storageReference.child(messagePushID + "." + "pdf");
                Toast.makeText(MainActivity.this,filepath.getName(),Toast.LENGTH_SHORT).show();
            filepath.putFile(imageuri)
                    .continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return filepath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                            Uri uri = task.getResult();
                            String myurl;
                            myurl = uri.toString();
                            Toast.makeText(MainActivity.this,"Uploaded Successfully",Toast.LENGTH_SHORT).show();
                        }else {
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this,"UploadedFailed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }
    }


