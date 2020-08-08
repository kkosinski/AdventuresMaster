package com.wintermute.adventuresmaster.database.app;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.wintermute.adventuresmaster.database.dao.ActivityDescDao;
import com.wintermute.adventuresmaster.database.dao.MenuItemDao;
import com.wintermute.adventuresmaster.database.entity.ActivityDesc;
import com.wintermute.adventuresmaster.database.entity.ActivityExtras;
import com.wintermute.adventuresmaster.database.entity.MenuItem;

/**
 * Abstract class containing all information to create database. Singleton.
 *
 * @author wintermute
 */
@Database(entities = {MenuItem.class, ActivityDesc.class, ActivityExtras.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    public static AppDatabase INSTANCE;

    public abstract MenuItemDao menuItemDao();
    public abstract ActivityDescDao activityDescDao();

    /**
     * Create an instance if not existent, return created if already existing.
     *
     * @param context application context.
     * @return instance of this class.
     */
    public static AppDatabase getAppDatabase(Context context)
    {
        if (INSTANCE == null)
        {
            INSTANCE = Room
                .databaseBuilder(context, AppDatabase.class, "adventuresmaster")
                .createFromAsset("databases/initial.db")
                .build();
        }
        return INSTANCE;
    }


    /**
     * Destroy instance of this class.
     */
    public static void destroyInstance()
    {
        INSTANCE = null;
    }
}