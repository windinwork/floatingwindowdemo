package com.windinwork.floatingwindowdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Toast;

public class FloatingWindow extends FrameLayout {

    public FloatingWindow(@NonNull Context context) {
        super(context);
        init();
    }

    public FloatingWindow(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatingWindow(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        final Context context = getContext();

        View view = new FloatingBall(context);
        LayoutParams lp = new LayoutParams(dp2px(context, 64), dp2px(context, 64));
        lp.topMargin = dp2px(context, 256);
        lp.gravity = Gravity.END;
        addView(view, lp);

        view.setOnTouchListener(new OnTouchListener() {
            float translationX;
            float translationY;
            float downX;
            float downY;
            boolean move;
            int slop = ViewConfiguration.get(context).getScaledTouchSlop();

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                if (action == MotionEvent.ACTION_DOWN) {
                    downX = event.getRawX();
                    downY = event.getRawY();
                    move = false;
                } else if (action == MotionEvent.ACTION_MOVE) {
                    float moveX = event.getRawX() - downX;
                    float moveY = event.getRawY() - downY;
                    v.setTranslationX(translationX + moveX);
                    v.setTranslationY(translationY + moveY);
                    if (moveX > slop && moveY > slop) {
                        move = true;
                    }
                } else if (action == MotionEvent.ACTION_UP) {
                    translationX = v.getTranslationX();
                    translationY = v.getTranslationY();
                    if (!move) {
                        onFloatingBallClick(context);
                    }
                }
                return true;
            }
        });
    }

    private void onFloatingBallClick(Context context) {
        Toast.makeText(context, "click the tools", Toast.LENGTH_SHORT).show();
    }

    private int dp2px(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
