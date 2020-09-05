package com.wintermute.adventuresmaster.database.entity.settings;

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
public class HueBridge
{
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "user")
    private String user;

    @ColumnInfo(name = "url")
    private String url;

    /**
     * Creates instance with url.
     *
     * @param url to api of bridge.
     */
    public HueBridge(String url){
        this.url = url;
    }
}
