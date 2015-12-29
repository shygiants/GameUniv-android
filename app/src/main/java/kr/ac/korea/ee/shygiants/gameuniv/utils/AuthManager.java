package kr.ac.korea.ee.shygiants.gameuniv.utils;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.activities.AuthorizationActivity;
import kr.ac.korea.ee.shygiants.gameuniv.models.Response;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

/**
 * Created by SHYBook_Air on 15. 11. 10..
 */
public class AuthManager {

    public interface UserInfoCallback {
        void onGettingUserInfo(final User user);
    }

    public interface AuthCodeCallback {
        void onGettingAuthCode(final String authCode);
    }

    // Constants
    private String ACCOUNT_TYPE;
    private String FULL_ACCESS;

    // Context
    private Activity context;
    private boolean loadContents;
    private AccountManager accountManager;

    // For getting user info
    private String email;
    private String authToken;

    private UserInfoCallback userInfoCallback;
    private User user;

    public static AuthManager init(Activity context) {
        return initWithCustomCallback(context, null);
    }

    public static AuthManager initWithCustomCallback(Activity context, UserInfoCallback customCallback) {
        AuthManager ourInstance = new AuthManager();
        ourInstance.context = context;
        ourInstance.loadContents = !(context instanceof AuthorizationActivity);

        ourInstance.ACCOUNT_TYPE = ourInstance.context.getString(R.string.auth_account_type);
        ourInstance.FULL_ACCESS = ourInstance.context.getString(R.string.auth_full_access);

        ourInstance.userInfoCallback = customCallback;

        ourInstance.requestAuthToken();

        return ourInstance;
    }

    private void requestAuthToken() {
        accountManager = AccountManager.get(context);
        accountManager.getAuthTokenByFeatures(ACCOUNT_TYPE, FULL_ACCESS, null, context, null, null,
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
                        if (loadContents)
                            ContentsStore.initFeed(user);
                        Log.i("AuthManager", "User is initialized");
                        if (userInfoCallback != null)
                            userInfoCallback.onGettingUserInfo(user);
                    }
                })
                .showSnackBar(false)
                .on(401, new NetworkTask.OnResponseListener<User>() {
                    @Override
                    public void on(int statusCode, retrofit.Response<User> response) {
                        accountManager.invalidateAuthToken(ACCOUNT_TYPE, authToken);
                        requestAuthToken();
                    }
                })
                .build();
        getUser.execute();
    }

    public void getUser(UserInfoCallback callback) {
        // TODO: Sync with server
        if (user != null)
            callback.onGettingUserInfo(user);
        else
            userInfoCallback = callback;
    }

    public void getAuthCode(String gameId, final AuthCodeCallback callback) {
        if (email == null || authToken == null) callback.onGettingAuthCode(null);

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
                        callback.onGettingAuthCode(location.getQueryParameter("code"));
                    }
                })
                .showSnackBar(false)
                .build();
        getAuthCode.execute();
    }
}
