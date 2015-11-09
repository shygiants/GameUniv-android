package kr.ac.korea.ee.shygiants.gameuniv.activities;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.apis.Users;
import kr.ac.korea.ee.shygiants.gameuniv.models.AuthToken;
import kr.ac.korea.ee.shygiants.gameuniv.models.RequestBody;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;
import kr.ac.korea.ee.shygiants.gameuniv.utils.Owner;
import kr.ac.korea.ee.shygiants.gameuniv.utils.RESTAPI;
import retrofit.Callback;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String ACCOUNT_TYPE;
    private String FULL_ACCESS;

    private Users usersAPI = RESTAPI.create(Users.class);

    private TextView userNameText;
    private TextView emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ACCOUNT_TYPE = getString(R.string.auth_account_type);
        FULL_ACCESS = getString(R.string.auth_full_access);

        // Get auth token
        AccountManager accountManager = AccountManager.get(this);
        accountManager.getAuthTokenByFeatures(ACCOUNT_TYPE, FULL_ACCESS, null, this, null, null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        Bundle bundle;
                        try {
                            bundle = future.getResult();
                            final String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                            final String email = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
                            // TODO: delete this log from the product
                            Log.d("SHY", "EMAIL: " + email);
                            Log.d("SHY", "AUTH_TOKEN: " + authToken);
                            Owner.init(email, authToken);
                            getUserInfo();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, null);

        userNameText = (TextView) findViewById(R.id.userNameTextView);
        emailText = (TextView) findViewById(R.id.emailTextView);
    }

    void getUserInfo() {
        usersAPI.getUser(Owner.getEmail(), Owner.getToken())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(retrofit.Response<User> response, Retrofit retrofit) {
//                        showProgress(false);
                        switch (response.code()) {
                            case 401: // Unauthorized
                                // TODO: Fancy error handling
                                break;
                            case 500:
                                // TODO: Server error handling
                                break;
                            default:
                                User user = response.body();
                                if (user.isSuccess()) {
                                    // TODO: Getting user info success
                                    Log.i("USERNAME", user.getUserName());
                                    Log.i("EMAIL", user.getEmail());
                                    userNameText.setText(user.getUserName());
                                    emailText.setText(user.getEmail());
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // TODO: Something wrong on network
                        Log.e("SHY", "MainActivity onFailure");
                        t.printStackTrace();
                    }
                });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
