package com.wintermute.adventuresmaster.database.entity.menu;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a board containing scenes, boards or nested boards of one of these types.
 *
 * @author wintermute
 */
@Data
@NoArgsConstructor
@Entity(tableName = "board")
public class Board
{
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "isContentTable")
    private boolean isContentTable;

    @ColumnInfo(name = "parentId")
    private long parentId;

    public Board(@NonNull String name, @NonNull String type, boolean isContentTable, long parentId)
    {
        this.name = name;
        this.type = type;
        this.isContentTable = isContentTable;
        this.parentId = parentId;
    }
}
