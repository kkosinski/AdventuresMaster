package com.wintermute.adventuresmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.helper.LayoutFactory;
import com.wintermute.adventuresmaster.model.ViewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * This is an dynamic RecyclerViewAdapter. It decides how many views per item has to be rendered by the count of args
 * contained by each row.
 *
 * @author wintermute
 */
public class DynamicAdapter extends RecyclerView.Adapter<DynamicAdapter.ViewHolder>
{
    private List<ViewItem> items;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context ctx;
    private int rowLength;

    // data is passed into the constructor
    public DynamicAdapter(Context context, List<ViewItem> data)
    {
        this.mInflater = LayoutInflater.from(context);
        this.items = data;
        this.ctx = context;
        rowLength = getsublabelsLength();
    }

    private int getsublabelsLength()
    {
        int result = 0;
        for (ViewItem item : items)
        {
            result = Math.max(result, item.getSubLabels().size());
        }
        return result;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType)
    {
        View view = mInflater.inflate(R.layout.list_item_template, parent, false);
        ViewItem viewItem = items.get(0);
        generateRowLayout(view, viewItem);
        return new ViewHolder(view, viewItem.getLabel(), rowLength);
    }

    private void generateRowLayout(View view, ViewItem viewItem)
    {
        LayoutFactory factory = LayoutFactory.getInstance();
        LinearLayout container = factory.initLayoutWithParams(ctx);
        container.addView(factory.createViewElement(ctx, viewItem.getLabel()));

        for (int i = 0; i < rowLength; i++)
        {
            container.addView(factory.createViewElement(ctx, i));
        }

        LinearLayout layout = view.findViewById(R.id.list_item_template_id);
        layout.addView(container);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        ViewItem item = items.get(position);
        holder.title.setText(item.getLabel());

        if (item.getSubLabels() != null)
        {
            for (int i = 0; i < holder.subViews.size(); i++)
            {
                if (i < item.getSubLabels().size())
                {
                    holder.subViews.get(i).setText(item.getSubLabels().get(i));
                } else
                {
                    holder.getView(i).setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView title;
        List<TextView> subViews;
        View view;

        ViewHolder(View view, String title, int elementsCount)
        {
            super(view);
            this.view = view;

            this.title = view.findViewWithTag(title);
            view.setOnClickListener(this);

            this.subViews = new ArrayList<>();
            for (int i = 0; i < elementsCount; i++)
            {
                TextView subViews = view.findViewWithTag(i);
                subViews.setOnClickListener(this);
                this.subViews.add(subViews);
            }
        }

        @Override
        public void onClick(View view)
        {
            if (mClickListener != null)
            { mClickListener.onItemClick(view, getAdapterPosition()); }
        }

        View getView(int id)
        {
            return view.findViewWithTag(id);
        }
    }

    public void setClickListener(ItemClickListener itemClickListener)
    {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener
    {
        void onItemClick(View view, int position);
    }
}
