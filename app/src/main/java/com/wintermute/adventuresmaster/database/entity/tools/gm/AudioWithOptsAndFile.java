package com.wintermute.adventuresmaster.database.entity.tools.gm;

import androidx.room.Embedded;
import androidx.room.Relation;
import lombok.Data;

import java.util.List;

/**
 * Builds relation between {@link AudioInScene} and {@link AudioFile} to empower returning AudioInScene including
 * AudioFile title and path.
 *
 * @author wintermute
 */
@Data
public class AudioWithOptsAndFile
{
    @Embedded
    AudioInScene audioInScene;

    @Relation(parentColumn = "audioFile", entityColumn = "id", entity = AudioFile.class)
    List<AudioFile> audioFiles;
}