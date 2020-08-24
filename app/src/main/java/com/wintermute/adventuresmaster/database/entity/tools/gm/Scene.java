package com.wintermute.adventuresmaster.database.entity.tools.gm;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.wintermute.adventuresmaster.database.entity.menu.Board;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a scene contained withing GM tools including information about configured light and audio background.
 *
 * @author wintermute
 */
@Data
@NoArgsConstructor
@Entity(indices = {@Index(value = "inBoard")},
        foreignKeys = @ForeignKey(entity = Board.class, parentColumns = "id", childColumns = "inBoard",
                                  onDelete = CASCADE))
public class Scene
{
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "inBoard")
    private long inBoard;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "light")
    private Long light;

    @ColumnInfo(name = "effect")
    private Long effect;

    @ColumnInfo(name = "music")
    private Long music;

    @ColumnInfo(name = "ambience")
    private Long ambience;

    public Scene(@NonNull String title, long inBoard)
    {
        this.title = title;
        this.inBoard = inBoard;
    }
}
