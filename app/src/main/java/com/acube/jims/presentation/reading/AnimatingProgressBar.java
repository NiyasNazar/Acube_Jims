package com.acube.jims.presentation.reading;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ProgressBar;

import com.nineoldandroids.animation.ValueAnimator;

public class AnimatingProgressBar extends ProgressBar {

    private static final Interpolator DEFAULT_INTERPOLATER = new AccelerateDecelerateInterpolator();

    private ValueAnimator animator;
    private ValueAnimator animatorSecondary;
    private boolean animate = true;

    public AnimatingProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AnimatingProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatingProgressBar(Context context) {
        super(context);
    }

    public boolean isAnimate() {
        return animate;
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
    }

    @Override
    public synchronized void setProgress(int progress) {
        if (!animate) {
            super.setProgress(progress);
            return;
        }
        if (animator != null)
            animator.cancel();
        if (animator == null) {
            animator = ValueAnimator.ofInt(getProgress(), progress);
            animator.setInterpolator(DEFAULT_INTERPOLATER);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    AnimatingProgressBar.super.setProgress((Integer) animation.getAnimatedValue());
                }
            });
        } else
            animator.setIntValues(getProgress(), progress);
        animator.start();

    }

    @Override
    public synchronized void setSecondaryProgress(int secondaryProgress) {
        if (!animate) {
            super.setSecondaryProgress(secondaryProgress);
            return;
        }
        if (animatorSecondary != null)
            animatorSecondary.cancel();
        if (animatorSecondary == null) {
            animatorSecondary = ValueAnimator.ofInt(getProgress(), secondaryProgress);
            animatorSecondary.setInterpolator(DEFAULT_INTERPOLATER);
            animatorSecondary.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    AnimatingProgressBar.super.setSecondaryProgress((Integer) animation
                            .getAnimatedValue());
                }
            });
        } else
            animatorSecondary.setIntValues(getProgress(), secondaryProgress);
        animatorSecondary.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null)
            animator.cancel();
        if (animatorSecondary != null)
            animatorSecondary.cancel();
    }

}