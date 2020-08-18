package com.wintermute.adventuresmaster.database.dao.menu;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.wintermute.adventuresmaster.database.entity.menu.Board;

import java.util.List;

/**
 * Access to the board in database.
 *
 * @author wintermute
 */
@Dao
public interface BoardDao
{
    @Query("SELECT * FROM board WHERE parentId = -1 AND type = :type")
    LiveData<List<Board>> getTopLevelBoards(String type);

    @Query("SELECT * FROM board WHERE parentId = :parentId AND type = :type")
    LiveData<List<Board>> getBoardsByParentId(long parentId, String type);

    @Query("SELECT * FROM board WHERE id = :id")
    LiveData<Board> getById(long id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Board board);
}
