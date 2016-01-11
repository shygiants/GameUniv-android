package io.github.shygiants.gameuniv.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import io.github.shygiants.gameuniv.R;

/**
 * Created by SHYBook_Air on 2016. 1. 11..
 */
public class FloatingActionButton extends com.getbase.floatingactionbutton.FloatingActionButton {

    public FloatingActionButton(Context context) {
        this(context, null);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setColor();
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setColor();
    }

    private void setColor() {
        setColorNormalResId(R.color.colorAccent);
        setColorPressedResId(R.color.colorAccentPressed);
    }

    @Override
    public void setIconDrawable(Drawable iconDrawable) {
        iconDrawable.setTint(getResources().getColor(R.color.colorWhite));
        super.setIconDrawable(iconDrawable);
    }
}
