package com.wintermute.adventuresmaster.helper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wintermute.adventuresmaster.R;

/**
 * Manages creating and editing layouts. Singleton.
 */
public class LayoutFactory
{

    private LayoutFactory(){};


    private static class InstanceCreator {
        private static final LayoutFactory LAYOUT_FACTORY = new LayoutFactory();
    }

    /**
     * @return instance LayoutFactory instance.
     */
    public static LayoutFactory getInstance() {
        return InstanceCreator.LAYOUT_FACTORY;
    }


    /**
     * Init default linear layout.
     * @param view of current activity.
     * @return default layout
     */
    public LinearLayout getDefaultLayout(View view)
    {
        return view.findViewById(R.id.default_layout);
    }

    /**
     * Adds children to default layout.
     * @param layout target.
     * @param child element to add.
     */
    public void addViewToDefaultLayout(LinearLayout layout, View child)
    {
        layout.addView(child, getDefaultLayoutParams());
    }

    /**
     * Initializes new layout (container) within another layout and tags it.
     *
     * @param ctx of activity containing this layout.
     * @return parameterized linear layout.
     */
    public LinearLayout initLayoutWithParams(Context ctx){
        LinearLayout container = new LinearLayout(ctx);
        container.setLayoutParams(getDefaultLayoutParams());
        container.setOrientation(LinearLayout.VERTICAL);
        return container;
    }

    /**
     * Creates view view for container within layout.
     *
     *  @param ctx of activity containing this view.
     * @param id to set the tag on created view.
     * @return created, tagged view.
     */
    //TODO: think about generic
    public TextView createViewElement(Context ctx, Object id)
    {
        TextView result = new TextView(ctx);
        result.setTag(id);
        return result;
    }

    private LinearLayout.LayoutParams getDefaultLayoutParams()
    {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    }

}
