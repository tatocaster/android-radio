package me.tatocaster.radiostreamtest.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import me.tatocaster.radiostreamtest.MediaPlayerWrapper;
import me.tatocaster.radiostreamtest.R;
import me.tatocaster.radiostreamtest.RadioDataManager;
import me.tatocaster.radiostreamtest.interfaces.ICurrentTrackReceiver;
import me.tatocaster.radiostreamtest.interfaces.IStationPLSReceiver;
import me.tatocaster.radiostreamtest.model.CurrentTrackInfo;

/**
 * Created by tatocaster on 2015-06-07.
 */
public class PlayerActivity extends Activity implements View.OnClickListener {

    RadioDataManager rDM;
    MediaPlayerWrapper mPlayer;
    Button pauseBtn, resumeBtn;
    Handler handler;
    CurrentTrackUpdater autoUpdater;
    private boolean inProgress;
    private int stationID;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity);

        pauseBtn = (Button) findViewById(R.id.pause_btn);
        pauseBtn.setOnClickListener(this);
        resumeBtn = (Button) findViewById(R.id.resume_btn);
        resumeBtn.setOnClickListener(this);


        imageView = (ImageView) findViewById(R.id.artist_image);

        handler = new Handler();
        autoUpdater = new CurrentTrackUpdater();

        rDM = new RadioDataManager(this);
        mPlayer = new MediaPlayerWrapper();

        stationID = getIntent().getIntExtra("stationID", 0);

        rDM.getStationPLS(new IStationPLSReceiver() {
            @Override
            public void onStationPLSReceived(String streamURL) {
                mPlayer.playStream(streamURL);
                resumeBtn.setClickable(true);
                pauseBtn.setClickable(true);
            }
        }, stationID);
        handler.post(autoUpdater);
    }


    /**
     * CurrentTrackUpdater
     */
    private class CurrentTrackUpdater implements Runnable {
        @Override
        public void run() {
            if (!inProgress) {
                getCurrentTrack();
            }
            handler.postDelayed(this, 10000);
        }
    }

    /**
     * get current track from web service
     */
    private void getCurrentTrack() {
        if (!mPlayer.isPlaying()) {
            return;
        }
        inProgress = true;
        rDM.getCurrentTrackInfo(new ICurrentTrackReceiver() {
            @Override
            public void onCurrentTrackReceived(CurrentTrackInfo currentTrackInfo) {
                if(currentTrackInfo.getArtistImageURL() == ""){
                    Glide.with(PlayerActivity.this).load(R.mipmap.ic_launcher).into(imageView);
                }
                else{
                    Glide.with(PlayerActivity.this).load(currentTrackInfo.getArtistImageURL()).into(imageView);
                }
                Toast.makeText(PlayerActivity.this, currentTrackInfo.getArtistImageURL(), Toast.LENGTH_SHORT).show();
                inProgress = false;
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