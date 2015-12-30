package io.github.shygiants.gameuniv.apis;

import java.util.ArrayList;

import io.github.shygiants.gameuniv.models.Moment;
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
    Call<ArrayList<Moment>> getTimelineForUser(@Path("userEmail") String email, @Header("Authorization") String authToken);

    @GET("/moments/timeline/games/{gameId}")
    Call<ArrayList<Moment>> getTimelineForGame(@Path("gameId") String gameId, @Header("Authorization") String authToken);
}
