package com.wintermute.adventuresmaster.database.entity.tools.gm;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Embedded;
import androidx.room.Relation;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds relation between {@link AudioInScene} and {@link AudioFile} to empower returning AudioInScene including
 * AudioFile title and path.
 *
 * @author wintermute
 */
@Data
@NoArgsConstructor
public class AudioFileWithOpts implements Parcelable
{
    @Embedded
    AudioInScene audioInScene;

    @Relation(parentColumn = "audioFile", entityColumn = "id", entity = AudioFile.class)
    List<AudioFile> audioFiles;

    protected AudioFileWithOpts(Parcel in)
    {
        audioInScene = in.readParcelable(AudioInScene.class.getClassLoader());
        audioFiles = new ArrayList<>();
        in.readTypedList(audioFiles, AudioFile.CREATOR);
    }

    public static final Creator<AudioFileWithOpts> CREATOR = new Creator<AudioFileWithOpts>()
    {
        @Override
        public AudioFileWithOpts createFromParcel(Parcel in)
        {
            return new AudioFileWithOpts(in);
        }

        @Override
        public AudioFileWithOpts[] newArray(int size)
        {
            return new AudioFileWithOpts[size];
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
        dest.writeParcelable(audioInScene, flags);
        dest.writeTypedList(audioFiles);
    }
}