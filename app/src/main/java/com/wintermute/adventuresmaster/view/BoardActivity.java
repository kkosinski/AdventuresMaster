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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.adapter.DynamicAdapter;
import com.wintermute.adventuresmaster.database.entity.menu.Board;
import com.wintermute.adventuresmaster.model.DynamicListItem;
import com.wintermute.adventuresmaster.view.components.BoardDialog;
import com.wintermute.adventuresmaster.view.tools.gm.SceneActivity;
import com.wintermute.adventuresmaster.viewmodel.BoardViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Endpoint for the user to execute CRUD on {@link com.wintermute.adventuresmaster.database.entity.menu.Board}, {@link
 * com.wintermute.adventuresmaster.database.entity.tools.gm.Scene} and SoundBoard.
 *
 * @author wintermute
 */
public class BoardActivity extends AppCompatActivity
    implements BoardDialog.OnSelectedItem, BoardDialog.OnCreateBoardConfirm, DynamicAdapter.ItemClickListener
{

    private static final int BOARD_INDEX = 0;
    private static final int CONTENT_INDEX = 1;
    public String boardType;

    private TextView emptyBoards;
    private Button addContent;
    private RecyclerView contentView;
    private BoardViewModel model;
    private Board currentBoard;
    private boolean noBoardsExisting;

    private RecyclerView contentList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_activity);

        initComponents();

        model = new ViewModelProvider(this).get(BoardViewModel.class);

        Observer<List<Board>> observer = boards ->
        {
            if (boards.isEmpty())
            {
                noBoardsExisting = true;
                viewWithEmptyContent();
                return;
            }
            updateViewContent(boards);
        };

        model.getTopLevelBoards(this, boardType).observe(BoardActivity.this, observer);
    }

    private void viewWithEmptyContent()
    {
        addContent.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
        emptyBoards.setVisibility(View.VISIBLE);
    }

    private void initComponents()
    {
        boardType = getIntent().getStringExtra("type");
        contentList = initRecyclerView();
        emptyBoards = findViewById(R.id.board_activity_empty_boards);
        emptyBoards.setOnClickListener(v ->
        {
            Dialog dialog;
            if (noBoardsExisting)
            {
                dialog = new BoardDialog(this, true,
                    getLayoutInflater().inflate(R.layout.board_dialog_create_board, null)).onCreateDialog(null);
            } else
            {
                dialog = new BoardDialog(this).onCreateDialog(null);
            }
            dialog.show();
        });

        contentView = findViewById(R.id.board_activity_content_view);
        addContent = findViewById(R.id.board_activity_add_content);
        addContent.setOnClickListener(v -> new BoardDialog(this).onCreateDialog(null).show());
    }

    private RecyclerView initRecyclerView()
    {
        RecyclerView result = findViewById(R.id.board_activity_content_view);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        result.setLayoutManager(layout);
        DividerItemDecoration divider = new DividerItemDecoration(result.getContext(), layout.getOrientation());
        result.addItemDecoration(divider);
        return result;
    }

    private void updateViewContent(List<Board> boards)
    {
        emptyBoards.setVisibility(View.GONE);
        addContent.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.VISIBLE);
        DynamicAdapter contentAdapter = initAdapter(boards);
        contentList.setAdapter(contentAdapter);
    }

    private DynamicAdapter initAdapter(List<Board> items)
    {
        List<DynamicListItem> dynamicItems = new ArrayList<>();
        items.forEach(i ->
        {
            dynamicItems.add(new DynamicListItem(i.getName(), i.getId()));
        });
        DynamicAdapter adapter = new DynamicAdapter(this, dynamicItems);
        adapter.setClickListener(this);
        return adapter;
    }

    @Override
    public void selectedItem(int selectionIndex)
    {
        if (selectionIndex == BOARD_INDEX)
        {
            Dialog dialog = new BoardDialog(this, true,
                getLayoutInflater().inflate(R.layout.board_dialog_create_board, null)).onCreateDialog(null);
            dialog.show();
        } else if (selectionIndex == CONTENT_INDEX)
        {
            if ("scenes".equals(boardType))
            {
                startActivity(new Intent(this, SceneActivity.class));
            } else if ("effects".equals(boardType))
            {
                //TODO: implement
                startActivity(new Intent(this, Void.class));
            }
        }
    }

    @Override
    public void createBoard(String name)
    {
        long parentId = currentBoard == null ? -1L : currentBoard.getId();
        model.createNewBoard(this, name, boardType, parentId);
    }

    @Override
    public void onItemClick(View view, int position, long itemId)
    {
        updateCurrentBoard(itemId);

        Observer<List<Board>> itemListObserver = boards ->
        {
            if (boards.isEmpty())
            {
                noBoardsExisting = false;
                viewWithEmptyContent();
                return;
            }
            updateViewContent(boards);
        };
        model.getBoardsByParentId(this, itemId, boardType).observe(this, itemListObserver);
    }

    private void updateCurrentBoard(long itemId)
    {
        Observer<Board> itemObserver = board -> currentBoard = board;
        model.getBoardById(this, itemId).observe(this, itemObserver);
    }

    @Override
    public void onBackPressed()
    {
        if (currentBoard == null)
        {
            finish();
        } else
        {
            Observer<List<Board>> observer = this::updateViewContent;
            model.getBoardsByParentId(this, currentBoard.getParentId(), boardType).observe(this, observer);
            updateCurrentBoard(currentBoard.getParentId());
        }
    }
}