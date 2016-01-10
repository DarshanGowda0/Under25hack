package com.srinath.hcfab.under25hack;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;


import java.io.IOException;

import co.mobiwise.library.MusicPlayerView;

public class PlayingNow extends AppCompatActivity {
    MusicPlayerView mpv;
    MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_playing_now);

        musicViewSetup();

    }


    private void musicViewSetup() {
        mpv = (MusicPlayerView) findViewById(R.id.mpv);
        mpv.setCoverURL("https://upload.wikimedia.org/wikipedia/en/b/b3/MichaelsNumberOnes.JPG");
        mpv.playSoundEffect(R.raw.test);
        mMediaPlayer = MediaPlayer.create(PlayingNow.this, R.raw.test);
        /*try {
            mMediaPlayer.setDataSource("http://84.200.84.218/uploads/1452372066069.mp4");
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        mpv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mpv.isRotating()) {
                    mpv.stop();
                    mMediaPlayer.pause();
                } else {
                    mpv.start();
                    mMediaPlayer.start();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mMediaPlayer.pause();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMediaPlayer != null) {
            if (mpv.isRotating()) {
                mpv.stop();
                mMediaPlayer.start();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            mMediaPlayer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
