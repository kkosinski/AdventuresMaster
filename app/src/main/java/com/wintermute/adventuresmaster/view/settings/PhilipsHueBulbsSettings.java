package com.wintermute.adventuresmaster.view.settings;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.database.entity.settings.HueBridge;
import com.wintermute.adventuresmaster.database.entity.settings.HueBulb;
import com.wintermute.adventuresmaster.services.network.RestGun;
import com.wintermute.adventuresmaster.view.custom.adapter.BulbViewAdapter;
import com.wintermute.adventuresmaster.viewmodel.HueBulbViewModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity provides the functionality to manage philips hue bulbs paired with {@link HueBridge}
 *
 * @author wintermute
 */
public class PhilipsHueBulbsSettings extends AppCompatActivity
    implements RestGun.OnSuccess, BulbViewAdapter.BulbItemClickListener
{
    private List<HueBulb> bulbs;
    private HueBulbViewModel model;
    private BulbViewAdapter adapter;
    private HueBridge currentBridge;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_hue_bulbs_settings_activity);

        init();
    }

    private void init()
    {
        currentBridge = getIntent().getParcelableExtra("bridge");

        RecyclerView bulbsListView = findViewById(R.id.philips_hue_bulbs_list);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        bulbsListView.setLayoutManager(layout);
        DividerItemDecoration divider = new DividerItemDecoration(bulbsListView.getContext(), layout.getOrientation());
        bulbsListView.addItemDecoration(divider);

        bulbs = new ArrayList<>();
        adapter = new BulbViewAdapter(this, bulbs);
        adapter.setClickListener(this);
        bulbsListView.setAdapter(adapter);

        Button storeBulbs = findViewById(R.id.philips_hue_bulbs_pair_bulbs);
        storeBulbs.setOnClickListener(v ->
        {
            model.updatePairedBulbs(this);
            finish();
        });

        model = new ViewModelProvider(this).get(HueBulbViewModel.class);
        getAvailableBulbs();
    }

    private void getAvailableBulbs()
    {
        HueBridge bridge = getIntent().getParcelableExtra("bridge");
        if (bridge != null)
        {
            model.requestAvailableBulbs(this, bridge);
        }
    }

    @Override
    public void onResponse(JSONArray response)
    {

    }

    @Override
    public void onResponse(JSONObject response)
    {
        bulbs.addAll(model.getBulbs(response, currentBridge.getId()));
        model.getPairedBulbs(this, currentBridge.getId()).observe(this, hueBulbs ->
        {
            hueBulbs.forEach(p ->
            {
                for (HueBulb bulb : bulbs)
                {
                    if (p.getId() == bulb.getId())
                    {
                        bulb.setSelected(true);
                    }
                }
            });
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onBulbClick(boolean state, int position)
    {
        bulbs.get(position).setSelected(state);
        model.classifyBulb(bulbs.get(position));
    }
}