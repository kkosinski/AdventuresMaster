package com.wintermute.adventuresmaster.database.entity.tools.gm;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import lombok.Data;

import java.io.File;
import java.util.Objects;

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
    @ColumnInfo(name = "title")
    private String title;

    @NonNull
    @ColumnInfo(name = "path")
    private String uri;

    /**
     * Describes what is needed to create file.
     *
     * @param uri of file to store in database.
     */
    public AudioFile(@NonNull String uri)
    {
        this.uri = uri;
        this.title = new File(Objects.requireNonNull(Uri.parse(uri).getPath())).getName();
    }
}
