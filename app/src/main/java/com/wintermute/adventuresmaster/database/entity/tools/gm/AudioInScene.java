package com.wintermute.adventuresmaster.database.entity.tools.gm;

import static androidx.room.ForeignKey.CASCADE;

import android.os.Parcel;
import android.os.Parcelable;
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
public class AudioInScene implements Parcelable
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

    protected AudioInScene(Parcel in)
    {
        id = in.readLong();
        inScene = in.readLong();
        audioFile = in.readLong();
        volume = in.readInt();
        repeat = in.readByte() != 0;
        playAfterEffect = in.readByte() != 0;
        tag = in.readString();
    }

    public static final Creator<AudioInScene> CREATOR = new Creator<AudioInScene>()
    {
        @Override
        public AudioInScene createFromParcel(Parcel in)
        {
            return new AudioInScene(in);
        }

        @Override
        public AudioInScene[] newArray(int size)
        {
            return new AudioInScene[size];
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
        dest.writeLong(inScene);
        dest.writeLong(audioFile);
        dest.writeInt(volume);
        dest.writeByte((byte) (repeat ? 1 : 0));
        dest.writeByte((byte) (playAfterEffect ? 1 : 0));
        dest.writeString(tag);
    }
}
