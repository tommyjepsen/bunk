package com.tonsstudio.bunk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.activity_main_logo_iv)
    ImageView activityMainLogoIv;
    @Bind(R.id.activity_main_fadein_fl)
    FrameLayout activityMainFadeinFl;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        activityMainLogoIv.animate().translationY(50).setStartDelay(100).setDuration(2000).start();

        MobileAds.initialize(this, "ca-app-pub-9538156498936820~7513431590");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9538156498936820/1466897991");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                activityMainLogoIv.animate().alpha(0).setStartDelay(200).setDuration(250).start();
                activityMainFadeinFl.animate().alpha(1).setStartDelay(200).setDuration(1000).start();

                activityMainLogoIv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(MainActivity.this, MusicActivity.class);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                    }
                }, 1500);
            }

            @Override
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                activityMainLogoIv.animate().alpha(0).setStartDelay(200).setDuration(250).start();
                activityMainFadeinFl.animate().alpha(1).setStartDelay(200).setDuration(1000).start();

                activityMainLogoIv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(MainActivity.this, MusicActivity.class);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                    }
                }, 1500);
            }
        });
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mInterstitialAd.loadAd(adRequest);
    }
}
