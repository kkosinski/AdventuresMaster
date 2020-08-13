package com.wintermute.adventuresmaster.database.entity.tools.gm;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Scene
{
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "effectId")
    private long effectId;

    @ColumnInfo(name = "musicId")
    private long musicId;

    @ColumnInfo(name = "ambienceId")
    private long ambienceId;
}
