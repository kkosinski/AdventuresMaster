package com.wintermute.adventuresmaster.view.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.database.entity.settings.HueBridge;
import com.wintermute.adventuresmaster.services.light.RestGun;
import com.wintermute.adventuresmaster.view.custom.ConnectHueBridgeDialog;
import com.wintermute.adventuresmaster.view.custom.adapter.HueBridgeViewAdapter;
import com.wintermute.adventuresmaster.viewmodel.HueBridgeViewModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * In this activity the user can register his device in philips hue bridge, change default hue and select hue bulbs.
 *
 * @author wintermute
 */
public class PhilipsHueConfig extends AppCompatActivity
    implements ConnectHueBridgeDialog.OnConnectClickedListener, RestGun.OnSuccess, AdapterView.OnItemSelectedListener
{
    private HueBridgeViewModel model;
    private List<HueBridge> bridges;
    private HueBridgeViewAdapter adapter;
    private Button setAsDefaultDevice, connectBulbs;
    private Spinner registeredDevicesView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_hue_config_activity);

        init();
    }

    private void init()
    {
        model = new ViewModelProvider(this).get(HueBridgeViewModel.class);

        Button discoverBridge = findViewById(R.id.philips_hue_config_discover);
        discoverBridge.setOnClickListener(v -> showResult(model.discoverBridge()));

        setAsDefaultDevice = findViewById(R.id.philips_hue_config_set_default);
        setAsDefaultDevice.setOnClickListener(
            v -> model.changeDefaultDevice(this, (HueBridge) registeredDevicesView.getSelectedItem()));

        connectBulbs = findViewById(R.id.philips_hue_config_connect_bulbs);
        connectBulbs.setOnClickListener(v -> startActivity(
            new Intent(this, PhilipsHueBulbsSettings.class).putExtra("bridge",
                ((HueBridge) registeredDevicesView.getSelectedItem()))));

        registeredDevicesView = findViewById(R.id.philips_hue_config_bridge_spinner);
        registeredDevicesView.setOnItemSelectedListener(this);

        bridges = new ArrayList<>();

        adapter = new HueBridgeViewAdapter(this, bridges);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        registeredDevicesView.setAdapter(adapter);

        showBridges();
    }

    private void showBridges()
    {
        model.getRegisteredDevices(this).observe(this, hueBridges ->
        {
            if (hueBridges.isEmpty())
            {
                registeredDevicesView.setVisibility(View.GONE);
                setAsDefaultDevice.setVisibility(View.GONE);
                connectBulbs.setVisibility(View.GONE);
            } else
            {
                bridges.clear();
                bridges.addAll(hueBridges);
                registeredDevicesView.setSelection(0);
                updateBridgesView();
            }
        });
    }

    private void updateBridgesView()
    {
        adapter.notifyDataSetChanged();
        if (!bridges.isEmpty())
        {
            findViewById(R.id.philips_hue_config_bridge_spinner).setVisibility(View.VISIBLE);
            connectBulbs.setVisibility(View.VISIBLE);
        }
    }

    private void showResult(boolean discoveringResult)
    {
        new ConnectHueBridgeDialog(this, discoveringResult).onCreateDialog(null).show();
    }

    private void updateDefaultHueBridge(int position)
    {
        if (bridges.get(position).isDefaultDevice())
        {
            setAsDefaultDevice.setVisibility(View.GONE);
            return;
        }
        setAsDefaultDevice.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnectClicked()
    {
        if (!model.registerHueBridge(this))
        {
            Toast.makeText(this, "Already registered", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(JSONArray response)
    {
        if (model.registerDevice(response, this))
        {
            Toast.makeText(this, "Successfully registered", Toast.LENGTH_SHORT).show();
        } else
        {
            Toast.makeText(this, "Failed to register", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(JSONObject response)
    {
        model.setDeviceName(this, response);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        updateDefaultHueBridge(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
    }
}