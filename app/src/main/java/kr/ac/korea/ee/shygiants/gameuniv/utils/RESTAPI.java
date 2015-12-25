package kr.ac.korea.ee.shygiants.gameuniv.utils;

import kr.ac.korea.ee.shygiants.gameuniv.apis.Games;
import kr.ac.korea.ee.shygiants.gameuniv.apis.Moments;
import kr.ac.korea.ee.shygiants.gameuniv.apis.Users;
import kr.ac.korea.ee.shygiants.gameuniv.apis.Tokens;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by SHYBook_Air on 15. 10. 5..
 */
public class RESTAPI {

//    private final static String apiEndPoint = "172.30.21.55";
    // 학회실
//    private final static String apiEndPoint = "192.168.0.26";
    // 자취방
    private final static String apiEndPoint = "192.168.0.7";
    private final static String port = "3000";

    private static RESTAPI ourInstance = new RESTAPI();

    public static Users Users = create(Users.class);
    public static Moments Moments = create(Moments.class);
    public static Games Games = create(Games.class);
    public static Tokens Tokens = create(Tokens.class);

    private static <T> T create(final Class<T> service) {
        return ourInstance.retrofit.create(service);
    }

    public static String getURL() {
        return "http://" + apiEndPoint + ":" + port;
    }

    private Retrofit retrofit;

    private RESTAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(getURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit.client().setFollowRedirects(false);
    }
}
