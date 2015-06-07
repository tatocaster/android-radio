package me.tatocaster.radiostreamtest.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import me.tatocaster.radiostreamtest.MediaPlayerWrapper;
import me.tatocaster.radiostreamtest.R;
import me.tatocaster.radiostreamtest.RadioDataManager;
import me.tatocaster.radiostreamtest.interfaces.IStationPLSReceiver;

/**
 * Created by tatocaster on 2015-06-07.
 */
public class PlayerActivity extends Activity implements View.OnClickListener {

    RadioDataManager rDM;
    MediaPlayerWrapper mPlayer;
    Button pauseBtn, resumeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity);

        pauseBtn = (Button) findViewById(R.id.pause_btn);
        pauseBtn.setOnClickListener(this);
        resumeBtn = (Button) findViewById(R.id.resume_btn);
        resumeBtn.setOnClickListener(this);


        rDM = new RadioDataManager(this);
        mPlayer = new MediaPlayerWrapper();

        int stationID = getIntent().getIntExtra("stationID", 0);

        rDM.getStationPLS(new IStationPLSReceiver() {
            @Override
            public void onStationPLSReceived(String streamURL) {
                if (mPlayer != null) {
                    mPlayer.reset();
                }
                if(mPlayer.isPlaying()){
                    mPlayer.stop();
                    mPlayer.release();
                }
                mPlayer.playStream(streamURL);
            }
        }, stationID);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pause_btn: {
                mPlayer.pause();
                break;
            }
            case R.id.resume_btn: {
                mPlayer.resume();
                break;
            }
        }
    }


}