package com.wintermute.adventuresmaster.database.dao.tools.gm;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Scene;
import com.wintermute.adventuresmaster.database.entity.tools.gm.SceneDesc;

import java.util.List;

/**
 * SceneDao as database access object. Bug in RoomDatabase forces the naming of parameters passed into queries to be
 * like arg0, etc. Otherwise tests does not work.
 *
 * @author wintermute
 */
@Dao
public interface SceneDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Scene scene);

    /**
     * @param arg0 board id containing the requested scenes.
     * @return all children scenes for target board.
     */
    @Transaction
    @Query("SELECT * FROM scene WHERE inBoard=:arg0")
    LiveData<List<SceneDesc>> getScenesInBoard(long arg0);

    @Update
    int update(Scene target);
}
