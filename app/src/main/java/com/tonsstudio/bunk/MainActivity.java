package com.tonsstudio.bunk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.activity_main_logo_iv)
    ImageView activityMainLogoIv;
    @Bind(R.id.activity_main_fadein_fl)
    FrameLayout activityMainFadeinFl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        activityMainFadeinFl.animate().alpha(1).setStartDelay(2000).setDuration(1000).start();
        activityMainLogoIv.animate().translationY(50).setStartDelay(100).setDuration(2000).start();
        activityMainLogoIv.animate().alpha(0).setStartDelay(2000).setDuration(500).start();

        activityMainLogoIv.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, MusicActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        }, 3000);


    }
}
