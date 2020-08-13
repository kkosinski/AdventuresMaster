package com.wintermute.adventuresmaster.database.entity.menu;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents activity extras as object.
 *
 * @author wintermute
 */
@Data
@NoArgsConstructor
@Entity(tableName = "activityExtras", indices = @Index(value = "activityId"),
        foreignKeys = @ForeignKey(entity = ActivityDesc.class, parentColumns = "activityId",
                                  childColumns = "activityId", onDelete = ForeignKey.CASCADE))
public class ActivityExtras
{
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "activityId")
    private long activityId;

    @NonNull
    @ColumnInfo(name = "key")
    private String key;

    @NonNull
    @ColumnInfo(name = "value")
    private String value;
}

