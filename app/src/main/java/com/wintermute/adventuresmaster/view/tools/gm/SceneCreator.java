package com.wintermute.adventuresmaster.view.tools.gm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.view.custom.SceneAudioEntry;
import com.wintermute.adventuresmaster.viewmodel.CreateSceneViewModel;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * In this activity a scene is composed and the responsible view model ({@link CreateSceneViewModel}) is notified to
 * persist these changes once the user want to save it.
 *
 * @author wintermute
 */
public class SceneCreator extends AppCompatActivity
    implements SceneAudioEntry.OnSelectAudioClick, SceneAudioEntry.OnPlayAudioClick
{
    private String audioEntryType;
    private SceneAudioEntry effect, music, ambience;
    private String effectPath, musicPath, ambiencePath;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scene_activity);

        initComponents();
    }

    private void initComponents()
    {
        Button save = findViewById(R.id.scene_activity_save_scene);
        save.setOnClickListener(v ->
        {
            TextView sceneNameView = findViewById(R.id.scene_activity_scene_name);
            String sceneName = sceneNameView.getText().toString();
            if ("".equals(sceneName))
            {
                Toast.makeText(this, "Name must not be empty", Toast.LENGTH_SHORT).show();
                sceneNameView.setHintTextColor(Color.RED);
                return;
            }
            storeScene(sceneName);
            finish();
        });

        effect = findViewById(R.id.scene_activity_effect);
        effect.disablePlayAfterEffectOption();
        music = findViewById(R.id.scene_activity_music);
        ambience = findViewById(R.id.scene_activity_ambience);
        SceneAudioEntry[] audioEntries = new SceneAudioEntry[] {effect, music, ambience};

        Arrays.stream(audioEntries).forEach(a ->
        {
            a.setOnSelectAudioClick(this);
            a.setOnPlayAudioClick(this);
        });
    }

    @Override
    public void onSelectAudioClickListener(View v)
    {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        startActivityForResult(intent, 1);

        ViewParent rootElement = v.getParent().getParent().getParent();
        audioEntryType = (String) ((View) rootElement).getTag();
    }

    @Override
    public void onPlayClickListener(View v)
    {
        ViewParent rootElement = v.getParent().getParent();
        audioEntryType = (String) ((View) rootElement).getTag();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            String selectedFilePath = Objects.requireNonNull(data.getData()).getPath();
            if ("effect".equals(audioEntryType))
            {
                effectPath = selectedFilePath;
                effect.setSceneAudioFileTitle(sanitizeFileName(selectedFilePath));
            } else if ("music".equals(audioEntryType))
            {
                musicPath = selectedFilePath;
                music.setSceneAudioFileTitle(sanitizeFileName(selectedFilePath));
            }
            if ("ambience".equals(audioEntryType))
            {
                ambiencePath = selectedFilePath;
                ambience.setSceneAudioFileTitle(sanitizeFileName(selectedFilePath));
            }
        }
    }

    private void storeScene(String sceneName)
    {
        HashMap<SceneAudioEntry, String> audioWithPath = new HashMap<>();
        if (effectPath != null)
        {
            audioWithPath.put(effect, effectPath);
        }

        if (musicPath != null)
        {
            audioWithPath.put(music, musicPath);
        }

        if (ambiencePath != null)
        {
            audioWithPath.put(ambience, ambiencePath);
        }

        CreateSceneViewModel model = new ViewModelProvider(this).get(CreateSceneViewModel.class);
        model.createSceneWithAllDependingOperations(this, sceneName, getIntent().getLongExtra("inBoard", 0L),
            audioWithPath);
    }

    private String sanitizeFileName(String path)
    {
        String result = new File(path).getName();
        return result.length() < 30 ? result : result.substring(0, 27) + "...";
    }
}