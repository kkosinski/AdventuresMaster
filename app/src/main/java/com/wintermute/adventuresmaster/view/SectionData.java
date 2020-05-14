package com.wintermute.adventuresmaster.view;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SectionData
{
    private Long id;
    private String label;
    private String parent;
    private Object content;
}
