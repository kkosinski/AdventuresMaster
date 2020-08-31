package com.wintermute.adventuresmaster.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.ArrayMap;
import androidx.lifecycle.ViewModel;
import com.wintermute.adventuresmaster.database.app.AppDatabase;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioFile;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioFileWithOpts;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioInScene;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Scene;
import com.wintermute.adventuresmaster.services.player.GameAudioPlayer;
import com.wintermute.adventuresmaster.services.player.SceneManager;
import com.wintermute.adventuresmaster.view.custom.SceneAudioEntry;
import com.wintermute.adventuresmaster.view.tools.gm.SceneCreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Handles logic and notifies the {@link SceneCreator} about changes.
 *
 * @author wintermute
 */
public class CreateSceneViewModel extends ViewModel
{

    private Map<String, AudioFileWithOpts> preparedAudio = new HashMap<>();
    private GameAudioPlayer gameAudioPlayer = GameAudioPlayer.getInstance();

    /**
     * Creates audio files if these don´t already exists, creates opts and scene.
     *
     * @param context of calling activity.
     * @param inBoard is the boardId to attach the scene to it.
     */
    public void storeSceneAndAudio(Context context, String sceneName, long inBoard)
    {
        AppDatabase appDatabase = AppDatabase.getAppDatabase(context);

        Scene scene = new Scene(sceneName, inBoard);
        try
        {
            Long sceneId = new InsertTask(appDatabase).execute(scene).get();
            for (AudioFileWithOpts target : preparedAudio.values())
            {
                Long audioFileId = new InsertTask(appDatabase).execute(target.getAudioFiles().get(0)).get();
                AudioInScene audioInScene = target.getAudioInScene();
                audioInScene.setAudioFile(audioFileId);
                audioInScene.setInScene(sceneId);

                new InsertTask(appDatabase).execute(audioInScene);
            }
        } catch (ExecutionException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }

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

    public void playTrack(Context ctx, String tag)
    {
        if (preparedAudio.containsKey(tag))
        {
            AudioFileWithOpts target = preparedAudio.get(tag);
            gameAudioPlayer.playAudioType(ctx, target);
        }
    }

    public void playSceneForPreview(Context context)
    {
        Intent scenePreview = new Intent(context, SceneManager.class);
        scenePreview.putParcelableArrayListExtra("audioList", new ArrayList<>(preparedAudio.values()));
        context.startForegroundService(scenePreview);
    }

    /**
     * Async task to operate on databases for {@link SceneCreator}. Performs insert operation. Except in case when row
     * exists yet and should not be created twice, in this case this task performs select operation.
     */
    private static class InsertTask extends AsyncTask<Object, Void, Long>
    {

        @SuppressLint("StaticFieldLeak")
        private AppDatabase appDatabase;

        /**
         * Creates an instance of async task.
         *
         * @param appDatabase instance of database.
         */
        InsertTask(AppDatabase appDatabase)
        {
            this.appDatabase = appDatabase;
        }

        @Override
        protected Long doInBackground(Object... objects)
        {
            if (objects[0] instanceof AudioFile)
            {
                long createdAudioFileId = appDatabase.audioFileDao().insert((AudioFile) objects[0]);
                if (createdAudioFileId == -1L)
                {
                    return appDatabase.audioFileDao().getIdByPath(((AudioFile) objects[0]).getUri());
                }
                return createdAudioFileId;
            } else if (objects[0] instanceof AudioInScene)
            {
                return appDatabase.audioInSceneDao().insert((AudioInScene) objects[0]);
            } else if (objects[0] instanceof Scene)
            {
                return appDatabase.sceneDao().insert((Scene) objects[0]);
            }
            return null;
        }
    }
}