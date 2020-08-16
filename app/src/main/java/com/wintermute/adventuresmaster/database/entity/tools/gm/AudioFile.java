package com.wintermute.adventuresmaster.database.entity.tools.gm;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import lombok.Data;

/**
 * Represents path to audio file stored on device.
 *
 * @author wintermute
 */
@Data
@Entity(tableName = "audioFile", indices = @Index(value = "path", unique = true))
public class AudioFile
{
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "path")
    private String path;

    public AudioFile(@NonNull String path)
    {
        this.path = path;
    }
}
