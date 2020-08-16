package com.wintermute.adventuresmaster.database.entity.tools.gm;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import lombok.Data;

/**
 * Represents table chaining audio file with parameters needed for scenes and soundboard.
 *
 * @author wintermute
 */
@Data
@Entity(tableName = "audioWithOpts", indices = @Index(value = "audioFileId"),
        foreignKeys = @ForeignKey(entity = AudioFile.class, parentColumns = "id", childColumns = "audioFileId",
                                  onDelete = ForeignKey.CASCADE))
public class AudioWithOpts
{
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "audioFileId")
    private long audioFileId;

    @ColumnInfo(name = "volume")
    private int volume;

    @ColumnInfo(name = "repeat")
    private boolean repeat;

    @ColumnInfo(name = "playAfterEffect")
    private boolean playAfterEffect;

    public AudioWithOpts(long audioFileId, int volume, boolean repeat, boolean playAfterEffect){
        this.audioFileId = audioFileId;
        this.volume = volume;
        this.repeat = repeat;
        this.playAfterEffect = playAfterEffect;
    }
}
