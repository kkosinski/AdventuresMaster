package com.wintermute.adventuresmaster.database.entity.settings;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents hue bulbs, which can be connected to the bridge.
 *
 * @author wintermute
 */
@Data
@NoArgsConstructor
@Entity(tableName = "hueBulb", indices = @Index(value = "hueBridge"),
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

    public HueBulb(long id, long hueBridge, String name, String type) {
        this.id = id;
        this.hueBridge = hueBridge;
        this.name = name;
        this.type = type;
    }
}
