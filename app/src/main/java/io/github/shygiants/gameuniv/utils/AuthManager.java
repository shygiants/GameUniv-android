package io.github.shygiants.gameuniv.utils;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.activities.MainActivity;
import io.github.shygiants.gameuniv.app.GameUniv;
import io.github.shygiants.gameuniv.models.Response;
import io.github.shygiants.gameuniv.models.User;
import retrofit.Call;

/**
 * Created by SHYBook_Air on 15. 11. 10..
 */
public class AuthManager {

    private static AuthManager singleton;

    // Constants
    String ACCOUNT_TYPE;
    String FULL_ACCESS;

    // Context
    private Context context;
    private AccountManager accountManager;
    private Activity activity;

    // For getting user info
    private String email;
    private String authToken;

    private Callback<User> userCallback;
    private User user;

    private AuthManager(Context context) {
        this.context = context;
        ACCOUNT_TYPE = context.getString(R.string.auth_account_type);
        FULL_ACCESS = context.getString(R.string.auth_full_access);
        accountManager = AccountManager.get(context);
    }

    public static void init(Context context) {
        if (singleton == null) singleton = new AuthManager(context);
    }

    public static AuthManager getInstance() {
        // TODO: Throw exception
        if (singleton == null) return null;
        return singleton;
    }

    public void registerCallback(Callback<User> userCallback) {
        if (user != null) userCallback.pass(user);
        else this.userCallback = userCallback;
    }

    public void requestAuthToken() {
        if (activity != null) return;
        activity = ((GameUniv)context.getApplicationContext()).getCurrentActivity();
        getAuthToken();
    }

    private void getAuthToken() {
        accountManager.getAuthTokenByFeatures(ACCOUNT_TYPE, FULL_ACCESS, null, activity, null, null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        // HERE IS NOT MAIN THREAD!
                        Bundle bundle;
                        try {
                            bundle = future.getResult();
                            authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                            email = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);

                            Log.i("AuthManager", "Email \'" + email + "\' is initialized");
                            // TODO: delete this log from the product
                            Log.i("AuthManager", "Auth token \'" + authToken + "\' is initialized");

                            getUserInfo();
                        } catch (OperationCanceledException e) {
                            // TODO: Exception handling
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (AuthenticatorException e) {
                            e.printStackTrace();
                        }
                    }
                }, null);
    }

    private void getUserInfo() {
        Call<User> task = RESTAPI.Users.getUser(email, authToken);
        NetworkTask<User> getUser = new NetworkTask.Builder<>(task)
                .onSuccess(new NetworkTask.OnSuccessListener<User>() {
                    @Override
                    public void onSuccess(User responseBody) {
                        user = responseBody;
                        user.setAuthToken(authToken);
                        if (activity instanceof MainActivity)
                            ContentsStore.initFeed(user);
                        Log.i("AuthManager", "User is initialized");
                        if (userCallback != null)
                            userCallback.pass(user);
                    }
                })
                .showSnackBar(false)
                .on(401, new NetworkTask.OnResponseListener<User>() {
                    @Override
                    public void on(int statusCode, retrofit.Response<User> response) {
                        accountManager.invalidateAuthToken(ACCOUNT_TYPE, authToken);
                        getAuthToken();
                    }
                })
                .build();
        getUser.execute();
    }

    public void getAuthCode(String gameId, final Callback<String> callback) {
        if (email == null || authToken == null) callback.pass(null);

        Call<Response> task = RESTAPI.Tokens.getAuthCode(email, authToken, gameId, "code");
        NetworkTask<Response> getAuthCode = new NetworkTask.Builder<>(task)
                .onSuccess(new NetworkTask.OnSuccessListener<Response>() {
                    @Override
                    public void onSuccess(Response responseBody) { /* DO NOTHING */ }
                })
                // TODO: Error Handling
                .on(302, new NetworkTask.OnResponseListener<Response>() {
                    @Override
                    public void on(int statusCode, retrofit.Response<Response> response) {
                        Uri location = Uri.parse(response.headers().get("Location"));
                        callback.pass(location.getQueryParameter("code"));
                    }
                })
                .showSnackBar(false)
                .build();
        getAuthCode.execute();
    }
}
