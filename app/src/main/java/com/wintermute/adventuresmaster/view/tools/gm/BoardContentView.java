package com.wintermute.adventuresmaster.view.tools.gm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.database.entity.tools.gm.SceneDesc;
import com.wintermute.adventuresmaster.dynamiclist.DynamicAdapter;
import com.wintermute.adventuresmaster.dynamiclist.DynamicListHelper;
import com.wintermute.adventuresmaster.dynamiclist.DynamicListItem;
import com.wintermute.adventuresmaster.viewmodel.BoardViewModel;

import java.util.List;

/**
 * Contains lists with {@link com.wintermute.adventuresmaster.database.entity.tools.gm.Scene} or Soundboard contained in
 * game master´s tools.
 *
 * @author wintermute
 */
public class BoardContentView extends AppCompatActivity implements DynamicAdapter.ItemClickListener
{
    private BoardViewModel model;
    private List<SceneDesc> storedScenes;
    private long sceneId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scene_table_activity);

        init();
    }

    private void init()
    {
        sceneId = getIntent().getLongExtra("inBoard", 0L);
        model = new ViewModelProvider(this).get(BoardViewModel.class);

        Button addEntry = findViewById(R.id.board_content_table_add_entry);
        addEntry.setOnClickListener(
            v -> startActivity(new Intent(this, SceneCreator.class).putExtra("inBoard", sceneId)));

        RecyclerView recyclerView =
            DynamicListHelper.getInstance().initRecyclerView(this, findViewById(R.id.board_content_table_content_list));

        model.getScenesInBoard(this, sceneId).observe(this, scenes ->
        {
            storedScenes = scenes;
            List<DynamicListItem> listContent = model.loadStoredScenes(scenes);

            DynamicAdapter dynamicAdapter = DynamicListHelper.getInstance().initAdapter(this, listContent, this);
            recyclerView.setAdapter(dynamicAdapter);
            dynamicAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onDynamicListItemClick(View view, int position, long itemId)
    {
        model.startNewScene(this, storedScenes.get(position));
    }
}