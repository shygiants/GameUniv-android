package kr.ac.korea.ee.shygiants.gameuniv.utils;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

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

        public Builder<U> setContext(View context) {
            handler.setContext(context);
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

    private Call<T> task;
    private View context;
    private OnSuccessListener<T> onSuccessListener;
    private Map<Integer, OnResponseListener<T>> callbacks = new HashMap<>();
    private boolean showSnackBar = true;

    private NetworkTask(Call<T> task) {
        this.task = task;
    }

    private void makeSnackBar() {
        if (!showSnackBar) return;
        Snackbar snackbar = Snackbar
                .make(context, "Connection Failed", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        execute();
                    }
                });

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
                makeSnackBar();
                break;
            case 404: // Not Found
                makeSnackBar();
                break;
            case 400: // Bad Request
                makeSnackBar();
                break;
            case 401: // Unauthorized
                makeSnackBar();
                break;
            default:
                break;
        }

        if (callbacks.containsKey(statusCode))
            callbacks.get(statusCode).on(statusCode, response);
    }

    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
        makeSnackBar();
    }

    private void logResponse(Response<?> response) {
        Log.i("NetworkTask", Integer.toString(response.code()));
    }

    private void setOnSuccessListener(OnSuccessListener<T> onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    private void setContext(View context) {
        this.context = context;
    }

    private boolean isSufficient() {
        return (task != null && onSuccessListener != null && (!showSnackBar && context != null));
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
        task.enqueue(this);
    }
}