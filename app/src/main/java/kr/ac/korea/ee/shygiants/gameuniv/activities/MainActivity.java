package kr.ac.korea.ee.shygiants.gameuniv.activities;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.fragments.NewsfeedFragment;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;
import kr.ac.korea.ee.shygiants.gameuniv.utils.AuthManager;
import kr.ac.korea.ee.shygiants.gameuniv.utils.ContentsStore;
import kr.ac.korea.ee.shygiants.gameuniv.utils.ImageHandler;
import kr.ac.korea.ee.shygiants.gameuniv.utils.OnCreateGradientListener;
import kr.ac.korea.ee.shygiants.gameuniv.utils.TransactionManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private View profileArea;
    private CircleImageView profilePhoto;
    private TextView userNameText;
    private TextView emailText;

    private AuthManager authManager;

    private NewsfeedFragment newsfeedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsfeedFragment = new NewsfeedFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, newsfeedFragment).commit();


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        profileArea = findViewById(R.id.profile_area);
        profileArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionManager.commitTransaction(ContentsStore.getUser(), getSupportFragmentManager());
            }
        });
        profilePhoto = (CircleImageView) findViewById(R.id.profile_photo);
        userNameText = (TextView) findViewById(R.id.userNameTextView);
        emailText = (TextView) findViewById(R.id.emailTextView);

        ImageHandler.init(this);
        authManager = AuthManager.initWithCustomCallback(this, new AuthManager.UserInfoCallback() {
            @Override
            public void onGettingUserInfo(User user) {
                user.getProfilePhoto(profilePhoto);
                user.getProfilePhotoGradient(new OnCreateGradientListener() {
                    @Override
                    public void onCreateGradient(GradientDrawable gradient) {
                        profileArea.setBackground(gradient);
                    }
                });
                userNameText.setText(user.getUserName());
                emailText.setText(user.getEmail());
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
        if (id == android.R.id.home) {
            getSupportFragmentManager().popBackStack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_newsfeed) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newsfeedFragment);
            transaction.commit();
        } else if (id == R.id.nav_games) {

        } else if (id == R.id.nav_achievement) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initNavigationView(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void disableNavigationView() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
}
