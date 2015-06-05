package me.tatocaster.radiostreamtest.ui;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.List;

import me.tatocaster.radiostreamtest.R;
import me.tatocaster.radiostreamtest.RadioDataManager;
import me.tatocaster.radiostreamtest.adapter.StationAdapter;
import me.tatocaster.radiostreamtest.interfaces.ITopStationReceiver;
import me.tatocaster.radiostreamtest.model.Station;


public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    String streamingURL = "";
    Button pauseBtn, resumeBtn;
    MediaPlayer mediaPlayer;
    private RecyclerView mRecyclerView;
    StationAdapter stationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pauseBtn = (Button) findViewById(R.id.pause_btn);
        pauseBtn.setOnClickListener(this);
        resumeBtn = (Button) findViewById(R.id.resume_btn);
        resumeBtn.setOnClickListener(this);

        int url = 168811;

        // radio manager initilization
        RadioDataManager radioDM = new RadioDataManager(this);

        // recycle view init
        mRecyclerView = (RecyclerView) findViewById(R.id.stationList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        radioDM.getStationPLS(new IStationPLSReceiver() {
//            @Override
//            public void onStationPLSReceived(String streamURL) {
//                streamingURL = streamURL;
//                if (streamingURL.equals("")) {
//                    pauseBtn.setVisibility(View.INVISIBLE);
//                    resumeBtn.setVisibility(View.INVISIBLE);
//                    return;
//                }
//                mediaPlayer = new MediaPlayer();
//                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                try {
//                    mediaPlayer.setDataSource(streamingURL);
//                    mediaPlayer.prepare(); // might take long! (for buffering, etc)
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                mediaPlayer.start();
//            }
//        }, url);


        // no genre, just top stations
        radioDM.getTopStations(new ITopStationReceiver() {
            @Override
            public void onTopStationsReceived(List<Station> stations) {
                // setting adapter for recycle view
                stationAdapter = new StationAdapter(MainActivity.this, stations);
                mRecyclerView.setAdapter(stationAdapter);
                stationAdapter.notifyDataSetChanged();
            }
        }, "");

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
