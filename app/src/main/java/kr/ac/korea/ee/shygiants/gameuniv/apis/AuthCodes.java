package kr.ac.korea.ee.shygiants.gameuniv.apis;

import kr.ac.korea.ee.shygiants.gameuniv.models.Response;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by SHYBook_Air on 15. 11. 15..
 */
public interface AuthCodes {

    @GET("/authCodes/{email}")
    Call<Response> getAuthCode(@Path("email") String email, @Header("Authorization") String token, @Query("client_id") String clientId, @Query("response_type") String responseType);
}
