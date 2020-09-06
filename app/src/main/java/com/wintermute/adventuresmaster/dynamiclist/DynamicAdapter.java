package com.wintermute.adventuresmaster.dynamiclist;

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

import java.util.ArrayList;
import java.util.List;

/**
 * This is an dynamic RecyclerViewAdapter. An dynamic view is created depending on list item and how many text view
 * (additional information) it should have. It may have fixed size.
 *
 * @author wintermute
 */
public class DynamicAdapter extends RecyclerView.Adapter<DynamicAdapter.ViewHolder>
{
    private List<DynamicListItem> items;
    private ItemClickListener mClickListener;
    private Context ctx;
    private int additionalInfoLength;

    public DynamicAdapter(Context context, List<DynamicListItem> data)
    {
        this.items = data;
        this.ctx = context;
        additionalInfoLength = data.stream().mapToInt(i -> i.getAdditionalInfo().size()).max().orElse(0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType)
    {
        LayoutInflater mInflater = LayoutInflater.from(ctx);
        View view = mInflater.inflate(R.layout.dynamic_list_item_template, parent, false);
        DynamicListItem viewItem = items.get(0);
        generateSingleItemLayout(view, viewItem);
        return new ViewHolder(view, viewItem.getTitle(), additionalInfoLength);
    }

    private void generateSingleItemLayout(View view, DynamicListItem viewItem)
    {
        LayoutFactory factory = LayoutFactory.getInstance();
        LinearLayout container = factory.initLayoutWithParams(ctx);

        //define how the single item looks like
        container.addView(factory.createViewElement(ctx, viewItem.getTitle()));
        for (int i = 0; i < additionalInfoLength; i++)
        {
            container.addView(factory.createViewElement(ctx, i));
        }
        LinearLayout layout = view.findViewById(R.id.dynamic_list_item_template_id);
        layout.addView(container);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        DynamicListItem item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.itemId = item.getId();

        for (int i = 0; i < holder.additionalInfo.size(); i++)
        {
            if (i < item.getAdditionalInfo().size())
            {
                holder.additionalInfo.get(i).setText(item.getAdditionalInfo().get(i));
            } else
            {
                holder.getView(i).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    /**
     * Represents the displayed item in the list.
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView title;
        private List<TextView> additionalInfo;
        private View displayedItem;
        private long itemId;

        ViewHolder(View view, String title, int elementsCount)
        {
            super(view);
            this.displayedItem = view;

            this.title = view.findViewWithTag(title);
            view.setOnClickListener(this);

            this.additionalInfo = new ArrayList<>();
            for (int i = 0; i < elementsCount; i++)
            {
                TextView subViews = view.findViewWithTag(i);
                subViews.setOnClickListener(this);
                this.additionalInfo.add(subViews);
            }
        }

        @Override
        public void onClick(View view)
        {
            if (mClickListener != null)
            { mClickListener.onDynamicListItemClick(view, getAdapterPosition(), itemId); }
        }

        View getView(int id)
        {
            return displayedItem.findViewWithTag(id);
        }
    }

    public void setClickListener(ItemClickListener itemClickListener)
    {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener
    {
        void onDynamicListItemClick(View view, int position, long itemId);
    }
}
