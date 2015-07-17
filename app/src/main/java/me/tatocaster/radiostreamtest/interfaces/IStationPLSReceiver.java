package me.tatocaster.radiostreamtest.interfaces;

import java.util.List;

/**
 * Created by tatocaster on 2015-06-06.
 */
public interface IStationPLSReceiver {
    void onStationPLSReceived(List<String> streamURLList);
}
