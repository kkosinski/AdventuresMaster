package com.wintermute.adventuresmaster.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.ViewModel;
import com.wintermute.adventuresmaster.database.app.AppDatabase;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioFile;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioInScene;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Scene;
import com.wintermute.adventuresmaster.view.tools.gm.SceneCreator;

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

    /**
     * Creates audio files if these don´t already exists, adds opts to it and stores it together with scene.
     *
     * @param ctx activity context.
     * @param audioWithPath audio entry with all information and path to the audio file.
     * @param sceneName title of scene to display in list after its created and once it will be listed.
     */
    public void createSceneWithAllDependingOperations(Context ctx, String sceneName, long inBoard,
                                                      HashMap<AudioInScene, String> audioWithPath)
    {
        Scene target = new Scene(sceneName, inBoard);
        try
        {
            AppDatabase appDatabase = AppDatabase.getAppDatabase(ctx);
            long sceneId = new InsertTask(appDatabase).execute(target).get();
            target.setId(sceneId);
            for (Map.Entry<AudioInScene, String> audioEntry : audioWithPath.entrySet())
            {

                AudioInScene audioInScene = audioEntry.getKey();
                audioInScene.setInScene(sceneId);
                audioInScene.setAudioFile(
                    new InsertTask(appDatabase).execute(new AudioFile(audioEntry.getValue())).get());

                long sceneInAudioId = new InsertTask(appDatabase).execute(audioInScene).get();
                if ("effect".equals(audioInScene.getTag()))
                {
                    target.setEffect(sceneInAudioId);
                } else if ("music".equals(audioInScene.getTag()))
                {
                    target.setMusic(sceneInAudioId);
                } else if ("ambience".equals(audioInScene.getTag()))
                {
                    target.setAmbience(sceneInAudioId);
                }
            }
            new UpdateTask(AppDatabase.getAppDatabase(ctx)).execute(target);
        } catch (ExecutionException | InterruptedException e)
        {
            e.printStackTrace();
        }
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
                    return appDatabase.audioFileDao().getIdByPath(((AudioFile) objects[0]).getPath());
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

    /**
     * Async task to operate on databases for {@link SceneCreator}. Performs update operation on {@link Scene} table.
     */
    private static class UpdateTask extends AsyncTask<Scene, Void, Void>
    {

        private AppDatabase appDatabase;

        /**
         * Creates an instance of async task.
         *
         * @param appDatabase instance of database to access..
         */
        UpdateTask(AppDatabase appDatabase)
        {
            this.appDatabase = appDatabase;
        }

        @Override
        protected Void doInBackground(Scene... scenes)
        {
            appDatabase.sceneDao().update(scenes[0]);
            return null;
        }
    }
}