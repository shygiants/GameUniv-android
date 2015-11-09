package kr.ac.korea.ee.shygiants.gameuniv.apis;

import kr.ac.korea.ee.shygiants.gameuniv.models.AuthToken;
import kr.ac.korea.ee.shygiants.gameuniv.models.RequestBody;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by SHYBook_Air on 15. 10. 5..
 */
public interface AuthTokens {

    @POST("/authTokens/{email}")
    Call<AuthToken> getAuthToken(@Path("email") String email, @Body RequestBody body);
}
