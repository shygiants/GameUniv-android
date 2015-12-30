package kr.ac.korea.ee.shygiants.gameuniv.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.View;

import kr.ac.korea.ee.shygiants.gameuniv.activities.MainActivity;
import kr.ac.korea.ee.shygiants.gameuniv.utils.AuthManager;
import kr.ac.korea.ee.shygiants.gameuniv.utils.ImageHandler;
import kr.ac.korea.ee.shygiants.gameuniv.utils.NetworkTask;

/**
 * Created by SHY_mini on 2015. 12. 30..
 */
public class GameUniv extends Application implements Application.ActivityLifecycleCallbacks {

    private Activity currentActivity;
    private View containerView;

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public View getContainerView() {
        return containerView;
    }

    public void setContainerView(View containerView) {
        this.containerView = containerView;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ImageHandler.init(this);
        NetworkTask.init(this);
        AuthManager.init(this);
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        currentActivity = activity;
        AuthManager.getInstance().requestAuthToken();
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        currentActivity = null;
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }
}
