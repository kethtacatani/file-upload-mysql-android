package com.example.uploadimageactivity;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface HttpService {

    @Multipart
    @POST("RestApi/upload_api.php") //replace Restapi with the name of the folder in htdocs
    Call<FileModel> callUploadApi(@Part MultipartBody.Part image);

    @Multipart
    @POST("RestApi/multi_upload.php") //this as well
    Call<FileModel> callMultipleUploadApi(@Part List<MultipartBody.Part> image);
}
