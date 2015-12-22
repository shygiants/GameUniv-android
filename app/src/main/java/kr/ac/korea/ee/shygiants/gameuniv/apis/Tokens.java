package kr.ac.korea.ee.shygiants.gameuniv.apis;

import kr.ac.korea.ee.shygiants.gameuniv.models.AuthToken;
import kr.ac.korea.ee.shygiants.gameuniv.models.RequestBody;
import kr.ac.korea.ee.shygiants.gameuniv.models.Response;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by SHYBook_Air on 2015. 12. 22..
 */
public interface Tokens {
    @GET("/tokens/authCodes/{email}")
    Call<Response> getAuthCode(@Path("email") String email, @Header("Authorization") String token, @Query("client_id") String clientId, @Query("response_type") String responseType);

    @POST("/tokens/authTokens/{email}")
    Call<AuthToken> getAuthToken(@Path("email") String email, @Body RequestBody body);
}
