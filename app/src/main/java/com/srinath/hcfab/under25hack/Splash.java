package com.srinath.hcfab.under25hack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import co.mobiwise.library.MusicPlayerView;

public class Splash extends AppCompatActivity {

    MusicPlayerView mpv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
   musicViewSetup();

}

    private void musicViewSetup() {
        mpv = (MusicPlayerView) findViewById(R.id.mpv);

        mpv.setCoverURL("https://upload.wikimedia.org/wikipedia/en/b/b3/MichaelsNumberOnes.JPG");
        mpv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mpv.isRotating()) {
                    mpv.stop();
                } else {
                    mpv.start();
                }
            }
        });}
}
