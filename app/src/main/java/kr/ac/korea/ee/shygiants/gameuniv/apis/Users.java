package kr.ac.korea.ee.shygiants.gameuniv.apis;

import kr.ac.korea.ee.shygiants.gameuniv.models.RequestBody;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by SHYBook_Air on 15. 10. 5..
 */
public interface Users {

    @POST("/users")
    Call<User> signUp(@Body RequestBody body);

    @GET("/users/{email}")
    Call<User> getUser(@Path("email") String email, @Header("Authorization") String token);

}
