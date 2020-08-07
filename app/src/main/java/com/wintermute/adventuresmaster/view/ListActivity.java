package com.wintermute.adventuresmaster.view;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.adapter.DynamicAdapter;
import com.wintermute.adventuresmaster.model.ViewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity rendering lists. Work in progress
 *
 * @author wintermute
 */
//TODO: Find out if this class become obsolete in near future.
public class ListActivity extends AppCompatActivity implements DynamicAdapter.ItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        List<ViewItem> items = new ArrayList<>();

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

    private DynamicAdapter initAdapter(List<ViewItem> items){
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
