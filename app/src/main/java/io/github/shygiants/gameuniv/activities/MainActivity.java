package io.github.shygiants.gameuniv.activities;

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

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.app.GameUniv;
import io.github.shygiants.gameuniv.fragments.GameListFragment;
import io.github.shygiants.gameuniv.fragments.NewsfeedFragment;
import io.github.shygiants.gameuniv.models.User;
import io.github.shygiants.gameuniv.utils.AuthManager;
import io.github.shygiants.gameuniv.utils.Callback;
import io.github.shygiants.gameuniv.utils.ContentsStore;
import io.github.shygiants.gameuniv.utils.OnCreateGradientListener;
import io.github.shygiants.gameuniv.utils.TransactionManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.profile_area)
    View profileArea;
    @Bind(R.id.profile_photo)
    CircleImageView profilePhoto;
    @Bind(R.id.user_name_text)
    TextView userNameText;
    @Bind(R.id.email_text)
    TextView emailText;

    private NewsfeedFragment newsfeedFragment;
    private GameListFragment gameListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ((GameUniv)getApplication()).setContainerView(drawer);

        AuthManager.getInstance().registerCallback(new Callback<User>() {
            @Override
            public void pass(User user) {
                user.getProfilePhoto(profilePhoto);
                user.getProfilePhotoGradient(new OnCreateGradientListener() {
                    @Override
                    public void onCreateGradient(GradientDrawable gradient) {
                        profileArea.setBackground(gradient);
                    }
                });
                userNameText.setText(user.getUserName());
                emailText.setText(user.getEmail());
                profileArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TransactionManager.commitTransaction(ContentsStore.getInstance().getUser(), getSupportFragmentManager());
                    }
                });

                newsfeedFragment = new NewsfeedFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, newsfeedFragment).commit();
            }
        });



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    public View getContainerView() {
        return drawer;
    }

    @Override
    public void onBackPressed() {
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
            if (gameListFragment == null) {
                gameListFragment = new GameListFragment();
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, gameListFragment);
            transaction.commit();

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
