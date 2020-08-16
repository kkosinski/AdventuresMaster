package com.wintermute.adventuresmaster.database.app;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.wintermute.adventuresmaster.database.dao.menu.ActivityDescDao;
import com.wintermute.adventuresmaster.database.dao.menu.ActivityExtrasDao;
import com.wintermute.adventuresmaster.database.dao.menu.BoardDao;
import com.wintermute.adventuresmaster.database.dao.menu.MenuItemDao;
import com.wintermute.adventuresmaster.database.dao.tools.gm.AudioFileDao;
import com.wintermute.adventuresmaster.database.dao.tools.gm.AudioWithOptsDao;
import com.wintermute.adventuresmaster.database.dao.tools.gm.SceneDao;
import com.wintermute.adventuresmaster.database.entity.menu.ActivityDesc;
import com.wintermute.adventuresmaster.database.entity.menu.ActivityExtras;
import com.wintermute.adventuresmaster.database.entity.menu.Board;
import com.wintermute.adventuresmaster.database.entity.menu.MenuItem;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioFile;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioWithOpts;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Scene;

/**
 * Abstract class containing all information to create database. Singleton.
 *
 * @author wintermute
 */
@Database(entities = {MenuItem.class, ActivityDesc.class, ActivityExtras.class, AudioFile.class, AudioWithOpts.class,
                      Scene.class, Board.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    public static AppDatabase INSTANCE;

    public abstract MenuItemDao menuItemDao();

    public abstract ActivityDescDao activityDescDao();

    public abstract ActivityExtrasDao activityExtrasDao();

    public abstract AudioFileDao audioFileDao();

    public abstract AudioWithOptsDao audioWithOptsDao();

    public abstract SceneDao sceneDao();

    public abstract BoardDao boardDao();

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
                .databaseBuilder(context, AppDatabase.class, "adventuresmaster.db")
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