package kr.ac.korea.ee.shygiants.gameuniv.apis;

import java.util.ArrayList;

import kr.ac.korea.ee.shygiants.gameuniv.models.Game;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;

/**
 * Created by SHYBook_Air on 15. 11. 24..
 */
public interface Games {
    @GET("/games/{gameId}")
    Call<Game> getGame(@Header("Authorization") String authToken, @Path("gameId") String gameId);

    @GET("/games")
    Call<ArrayList<Game>> getAllGames();
}
