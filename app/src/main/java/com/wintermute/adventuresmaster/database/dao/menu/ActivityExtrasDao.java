package com.wintermute.adventuresmaster.database.dao.menu;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import com.wintermute.adventuresmaster.database.entity.menu.ActivityExtras;

import java.util.List;

/**
 * Represents the activity extras as database access object. Bug in RoomDatabase forces the naming of parameters
 * passed into queries to be like arg0, etc. Otherwise tests does not work.
 *
 * @author wintermute
 */
@Dao
public interface ActivityExtrasDao
{
    /**
     * @param arg0 requested activity id.
     * @return activity extras for target activity.
     */
    @Query("SELECT * FROM activityExtras WHERE activityId = :arg0")
    LiveData<List<ActivityExtras>> getExtrasForActivity(long arg0);
}
