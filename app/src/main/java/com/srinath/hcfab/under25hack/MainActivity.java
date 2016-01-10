package com.srinath.hcfab.under25hack;

import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.tyorikan.voicerecordingvisualizer.RecordingSampler;
import com.tyorikan.voicerecordingvisualizer.VisualizerView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements RecordingSampler.CalculateVolumeListener {

    String attachmentName = "dp";
    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    String filename = "";
    CircleImageView record_song;
    boolean isRecorded = false;
    MediaRecorder recorder;
    File file;
    VisualizerView visualizerView;
    RecordingSampler recordingSampler;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        visualizerView  = (VisualizerView) findViewById(R.id.visualizer);
        record_song= (CircleImageView) findViewById(R.id.record_song);
        record_song.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (isRecorded) {
                        } else {
                            record_song.setImageResource(R.drawable.ic_mic_none_white_24dp);
                            startRecording();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!isRecorded) {
                            isRecorded = true;
                            stopRecording();
                            record_song.setImageResource(R.drawable.ic_done_white_24dp);
                        }
                        break;
                }
                return true;
            }
        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomDialog cd = new CustomDialog(MainActivity.this);
                cd.show();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void startRecording() {

        Log.d("hello", "started recording " + getFilename());
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setOutputFile(getFilename());
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        recordingSampler = new RecordingSampler();
        recordingSampler.setVolumeListener(this);  // for custom implements
        recordingSampler.setSamplingInterval(100); // voice sampling interval
        recordingSampler.link(visualizerView);     // link to visualizer

        recordingSampler.startRecording();
//        try {
////            recorder.prepare();
////            recorder.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public void stopRecording() {
        Log.d("hello", "stop recording");
//        recorder.stop();
//        recorder.reset();
//        recorder.release();
        recordingSampler.stopRecording();
        recordingSampler.release();

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

    @Override
    public void onCalculateVolume(int volume) {

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
                httpUrlConnection.setDoOutput(true);
                httpUrlConnection.setDoInput(true);

                httpUrlConnection.setRequestMethod("POST");
                httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
                httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
                httpUrlConnection.setRequestProperty(
                        "Content-Type", "multipart/form-data;boundary=" + boundary);

                FileInputStream fileInputStream = new FileInputStream(f);

                DataOutputStream request = new DataOutputStream(
                        httpUrlConnection.getOutputStream());

                request.writeBytes(twoHyphens + boundary + crlf);
                request.writeBytes("Content-Disposition: form-data; name=\"" +
                        attachmentName + "\";filename=\"" +
                        filename + ".mp4" + "\"" + crlf);
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
