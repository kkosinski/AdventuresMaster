package com.wintermute.adventuresmaster.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import com.wintermute.adventuresmaster.database.entity.ActivityDesc;

/**
 * Represents the activity description as database access object.
 */
@Dao
public interface ActivityDescDao
{
    @Query("SELECT * FROM activityDesc WHERE activityId = :activityId")
    LiveData<ActivityDesc> getActivityDesc(long activityId);
}
