package com.wintermute.adventuresmaster.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.wintermute.adventuresmaster.database.app.AppDatabase;
import com.wintermute.adventuresmaster.database.entity.menu.Board;

import java.util.List;

/**
 * Handles logic and notifies the {@link com.wintermute.adventuresmaster.view.BoardActivity} about changes.
 *
 * @author wintermute
 */
public class BoardViewModel extends ViewModel
{
    public LiveData<List<Board>> getTopLevelBoards(Context ctx, String type)
    {
        return AppDatabase.getAppDatabase(ctx).boardDao().getTopLevelBoards(type);
    }

    public LiveData<List<Board>> getBoardsByParentId(Context ctx, long id, String type)
    {
        return AppDatabase.getAppDatabase(ctx).boardDao().getBoardsByParentId(id, type);
    }

    public LiveData<Board> getBoardById(Context ctx, long id){
        return AppDatabase.getAppDatabase(ctx).boardDao().getById(id);
    }

    public void createNewBoard(Context ctx, String name, String type, long parentId)
    {
        Board board = new Board(name, type, parentId);
        new InsertTask(ctx).execute(board);
        new UpdateTask(ctx).execute(parentId);
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

    private static class UpdateTask extends AsyncTask<Long, Void, Void>
    {

        @SuppressLint("StaticFieldLeak")
        private Context ctx;

        UpdateTask(Context ctx)
        {
            this.ctx = ctx;
        }

        @Override
        protected Void doInBackground(Long... ids)
        {
            AppDatabase.getAppDatabase(ctx).boardDao().makeBoardToParent(ids[0]);
            return null;
        }
    }
}
