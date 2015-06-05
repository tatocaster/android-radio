package me.tatocaster.radiostreamtest.interfaces;

import java.util.List;

import me.tatocaster.radiostreamtest.model.Station;

/**
 * Created by tatocaster on 2015-06-06.
 */
public interface ITopStationReceiver {
    void onTopStationsReceived(List<Station> stations);
}
