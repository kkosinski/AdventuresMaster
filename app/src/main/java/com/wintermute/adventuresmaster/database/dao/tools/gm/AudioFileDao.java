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

    @Query("SELECT id FROM audioFile WHERE uri = :uri")
    long getIdByUri(String uri);
}
