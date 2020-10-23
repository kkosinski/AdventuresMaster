package com.wintermute.adventuresmaster.viewmodel;

import android.content.Context;
import android.content.Intent;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Board;
import com.wintermute.adventuresmaster.database.entity.tools.gm.SceneDesc;
import com.wintermute.adventuresmaster.database.repository.BoardRepository;
import com.wintermute.adventuresmaster.dynamiclist.DynamicListItem;
import com.wintermute.adventuresmaster.services.player.SceneManager;
import com.wintermute.adventuresmaster.view.tools.gm.BoardContentView;
import com.wintermute.adventuresmaster.view.tools.gm.BoardPanel;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles logic and notifies the {@link BoardPanel} about changes.
 *
 * @author wintermute
 */
public class BoardViewModel extends ViewModel
{
    private BoardRepository repo;

    /**
     * Creates an instance of repository.
     *
     * @param repo accessing data from database.
     */
    public void initRepository(BoardRepository repo)
    {
        this.repo = repo;
    }

    /**
     * @param type of boards from type to get.
     * @return top level boards from requested type.
     */
    public LiveData<List<Board>> getTopLevelBoards(String type)
    {
        return repo.getTopLevelBoards(type);
    }

    /**
     * @param parentId of board to get its children.
     * @param type of boards from type to get.
     * @return children of parent board from requested type.
     */
    public LiveData<List<Board>> getBoardsByParentId(long parentId, String type)
    {
        return repo.getBoardsByParentId(parentId, type);
    }

    /**
     * @param id of board to get.
     * @return requested board.
     */
    public LiveData<Board> getBoardById(long id)
    {
        return repo.getBoardById(id);
    }

    /**
     * Delegates storing new created board in database to repository.
     *
     * @param board to create and store in database.
     */
    public void storeNewBoard(Board board)
    {
        repo.storeBoard(board);
    }

    /**
     * @param boardId to which the scenes belong.
     * @return list of scenes to show in @{@link BoardContentView}
     */
    public LiveData<List<SceneDesc>> getScenesInBoard(long boardId)
    {
        return repo.getScenesInBoard(boardId);
    }

    /**
     * Launches new scene.
     *
     * @param context of calling activity.
     * @param target scene containing light and audio
     */
    public void startNewScene(Context context, SceneDesc target)
    {
        Intent sceneService = new Intent(context, SceneManager.class);
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
}
