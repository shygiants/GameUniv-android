package kr.ac.korea.ee.shygiants.gameuniv.apis;

import java.util.ArrayList;

import kr.ac.korea.ee.shygiants.gameuniv.models.Moment;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;

/**
 * Created by SHYBook_Air on 15. 11. 19..
 */
public interface Moments {
    @GET("/moments/feed/users/{userEmail}")
    Call<ArrayList<Moment>> getFeed(@Path("userEmail") String email, @Header("Authorization") String authToken);

    @GET("/moments/timeline/users/{userEmail}")
    Call<ArrayList<Moment>> getTimeline(@Path("userEmail") String email, @Header("Authorization") String authToken);
}
