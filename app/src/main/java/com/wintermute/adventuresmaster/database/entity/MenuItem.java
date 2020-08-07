package com.wintermute.adventuresmaster.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents menu item as database entity.
 */
@Data
@NoArgsConstructor
@Entity(tableName = "menu")
public class MenuItem
{
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "isActivity")
    private boolean isActivity;

    @ColumnInfo(name = "parentId")
    private long parentId;
}
