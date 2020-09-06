package com.wintermute.adventuresmaster.database.entity.settings;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import lombok.Data;

/**
 * Represents hue bulbs, which can be connected to the bridge.
 *
 * @author wintermute
 */
@Data
@Entity(tableName = "hueBulb",
        foreignKeys = @ForeignKey(entity = HueBridge.class, parentColumns = "id", childColumns = "hueBridge",
                                  onDelete = CASCADE))
public class HueBulb
{
    @PrimaryKey
    private long id;

    @ColumnInfo(name = "hueBridge")
    private long hueBridge;

    @Ignore
    private String name;

    @Ignore
    private String type;

    @Ignore
    private boolean selected;
}
