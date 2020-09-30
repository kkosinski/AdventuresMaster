package com.wintermute.adventuresmaster.database.entity.settings;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents hue bridge with api url and credentials.
 *
 * @author wintermute
 */
@Data
@NoArgsConstructor
@Entity(tableName = "hueBridge", indices = {@Index(value = "deviceId", unique = true)})
public class HueBridge implements Parcelable
{
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "user")
    private String user;

    @NonNull
    @ColumnInfo(name = "deviceId")
    private String deviceId;

    @ColumnInfo(name = "deviceName")
    private String deviceName;

    @Ignore
    private String url;

    /**
     * Creates an instance.
     *
     * @param url of philips hue bridge.
     * @param deviceId unique hardware id.
     */
    public HueBridge(String url, String deviceId, String deviceName)
    {
        this.url = url;
        this.deviceName = deviceName;
        this.deviceId = deviceId;
    }

    protected HueBridge(Parcel in)
    {
        id = in.readLong();
        user = in.readString();
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
        dest.writeString(deviceName);
    }
}
