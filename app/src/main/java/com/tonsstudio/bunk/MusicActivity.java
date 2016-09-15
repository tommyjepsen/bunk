package com.tonsstudio.bunk;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MusicActivity extends AppCompatActivity {

    public static final String TAG = MusicActivity.class.getName();

    @Bind(R.id.activity_music_1_ib)
    ImageButton activityMusic1Ib;
    @Bind(R.id.activity_music_2_ib)
    ImageButton activityMusic2Ib;
    @Bind(R.id.activity_music_3_ib)
    ImageButton activityMusic3Ib;
    @Bind(R.id.activity_music_4_ib)
    ImageButton activityMusic4Ib;
    @Bind(R.id.activity_music_5_ib)
    ImageButton activityMusic5Ib;
    @Bind(R.id.activity_music_6_ib)
    ImageButton activityMusic6Ib;
    @Bind(R.id.activity_music_7_ib)
    ImageButton activityMusic7Ib;
    @Bind(R.id.activity_music_8_ib)
    ImageButton activityMusic8Ib;
    @Bind(R.id.activity_music_line_fl)
    FrameLayout activityMusicLineFl;
    @Bind(R.id.activity_music_9_ib)
    ImageButton activityMusic9Ib;
    @Bind(R.id.activity_music_10_ib)
    ImageButton activityMusic10Ib;
    @Bind(R.id.activity_music_11_ib)
    ImageButton activityMusic11Ib;
    @Bind(R.id.activity_music_12_ib)
    ImageButton activityMusic12Ib;
    @Bind(R.id.activity_music_13_ib)
    ImageButton activityMusic13Ib;
    @Bind(R.id.activity_music_14_ib)
    ImageButton activityMusic14Ib;
    @Bind(R.id.activity_music_15_ib)
    ImageButton activityMusic15Ib;
    @Bind(R.id.activity_music_16_ib)
    ImageButton activityMusic16Ib;
    @Bind(R.id.activity_music_17_ib)
    ImageButton activityMusic17Ib;
    @Bind(R.id.activity_music_18_ib)
    ImageButton activityMusic18Ib;

    Handler loop;
    Runnable loopRunnable;

    ArrayList<Integer> dots = new ArrayList<>();
    ArrayList<LoopSequence> loops = new ArrayList<>();

    boolean recording = false;
    int loopsSequenceTiming = 0;
    int id = 1;
    int screenWidth = 0;
    int screenHeight = 0;
    int speed = 10;
    int speedDraw = 15;
    String category = "d"; // or "s"

    @Bind(R.id.activity_music_sequence_nr_tv)
    TextView activityMusicSequenceNrTv;

    @Bind(R.id.activity_music_undo_nr_btn)
    Button activityMusicUndoNrBtn;
    @Bind(R.id.activity_music_rec_btn)
    Button activityMusicRecBtn;
    @Bind(R.id.activity_music_fadeout_fl)
    FrameLayout activityMusicFadeoutFl;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ButterKnife.bind(this);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9538156498936820~7513431590");
        mAdView = (AdView) findViewById(R.id.activity_music_adview);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        activityMusicFadeoutFl.animate().alpha(0).setStartDelay(100).setDuration(1500).start();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                activityMusicFadeoutFl.setVisibility(View.GONE);
            }
        }, 1800);

        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        loop = new Handler();

        loopRunnable = new Runnable() {
            @Override
            public void run() {

                /**
                 * Sequence
                 */
                //Count loop so they know what position the sample has in the row
                loopsSequenceTiming++;

                activityMusicSequenceNrTv.setText("" + (loopsSequenceTiming / 10));

                if (loops.size() != 0) {
                    // Play the loops
                    for (int b = 0; b < loops.size(); b++) {
                        if (loops.get(b).getTime() == loopsSequenceTiming) {
                            playSample(loops.get(b).getNr());
                        }
                    }
                }

                if (loopsSequenceTiming > speed * 10) {
                    createStartDot();

                    loopsSequenceTiming = 1;
                }

                /**
                 * Move dots
                 */
                for (int i = 0; i < dots.size(); i++) {
                    ImageView dot = (ImageView) findViewById(dots.get(i));
                    if (dot != null) {
                        dot.setTranslationY((dot.getTranslationY() + speedDraw));
                        if (dot.getTranslationY() > screenHeight) {
                            dots.remove(i);
                        }
                    }
                }

                start(); // Loop it
            }
        };
        start();
    }

    public void createDot(int dotNr) {
        id = id + 1;
        ImageView dot = new ImageView(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(50, 50);
        dot.setLayoutParams(lp);
        dot.setId(id);
        dot.setImageDrawable(getResources().getDrawable(R.drawable.ic_dot1));
        dot.setPaddingRelative(0, 0, 0, 0);
        activityMusicLineFl.addView(dot);
        dots.add(dot.getId());
        /**
         * If loop is enabled and dot not already exists, then add it
         */
    }

    public void createStartDot() {
        id = id + 1;
        ImageView dot = new ImageView(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(50, 50);
        dot.setLayoutParams(lp);
        dot.setId(id);
        dot.setImageDrawable(getResources().getDrawable(R.drawable.ic_start));
        dot.setPaddingRelative(0, 0, 0, 0);
        activityMusicLineFl.addView(dot);
        dots.add(dot.getId());
        /**
         * If loop is enabled and dot not already exists, then add it
         */
    }

    public void loopCreateSequence(int dotNr) {
        if (recording) {
            LoopSequence loopSequence = new LoopSequence();
            loopSequence.setNr(dotNr);
            loopSequence.setTime(loopsSequenceTiming);
            loops.add(loopSequence);
        }

        updateUndoBtnText();
    }

    public void start() {
        loop.postDelayed(loopRunnable, speed);
    }

    public int getAndroidRaw(String pDrawableName) {
        int resourceId = getResources().getIdentifier("raw/" + pDrawableName, null, this.getPackageName());
        return resourceId;
    }

    public void playSample(final int nr) {
        MediaPlayer mp;
        int rawId = getAndroidRaw(category + nr);

        try {
            switch (nr) {
                case 1:
                    mp = MediaPlayer.create(this, rawId);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    createDot(1);
                    break;
                case 2:
                    mp = MediaPlayer.create(this, rawId);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    createDot(2);
                    break;
                case 3:
                    mp = MediaPlayer.create(this, rawId);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    createDot(3);
                    break;
                case 4:
                    mp = MediaPlayer.create(this, rawId);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    createDot(4);
                    break;
                case 5:
                    mp = MediaPlayer.create(this, rawId);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    createDot(5);
                    break;
                case 6:
                    mp = MediaPlayer.create(this, rawId);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    createDot(6);
                    break;
                case 7:
                    mp = MediaPlayer.create(this, rawId);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    createDot(7);
                    break;
                case 8:
                    mp = MediaPlayer.create(this, rawId);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    createDot(8);
                    break;
                case 9:
                    mp = MediaPlayer.create(this, rawId);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    createDot(9);
                    break;
                case 10:
                    mp = MediaPlayer.create(this, rawId);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    createDot(10);
                    break;
                case 11:
                    mp = MediaPlayer.create(this, rawId);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    createDot(11);
                    break;
                case 12:
                    mp = MediaPlayer.create(this, rawId);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    createDot(12);
                    break;
                case 13:
                    mp = MediaPlayer.create(this, rawId);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    createDot(13);
                    break;
                case 14:
                    mp = MediaPlayer.create(this, rawId);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    createDot(14);
                    break;
                case 15:
                    mp = MediaPlayer.create(this, rawId);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    createDot(15);
                    break;
                case 16:
                    mp = MediaPlayer.create(this, rawId);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    createDot(16);
                    break;
                case 17:
                    mp = MediaPlayer.create(this, rawId);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    createDot(17);
                    break;
                case 18:
                    mp = MediaPlayer.create(this, rawId);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    createDot(18);
                    break;
                case 19:
                    mp = MediaPlayer.create(this, rawId);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    createDot(19);
                    break;

            }
        } catch (Exception e) {
            Log.e(TAG, "playSample: ", e);
        }
    }


    public void updateUndoBtnText() {
        activityMusicUndoNrBtn.setText("" + loops.size());
    }

    @OnClick(R.id.activity_music_undo_nr_btn)
    public void onUndoSequencePressed() {
        if (loops.size() > 0) {
            loops.remove(loops.size() - 1);
            updateUndoBtnText();
        }
    }

    @OnClick({R.id.activity_music_1_ib, R.id.activity_music_2_ib, R.id.activity_music_3_ib, R.id.activity_music_4_ib, R.id.activity_music_5_ib, R.id.activity_music_6_ib, R.id.activity_music_7_ib, R.id.activity_music_8_ib, R.id.activity_music_9_ib, R.id.activity_music_10_ib, R.id.activity_music_11_ib, R.id.activity_music_12_ib, R.id.activity_music_13_ib, R.id.activity_music_14_ib, R.id.activity_music_15_ib, R.id.activity_music_16_ib, R.id.activity_music_17_ib, R.id.activity_music_18_ib})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_music_1_ib:
                playSample(1);
                loopCreateSequence(1);

                break;
            case R.id.activity_music_2_ib:
                playSample(2);
                loopCreateSequence(2);

                break;
            case R.id.activity_music_3_ib:
                playSample(3);
                loopCreateSequence(3);

                break;
            case R.id.activity_music_4_ib:
                playSample(4);
                loopCreateSequence(4);

                break;
            case R.id.activity_music_5_ib:
                playSample(5);
                loopCreateSequence(5);

                break;
            case R.id.activity_music_6_ib:
                playSample(6);
                loopCreateSequence(6);

                break;
            case R.id.activity_music_7_ib:
                playSample(7);
                loopCreateSequence(7);

                break;
            case R.id.activity_music_8_ib:
                playSample(8);
                loopCreateSequence(8);

                break;
            case R.id.activity_music_9_ib:
                playSample(9);
                loopCreateSequence(9);
                break;
            case R.id.activity_music_10_ib:
                playSample(10);
                loopCreateSequence(10);
                break;
            case R.id.activity_music_11_ib:
                playSample(11);
                loopCreateSequence(11);
                break;
            case R.id.activity_music_12_ib:
                playSample(12);
                loopCreateSequence(12);
                break;
            case R.id.activity_music_13_ib:
                playSample(13);
                loopCreateSequence(13);
                break;
            case R.id.activity_music_14_ib:
                playSample(14);
                loopCreateSequence(14);
                break;
            case R.id.activity_music_15_ib:
                playSample(15);
                loopCreateSequence(15);
                break;
            case R.id.activity_music_16_ib:
                playSample(16);
                loopCreateSequence(16);
                break;
            case R.id.activity_music_17_ib:
                playSample(17);
                loopCreateSequence(17);
                break;
            case R.id.activity_music_18_ib:
                playSample(18);
                loopCreateSequence(18);
                break;
        }
    }

    @OnClick({R.id.activity_music_num_1_ib, R.id.activity_music_num_5_ib, R.id.activity_music_num_10_ib, R.id.activity_music_num_15_ib, R.id.activity_music_num_20_ib})
    public void onSpeedClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_music_num_1_ib:
                speed = 1;
                break;
            case R.id.activity_music_num_5_ib:
                speed = 5;
                break;
            case R.id.activity_music_num_10_ib:
                speed = 10;
                break;
            case R.id.activity_music_num_15_ib:
                speed = 15;
                break;
            case R.id.activity_music_num_20_ib:
                speed = 20;
                break;
        }

        if (speed == 1) {
            speedDraw = 50;
        } else if (speed == 5) {
            speedDraw = 40;
        } else if (speed == 10) {
            speedDraw = 30;
        } else if (speed == 15) {
            speedDraw = 20;
        } else if (speed == 20) {
            speedDraw = 10;
        }
    }

    @OnClick(R.id.activity_music_rec_btn)
    public void onClick() {
        if (recording) {
            activityMusicRecBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_btn_darkyellow));

            recording = false;
        } else {
            activityMusicRecBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_btn_darkyellowff));

            recording = true;
        }
    }
}
