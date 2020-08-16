package com.wintermute.adventuresmaster.database.dao.tools.gm;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioFile;

/**
 * Access audio files in database.
 *
 * @author wintermute
 */
@Dao
public interface AudioFileDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(AudioFile audioFile);

    @Query("SELECT * FROM audioFile WHERE id = :id")
    AudioFile getById(long id);

    @Query("SELECT id FROM audioFile WHERE path = :path")
    long getIdByPath(String path);
}
