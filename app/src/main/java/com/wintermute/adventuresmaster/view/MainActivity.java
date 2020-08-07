package com.wintermute.adventuresmaster.view;

import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.database.entity.MenuItem;
import com.wintermute.adventuresmaster.helper.LayoutFactory;
import com.wintermute.adventuresmaster.viewmodel.MenuViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private MenuViewModel model;
    private LayoutFactory layoutFactory;
    private LinearLayout layout;
    private Observer<List<MenuItem>> menuItemsObserver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init()
    {
        layoutFactory = LayoutFactory.getInstance();
        layout = layoutFactory.getDefaultLayout(getWindow().getDecorView());
        model = new ViewModelProvider(this).get(MenuViewModel.class);

        layout.removeAllViews();
        menuItemsObserver =
            currentItems -> currentItems.forEach(i -> layoutFactory.addViewToDefaultLayout(layout, initMenuItem(i)));

        model.getTopLevelItems(this).observe(MainActivity.this, menuItemsObserver);
    }

    private Button initMenuItem(MenuItem target)
    {
        Button result = new Button(this);
        result.setText(target.getTitle());
        result.setId(ViewCompat.generateViewId());
        result.setTag(target.getTitle());
        result.setOnClickListener(v ->
        {
            //TODO: update current item
            if (target.isActivity())
            {
                onBackPressed();
            } else
            {
                layout.removeAllViews();
                model.getSelectedItemContent(this, target).observe(MainActivity.this, menuItemsObserver);
            }
        });
        return result;
    }

    @Override
    public void onBackPressed()
    {
        //TODO: fix navigating back
    }
}
