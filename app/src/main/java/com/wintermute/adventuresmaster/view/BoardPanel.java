package com.wintermute.adventuresmaster.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.database.entity.menu.Board;
import com.wintermute.adventuresmaster.dynamiclist.DynamicAdapter;
import com.wintermute.adventuresmaster.dynamiclist.DynamicListHelper;
import com.wintermute.adventuresmaster.dynamiclist.DynamicListItem;
import com.wintermute.adventuresmaster.view.custom.BoardDialog;
import com.wintermute.adventuresmaster.view.tools.gm.BoardContentTable;
import com.wintermute.adventuresmaster.viewmodel.BoardViewModel;

import java.util.ArrayList;
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

    private RecyclerView contentList;
    private BoardViewModel model;
    private Board currentBoard;
    private DynamicAdapter adapter;
    private List<DynamicListItem> dynamicItems;
    private DynamicListHelper recyclerViewHelper;

    public String boardType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_activity);

        init();

        model.getTopLevelBoards(this, boardType).observe(BoardPanel.this, boards ->
        {
            if (adapter == null)
            {
                dynamicItems = new ArrayList<>();
                boards.forEach(i -> dynamicItems.add(new DynamicListItem(i.getName(), i.getId())));
                adapter = recyclerViewHelper.initAdapter(this, dynamicItems, this);
                contentList.setAdapter(adapter);
            } else
            {
                updateViewContent(boards);
            }
        });
    }

    private void init()
    {
        model = new ViewModelProvider(this).get(BoardViewModel.class);
        recyclerViewHelper = DynamicListHelper.getInstance();
        boardType = getIntent().getStringExtra("type");
        contentList = recyclerViewHelper.initRecyclerView(this, findViewById(R.id.board_activity_content_view));
        Button addContent = findViewById(R.id.board_activity_add_content);
        addContent.setOnClickListener(v -> new BoardDialog(this).onCreateDialog(null).show());
    }

    private <T> void updateViewContent(List<Board> items)
    {
        if (dynamicItems == null)
        {
            dynamicItems = new ArrayList<>();
        } else
        {
            dynamicItems.clear();
        }
        items.forEach(i -> dynamicItems.add(new DynamicListItem(i.getName(), i.getId())));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogItemSelected(int selectionIndex)
    {
        Dialog dialog;
        if (selectionIndex == BOARD_INDEX)
        {
            dialog = getCreateBoardDialog(false);
            dialog.setTitle("Folder name: ");
        } else
        {
            dialog = getCreateBoardDialog(true);
            dialog.setTitle(boardType + " table name: ");
        }
        dialog.show();
    }

    private Dialog getCreateBoardDialog(boolean createContentTable)
    {
        Dialog dialog = new BoardDialog(this, true, createContentTable,
            getLayoutInflater().inflate(R.layout.board_dialog_create_board, null)).onCreateDialog(null);
        return dialog;
    }

    private void retrieveBoardAndPerformAction(long itemId)
    {
            Observer<Board> itemObserver = board ->
            {
                currentBoard = board;
                try {
                    handleBoardClicked(itemId);
                } catch (NullPointerException e) {
                    model.getTopLevelBoards(this, boardType).observe(this, this::updateViewContent);
                }
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
            if ("scenes".equals(boardType))
            {
                startActivity(new Intent(this, BoardContentTable.class).putExtra("inBoard", itemId));
            }
        } else
        {
            Observer<List<Board>> itemListObserver = this::updateViewContent;
            model.getBoardsByParentId(this, itemId, boardType).observe(this, itemListObserver);
        }
    }

    @Override
    public void onBackPressed()
    {
        try {
            retrieveBoardAndPerformAction(currentBoard.getParentId());
        } catch (NullPointerException e){
            finish();
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
