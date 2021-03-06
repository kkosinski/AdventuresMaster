package com.wintermute.adventuresmaster.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.database.entity.menu.MenuItem;
import com.wintermute.adventuresmaster.database.repository.MenuRepository;
import com.wintermute.adventuresmaster.helper.LayoutFactory;
import com.wintermute.adventuresmaster.viewmodel.MenuViewModel;

import java.util.List;

/**
 * Main activity. Default entry point of this application.
 *
 * @author wintermute
 */
public class AdventuresMaster extends AppCompatActivity
{

    private MenuViewModel model;
    private LayoutFactory layoutFactory;
    private LinearLayout layout;
    private Observer<List<MenuItem>> menuItemsObserver;
    private MenuItem currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        init();
    }

    private void init()
    {
        grantUserPermission();
        layoutFactory = LayoutFactory.getInstance();
        layout = layoutFactory.getDefaultLayout(getWindow().getDecorView());
        model = new ViewModelProvider(this).get(MenuViewModel.class);
        model.initRepository(new MenuRepository(this));
        menuItemsObserver = displayedMenuItems -> displayedMenuItems.forEach(
            i -> layoutFactory.addViewToDefaultLayout(layout, initMenuItem(i)));

        model.getTopLevelItems().observe(AdventuresMaster.this, menuItemsObserver);
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
                model.getActivity(target).observe(this, activityDesc ->
                {
                    try
                    {
                        String packageInfo =
                            activityDesc.getPackageInfo() == null ? "" : activityDesc.getPackageInfo() + ".";
                        Class<?> c =
                            Class.forName(this.getPackageName() + ".view." + packageInfo + activityDesc.getClassName());
                        Intent intent = new Intent(this, c);
                        if (activityDesc.isHasExtras())
                        {
                            model.getActivityExtras(activityDesc).observe(AdventuresMaster.this, activityExtras ->
                            {
                                activityExtras.forEach(e -> intent.putExtra(e.getKey(), e.getValue()));
                                startActivity(intent);
                            });
                        } else
                        {
                            startActivity(intent);
                        }
                    } catch (ClassNotFoundException e)
                    {
                        Toast.makeText(this, "This function is not implemented yet", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
            } else
            {
                currentItem = target;
                layout.removeAllViews();
                model.getSelectedItemContent(target).observe(AdventuresMaster.this, menuItemsObserver);
            }
        });
        return result;
    }

    private void updateCurrentItem()
    {
        final Observer<MenuItem> parentFetcher = p -> currentItem = p;
        model.getItemParent(currentItem).observe(AdventuresMaster.this, parentFetcher);
    }

    /**
     * Grants permissions to the application to access the storage.
     */
    private void grantUserPermission()
    {
        String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS};
        for (String permission : permissions)
        {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
            {
                shouldShowRequestPermissionRationale(permission);
                requestPermissions(new String[] {permission}, 0);
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        if (currentItem != null && currentItem.getId() != -1L)
        {
            layout.removeAllViews();
            model.getItemParentContent(currentItem).observe(this, menuItemsObserver);
            if (currentItem.getParentId() != -1L)
            {
                updateCurrentItem();
            }
        }
    }
}
