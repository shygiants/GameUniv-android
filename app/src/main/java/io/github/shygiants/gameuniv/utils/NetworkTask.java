package io.github.shygiants.gameuniv.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.app.GameUniv;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by SHY_mini on 2015. 12. 29..
 */
public class NetworkTask<T> implements Callback<T> {

    public static class Builder<U> {

        private NetworkTask<U> handler;

        public Builder(Call<U> task) {
            handler = new NetworkTask<>(task);
        }

        public Builder<U> onSuccess(OnSuccessListener<U> listener) {
            handler.setOnSuccessListener(listener);
            return this;
        }

        public Builder<U> setContainer(View container) {
            handler.setContainer(container);
            return this;
        }

        public Builder<U> on(int statusCode, OnResponseListener<U> listener) {
            handler.putCallback(statusCode, listener);
            return this;
        }

        public Builder<U> showSnackBar(boolean show) {
            handler.showSnackBar(show);
            return this;
        }

        public NetworkTask<U> build() {
            if (handler.isSufficient()) return handler;
            else return null; // TODO: Throw exception
        }
    }


    public interface OnSuccessListener<T> {
        void onSuccess(T responseBody);
    }

    public interface OnResponseListener<T> {
        void on(int statusCode, Response<T> response);
    }

    private static Context context;
    private static String snackBarServerErr;
    private static String snackBarConnectionErr;
    private static String snackBarAction;
    public static void init(Context context) {
        NetworkTask.context = context;
        snackBarConnectionErr= context.getString(R.string.snack_bar_connection_err);
        snackBarServerErr= context.getString(R.string.snack_bar_server_err);
        snackBarAction = context.getString(R.string.snack_bar_action);
    }

    private Call<T> task;
    private View container;
    private OnSuccessListener<T> onSuccessListener;
    private Map<Integer, OnResponseListener<T>> callbacks = new HashMap<>();
    private boolean showSnackBar;

    private NetworkTask(Call<T> task) {
        this.task = task;
        showSnackBar = true;
    }

    private void makeSnackBar(String snackBarMsg) {
        if (!showSnackBar) return;
        View view = (container != null)?
                container : ((GameUniv)context.getApplicationContext()).getContainerView();
        Snackbar snackbar = Snackbar
                .make(view, snackBarMsg, Snackbar.LENGTH_INDEFINITE)
                .setAction(snackBarAction, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        execute();
                    }
                });

        Log.i("SnackBar", snackBarMsg);
        snackbar.show();
    }

    @Override
    public void onResponse(Response<T> response, Retrofit retrofit) {
        int statusCode = response.code();
        logResponse(response);
        switch (statusCode) {
            case 200:
                // TODO: Throw exception
                if (onSuccessListener == null) return;
                onSuccessListener.onSuccess(response.body());
                return;
            case 500: // Server Error
                makeSnackBar(snackBarServerErr);
                break;
            case 404: // Not Found
                makeSnackBar(snackBarServerErr);
                break;
            case 400: // Bad Request
                makeSnackBar(snackBarServerErr);
                break;
            case 401: // Unauthorized
                // TODO: Request new auth token
                makeSnackBar(snackBarServerErr);
                break;
            default:
                makeSnackBar(snackBarServerErr);
                break;
        }

        if (callbacks.containsKey(statusCode))
            callbacks.get(statusCode).on(statusCode, response);
    }

    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
        makeSnackBar(snackBarConnectionErr);
    }

    private void logResponse(Response<?> response) {
        Log.i("NetworkTask", Integer.toString(response.code()));
    }

    private void setOnSuccessListener(OnSuccessListener<T> onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    private void setContainer(View container) {
        this.container = container;
    }

    private boolean isSufficient() {
        return (task != null && onSuccessListener != null);
    }

    private void putCallback(int statusCode, OnResponseListener<T> callback) {
        // TODO: Throw exception
        if (callbacks.containsKey(statusCode)) return;
        callbacks.put(statusCode, callback);
    }

    private void showSnackBar(boolean show) {
        showSnackBar = show;
    }

    public void execute() {
        task.clone().enqueue(this);
    }
}