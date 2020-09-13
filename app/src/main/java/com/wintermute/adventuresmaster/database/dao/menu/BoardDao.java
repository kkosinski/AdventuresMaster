package com.wintermute.adventuresmaster.database.dao.menu;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.wintermute.adventuresmaster.database.entity.menu.Board;

import java.util.List;

/**
 * Access to the board in database. Bug in RoomDatabase forces the naming of parameters passed into queries to be like
 * arg0, etc. Otherwise tests does not work.
 *
 * @author wintermute
 */
@Dao
public interface BoardDao
{
    /**
     * @param arg0 type content hold in board.
     * @return top level boards for requested for selected boards type.
     */
    @Query("SELECT * FROM board WHERE parentId = -1 AND type = :arg0")
    LiveData<List<Board>> getTopLevelBoards(String arg0);

    /**
     * @TODO: remove requesting type of boards. It´s unnecessary as the type is currently asked in getTopLevelBoards.
     * @param arg0 board parent id
     * @param arg1 type of boards
     * @return children boards for requested board of specified type.
     */
    @Query("SELECT * FROM board WHERE parentId = :arg0 AND type = :arg1")
    LiveData<List<Board>> getBoardsByParentId(long arg0, String arg1);

    /**
     * @param arg0 board id.
     * @return requested board by id.
     */
    @Query("SELECT * FROM board WHERE id = :arg0")
    LiveData<Board> getById(long arg0);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Board board);
}
