package com.wintermute.adventuresmaster.database.dao.tools.gm;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Scene;

import java.util.List;

/**
 * SceneDao as database access object.
 *
 * @author wintermute
 */
@Dao
public interface SceneDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Scene scene);

    @Query("SELECT * FROM scene WHERE inBoard = :boardId")
    LiveData<List<Scene>> getOrderedByBoard(long boardId);
}
