package com.wintermute.adventuresmaster.view.settings;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.services.light.RestGun;
import com.wintermute.adventuresmaster.view.custom.ConnectHueBridgeDialog;
import com.wintermute.adventuresmaster.viewmodel.PhilipsHueConfigViewModel;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * In this activity the user can register his device in philips hue bridge, change default hue and select hue bulbs.
 *
 * @author wintermute
 */
public class PhilipsHueConfig extends AppCompatActivity
    implements ConnectHueBridgeDialog.OnConnectClickedListener, RestGun.OnSuccess
{
    private PhilipsHueConfigViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_hue_config_activity);

        init();
    }

    private void init()
    {
        model = new ViewModelProvider(this).get(PhilipsHueConfigViewModel.class);

        Button discoverBridge = findViewById(R.id.philips_hue_config_discover);
        discoverBridge.setOnClickListener(v -> showResult(model.discoverBridge()));
    }

    private void showResult(boolean discoveringResult)
    {
        new ConnectHueBridgeDialog(this, discoveringResult).onCreateDialog(null).show();
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
        if (model.checkResult(response, this))
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

    }
}