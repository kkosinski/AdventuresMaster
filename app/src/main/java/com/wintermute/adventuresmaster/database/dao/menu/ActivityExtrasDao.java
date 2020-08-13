package com.wintermute.adventuresmaster.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import com.wintermute.adventuresmaster.database.entity.ActivityExtras;

import java.util.List;

/**
 * Represents the activity extras as database access object.
 *
 * @author wintermute
 */
@Dao
public interface ActivityExtrasDao
{
    @Query("SELECT * FROM activityExtras WHERE activityId = :activityId")
    LiveData<List<ActivityExtras>> getExtrasForActivity(long activityId);
}
