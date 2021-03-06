package io.github.shygiants.gameuniv.activities;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.models.AuthToken;
import io.github.shygiants.gameuniv.models.RequestBody;
import io.github.shygiants.gameuniv.utils.NetworkTask;
import io.github.shygiants.gameuniv.utils.RESTAPI;
import io.github.shygiants.gameuniv.utils.Regex;
import retrofit.Call;
import retrofit.Response;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AccountAuthenticatorActivity {

    // UI references.
    @Bind(R.id.container)
    View container;
    @Bind(R.id.email)
    EditText mEmailView;
    @Bind(R.id.password)
    EditText mPasswordView;
    @Bind(R.id.login_form)
    View mProgressView;
    @Bind(R.id.login_progress)
    View mLoginFormView;

    // Intent arg keys
    public static final String ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public static final String AUTH_TYPE = "AUTH_TYPE";
    public static final String ACCOUNT_NAME = "ACCOUNT_NAME";

    private String authTokenType;
    private String accountType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        authTokenType = intent.getStringExtra(AUTH_TYPE);
        accountType = intent.getStringExtra(ACCOUNT_TYPE);

        if (authTokenType == null)
            authTokenType = getString(R.string.auth_full_access);


        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        // Set up the login form.

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = ButterKnife.findById(this, R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    @Override
    public void onBackPressed() { /* DO NOTHING */ }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            Call<AuthToken> task = RESTAPI.Tokens.getAuthToken(email, RequestBody.create(password));
            NetworkTask<AuthToken> networkTask = new NetworkTask.Builder<>(task)
                    .onSuccess(new NetworkTask.OnSuccessListener<AuthToken>() {
                        @Override
                        public void onSuccess(AuthToken authToken) {
                            showProgress(false);
                            loginSuccess(email, password, authToken);
                        }
                    })
                    .on(400, new NetworkTask.OnResponseListener<AuthToken>() {
                        @Override
                        public void on(int statusCode, Response<AuthToken> response) {
                            showProgress(false);
                            mEmailView.setError(getString(R.string.error_invalid_email));
                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                            mEmailView.requestFocus();
                        }
                    })
                    .showSnackBar(false)
                    .setContainer(container)
                    .build();
            networkTask.execute();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return Regex.isEmail(email);
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void loginSuccess(String email, String password, AuthToken authToken) {

        Bundle bundle = new Bundle();
        Intent intent = new Intent();

        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, email);
        bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
        bundle.putString(AccountManager.KEY_AUTHTOKEN, authToken.toString());

        intent.putExtras(bundle);

        AccountManager accountManager = AccountManager.get(this);
        final Account account = new Account(email, accountType);

        accountManager.addAccountExplicitly(account, password, null);
        accountManager.setAuthToken(account, authTokenType, authToken.toString());

        setAccountAuthenticatorResult(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}

