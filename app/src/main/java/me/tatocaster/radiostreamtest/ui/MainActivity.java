package me.tatocaster.radiostreamtest.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import me.tatocaster.radiostreamtest.Constants;
import me.tatocaster.radiostreamtest.R;
import me.tatocaster.radiostreamtest.RadioDataManager;
import me.tatocaster.radiostreamtest.adapter.StationAdapter;
import me.tatocaster.radiostreamtest.interfaces.ITopStationReceiver;
import me.tatocaster.radiostreamtest.model.Station;


public class MainActivity extends Activity implements Drawer.OnDrawerItemClickListener {

    private static final String TAG = "MainActivity";
    private Drawer materialDrawer;
    RadioDataManager radioDM;
    private RecyclerView mRecyclerView;
    StationAdapter stationAdapter;
    List<Station> stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize materialDrawer
        materialDrawer = initDrawerWithListeners();

        // radio manager initilization
        radioDM = new RadioDataManager(this);

        // recycle view init
        mRecyclerView = (RecyclerView) findViewById(R.id.stationList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        stations = new ArrayList<>();
        stationAdapter = new StationAdapter(MainActivity.this, stations);
        mRecyclerView.setAdapter(stationAdapter);

        // no genre, just top stations
        radioDM.getTopStations(new ITopStationReceiver() {
            @Override
            public void onTopStationsReceived(List<Station> stationsResponse) {
                // setting adapter for recycle view
                stations.addAll(stationsResponse);
                stationAdapter.notifyDataSetChanged();
            }
        }, "");

    }

    /**
     * initialize drawer
     *
     * @return Drawer result
     */
    private Drawer initDrawerWithListeners() {
        final Drawer result = new DrawerBuilder()
                .withActivity(this)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Alternative").withIdentifier(Constants.DRAWER_ALTERNATIVE_GENRE_ID),
                        new PrimaryDrawerItem().withName("Jazz").withIdentifier(Constants.DRAWER_JAZZ_GENRE_ID)
                )
                .withOnDrawerItemClickListener(this)
                .build();
        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
        switch (iDrawerItem.getIdentifier()) {
            case Constants.DRAWER_ALTERNATIVE_GENRE_ID:
                radioDM.getTopStations(new ITopStationReceiver() {
                    @Override
                    public void onTopStationsReceived(List<Station> stations) {
                        // setting adapter for recycle view
                        stationAdapter = new StationAdapter(MainActivity.this, stations);
                        mRecyclerView.swapAdapter(stationAdapter, true);
                        stationAdapter.notifyDataSetChanged();
                    }
                }, "Alternative");
                break;
        }
        return false;
    }
}
