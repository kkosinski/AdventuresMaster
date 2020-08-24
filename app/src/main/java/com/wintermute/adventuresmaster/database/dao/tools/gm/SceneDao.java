package com.wintermute.adventuresmaster.database.dao.tools.gm;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Scene;
import com.wintermute.adventuresmaster.database.entity.tools.gm.SceneWithAudio;

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

    @Transaction
    @Query("SELECT * FROM scene WHERE inBoard=:boardId")
    LiveData<List<SceneWithAudio>> getScenesInBoard(long boardId);

    @Update
    void update(Scene target);
}
