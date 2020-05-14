package com.wintermute.adventuresmaster.helper;

import com.wintermute.adventuresmaster.view.SectionData;

import java.util.ArrayList;
import java.util.List;

/**
 * Loads content of selected section.
 *
 * @author wintermute
 */
public class SectionLoader
{
    public List<SectionData> getSection(String parent){
       List<SectionData> result = new ArrayList<>();
       //TODO: read the form from database.
       result.add(new SectionData(0L, "button1", "root", "It´s me, a String!"));
       result.add(new SectionData(1L, "button2", "root", Void.class));
       result.add(new SectionData(2L, "button3", "root", "It´s me, an another String!"));
       return result;
    }
}
