package com.wintermute.adventuresmaster.database;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;
import com.wintermute.adventuresmaster.database.entity.menu.MenuItem;
import com.wintermute.adventuresmaster.database.repository.MenuRepository;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

public class MenuItemTest
{
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void getMenuItemsFromDatabase() throws InterruptedException
    {
        MenuRepository repo = new MenuRepository(ApplicationProvider.getApplicationContext());

        List<MenuItem> menu = LiveDataTestUtil.getValue(repo.getTopLevelItems());
        assertEquals(3, menu.size());

        MenuItem activity = LiveDataTestUtil.getValue(repo.getSelectedItemContent(menu.get(2))).get(0);
        assertTrue(activity.isActivity());
        assertTrue(LiveDataTestUtil.getValue(repo.getActivity(activity)).isHasExtras());
    }
}
