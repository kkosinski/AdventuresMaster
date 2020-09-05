package com.wintermute.adventuresmaster.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.wintermute.adventuresmaster.database.app.AppDatabase;
import com.wintermute.adventuresmaster.database.entity.menu.Board;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioFileWithOpts;
import com.wintermute.adventuresmaster.database.entity.tools.gm.SceneWithAudio;
import com.wintermute.adventuresmaster.view.tools.gm.BoardPanel;

import java.util.List;

/**
 * Handles logic and notifies the {@link BoardPanel} about changes.
 *
 * @author wintermute
 */
public class BoardViewModel extends ViewModel
{
    /**
     * @param ctx of calling activity
     * @param type of boards from type to get.
     * @return top level boards from requested type.
     */
    public LiveData<List<Board>> getTopLevelBoards(Context ctx, String type)
    {
        return AppDatabase.getAppDatabase(ctx).boardDao().getTopLevelBoards(type);
    }

    /**
     * @param ctx of calling activity.
     * @param parentId of board to get its children.
     * @param type of boards from type to get.
     * @return children of parent board from requested type.
     */
    public LiveData<List<Board>> getBoardsByParentId(Context ctx, long parentId, String type)
    {
        return AppDatabase.getAppDatabase(ctx).boardDao().getBoardsByParentId(parentId, type);
    }

    /**
     * @param ctx of calling activity.
     * @param id of board to get.
     * @return requested board.
     */
    public LiveData<Board> getBoardById(Context ctx, long id)
    {
        return AppDatabase.getAppDatabase(ctx).boardDao().getById(id);
    }

    /**
     * @param ctx of calling activity.
     * @param name of board to create.
     * @param type of this board (scene or soundboard).
     * @param isContentTable defines if it has nested boards (false) or content of given type (true).
     * @param parentId defines in which board this board should be created. If the board is toplevel, parentId is
     *     -1L.
     */
    public void createNewBoard(Context ctx, String name, String type, boolean isContentTable, long parentId)
    {
        Board board = new Board(name, type, isContentTable, parentId);
        new InsertTask(ctx).execute(board);
    }

    /**
     * @param ctx of calling activity.
     * @param boardId to which the scenes belong.
     * @return list of scenes to show in @{@link com.wintermute.adventuresmaster.view.tools.gm.BoardContentTable}
     */
    public LiveData<List<SceneWithAudio>> getScenesForBoard(Context ctx, long boardId)
    {
        return AppDatabase.getAppDatabase(ctx).sceneDao().getScenesInBoard(boardId);
    }

    /**
     * @param ctx of calling activity
     * @param sceneId of scene containing target audio
     * @return list with audio tracks contained in scene.
     */
    public LiveData<List<AudioFileWithOpts>> getAudioInScene(Context ctx, long sceneId){
        return AppDatabase.getAppDatabase(ctx).audioInSceneDao().getAudioForScenes(sceneId);
    }

    private static class InsertTask extends AsyncTask<Board, Void, Long>
    {
        @SuppressLint("StaticFieldLeak")
        private Context ctx;

        InsertTask(Context ctx)
        {
            this.ctx = ctx;
        }

        @Override
        protected Long doInBackground(Board... boards)
        {
            return AppDatabase.getAppDatabase(ctx).boardDao().insert(boards[0]);
        }
    }
}
