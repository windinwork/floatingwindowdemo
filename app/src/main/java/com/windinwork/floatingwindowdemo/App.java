package com.windinwork.floatingwindowdemo;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

public class App extends Application {
    private static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                if (activity instanceof FragmentActivity) {
                    FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();
                    fm.beginTransaction().add(new SupportFragment(), FRAGMENT_TAG).commitAllowingStateLoss();
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public static class SupportFragment extends Fragment {

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            Activity activity = getActivity();
            if (activity != null) {
                FloatingWindow fw = new FloatingWindow(activity);
                activity.addContentView(fw, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }
    }
}
