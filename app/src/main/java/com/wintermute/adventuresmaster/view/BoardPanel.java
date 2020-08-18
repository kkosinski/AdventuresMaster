package com.wintermute.adventuresmaster.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.database.entity.menu.Board;
import com.wintermute.adventuresmaster.dynamiclist.DynamicAdapter;
import com.wintermute.adventuresmaster.dynamiclist.DynamicListHelper;
import com.wintermute.adventuresmaster.view.custom.BoardDialog;
import com.wintermute.adventuresmaster.view.tools.gm.BoardContentTable;
import com.wintermute.adventuresmaster.viewmodel.BoardViewModel;

import java.util.List;

/**
 * Endpoint for the user to execute CRUD on {@link com.wintermute.adventuresmaster.database.entity.menu.Board}, {@link
 * com.wintermute.adventuresmaster.database.entity.tools.gm.Scene} and SoundBoard.
 *
 * @author wintermute
 */
public class BoardPanel extends AppCompatActivity
    implements BoardDialog.OnSelectedItem, BoardDialog.OnCreateBoardConfirm, DynamicAdapter.ItemClickListener,
    Retrievable
{

    private static final int BOARD_INDEX = 0;

    private TextView emptyBoards;
    private Button addContent;
    private RecyclerView contentList;
    private BoardViewModel model;
    private Board currentBoard;

    private DynamicListHelper recyclerView;
    public String boardType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_activity);

        init();

        model.getTopLevelBoards(this, boardType).observe(BoardPanel.this, boards ->
        {
            if (boards.isEmpty())
            {
                viewWithEmptyContent();
                return;
            }
            updateViewContent(boards);
        });
    }

    private void viewWithEmptyContent()
    {
        addContent.setVisibility(View.GONE);
        contentList.setVisibility(View.GONE);
        emptyBoards.setVisibility(View.VISIBLE);
    }

    private void init()
    {
        model = new ViewModelProvider(this).get(BoardViewModel.class);
        recyclerView = DynamicListHelper.getInstance();
        boardType = getIntent().getStringExtra("type");
        contentList = recyclerView.initRecyclerView(this, findViewById(R.id.board_activity_content_view), this);
        addContent = findViewById(R.id.board_activity_add_content);
        addContent.setOnClickListener(v -> showPreconfiguredDialog());
        emptyBoards = findViewById(R.id.board_activity_empty_boards);
        emptyBoards.setOnClickListener(v -> showPreconfiguredDialog());
    }

    private void showPreconfiguredDialog()
    {
        Dialog dialog;
        if (currentBoard == null || currentBoard.getId() == -1L)
        {
            dialog = getCreateBoardDialog(false);
        } else
        {
            dialog = new BoardDialog(this).onCreateDialog(null);
        }
        dialog.show();
    }

    private <T> void updateViewContent(List<Board> items)
    {
        emptyBoards.setVisibility(View.GONE);
        addContent.setVisibility(View.VISIBLE);
        contentList.setVisibility(View.VISIBLE);
        recyclerView.updateBoards((List<Board>) items);
    }

    @Override
    public void onDialogItemSelected(int selectionIndex)
    {
        Dialog dialog;

        if (selectionIndex == BOARD_INDEX)
        {
            dialog = getCreateBoardDialog(false);
            dialog.setTitle("Create new board");
        } else
        {
            dialog = getCreateBoardDialog(true);
            dialog.setTitle("Create new content");
        }
        dialog.show();
    }

    private Dialog getCreateBoardDialog(boolean createContentTable)
    {
        return new BoardDialog(this, true, createContentTable,
            getLayoutInflater().inflate(R.layout.board_dialog_create_board, null)).onCreateDialog(null);
    }

    private void retrieveBoardAndPerformAction(long itemId)
    {
        Observer<Board> itemObserver = board ->
        {
            currentBoard = board;
            handleBoardClicked(itemId);
        };
        model.getBoardById(this, itemId).observe(this, itemObserver);
    }

    @Override
    public void onCreateBoardConfirmed(String name, boolean isContentTable)
    {
        long parentId = currentBoard == null ? -1L : currentBoard.getId();
        model.createNewBoard(this, name, boardType, isContentTable, parentId);
    }

    @Override
    public void onDynamicListItemClick(View view, int position, long itemId)
    {
        retrieveBoardAndPerformAction(itemId);
    }

    @Override
    public void handleBoardClicked(long itemId)
    {
        if (currentBoard.isContentTable())
        {
            if ("scenes".equals(boardType)) {
                startActivity(new Intent(this, BoardContentTable.class).putExtra("inBoard", itemId));
            }
        } else
        {
            Observer<List<Board>> itemListObserver = boards ->
            {
                if (boards.isEmpty())
                {
                    viewWithEmptyContent();
                    return;
                }
                updateViewContent(boards);
            };
            model.getBoardsByParentId(this, itemId, boardType).observe(this, itemListObserver);
        }
    }

    @Override
    public void onBackPressed()
    {
        //TODO: fix board navigating
        if (currentBoard == null || currentBoard.getParentId() == -1L)
        {
            finish();
        } else
        {
            model
                .getBoardsByParentId(this, currentBoard.getParentId(), boardType)
                .observe(this, this::updateViewContent);
            retrieveBoardAndPerformAction(currentBoard.getParentId());
        }
    }
}

/**
 * Provides function to wait on asynchronous necessary data to handle item clicks.
 *
 * @author wintermute
 */
interface Retrievable
{
    void handleBoardClicked(long boardId);
}
