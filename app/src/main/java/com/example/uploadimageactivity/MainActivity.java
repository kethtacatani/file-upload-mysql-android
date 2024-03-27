package com.example.uploadimageactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button uploadButton, camButton;
    private Bitmap bitmap;
    private String selectedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        /**
         *
         *
         *
         *
         *
         * ====================================================
        //BEFORE ANYTHING ELSE PLEASE CONFIGURE
        //HttpService.java and RetroBuilder.java

         // CONFIGURE db_config.php and upload_api.php


         ==========================================================
              ^
             ***
            *****
           *******
          *********
         ***********
            *****
            *****
            *****


         **/


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        uploadButton = findViewById(R.id.uploadButton);
        camButton = findViewById(R.id.camButton);

        imageView.setOnClickListener(new View.OnClickListener() { //kani siya ang image view ang e click para mo open sa gallery
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFileToServer();
            }
        });

        camButton.setOnClickListener(new View.OnClickListener() { //para mu open sa cam
            @Override
            public void onClick(View v) {
                Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePic, 0);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode !=RESULT_CANCELED) {

            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap image = (Bitmap) data.getExtras().get("data");
                        selectedImage = FileUtils.getPath(MainActivity.this, getImageUri(MainActivity.this, image));
                        imageView.setImageBitmap(image);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {

                        Uri image = data.getData();
                        selectedImage = FileUtils.getPath(MainActivity.this, image);
                        Picasso.get().load(image).into(imageView);
                    }
            }
        }
    }

    public Uri getImageUri(Context context, Bitmap bitmap){ // magamit ni siya if mag pic ka gamit cam
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "myImage",""); //change the image title sa cam rani nga change name
        return Uri.parse(path);                                                                                         // ug sa gallery ang name sa file iya sundon kamo na diskarte
    }

    public void uploadFileToServer(){ //important function

        File file = new File(Uri.parse(selectedImage).getPath());

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("sendimage",file.getName(),requestBody);

        HttpService service = RetrofitBuilder.getClient().create(HttpService.class);

        Call<FileModel> call = service.callUploadApi(filePart);
        call.enqueue(new Callback<FileModel>() {
            @Override
            public void onResponse(Call<FileModel> call, Response<FileModel> response) {
                FileModel fileModel = response.body();
                Toast.makeText(MainActivity.this, fileModel.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<FileModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }



}