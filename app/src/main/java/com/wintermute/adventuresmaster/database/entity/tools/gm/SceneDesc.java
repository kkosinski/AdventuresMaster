package com.wintermute.adventuresmaster.database.entity.tools.gm;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Embedded;
import androidx.room.Relation;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SceneDesc implements Parcelable
{
    @Embedded
    Scene scene;

    @Relation(parentColumn = "id", entityColumn = "inScene", entity = AudioInScene.class)
    List<AudioFileWithOpts> audioInScene;

    @Relation(parentColumn = "id", entityColumn = "inScene", entity = Light.class)
    Light light;

    protected SceneDesc(Parcel in)
    {
        audioInScene = in.createTypedArrayList(AudioFileWithOpts.CREATOR);
        light = in.readParcelable(Light.class.getClassLoader());
    }

    public static final Creator<SceneDesc> CREATOR = new Creator<SceneDesc>()
    {
        @Override
        public SceneDesc createFromParcel(Parcel in)
        {
            return new SceneDesc(in);
        }

        @Override
        public SceneDesc[] newArray(int size)
        {
            return new SceneDesc[size];
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
        dest.writeTypedList(audioInScene);
        dest.writeParcelable(light, flags);
    }
}