package com.wintermute.adventuresmaster.database.entity.tools.gm;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
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
@Entity(tableName = "audioFile", indices = @Index(value = "uri", unique = true))
public class AudioFile implements Parcelable
{
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @NonNull
    @ColumnInfo(name = "uri")
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

    protected AudioFile(Parcel in)
    {
        id = in.readLong();
        title = in.readString();
        uri = in.readString();
    }

    public static final Creator<AudioFile> CREATOR = new Creator<AudioFile>()
    {
        @Override
        public AudioFile createFromParcel(Parcel in)
        {
            return new AudioFile(in);
        }

        @Override
        public AudioFile[] newArray(int size)
        {
            return new AudioFile[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(uri);
    }
}
