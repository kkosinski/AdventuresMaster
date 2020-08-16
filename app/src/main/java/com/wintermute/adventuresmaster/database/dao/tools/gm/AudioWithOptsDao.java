package com.wintermute.adventuresmaster.database.dao.tools.gm;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioWithOpts;

/**
 * Access audio with opts in database.
 */
@Dao
public interface AudioWithOptsDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(AudioWithOpts audioWithOpts);

    @Query("SELECT * FROM audioWithOpts WHERE audioFileId = :audioFileId")
    AudioWithOpts getById(long audioFileId);
}
