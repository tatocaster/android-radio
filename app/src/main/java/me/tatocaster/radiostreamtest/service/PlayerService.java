package me.tatocaster.radiostreamtest.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;

import java.io.IOException;

/**
 * Created by tatocaster on 6/12/2015.
 */
public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private static final String TAG = "PlayerService";
    MediaPlayer player;
    String streamURL = "";

    @Override
    public IBinder onBind(Intent intent) {
        streamURL = intent.getExtras().getString("streamURL");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initPlayer();
    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();
    }

    public void initPlayer() {
        player = new MediaPlayer();
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource(streamURL);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }
}
