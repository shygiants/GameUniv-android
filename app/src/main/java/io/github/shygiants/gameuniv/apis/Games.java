package io.github.shygiants.gameuniv.apis;

import com.squareup.okhttp.RequestBody;

import java.util.ArrayList;

import io.github.shygiants.gameuniv.models.Game;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;

/**
 * Created by SHYBook_Air on 15. 11. 24..
 */
public interface Games {
    @GET("/games/{gameId}")
    Call<Game> getGame(@Header("Authorization") String authToken, @Path("gameId") String gameId);

    @GET("/games")
    Call<ArrayList<Game>> getAllGames();

    @Multipart
    @PUT("/games/contents")
    Call<String> postContents(
            @Part("page_photo\"; filename=\"page_photo.png\" ") RequestBody photos
    );
}
