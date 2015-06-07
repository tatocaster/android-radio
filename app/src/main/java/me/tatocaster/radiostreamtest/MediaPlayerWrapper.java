package me.tatocaster.radiostreamtest;

import android.media.AudioManager;

import java.io.IOException;

/**
 * Created by tatocaster on 2015-06-06.
 */
public class MediaPlayerWrapper {

    android.media.MediaPlayer mediaPlayer;

    public MediaPlayerWrapper() {
        mediaPlayer = null;
        mediaPlayer = new android.media.MediaPlayer();
    }

    public void playStream(String streamURL) {
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(streamURL);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();

    }

    public void release() {
        mediaPlayer.release();
    }

    public void reset() {
        mediaPlayer.reset();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void resume() {
        mediaPlayer.start();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

}
