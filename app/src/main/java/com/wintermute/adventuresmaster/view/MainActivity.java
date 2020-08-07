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
    private MenuItem currentItem;

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

        menuItemsObserver = displayedMenuItems -> displayedMenuItems.forEach(
            i -> layoutFactory.addViewToDefaultLayout(layout, initMenuItem(i)));

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
            if (target.isActivity())
            {
            } else
            {
                currentItem = target;
                layout.removeAllViews();
                model.getSelectedItemContent(this, target).observe(MainActivity.this, menuItemsObserver);
            }
        });
        return result;
    }

    private void updateCurrentAndParrentItem()
    {
        Observer<MenuItem> parentFetcher = p -> currentItem = p;
        model.getItemParent(this, currentItem).observe(MainActivity.this, parentFetcher);
    }

    @Override
    public void onBackPressed()
    {
        System.out.println(currentItem.getId() + " " + currentItem.getParentId());
        if (currentItem.getId() != -1L)
        {
            layout.removeAllViews();
            model.getItemParentContent(this, currentItem).observe(this, menuItemsObserver);
            if (currentItem.getParentId() != -1L)
            {
                updateCurrentAndParrentItem();
            }
        }
    }
}
