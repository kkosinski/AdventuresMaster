package com.wintermute.adventuresmaster.database.dao.menu;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import com.wintermute.adventuresmaster.database.entity.menu.ActivityDesc;

/**
 * ActivityDesc as database access object. Bug in RoomDatabase forces the naming of parameters passed into queries to be
 * like arg0, etc. Otherwise tests does not work.
 *
 * @author wintermute
 */
@Dao
public interface ActivityDescDao
{
    /**
     * @param arg0 activityId of targeted activity.
     * @return activity with all information.
     */
    @Query("SELECT * FROM activityDesc WHERE activityId = :arg0")
    LiveData<ActivityDesc> getActivityDesc(long arg0);
}
