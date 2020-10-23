package com.wintermute.adventuresmaster.database.repository;

import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.wintermute.adventuresmaster.database.app.AppDatabase;
import com.wintermute.adventuresmaster.database.dao.tools.gm.AudioFileDao;
import com.wintermute.adventuresmaster.database.dao.tools.gm.AudioInSceneDao;
import com.wintermute.adventuresmaster.database.dao.tools.gm.LightDao;
import com.wintermute.adventuresmaster.database.dao.tools.gm.SceneDao;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioFile;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioInScene;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Light;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Scene;
import com.wintermute.adventuresmaster.database.entity.tools.gm.SceneDesc;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * This class manages scene data handling between the application and database.
 *
 * @author wintermute
 */
public class SceneRepository
{
    private SceneDao sceneDao;
    private AudioFileDao audioFileDao;
    private AudioInSceneDao audioInSceneDao;
    private LightDao lightDao;

    /**
     * Creates an instance.
     *
     * @param context of calling activity.
     */
    public SceneRepository(Context context)
    {
        AppDatabase database = AppDatabase.getAppDatabase(context);
        sceneDao = database.sceneDao();
        audioFileDao = database.audioFileDao();
        lightDao = database.lightDao();
        audioInSceneDao = database.audioInSceneDao();
    }

    /**
     * @param target scene to create.
     * @return id of new created scene if successful, otherwise -1.
     */
    public long storeScene(Scene target)
    {
        return executeTask(new InsertTask(sceneDao, false), target);
    }

    /**
     * @return list of scenes contained in selected board.
     */
    public LiveData<List<SceneDesc>> getScenesForBoard(long boardId)
    {
        return sceneDao.getScenesInBoard(boardId);
    }

    /**
     * @param target scene to update.
     * @return id of updated scene if successful, otherwise -1.
     */
    public long updateScene(Scene target)
    {
        return executeTask(new InsertTask(audioFileDao, true), target);
    }

    /**
     * Inserts audio track with settings for scene.
     *
     * @param target audio file reference with audio player settings.
     */
    public long storeSceneAudio(AudioInScene target)
    {
        return executeTask(new InsertTask(audioInSceneDao, false), target);
    }

    /**
     * Stores audio file.
     *
     * @param target audio file to create
     * @return id of newly created audio file if not present, otherwise id of existing audio file.
     */
    public long storeAudioFile(AudioFile target)
    {
        return executeTask(new InsertTask(audioFileDao, false), target);
    }

    /**
     * Stores light assigned to scene.
     *
     * @param target light to store.
     */
    public void storeLightInScene(Light target)
    {
        executeTask(new InsertTask(lightDao, false), target);
    }

    private long executeTask(AsyncTask task, Object target)
    {
        try
        {
            return (long) task.execute(target).get();
        } catch (ExecutionException | InterruptedException e)
        {
            e.printStackTrace();
        }
        return -1L;
    }
}

/**
 * Inserts or updates scenes and its dependencies in database. Executes insert if isUpdateTask is set to false,
 * otherwise executes update task. Returns id of modified or created object. If inserting {@link AudioFile} checks if
 * object exists in database before inserting it. If true, returns id of existing audio file.
 *
 * @author wintermute
 */
class InsertTask extends AsyncTask<Object, Void, Long>
{
    private Object dao;
    private boolean isUpdateTask;

    InsertTask(Object dao, boolean isUpdateTask)
    {
        this.dao = dao;
        this.isUpdateTask = isUpdateTask;
    }

    @Override
    protected Long doInBackground(Object... target)
    {
        Object content = target[0];
        if (content instanceof Scene)
        {
            return isUpdateTask ? ((SceneDao) dao).update((Scene) content) : ((SceneDao) dao).insert((Scene) content);
        } else if (content instanceof AudioFile)
        {
            long audioFileId = ((AudioFileDao) dao).insert((AudioFile) content);
            return audioFileId != -1L ? audioFileId : ((AudioFileDao) dao).getIdByUri(((AudioFile) content).getUri());
        } else if (content instanceof AudioInScene)
        {
            return ((AudioInSceneDao) dao).insert((AudioInScene) content);
        } else if (content instanceof Light)
        {
            return ((LightDao) dao).insert((Light) content);
        }

        return -1L;
    }
}