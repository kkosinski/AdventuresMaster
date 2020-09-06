package com.wintermute.adventuresmaster.database.entity.settings;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import lombok.Data;

/**
 * Represents hue bridge with api url and credentials.
 *
 * @author wintermute
 */
@Data
@Entity(tableName = "hueBridge", indices = {@Index(value = "url", unique = true)})
public class HueBridge implements Parcelable
{
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "user")
    private String user;

    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "defaultDevice")
    private boolean defaultDevice;

    @ColumnInfo(name = "deviceName")
    private String deviceName;

    /**
     * Creates instance with url.
     *
     * @param url to api of bridge.
     */
    public HueBridge(String url)
    {
        this.url = url;
    }

    protected HueBridge(Parcel in)
    {
        id = in.readLong();
        user = in.readString();
        url = in.readString();
        defaultDevice = in.readByte() != 0;
        deviceName = in.readString();
    }

    public static final Creator<HueBridge> CREATOR = new Creator<HueBridge>()
    {
        @Override
        public HueBridge createFromParcel(Parcel in)
        {
            return new HueBridge(in);
        }

        @Override
        public HueBridge[] newArray(int size)
        {
            return new HueBridge[size];
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
        dest.writeString(user);
        dest.writeString(url);
        dest.writeByte((byte) (defaultDevice ? 1 : 0));
        dest.writeString(deviceName);
    }
}
