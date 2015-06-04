package me.tatocaster.radiostreamtest.ui;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;

import java.io.IOException;

import me.tatocaster.radiostreamtest.R;
import me.tatocaster.radiostreamtest.network.VolleyClient;


public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    String streamingURL = "";
    Button pauseBtn, resumeBtn;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pauseBtn = (Button) findViewById(R.id.pause_btn);
        pauseBtn.setOnClickListener(this);
        resumeBtn = (Button) findViewById(R.id.resume_btn);
        resumeBtn.setOnClickListener(this);

        String url = "http://yp.shoutcast.com/sbin/tunein-station.pls?id=175821";

        VolleyClient.getInstance(this).checkoutPLS(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                String[] responseArray = response.split("\n");
                for (String kvPair : responseArray) {
                    if (!kvPair.contains("=")) {
                        continue;
                    }
                    String[] kv = kvPair.split("=");
                    String key = kv[0];
                    String value = kv[1];
                    if (key.equals("File1")) {
                        streamingURL = value;
                        break;
                    }
                }
                Log.d(TAG, streamingURL);
                if(streamingURL.equals("")){
                    pauseBtn.setVisibility(View.INVISIBLE);
                    resumeBtn.setVisibility(View.INVISIBLE);
                    return;
                }
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(streamingURL);
                    mediaPlayer.prepare(); // might take long! (for buffering, etc)
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        }, null, url);

    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pause_btn: {
                mediaPlayer.pause();
                break;
            }
            case R.id.resume_btn: {
                mediaPlayer.start();
                break;
            }
        }
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
}
