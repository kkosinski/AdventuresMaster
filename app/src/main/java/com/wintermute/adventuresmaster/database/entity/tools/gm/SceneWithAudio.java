package com.wintermute.adventuresmaster.database.entity.tools.gm;

import androidx.room.Embedded;
import androidx.room.Relation;
import lombok.Data;

import java.util.List;

/**
 * Builds relation between {@link Scene} and {@link AudioFileWithOpts} to empower returning Scene, its AudioInScene and
 * AudioFile title and path.
 *
 * @author wintermute
 */
@Data
public class SceneWithAudio
{
    @Embedded
    Scene scene;

    @Relation(parentColumn = "id", entityColumn = "inScene", entity = AudioInScene.class)
    List<AudioFileWithOpts> audioInScene;
}
