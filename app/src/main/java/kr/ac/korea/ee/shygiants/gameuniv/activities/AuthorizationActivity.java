package kr.ac.korea.ee.shygiants.gameuniv.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.apis.AuthCodes;
import kr.ac.korea.ee.shygiants.gameuniv.models.Response;
import kr.ac.korea.ee.shygiants.gameuniv.utils.Owner;
import kr.ac.korea.ee.shygiants.gameuniv.utils.RESTAPI;
import retrofit.Callback;
import retrofit.Retrofit;

public class AuthorizationActivity extends AppCompatActivity {

    private static final String GAME_LOGIN = "gameLogin";
    private static final String GAME_ID = "gameId";

    public static final String AUTH_CODE = "AUTH_CODE";

    // APIs
    private AuthCodes authCodesAPI = RESTAPI.create(AuthCodes.class);

    private String gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Uri data = intent.getData();
        String authority = data.getAuthority();
        gameId = data.getQueryParameter(GAME_ID);

        if (authority.equals(GAME_LOGIN)
            && !TextUtils.isEmpty(gameId)) {
            // TODO: prompt user
            setContentView(R.layout.activity_authorization);
        } else {
            fail();
        }
    }

    public void getAuthCode(View view) {
        // TODO: get auth code
        // TODO: if there is no auth token prompt user
        authCodesAPI.getAuthCode(Owner.getEmail(), Owner.getToken(), gameId, "code")
                .enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(retrofit.Response<Response> response, Retrofit retrofit) {

                        switch (response.code()) {
                            case 400: // Bad Request
                                // TODO: Fancy error handling
                                break;
                            case 500:
                                // TODO: Server error handling
                                break;
                            default:
                                Uri location = Uri.parse(response.headers().get("Location"));
                                returnAuthCode(location.getQueryParameter("code"));

                                break;
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // TODO: Something wrong on network
                        t.printStackTrace();
                    }
                });
    }

    private void returnAuthCode(String authCode) {
        Intent intent = new Intent();
        intent.putExtra(AUTH_CODE, authCode);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void fail() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
