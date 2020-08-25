package com.wintermute.adventuresmaster.view.tools.gm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioInScene;
import com.wintermute.adventuresmaster.services.player.AudioTypePlayer;
import com.wintermute.adventuresmaster.view.custom.SceneAudioEntry;
import com.wintermute.adventuresmaster.viewmodel.CreateSceneViewModel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * In this activity a scene is composed and the responsible view model ({@link CreateSceneViewModel}) is notified to
 * persist these changes once the user want to save it.
 *
 * @author wintermute
 */
public class SceneCreator extends AppCompatActivity
    implements SceneAudioEntry.OnSelectAudioClick, SceneAudioEntry.OnPlayAudioClick, SceneAudioEntry.OnChangedVolume
{
    private String audioEntryType;
    private SceneAudioEntry effect, music, ambience;
    private Map<String, SceneAudioEntry> sceneAudioEntries;
    private Map<String, String> audioFilePath;
    private AudioTypePlayer audioTypePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scene_activity);

        initComponents();
    }

    private void initComponents()
    {
        audioTypePlayer = AudioTypePlayer.getInstance();

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

        sceneAudioEntries = new HashMap<>();
        sceneAudioEntries.put("effect", effect);
        sceneAudioEntries.put("music", music);
        sceneAudioEntries.put("ambience", ambience);

        sceneAudioEntries.values().forEach(a ->
        {
            a.setOnSelectAudioClick(this);
            a.setOnPlayAudioClick(this);
            a.setOnChangedVolume(this);
        });
    }

    private void storeScene(String sceneName)
    {
        audioTypePlayer.stopAll();

        Map<String, SceneAudioEntry> sceneInAudioWithPath = new HashMap<String, SceneAudioEntry>()
        {{
            put("effect", effect);
            put("music", music);
            put("ambience", ambience);
        }};

        HashMap<AudioInScene, String> audioWithOptsAndPath = new HashMap<>();
        sceneInAudioWithPath
            .entrySet()
            .forEach(e -> composeAudioInSceneAndFilePath(audioWithOptsAndPath, e.getKey(), e.getValue()));

        CreateSceneViewModel model = new ViewModelProvider(this).get(CreateSceneViewModel.class);
        model.createSceneWithAllDependingOperations(this, sceneName, getIntent().getLongExtra("inBoard", 0L),
            audioWithOptsAndPath);
    }

    private void composeAudioInSceneAndFilePath(HashMap<AudioInScene, String> result, String key, SceneAudioEntry type)
    {
        if (audioFilePath != null) {
            if (audioFilePath.containsKey(key))
            {
                result.put(new AudioInScene(type.getVolume(), type.isRepeatTrack(), type.isPlayAfterEffect(), key),
                    audioFilePath.get(key));
            }
        }
    }

    private String sanitizeFileName(String path)
    {
        String result = new File(path).getName();
        return result.length() < 30 ? result : result.substring(0, 27) + "...";
    }

    @Override
    public void onSelectAudioClickListener(String tag)
    {
        audioEntryType = tag;

        startActivityForResult(new Intent(Intent.ACTION_OPEN_DOCUMENT).setType("audio/*"), 1);
    }

    @Override
    public void onPlayClickListener(String tag)
    {
        audioEntryType = tag;

        String fileUri = audioFilePath.containsKey(audioEntryType) ? audioFilePath.get(audioEntryType) : null;
        audioTypePlayer.startOrStopPlaying(this, fileUri, sceneAudioEntries.get(tag));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            if (audioFilePath == null)
            {
                audioFilePath = new HashMap<>();
            }

            String selectedFilePath = Objects.requireNonNull(data.getData()).getPath();
            if ("effect".equals(audioEntryType))
            {
                audioFilePath.put("effect", data.getDataString());
                effect.setSceneAudioFileTitle(sanitizeFileName(selectedFilePath));
            } else if ("music".equals(audioEntryType))
            {
                audioFilePath.put("music", data.getDataString());
                music.setSceneAudioFileTitle(sanitizeFileName(selectedFilePath));
            }
            if ("ambience".equals(audioEntryType))
            {
                audioFilePath.put("ambience", data.getDataString());
                ambience.setSceneAudioFileTitle(sanitizeFileName(selectedFilePath));
            }
        }
    }

    @Override
    public void onChangedVolume(int progress, String tag)
    {
        audioTypePlayer.adjustVolume(tag, progress);
    }
}