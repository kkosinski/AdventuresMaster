package com.wintermute.adventuresmaster.viewmodel;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.wintermute.adventuresmaster.database.app.AppDatabase;
import com.wintermute.adventuresmaster.database.entity.MenuItem;

import java.util.List;

/**
 * Observes changes in menu and sends notifications about changes to the activity showing menu.
 *
 * @author wintermute
 */
public class MenuViewModel extends ViewModel
{
    private final MutableLiveData<MenuItem> menuItem = new MutableLiveData<>();

    public LiveData<MenuItem> getItemInfo(Context ctx, MenuItem item){
        return AppDatabase.getAppDatabase(ctx).menuItemDao().getSelectedItem(item.getId());
    }

    public LiveData<List<MenuItem>> getTopLevelItems(Context ctx){
        return AppDatabase.getAppDatabase(ctx).menuItemDao().getMenuItems(-1L);
    }

    public LiveData<List<MenuItem>> getSelectedItemContent(Context ctx, MenuItem target)
    {
        return AppDatabase.getAppDatabase(ctx).menuItemDao().getMenuItems(target.getId());
    }

    public LiveData<List<MenuItem>> getItemParentContent(Context ctx, MenuItem target){
        return AppDatabase.getAppDatabase(ctx).menuItemDao().getMenuItems(target.getParentId());
    }
}
