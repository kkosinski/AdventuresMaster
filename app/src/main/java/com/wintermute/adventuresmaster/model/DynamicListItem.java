package com.wintermute.adventuresmaster.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Acts as information holder for items to display in dynamic list.
 *
 * @author wintermute
 */
@Data
@AllArgsConstructor
public class DynamicListItem
{
    private String title;
    private List<String> additionalInfo;
    private long id;

    /**
     * This constructor generates empty additional info list if no additional info is provided.
     *
     * @param title of target element
     * @param id equals to the id from the database of target item
     */
    public DynamicListItem(String title, long id) {
        this.title = title;
        this.additionalInfo = new ArrayList<>();
        this.id = id;
    }
}
