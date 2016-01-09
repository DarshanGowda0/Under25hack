package com.srinath.hcfab.under25hack;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Rohan on 1/9/2016.
 */
public class CustomDialog extends Dialog {

    boolean isRecorded = false;
    Button ok, cancel;
    CircleImageView record;
    MediaRecorder recorder;

    public CustomDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
        ok = (Button) findViewById(R.id.OK);
        cancel = (Button) findViewById(R.id.cancel);
        record = (CircleImageView) findViewById(R.id.record);
        record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (isRecorded) {
//                            uploadSong();
                        } else {
                            record.setImageResource(R.drawable.ic_mic_none_white_24dp);
                            startRecording();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!isRecorded) {
                            isRecorded = true;
                            stopRecording();
                            record.setImageResource(R.drawable.ic_done_white_24dp);
                        }
                        break;
                }
                return true;
            }
        });
    }


    public void startRecording() {

        Log.d("hello", "started recording " + getFilename());
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setOutputFile(getFilename());
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stopRecording() {
        Log.d("hello", "stop recording");
        recorder.stop();
        recorder.reset();
        recorder.release();
    }

    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, "Under25");

        if (!file.exists()) {
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp4");
    }

}
