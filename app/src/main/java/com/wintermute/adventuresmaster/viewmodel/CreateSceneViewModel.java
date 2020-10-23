package com.wintermute.adventuresmaster.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.lifecycle.ViewModel;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioFile;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioFileWithOpts;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioInScene;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Light;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Scene;
import com.wintermute.adventuresmaster.database.repository.SceneRepository;
import com.wintermute.adventuresmaster.services.player.GameAudioPlayer;
import com.wintermute.adventuresmaster.services.player.SceneManager;
import com.wintermute.adventuresmaster.view.custom.SceneAudioEntry;
import com.wintermute.adventuresmaster.view.tools.gm.SceneCreator;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles logic and notifies the {@link SceneCreator} about changes.
 *
 * @author wintermute
 */
public class CreateSceneViewModel extends ViewModel
{

    private Map<String, AudioFileWithOpts> preparedAudio = new HashMap<>();
    @Setter
    private Light light;
    private GameAudioPlayer gameAudioPlayer = GameAudioPlayer.getInstance();

    /**
     * Creates audio files if these don´t already exists, stores audio settings, scene and light settings.
     *
     * @param context of calling activity.
     * @param scene newly configured scene.
     */
    public void storeScene(Context context, Scene scene)
    {
        SceneRepository sceneRepository = new SceneRepository(context);

        scene.setId(sceneRepository.storeScene(scene));
        preparedAudio.values().forEach(configuredTrack -> {
            AudioFile audioFile = configuredTrack.getAudioFiles().get(0);
            audioFile.setId(sceneRepository.storeAudioFile(audioFile));

            AudioInScene audioInScene = configuredTrack.getAudioInScene();
            audioInScene.setAudioFile(audioFile.getId());
            audioInScene.setInScene(scene.getId());
            sceneRepository.storeSceneAudio(audioInScene);

            //Grant permission to access this uri on this device until revoked.
            context
                .getContentResolver()
                .takePersistableUriPermission(Uri.parse(audioFile.getUri()), Intent.FLAG_GRANT_READ_URI_PERMISSION);

        });
        if (light != null)
        {
            light.setInScene(scene.getId());
            sceneRepository.storeLightInScene(light);
        }
    }

    //TODO: delegate all methods operating on the audio player to the GameAudioPlayer
    /**
     * Terminate all existing players.
     */
    public void stopGameAudioPlayer()
    {
        gameAudioPlayer.stopAll();
    }

    /**
     * Updates volume for prepared audio.
     *
     * @param tag of audio type.
     * @param progress new volume value.
     */
    public void updateTrackVolume(String tag, int progress)
    {
        gameAudioPlayer.adjustVolume(tag, progress);
        if (preparedAudio.containsKey(tag))
        {
            AudioFileWithOpts updatedTarget = preparedAudio.get(tag);
            updatedTarget.getAudioInScene().setVolume(progress);
            preparedAudio.put(tag, updatedTarget);
        }
    }

    /**
     * Update loop option for prepared track.
     *
     * @param loop status.
     * @param tag of audio type.
     */
    public void updateLoopingStatus(boolean loop, String tag)
    {
        if (preparedAudio.containsKey(tag))
        {
            gameAudioPlayer.setLoopForPlayer(tag, loop);
            AudioFileWithOpts updatedTarget = preparedAudio.get(tag);
            updatedTarget.getAudioInScene().setRepeat(loop);
            preparedAudio.put(tag, updatedTarget);
        }
    }

    /**
     * Update loop option for prepared track.
     *
     * @param playAfterIntro scheduling status.
     * @param tag of audio type.
     */
    public void updateTrackScheduling(boolean playAfterIntro, String tag)
    {
        if (preparedAudio.containsKey(tag))
        {
            AudioFileWithOpts updatedTarget = preparedAudio.get(tag);
            updatedTarget.getAudioInScene().setPlayAfterEffect(playAfterIntro);
            preparedAudio.put(tag, updatedTarget);
        }
    }

    /**
     * Prepares audio file with configuration to persist in database.
     *
     * @param audio object containing opts.
     * @param uri of audio file on device.
     */
    public void prepareAudioFileWithOpts(SceneAudioEntry audio, String uri)
    {
        AudioFileWithOpts result = new AudioFileWithOpts();
        result.setAudioFiles(Collections.singletonList(new AudioFile(uri)));
        result.setAudioInScene(new AudioInScene(audio.getVolume(), audio.isRepeatTrack(), audio.isPlayAfterEffect(),
            audio.getTag().toString()));
        preparedAudio.put(audio.getTag().toString(), result);
    }

    /**
     * Plays single track
     *
     * @param ctx of calling activity
     * @param tag of selected player.
     */
    public void playTrack(Context ctx, String tag)
    {
        if (preparedAudio.containsKey(tag))
        {
            AudioFileWithOpts target = preparedAudio.get(tag);
            gameAudioPlayer.playAudioType(ctx, target);
        }
    }

    /**
     * Plays all tracks with all settings configured for created scene.
     *
     * @param context of calling activity.
     */
    public void playSceneForPreview(Context context)
    {
        Intent scenePreview = new Intent(context, SceneManager.class);
        scenePreview.putParcelableArrayListExtra("audioList", new ArrayList<>(preparedAudio.values()));
        context.startForegroundService(scenePreview);
    }
}