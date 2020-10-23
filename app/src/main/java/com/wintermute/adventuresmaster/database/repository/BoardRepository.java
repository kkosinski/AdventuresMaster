package com.wintermute.adventuresmaster.database.repository;

import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.wintermute.adventuresmaster.database.app.AppDatabase;
import com.wintermute.adventuresmaster.database.dao.menu.BoardDao;
import com.wintermute.adventuresmaster.database.dao.tools.gm.SceneDao;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Board;
import com.wintermute.adventuresmaster.database.entity.tools.gm.SceneDesc;
import com.wintermute.adventuresmaster.view.tools.gm.BoardContentView;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Data cache for boards coming from database.
 *
 * @author wintermute
 */
public class BoardRepository
{
    private BoardDao boardDao;
    //TODO: delete me after board and scene separation
    private SceneDao sceneDao;
    private Context context;

    /**
     * Creates an instance.
     *
     * @param context of calling activity.
     */
    public BoardRepository(Context context)
    {
        AppDatabase database = AppDatabase.getAppDatabase(context);
        boardDao = database.boardDao();
        sceneDao = database.sceneDao();
        this.context = context;
    }

    /**
     * @param type of boards from type to get.
     * @return top level boards from requested type.
     */
    public LiveData<List<Board>> getTopLevelBoards(String type)
    {
        return boardDao.getTopLevelBoards(type);
    }

    /**
     * @param parentId of board to get its children.
     * @param type of boards from type to get.
     * @return children of parent board from requested type.
     */
    public LiveData<List<Board>> getBoardsByParentId(long parentId, String type)
    {
        return boardDao.getBoardsByParentId(parentId, type);
    }

    /**
     * @param id of board to get.
     * @return requested board.
     */
    public LiveData<Board> getBoardById(long id)
    {
        return boardDao.getById(id);
    }

    /**
     * Stores newly created board in database.
     *
     * @param board to store in database.
     */
    public Long storeBoard(Board board)
    {
        try
        {
            return new InsertTask(AppDatabase.getAppDatabase(context)).execute(board).get();
        } catch (ExecutionException | InterruptedException e)
        {
            e.printStackTrace();
        }
        return -1L;
    }

    /**
     * @param boardId to which the scenes belong.
     * @return list of scenes to show in @{@link BoardContentView}
     *     <p>
     *     TODO: separate board view and scenes view
     */
    public LiveData<List<SceneDesc>> getScenesInBoard(long boardId)
    {
        return sceneDao.getScenesInBoard(boardId);
    }

    private static class InsertTask extends AsyncTask<Board, Void, Long>
    {
        private AppDatabase db;

        InsertTask(AppDatabase db)
        {
            this.db = db;
        }

        @Override
        protected Long doInBackground(Board... boards)
        {
            return db.boardDao().insert(boards[0]);
        }
    }
}
