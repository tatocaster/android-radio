package me.tatocaster.radiostreamtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.tatocaster.radiostreamtest.MediaPlayerWrapper;
import me.tatocaster.radiostreamtest.R;
import me.tatocaster.radiostreamtest.RadioDataManager;
import me.tatocaster.radiostreamtest.interfaces.IStationPLSReceiver;
import me.tatocaster.radiostreamtest.model.Station;

/**
 * Created by tatocaster on 6/5/2015.
 */
public class StationAdapter extends RecyclerView.Adapter<StationAdapter.StationsViewHolder> {

    List<Station> stations = new ArrayList<>();
    Context context;
    RadioDataManager rDM;

    public StationAdapter(Context context, List<Station> stations) {
        this.context = context;
        this.stations = stations;
    }

    @Override
    public StationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.station_item, parent, false);
        final StationsViewHolder viewHolder = new StationsViewHolder(view);

        rDM = new RadioDataManager(context);
        final MediaPlayerWrapper mPlayer = new MediaPlayerWrapper();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = viewHolder.getAdapterPosition();
                rDM.getStationPLS(new IStationPLSReceiver() {
                    @Override
                    public void onStationPLSReceived(String streamURL) {
                        if(mPlayer != null){
                            mPlayer.reset();
                        }
                        mPlayer.playStream(streamURL);
                    }
                }, stations.get(position).getStationId());
            }
        });

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }

    @Override
    public void onBindViewHolder(StationsViewHolder holder, int position) {
        Station station = stations.get(position);
        holder.title.setText(station.getStationName());
        holder.bitrate.setText(String.valueOf(station.getBitrate()));
    }


    static class StationsViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView bitrate;
        RadioDataManager rDM;

        public StationsViewHolder(View view) {
            super(view);
            rDM = new RadioDataManager(null);
            title = (TextView) view.findViewById(R.id.station_name);
            bitrate = (TextView) view.findViewById(R.id.bitrate);
        }
    }

}
