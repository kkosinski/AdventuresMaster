package com.wintermute.adventuresmaster.database.dao.settings;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.wintermute.adventuresmaster.database.entity.settings.HueBulb;

import java.util.List;

/**
 * Manages access for hue bulbs.
 *
 * @author wintermute
 */
@Dao
public interface HueBulbDao
{
    @Insert
    void insert(HueBulb hueBulb);

    @Query("SELECT * FROM hueBulb WHERE hueBridge = :bridgeId")
    LiveData<List<HueBulb>> getPairedBulbsDynamically(long bridgeId);

    @Query("SELECT * FROM hueBulb WHERE hueBridge = :bridgeId")
    List<HueBulb> getPairedBulbs(long bridgeId);
}
