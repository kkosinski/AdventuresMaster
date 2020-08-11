package com.wintermute.adventuresmaster.model;

import lombok.AllArgsConstructor;
import lombok.Data;

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
    private Long id;
}
