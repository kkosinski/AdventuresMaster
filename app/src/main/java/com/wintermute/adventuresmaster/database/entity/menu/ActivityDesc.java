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
 * Represents activity description as object.
 *
 * @author wintermute
 */
@Data
@NoArgsConstructor
@Entity(tableName = "activityDesc", indices = @Index(value = "activityId", unique = true),
        foreignKeys = @ForeignKey(entity = MenuItem.class, parentColumns = "id", childColumns = "activityId"))
public class ActivityDesc
{
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "activityId")
    private long activityId;

    @NonNull
    @ColumnInfo(name = "className")
    private String className;

    @ColumnInfo(name = "hasExtras")
    private boolean hasExtras;
}
