package com.wintermute.adventuresmaster.view;

import lombok.Data;

import java.util.List;

@Data
public class ViewItem
{
    private String label;
    private List<String> subLabels;
    private Long id;
}
