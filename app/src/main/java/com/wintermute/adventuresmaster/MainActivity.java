package com.wintermute.adventuresmaster;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.wintermute.adventuresmaster.helper.SectionLoader;
import com.wintermute.adventuresmaster.view.SectionData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity
{
    private SectionLoader sectionLoader;

    private static final Map<String, String> ROUTE = new HashMap<String, String>()
    {{
        put("Host/Join game", "1");
        put("Scene Manager", "2");
        put("Settings", "3");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init()
    {
        sectionLoader = new SectionLoader();
        LinearLayout layout = findViewById(R.id.main_menu_panel);
        LinearLayout.LayoutParams layoutParams =
            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //TODO: eventually create Enum.
        List<SectionData> root = sectionLoader.getSection("root");
        for (SectionData element : root)
        {
            layout.addView(initMenuButton(element), layoutParams);
        }
    }

    private Button initMenuButton(SectionData data)
    {
        Button result = new Button(getApplicationContext());
        result.setText(data.getLabel());
        result.setId(new Random().nextInt());
        //TODO: differentiate between SectionData and new Activity
        result.setOnClickListener((v) -> sectionLoader.getSection(data.getLabel()));
        return result;
    }
}
