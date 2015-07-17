package me.tatocaster.radiostreamtest.ui;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    MediaPlayer mediaPlayer = null;
    Button pauseBtn, resumeBtn;
    Handler handler;
    CurrentTrackUpdater autoUpdater;
    private boolean inProgress;
    private int stationID;
    ImageView imageView;
    boolean isPlaying = false;
    int streamIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity);

        pauseBtn = (Button) findViewById(R.id.pause_btn);
        pauseBtn.setOnClickListener(this);
        resumeBtn = (Button) findViewById(R.id.resume_btn);
        resumeBtn.setOnClickListener(this);


        imageView = (ImageView) findViewById(R.id.artist_image);


        // es ckde block ar unda iyos aq UI ichedeba da imitom
        handler = new Handler();
        autoUpdater = new CurrentTrackUpdater();

        rDM = new RadioDataManager(this);

        stationID = getIntent().getIntExtra("stationID", 0);

        rDM.getStationPLS(new IStationPLSReceiver() {
            @Override
            public void onStationPLSReceived(List<String> streamURLList) {
                playRadio(streamURLList);
                resumeBtn.setClickable(true);
                pauseBtn.setClickable(true);
            }
        }, stationID, false);
        handler.post(autoUpdater);
    }


    public void playRadio(final List<String> streamURLList) {
        if (isPlaying) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        mediaPlayer = new MediaPlayer();
        if (streamURLList.isEmpty()) {
            return;
        }
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            Map<String, String> headerMap = new HashMap<String, String>();
            headerMap.put("User-Agent", "vlc/1.1.4 LibVLC/1.1.4");
            Uri uri = Uri.parse(streamURLList.get(streamIndex));
            mediaPlayer.setDataSource(this, uri, headerMap);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    isPlaying = true;
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    if (streamIndex == streamURLList.size()) {
                        isPlaying = false;
                        return false;
                    } else {
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        streamIndex++;
                        playRadio(streamURLList);
                        return true;
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            handler.postDelayed(this, 5000);
        }
    }

    /**
     * get current track from web service
     */
    private void getCurrentTrack() {
        if (!isPlaying) {
            return;
        }
        inProgress = true;
        rDM.getCurrentTrackInfo(new ICurrentTrackReceiver() {
            @Override
            public void onCurrentTrackReceived(CurrentTrackInfo currentTrackInfo) {
                if (currentTrackInfo.getArtistImageURL().equals("") || currentTrackInfo.getArtistImageURL() == null) {
                    Glide.with(PlayerActivity.this).load(R.mipmap.ic_launcher).into(imageView);
                } else {
                    Glide.with(PlayerActivity.this).load(currentTrackInfo.getArtistImageURL()).into(imageView);
                }
                Toast.makeText(PlayerActivity.this, currentTrackInfo.getArtistName(), Toast.LENGTH_SHORT).show();
                inProgress = false;
            }
        }, stationID);

    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pause_btn: {
                mediaPlayer.pause();
                isPlaying = false;
                break;
            }
            case R.id.resume_btn: {
                mediaPlayer.start();
                isPlaying = true;
                break;
            }
        }
    }

}