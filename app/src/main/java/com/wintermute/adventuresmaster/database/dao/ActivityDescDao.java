package com.wintermute.adventuresmaster.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import com.wintermute.adventuresmaster.database.entity.ActivityDesc;

/**
 * ActivityDesc as database access object.
 *
 * @author wintermute
 */
@Dao
public interface ActivityDescDao
{
    @Query("SELECT * FROM activityDesc WHERE activityId = :activityId")
    LiveData<ActivityDesc> getActivityDesc(long activityId);
}
