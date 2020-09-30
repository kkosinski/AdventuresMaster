package com.wintermute.adventuresmaster.database.dao.settings;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.wintermute.adventuresmaster.database.entity.settings.HueBridge;

import java.util.List;

/**
 * Manages hue bridge access. Bug in RoomDatabase forces the naming of parameters passed into queries to be like arg0,
 * etc. Otherwise tests does not work.
 *
 * @author wintermute
 */
@Dao
public interface HueBridgeDao
{
    @Insert
    long insert(HueBridge target);

    /**
     * @param arg0 user authorized to make api calls.
     * @return philips hue bridge selected by unique user per device.
     */
    @Query("SELECT * FROM hueBridge WHERE deviceId = :arg0")
    HueBridge getByDeviceId(String arg0);

    /**
     * @deprecated this api call does not work anymore.
     * //TODO: delete me. Critical.
     */
    @Query("SELECT * FROM hueBridge")
    HueBridge getDefaultDevice();

    @Update
    void update(HueBridge target);

    /**
     * @deprecated this api call does not work anymore. Critical.
     * //TODO: delete me. Critical.
     */
    @Query("SELECT * FROM hueBridge")
    LiveData<List<HueBridge>> getAll();
}
