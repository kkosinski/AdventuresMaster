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

/**
 * Represents light information to send to philips hue.
 *
 * @author wintermute
 */
@Data
@Entity(tableName = "light", indices = @Index("inScene"), foreignKeys = {
    @ForeignKey(entity = Scene.class, parentColumns = "id", childColumns = "inScene", onDelete = CASCADE)})
public class Light implements Parcelable
{
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "color")
    private int color;

    @ColumnInfo(name = "brightness")
    private int brightness;

    @ColumnInfo(name = "inScene")
    private long inScene;

    public Light(int color, int brightness)
    {
        this.color = color;
        this.brightness = brightness;
    }

    protected Light(Parcel in)
    {
        id = in.readLong();
        color = in.readInt();
        brightness = in.readInt();
    }

    public static final Creator<Light> CREATOR = new Creator<Light>()
    {
        @Override
        public Light createFromParcel(Parcel in)
        {
            return new Light(in);
        }

        @Override
        public Light[] newArray(int size)
        {
            return new Light[size];
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
        dest.writeInt(color);
        dest.writeInt(brightness);
    }
}
