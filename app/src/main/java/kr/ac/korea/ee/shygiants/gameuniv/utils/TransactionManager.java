package kr.ac.korea.ee.shygiants.gameuniv.utils;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.gson.Gson;

import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.fragments.GameFragment;
import kr.ac.korea.ee.shygiants.gameuniv.fragments.ProfileFragment;
import kr.ac.korea.ee.shygiants.gameuniv.models.Game;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;

/**
 * Created by SHYBook_Air on 15. 12. 9..
 */
public class TransactionManager {

    private static Gson gson = new Gson();

    public static void commitTransaction(User user, FragmentManager manager) {
        boolean isOwner = user.equals(ContentsStore.getUser());
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle arguments = new Bundle();
        arguments.putBoolean(ProfileFragment.IS_OWNER, isOwner);
        arguments.putString(ProfileFragment.TIMELINE_USER, gson.toJson(user));
        profileFragment.setArguments(arguments);

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, profileFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void commitTransaction(Game game, FragmentManager manager) {
        Bundle arguments = new Bundle();
        arguments.putString(GameFragment.GAME, gson.toJson(game));
        GameFragment gameFragment = new GameFragment();
        gameFragment.setArguments(arguments);

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, gameFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
