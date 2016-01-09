package io.github.shygiants.gameuniv.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

/**
 * Created by SHY_mini on 2016. 1. 9..
 */
public class KeyboardHandlerRelativeLayout extends RelativeLayout implements View.OnTouchListener {

    public interface OnKeyboardEventListener {
        void onKeyboardShow();
        void onKeyboardHide();
    }

    private boolean isKeyboardShown;
    private OnKeyboardEventListener listener;
    private InputMethodManager inputMethodManager;

    public KeyboardHandlerRelativeLayout(Context context) {
        this(context, null);
    }

    public KeyboardHandlerRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardHandlerRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public KeyboardHandlerRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOnTouchListener(this);
        inputMethodManager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    public void setOnKeyboardEventListener(OnKeyboardEventListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && isKeyboardShown) {
            isKeyboardShown = false;
            listener.onKeyboardHide();
        }

        return super.dispatchKeyEventPreIme(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int proposedHeight = MeasureSpec.getSize(heightMeasureSpec);
        final int actualHeight = getHeight();
        if (actualHeight > proposedHeight && !isKeyboardShown) {
            isKeyboardShown = true;
            listener.onKeyboardShow();
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
        if (isKeyboardShown) {
            isKeyboardShown = false;
            listener.onKeyboardHide();
        }

        return false;
    }
}
