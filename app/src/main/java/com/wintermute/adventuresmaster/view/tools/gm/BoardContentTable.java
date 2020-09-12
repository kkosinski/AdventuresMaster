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
import com.wintermute.adventuresmaster.services.player.GameAudioPlayer;
import com.wintermute.adventuresmaster.services.player.SceneManager;
import com.wintermute.adventuresmaster.viewmodel.BoardViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class BoardContentTable extends AppCompatActivity implements DynamicAdapter.ItemClickListener
{
    private BoardViewModel model;
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

        model.getScenesForBoard(this, sceneId).observe(this, scenes ->
        {
            List<DynamicListItem> listContent = new ArrayList<>();
            scenes.forEach(s ->
            {
                List<String> additionalInfo = new ArrayList<>();
                s
                    .getAudioInScene()
                    .forEach(a -> additionalInfo.add(
                        a.getAudioInScene().getTag().toUpperCase() + ": " + a.getAudioFiles().get(0).getTitle()));
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
        GameAudioPlayer gameAudioPlayer = GameAudioPlayer.getInstance();
        gameAudioPlayer.stopAll();
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setClass(this, SceneManager.class);

        //TODO: get scene with audio and light settings instead of audio files only
        model.getAudioInScene(this, itemId).observe(this, audioFileWithOpts ->
        {
            intent.putParcelableArrayListExtra("audioList", new ArrayList<>(audioFileWithOpts));
            model.prepareSceneService(this, intent);
        });

        //TODO: prevent calling light like this. More information in todo above.
        model.getLight(this, itemId).observe(this, light ->
        {
            intent.putExtra("light", light);
            model.prepareSceneService(this, intent);
        });
    }
}