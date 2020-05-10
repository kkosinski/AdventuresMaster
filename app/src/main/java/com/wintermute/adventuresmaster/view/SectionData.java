package com.wintermute.adventuresmaster.view;

import lombok.Data;

@Data
public class SectionData
{
    private Long id;
    private String label;
    private String parent;
    private Object content;
}
