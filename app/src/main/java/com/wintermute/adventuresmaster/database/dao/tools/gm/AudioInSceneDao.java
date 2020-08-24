package com.wintermute.adventuresmaster.database.dao.tools.gm;

import androidx.room.Dao;
import androidx.room.Insert;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioInScene;

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
}
