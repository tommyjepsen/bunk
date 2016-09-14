package com.tonsstudio.bunk;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

    Handler loop;
    Runnable loopRunnable;

    ArrayList<Integer> dots = new ArrayList<>();
    ArrayList<LoopSequence> loops = new ArrayList<>();

    boolean loopsSequence = false;
    int loopsSequenceTiming = 0;
    int highestLoopTime = 0;
    int id = 1;
    int screenWidth = 0;
    int screenHeight = 0;
    int speed = 10;

    @Bind(R.id.activity_music_sequence_nr_tv)
    TextView activityMusicSequenceNrTv;
    @Bind(R.id.activity_music_startover_btn)
    Button activityMusicStartoverBtn;
    @Bind(R.id.activity_music_undo_btn)
    Button activityMusicUndoBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ButterKnife.bind(this);

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

                if (loopsSequenceTiming > 100) {
                    loopsSequenceTiming = 1;
                }

                /**
                 * Move dots
                 */
                for (int i = 0; i < dots.size(); i++) {
                    ImageView dot = (ImageView) findViewById(dots.get(i));
                    if (dot != null) {
                        dot.setTranslationY((dot.getTranslationY() + 15));
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
        if (dotNr == 1) {
            dot.setImageDrawable(getResources().getDrawable(R.drawable.ic_dot1));
        } else if (dotNr == 2) {
            dot.setImageDrawable(getResources().getDrawable(R.drawable.ic_dot2));
        } else if (dotNr == 3) {
            dot.setImageDrawable(getResources().getDrawable(R.drawable.ic_dot3));
        } else if (dotNr == 4) {
            dot.setImageDrawable(getResources().getDrawable(R.drawable.ic_dot4));
        } else if (dotNr == 5) {
            dot.setImageDrawable(getResources().getDrawable(R.drawable.ic_dot5));
        } else if (dotNr == 6) {
            dot.setImageDrawable(getResources().getDrawable(R.drawable.ic_dot6));
        } else if (dotNr == 7) {
            dot.setImageDrawable(getResources().getDrawable(R.drawable.ic_dot7));
        } else if (dotNr == 8) {
            dot.setImageDrawable(getResources().getDrawable(R.drawable.ic_dot8));
        }
        dot.setPaddingRelative(0, 0, 0, 0);
        activityMusicLineFl.addView(dot);
        dots.add(dot.getId());

        /**
         * If loop is enabled and dot not already exists, then add it
         */
    }

    public void loopCreateSequence(int dotNr) {
        LoopSequence loopSequence = new LoopSequence();
        loopSequence.setNr(dotNr);
        loopSequence.setTime(loopsSequenceTiming);
        loops.add(loopSequence);

        updateUndoBtnText();
    }

    public void start() {
        loop.postDelayed(loopRunnable, speed);
    }

    public void playSample(int nr) {
        switch (nr) {
            case 1:
                final MediaPlayer mp1 = MediaPlayer.create(this, R.raw.kick);
                mp1.start();
                mp1.getDuration();
                mp1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
                createDot(1);
                break;
            case 2:
                final MediaPlayer mp2 = MediaPlayer.create(this, R.raw.snare);
                mp2.start();
                mp2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
                createDot(2);
                break;
            case 3:
                final MediaPlayer mp3 = MediaPlayer.create(this, R.raw.bass);
                mp3.start();
                mp3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
                createDot(3);
                break;
            case 4:
                final MediaPlayer mp4 = MediaPlayer.create(this, R.raw.dy);
                mp4.start();
                mp4.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
                createDot(4);
                break;
            case 5:
                final MediaPlayer mp5 = MediaPlayer.create(this, R.raw.hyr);
                mp5.start();
                mp5.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
                createDot(5);
                break;
            case 6:
                final MediaPlayer mp6 = MediaPlayer.create(this, R.raw.bass2);
                mp6.start();
                mp6.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
                createDot(6);
                break;
            case 7:
                final MediaPlayer mp7 = MediaPlayer.create(this, R.raw.bass);
                mp7.start();
                mp7.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
                createDot(7);
                break;
            case 8:
                final MediaPlayer mp8 = MediaPlayer.create(this, R.raw.bass);
                mp8.start();
                mp8.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
                createDot(8);
                break;
        }
    }

    @OnClick({R.id.activity_music_1_ib, R.id.activity_music_2_ib, R.id.activity_music_3_ib, R.id.activity_music_4_ib, R.id.activity_music_5_ib, R.id.activity_music_6_ib, R.id.activity_music_7_ib, R.id.activity_music_8_ib})
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
        }
    }

    public void updateUndoBtnText() {
        activityMusicUndoBtn.setText("Samples: " + loops.size());
    }

    @OnClick(R.id.activity_music_startover_btn)
    public void onLoopSequencePressed() {
        loops.clear();
    }

    @OnClick(R.id.activity_music_undo_btn)
    public void onUndoSequencePressed() {
        if (loops.size() > 0) {
            loops.remove(loops.size() - 1);
            updateUndoBtnText();
        }
    }
}
