package io.github.shygiants.gameuniv.apis;

import io.github.shygiants.gameuniv.models.RequestBody;
import io.github.shygiants.gameuniv.models.User;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;

/**
 * Created by SHYBook_Air on 15. 10. 5..
 */
public interface Users {

    @POST("/users")
    Call<User> signUp(@Body RequestBody body);

    @GET("/users/{email}")
    Call<User> getUser(@Path("email") String email, @Header("Authorization") String token);

    @Multipart
    @PUT("/users/{email}/profilePhotos")
    Call<String> uploadProfilePhoto(
            @Path("email") String email,
            @Part("profile_photo\"; filename=\"profile.png\" ") com.squareup.okhttp.RequestBody photoFile,
            @Header("Authorization") String token);
}