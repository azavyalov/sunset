package com.azavyalov.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SunsetFragment extends Fragment {

    private View mSceneView;
    private View mSunView;
    private View mSkyView;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;

    private boolean sunset = true;
    private int shortDuration = 1500;
    private int longDuration = 3000;

    public static SunsetFragment newInstance() {
        return new SunsetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sunset, container, false);
        mSceneView = view;
        mSunView = view.findViewById(R.id.sun);
        mSkyView = view.findViewById(R.id.sky);

        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sunset) {
                    startSunsetAnimation();
                    sunset = false;
                } else {
                    startSunriseAnimation();
                    sunset = true;
                }
            }
        });
        return view;
    }

    private void startSunriseAnimation() {

        float sunYStart = mSkyView.getHeight();
        float sunYEnd = mSunView.getTop();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(buildHeightAnimator(
                        longDuration, sunYStart, sunYEnd))
                .with(buildSkyAnimator(
                        longDuration, mNightSkyColor, mSunsetSkyColor))
                .before(buildSkyAnimator(
                        shortDuration, mSunsetSkyColor, mBlueSkyColor));
        animatorSet.start();
    }

    private void startSunsetAnimation() {

        float sunYStart = mSunView.getTop();
        float sunYEnd = mSkyView.getHeight();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(buildHeightAnimator(
                        longDuration, sunYStart, sunYEnd))
                .with(buildSkyAnimator(
                        longDuration, mBlueSkyColor, mSunsetSkyColor))
                .before(buildSkyAnimator(
                        shortDuration, mSunsetSkyColor, mNightSkyColor));
        animatorSet.start();
    }

    private ObjectAnimator buildHeightAnimator(int duration, float sunYStart, float sunYEnd) {
        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(duration);
        heightAnimator.setInterpolator(new AccelerateInterpolator());
        return heightAnimator;
    }

    private ObjectAnimator buildSkyAnimator(int duration, int... colors) {
        ObjectAnimator skyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", colors)
                .setDuration(duration);
        skyAnimator.setEvaluator(new ArgbEvaluator());
        return skyAnimator;
    }
}
