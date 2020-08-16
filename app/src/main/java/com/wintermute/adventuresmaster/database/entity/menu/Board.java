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
@Entity
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

    @ColumnInfo(name = "hasNestedBoards")
    private boolean hasNestedBoards;

    @ColumnInfo(name = "parentId")
    private long parentId;

    public Board(@NonNull String name, @NonNull String type, long parentId)
    {
        this.name = name;
        this.type = type;
        this.hasNestedBoards = false;
        this.parentId = parentId;
    }
}
