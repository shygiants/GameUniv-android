package kr.ac.korea.ee.shygiants.gameuniv.utils;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by SHYBook_Air on 15. 10. 5..
 */
public class RESTAPI {

    private final static String apiEndPoint = "172.30.82.83";
    // 학회실
//    private final static String apiEndPoint = "192.168.0.26";
    // 자취방
//    private final static String apiEndPoint = "192.168.0.4";
    private final static String port = "3000";

    private static RESTAPI ourInstance = new RESTAPI();

    public static <T> T create(final Class<T> service) {
        return ourInstance.retrofit.create(service);
    }

    private Retrofit retrofit;

    private RESTAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + apiEndPoint + ":" + port)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit.client().setFollowRedirects(false);
    }
}
