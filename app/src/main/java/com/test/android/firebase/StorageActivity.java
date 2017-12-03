package com.test.android.firebase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class StorageActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView imageView;

    private TextView imageLoadingView;

    private Button loadFromStorage;
    private Button saveToStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        createComponents();
        initComponents();
    }

    private void createComponents() {
        this.imageView = findViewById(R.id.imageFromStorage);
        this.imageLoadingView = findViewById(R.id.imageLoadingView);
        this.loadFromStorage = findViewById(R.id.loadFromStorageBtn);
        this.saveToStorage = findViewById(R.id.saveToStorageBtn);
    }

    private void initComponents() {
        initLoadFromStorage();
        initSaveToStorage();
    }

    private void initLoadFromStorage() {

        this.loadFromStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLoadingView.setText("Start Loading");
                try {
                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                    StorageReference storageRef = firebaseStorage.getReference();
                    StorageReference pathReference = storageRef.child("camera-result.jpg");

                    final long ONE_MEGABYTE = 1024 * 1024;
                    pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            if (bytes.length > 0) {
                                Bitmap myBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                imageView.setImageBitmap(myBitmap);
                                imageLoadingView.setText("Image loaded!");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initSaveToStorage() {

        this.saveToStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLoadingView.setText("Open Camera");
                dispatchTakePictureIntent();
            }
        });
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            imageLoadingView.setText("Saving image");

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageRef = firebaseStorage.getReference();
            StorageReference mountainsRef = storageRef.child("camera-result.jpg");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            boolean compress = imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] bytes = baos.toByteArray();

            UploadTask uploadTask = mountainsRef.putBytes(bytes);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Handle Success
                    imageLoadingView.setText("Done!");
                }
            });
        }
    }
}
