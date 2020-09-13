package com.wintermute.adventuresmaster.view.tools.gm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.flask.colorpicker.ColorPickerView;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Light;
import com.wintermute.adventuresmaster.services.light.ColorHelper;
import com.wintermute.adventuresmaster.services.network.RestGun;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Activity to set up light settings for scene.
 *
 * @author wintermute
 */
public class LightSettings extends AppCompatActivity implements RestGun.OnSuccess
{
    private boolean showChanges;
    private ColorPickerView colorPicker;
    private SeekBar brightnessBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light_settings_activity);

        init();
    }

    private void init()
    {
        RestGun restGun = new RestGun(this);
        showChanges = false;

        CheckBox fadeIn = findViewById(R.id.light_settings_fade_effect);
        Button switchChangesState = findViewById(R.id.light_settings_show_changes);
        switchChangesState.setOnClickListener(v ->
        {
            if (showChanges)
            {
                switchChangesState.setText(R.string.light_settings_show_changes_off);
                showChanges = false;
            } else
            {
                switchChangesState.setText(R.string.light_settings_show_changes_on);
                showChanges = true;
            }
        });

        colorPicker = findViewById(R.id.light_settings_color_picker);
        colorPicker.addOnColorSelectedListener(v ->
        {
            if (showChanges)
            {
                restGun.changeColor(ColorHelper.extractHueColorCoordinates(colorPicker.getSelectedColor()),
                    fadeIn.isChecked());
            }
        });

        brightnessBar = findViewById(R.id.light_settings_brightness_bar);
        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (showChanges)
                {
                    restGun.changeBrightness(progress, fadeIn.isChecked());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        Button save = findViewById(R.id.light_settings_save);
        save.setOnClickListener(v ->
        {
            if (colorPicker.getSelectedColor() != -1)
            {
                setResult(RESULT_OK, new Intent().putExtra("preparedLight",
                    new Light(colorPicker.getSelectedColor(), brightnessBar.getProgress(), fadeIn.isChecked())));
                finish();
            } else
            {
                Toast.makeText(this, "Please select color first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onResponse(JSONArray response)
    {

    }

    @Override
    public void onResponse(JSONObject response)
    {

    }
}