package com.wintermute.adventuresmaster.database.entity.tools.gm;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import lombok.Data;

@Data
@Entity(tableName = "audioInBoard", indices = {@Index("audioFile"), @Index("inScene")}, foreignKeys = {
    @ForeignKey(entity = Scene.class, parentColumns = "id", childColumns = "inScene", onDelete = CASCADE),
    @ForeignKey(entity = AudioFile.class, parentColumns = "id", childColumns = "audioFile", onDelete = CASCADE)})
public class AudioInScene
{
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "inScene")
    private long inScene;

    @ColumnInfo(name = "audioFile")
    private long audioFile;

    @ColumnInfo(name = "volume")
    private int volume;

    @ColumnInfo(name = "repeat")
    private boolean repeat;

    @ColumnInfo(name = "playAfterEffect")
    private boolean playAfterEffect;

    @ColumnInfo(name = "tag")
    private String tag;

    public AudioInScene(int volume, boolean repeat, boolean playAfterEffect, String tag)
    {
        this.volume = volume;
        this.repeat = repeat;
        this.playAfterEffect = playAfterEffect;
        this.tag = tag;
    }
}
