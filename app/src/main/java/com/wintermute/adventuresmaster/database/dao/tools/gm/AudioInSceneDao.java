package com.wintermute.adventuresmaster.database.dao.tools.gm;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioFileWithOpts;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioInScene;

import java.util.List;

/**
 * Dao for managing {@link AudioInScene}
 *
 * @author wintermute
 */
@Dao
public interface AudioInSceneDao
{
    @Insert
    long insert(AudioInScene audioInScene);

    @Transaction
    @Query("SELECT * FROM audioInScene WHERE inScene = :sceneId")
    LiveData<List<AudioFileWithOpts>> getAudioForScenes(long sceneId);
}
