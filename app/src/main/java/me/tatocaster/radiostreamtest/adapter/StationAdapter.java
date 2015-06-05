package me.tatocaster.radiostreamtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.tatocaster.radiostreamtest.R;
import me.tatocaster.radiostreamtest.model.Station;

/**
 * Created by tatocaster on 6/5/2015.
 */
public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder> {

    List<Station> stations = new ArrayList<>();
    Context context;

    public StationAdapter(Context context, List<Station> stations) {
        this.context = context;
        this.stations = stations;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.station_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Station station = stations.get(position);
        holder.title.setText(station.getStationName());
        holder.bitrate.setText(String.valueOf(station.getBytrate()));
    }


    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView bitrate;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            title = (TextView) view.findViewById(R.id.station_name);
            bitrate = (TextView) view.findViewById(R.id.bitrate);
        }

        @Override
        public void onClick(View view) {
            Log.d("RECYCLEVIEW", title.getText().toString());
        }
    }

}
