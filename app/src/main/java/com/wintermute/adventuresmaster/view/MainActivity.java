package com.wintermute.adventuresmaster;

import android.content.Intent;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.core.view.ViewCompat;
import com.wintermute.adventuresmaster.helper.LayoutFactory;
import com.wintermute.adventuresmaster.helper.SectionLoader;
import com.wintermute.adventuresmaster.view.ListActivity;
import com.wintermute.adventuresmaster.view.SectionData;

import java.util.List;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init()
    {
        LayoutFactory layoutFactory = LayoutFactory.getInstance();
        LinearLayout layout = layoutFactory.getDefaultLayout(getWindow().getDecorView());

        SectionLoader sectionLoader = new SectionLoader();
        List<SectionData> root = sectionLoader.getSection("root");
        for (SectionData element : root)
        {
            layoutFactory.addViewToDefaultLayout(layout, initMenuButton(element));
        }
    }

    private Button initMenuButton(SectionData data)
    {
        Button result = new Button(this);
        result.setText(data.getLabel());
        result.setId(ViewCompat.generateViewId());
        result.setTag(data.getLabel());
        result.setOnClickListener((v) ->
        {
            if (data.getContent() instanceof String)
            {
                System.out.println(data.getLabel());
            } else if (data.getContent() instanceof Class)
            {
                //TODO: missing logic
                startActivity(new Intent(MainActivity.this, ListActivity.class));
                System.out.println("New activity should be started");
            }
        });
        return result;
    }
}
