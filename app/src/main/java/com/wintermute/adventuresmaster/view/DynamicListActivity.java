package com.wintermute.adventuresmaster.view;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.adapter.DynamicAdapter;
import com.wintermute.adventuresmaster.model.DynamicListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Example for implementic the dynamic list in activity.
 * @deprecated (this class will be removed in near future.)
 *
 * @author wintermute
 *
 */
//TODO: Remove this class as soon as possible. For this time it is only usage example.
public class DynamicListActivity extends AppCompatActivity implements DynamicAdapter.ItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_list_activity);

        List<String> labelChildren = Arrays.asList("info");

        List<DynamicListItem> items = new ArrayList<>();
        items.add(new DynamicListItem("label 1", labelChildren, 0L));
        labelChildren = Arrays.asList("additional", "info");
        items.add(new DynamicListItem("label 2", labelChildren, 0L));
        labelChildren = Arrays.asList("other", "additional info");
        items.add(new DynamicListItem("label 3", labelChildren, 0L));

        RecyclerView recyclerView = initRecyclerView();
        DynamicAdapter dynamicAdapter = initAdapter(items);
        recyclerView.setAdapter(dynamicAdapter);
    }

    private RecyclerView initRecyclerView(){
        RecyclerView result = findViewById(R.id.list);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        result.setLayoutManager(layout);
        DividerItemDecoration divider = new DividerItemDecoration(result.getContext(), layout.getOrientation());
        result.addItemDecoration(divider);
        return result;
    }

    private DynamicAdapter initAdapter(List<DynamicListItem> items){
        DynamicAdapter adapter = new DynamicAdapter(this, items);
        adapter.setClickListener(this);
        return adapter;
    }

    @Override
    public void onItemClick(View view, int position)
    {
        System.out.println("clicked " + position);
    }
}
