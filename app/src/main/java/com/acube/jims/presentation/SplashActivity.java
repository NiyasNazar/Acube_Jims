package com.acube.jims.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.acube.jims.R;
import com.acube.jims.databinding.ActivitySplashBinding;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.presentation.Login.View.LoginActivity;
import com.acube.jims.utils.OnSingleClickListener;
import com.gelitenight.waveview.library.WaveView;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    AnimationDrawable wifiAnimation;
    ActivitySplashBinding binding;
    private AnimatorSet mAnimatorSet;
    private int mBorderColor = Color.parseColor("#44FFFFFF");
    private int mBorderWidth = 80;
    LayerDrawable progressBarDrawable;
    Drawable progressDrawable;
    //found=1,Unknown=3,Locmismatch=2
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);


        binding.start.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
               // new asyncTaskUpdateProgress().execute();
            }
        });
         progressBarDrawable = (LayerDrawable) binding.vprogressbar.getProgressDrawable();
        Drawable backgroundDrawable = progressBarDrawable.getDrawable(0);
         progressDrawable = progressBarDrawable.getDrawable(1);

        backgroundDrawable.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), PorterDuff.Mode.SRC_IN);
        progressDrawable.setColorFilter(ContextCompat.getColor(this.getApplicationContext(), R.color.red), PorterDuff.Mode.SRC_IN);



        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setBackgroundResource(R.drawable.animation);
        wifiAnimation = (AnimationDrawable) imageView.getBackground();
        boolean urlupdated = LocalPreferences.retrieveBooleanPreferences(getApplicationContext(), "urlupdated");
        wifiAnimation.start();
        binding.mwave.setBorder(mBorderWidth, mBorderColor);
        binding.mwave.setShowWave(true);
        binding.mwave.setShapeType(WaveView.ShapeType.SQUARE);
        List<Animator> animators = new ArrayList<>();
        binding.mwave.setWaveColor(
                Color.parseColor("#40b7d28d"),
                Color.parseColor("#80b7d28d"));

        ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(
                binding.mwave, "waterLevelRatio", 0f, 0.5f);
        waterLevelAnim.setDuration(10000);
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        animators.add(waterLevelAnim);
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animators);

        if (mAnimatorSet != null) {
            mAnimatorSet.start();
        }

    new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (urlupdated){
                    Intent i= new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i); //start new activity
                    finish();
                }else{
                    Intent i= new Intent(SplashActivity.this, SettingsActivity.class);
                    startActivity(i); //start new activity
                    finish();
                }





            }
        }, 1000);
    }

    public class asyncTaskUpdateProgress extends AsyncTask<Void, Integer, Void> {

        int progress;

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progress = 0;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            binding.vprogressbar.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub

            while (progress < 100) {
                if (progress>50){
                    progressDrawable.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.green), PorterDuff.Mode.SRC_IN);

                }
                progress++;
                publishProgress(progress);
               // SystemClock.sleep(100);
            }
            return null;
        }

    }
}