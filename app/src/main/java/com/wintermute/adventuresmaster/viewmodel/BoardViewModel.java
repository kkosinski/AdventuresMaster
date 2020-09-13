package com.wintermute.adventuresmaster.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.wintermute.adventuresmaster.database.app.AppDatabase;
import com.wintermute.adventuresmaster.database.entity.menu.Board;
import com.wintermute.adventuresmaster.database.entity.tools.gm.SceneDesc;
import com.wintermute.adventuresmaster.dynamiclist.DynamicListItem;
import com.wintermute.adventuresmaster.services.player.SceneManager;
import com.wintermute.adventuresmaster.view.tools.gm.BoardContentView;
import com.wintermute.adventuresmaster.view.tools.gm.BoardPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
     * @param context of calling activity.
     * @param name of board to create.
     * @param type of this board (scene or soundboard).
     * @param isContentTable defines if it has nested boards (false) or content of given type (true).
     * @param parentId defines in which board this board should be created. If the board is toplevel, parentId is
     *     -1L.
     */
    public void createNewBoard(Context context, String name, String type, boolean isContentTable, long parentId)
    {
        Board board = new Board(name, type, isContentTable, parentId);
        new InsertTask(AppDatabase.getAppDatabase(context)).execute(board);
    }

    /**
     * @param ctx of calling activity.
     * @param boardId to which the scenes belong.
     * @return list of scenes to show in @{@link BoardContentView}
     */
    public LiveData<List<SceneDesc>> getScenesInBoard(Context ctx, long boardId)
    {
        return AppDatabase.getAppDatabase(ctx).sceneDao().getScenesInBoard(boardId);
    }

    /**
     * Launches new scene.
     *
     * @param context of calling activity.
     * @param target scene containing light and audio
     */
    public void startNewScene(Context context, SceneDesc target)
    {
        Intent sceneService = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        sceneService.setClass(context, SceneManager.class);
        sceneService.putExtra("scene", target);
        context.startForegroundService(sceneService);
    }

    /**
     * @param scenes stored in database for currently requested board.
     * @return list scenes with all information mapped to {@link DynamicListItem}.
     */
    public List<DynamicListItem> loadStoredScenes(List<SceneDesc> scenes)
    {
        return scenes
            .stream()
            .map(s -> new DynamicListItem(s.getScene().getTitle(), getScenesAudioFilesTitles(s), s.getScene().getId()))
            .collect(Collectors.toList());
    }

    private List<String> getScenesAudioFilesTitles(SceneDesc sceneDesc)
    {
        return sceneDesc
            .getAudioInScene()
            .stream()
            .map(a -> a.getAudioInScene().getTag().toUpperCase() + ": " + a.getAudioFiles().get(0).getTitle())
            .collect(Collectors.toList());
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
