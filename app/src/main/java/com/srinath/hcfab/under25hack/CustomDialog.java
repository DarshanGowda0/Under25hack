package com.srinath.hcfab.under25hack;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Rohan on 1/9/2016.
 */
public class CustomDialog extends Dialog {

    String attachmentName = "mySong";
    String attachmentFileName = "testSong.mp4";
    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    String filename = "";
    boolean isRecorded = false;
    Button ok, cancel;
    CircleImageView record;
    MediaRecorder recorder;
    File file;

    public CustomDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
        ok = (Button) findViewById(R.id.OK);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new uploadSong().execute();
            }
        });
        cancel = (Button) findViewById(R.id.cancel);
        record = (CircleImageView) findViewById(R.id.record);
        record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (isRecorded) {
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
        file = new File(filepath, "Under25");

        if (!file.exists()) {
            file.mkdirs();
        }
        filename = "" + System.currentTimeMillis();
        return (file.getAbsolutePath() + "/" + filename + ".mp4");
    }

    public class uploadSong extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            File f = new File(file.getAbsolutePath()+"/"+filename+".mp4");
            try {
                HttpURLConnection httpUrlConnection;
                URL url = new URL("http://204.152.203.111/under25/insert_song.php");
                httpUrlConnection = (HttpURLConnection) url.openConnection();
                httpUrlConnection.setUseCaches(false);
                httpUrlConnection.setInstanceFollowRedirects(false);
//                httpUrlConnection.setDoOutput(true);
                httpUrlConnection.setDoInput(true);

                httpUrlConnection.setRequestMethod("POST");
//                httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
//                httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
                httpUrlConnection.setRequestProperty(
                        "Content-Type", "multipart/form-data;boundary=" + boundary);

                FileInputStream fileInputStream = new FileInputStream(f);

                DataOutputStream request = new DataOutputStream(
                        httpUrlConnection.getOutputStream());

                request.writeBytes(twoHyphens + boundary + crlf);
                request.writeBytes("Content-Disposition: form-data; name=\"" +
                        attachmentName + "\";filename=\"" +
                        f.toString() + "\"" + crlf);
                request.writeBytes(crlf);


                byte[] pixels;
                pixels = new byte[Math.min((int) f.length(), 20 * 1024)];

                int length;
                while ((length = fileInputStream.read(pixels)) != -1) {
                    request.write(pixels, 0, length);
                }

                request.write(pixels);

                request.writeBytes(crlf);
                request.writeBytes(twoHyphens + boundary +
                        twoHyphens + crlf);

                request.flush();
                request.close();

                fileInputStream.close();
                Log.d("responsecode", "" + httpUrlConnection.getResponseCode());

                DataInputStream dis = new DataInputStream(httpUrlConnection.getInputStream());
                StringBuilder response = new StringBuilder();

                String line;
                while ((line = dis.readLine()) != null) {
                    response.append(line).append('\n');
                }

                Log.d("response", ""+response);


/*                InputStream responseStream = new
                        BufferedInputStream(httpUrlConnection.getInputStream());

                BufferedReader responseStreamReader =
                        new BufferedReader(new InputStreamReader(responseStream));

                String line = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((line = responseStreamReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                responseStreamReader.close();

                String response = stringBuilder.toString();
                responseStream.close();*/
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
