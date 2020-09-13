package com.wintermute.adventuresmaster.database.dao.tools.gm;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioFile;

/**
 * Access audio files in database. Bug in RoomDatabase forces the naming of parameters passed into queries to be * like
 * arg0, etc. Otherwise tests does not work.
 *
 * @author wintermute
 */
@Dao
public interface AudioFileDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(AudioFile audioFile);

    /**
     * @param arg0 uri for file.
     * @return audio file by unique uri.
     */
    @Query("SELECT id FROM audioFile WHERE uri = :arg0")
    long getIdByUri(String arg0);
}
