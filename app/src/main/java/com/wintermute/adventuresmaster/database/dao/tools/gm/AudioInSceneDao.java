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
 * Dao for managing {@link AudioInScene}. Bug in RoomDatabase forces the naming of parameters passed into queries to be
 * like arg0, etc. Otherwise tests does not work.
 *
 * @author wintermute
 */
@Dao
public interface AudioInSceneDao
{
    @Insert
    long insert(AudioInScene audioInScene);

    /**
     * @param arg0 scene id containing targeted audio files.
     * @return list of audio files for targeted scene.
     */
    @Transaction
    @Query("SELECT * FROM audioInScene WHERE inScene = :arg0")
    LiveData<List<AudioFileWithOpts>> getAudioForScenes(long arg0);
}
