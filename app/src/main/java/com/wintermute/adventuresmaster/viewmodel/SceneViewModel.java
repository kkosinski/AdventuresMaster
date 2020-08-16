package com.wintermute.adventuresmaster.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.ViewModel;
import com.wintermute.adventuresmaster.database.app.AppDatabase;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioFile;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioWithOpts;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Scene;
import com.wintermute.adventuresmaster.view.components.SceneAudioEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Handles logic and notifies the {@link com.wintermute.adventuresmaster.view.tools.gm.SceneActivity} about changes.
 *
 * @author wintermute
 */
public class SceneViewModel extends ViewModel
{

    public void setSceneAudio(Context ctx, HashMap<SceneAudioEntry, String> audioWithPath, String sceneName)
    {
        HashMap<String, Long> audioFileTypeAndId = new HashMap<>();
        for (Map.Entry<SceneAudioEntry, String> audioEntry : audioWithPath.entrySet())
        {
            SceneAudioEntry audioDesc = audioEntry.getKey();
            try
            {
                Long audioFileId = new DatabaseOperationTask(ctx).execute(new AudioFile(audioEntry.getValue())).get();

                Long audioWithOptsId = new DatabaseOperationTask(ctx)
                    .execute(new AudioWithOpts(audioFileId, audioDesc.getVolume(), audioDesc.isRepeatTrack(), audioEntry.getKey().isPlayAfterEffect()))
                    .get();

                audioFileTypeAndId.put(audioEntry.getKey().getTag().toString(), audioWithOptsId);
            } catch (ExecutionException | InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        new DatabaseOperationTask(ctx).execute(compose(sceneName, audioFileTypeAndId));
    }

    private Scene compose(String sceneName, HashMap<String, Long> sceneAudioWithOpts)
    {
        Scene result = new Scene();
        result.setTitle(sceneName);
        if (sceneAudioWithOpts.containsKey("effect"))
        {
            result.setEffect(sceneAudioWithOpts.get("effect"));
        }
        if (sceneAudioWithOpts.containsKey("music"))
        {
            result.setMusic(sceneAudioWithOpts.get("music"));
        }
        if (sceneAudioWithOpts.containsKey("ambience"))
        {
            result.setAmbience(sceneAudioWithOpts.get("ambience"));
        }

        return result;
    }

    /**
     * Async task to operate on databases for {@link com.wintermute.adventuresmaster.view.tools.gm.SceneActivity}.
     * Performs mostly operation insert operation. Except in case when row exists yet and should not be created twice,
     * in this case this task performs select operation.
     */
    private static class DatabaseOperationTask extends AsyncTask<Object, Void, Long>
    {

        @SuppressLint("StaticFieldLeak")
        private Context context;

        /**
         * Creates an instance of async task.
         *
         * @param context of calling activity. This param is needed for getting database access.
         */
        DatabaseOperationTask(Context context)
        {
            this.context = context;
        }

        @Override
        protected Long doInBackground(Object... objects)
        {
            AppDatabase appDatabase = AppDatabase.getAppDatabase(context);
            if (objects[0] instanceof AudioFile)
            {
                long createdId = appDatabase.audioFileDao().insert((AudioFile) objects[0]);
                if (createdId == -1L)
                {
                    return appDatabase.audioFileDao().getIdByPath(((AudioFile) objects[0]).getPath());
                }
                return createdId;
            } else if (objects[0] instanceof AudioWithOpts)
            {
                return appDatabase.audioWithOptsDao().insert((AudioWithOpts) objects[0]);
            } else if (objects[0] instanceof Scene)
            {
                return appDatabase.sceneDao().insert((Scene) objects[0]);
            }
            return -1L;
        }
    }
}