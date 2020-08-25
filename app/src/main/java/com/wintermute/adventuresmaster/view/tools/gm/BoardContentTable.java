package com.wintermute.adventuresmaster.view.tools.gm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.dynamiclist.DynamicAdapter;
import com.wintermute.adventuresmaster.dynamiclist.DynamicListHelper;
import com.wintermute.adventuresmaster.dynamiclist.DynamicListItem;
import com.wintermute.adventuresmaster.viewmodel.BoardViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class BoardContentTable extends AppCompatActivity implements DynamicAdapter.ItemClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scene_table_activity);

        init();
    }

    private void init()
    {
        long currentBoardId = getIntent().getLongExtra("inBoard", 0L);
        BoardViewModel model = new ViewModelProvider(this).get(BoardViewModel.class);

        Button addEntry = findViewById(R.id.board_content_table_add_entry);
        addEntry.setOnClickListener(
            v -> startActivity(new Intent(this, SceneCreator.class).putExtra("inBoard", currentBoardId)));

        RecyclerView recyclerView =
            DynamicListHelper.getInstance().initRecyclerView(this, findViewById(R.id.board_content_table_content_list));

        model.getScenesForBoard(this, currentBoardId).observe(this, scenes ->
        {
            List<DynamicListItem> listContent = new ArrayList<>();
            scenes.forEach(s ->
            {
                List<String> additionalInfo = new ArrayList<>();
                s
                    .getAudioInScene()
                    .forEach(a -> additionalInfo.add(
                        a.getAudioInScene().getTag() + ": " + a.getAudioFiles().get(0).getTitle()));
                listContent.add(new DynamicListItem(s.getScene().getTitle(), additionalInfo, s.getScene().getId()));
            });

            DynamicAdapter dynamicAdapter = DynamicListHelper.getInstance().initAdapter(this, listContent, this);
            recyclerView.setAdapter(dynamicAdapter);
            dynamicAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onDynamicListItemClick(View view, int position, long itemId)
    {
        //TODO: dependes which object is called
    }
}