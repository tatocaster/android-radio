package me.tatocaster.radiostreamtest.interfaces;

import me.tatocaster.radiostreamtest.model.CurrentTrackInfo;

/**
 * Created by tatocaster on 6/8/2015.
 */
public interface ICurrentTrackReceiver {
    void onCurrentTrackReceived(CurrentTrackInfo currentTrackInfo);
}
