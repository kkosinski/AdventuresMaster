package com.wintermute.adventuresmaster.view.custom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.database.entity.settings.HueBulb;

import java.util.List;

/**
 * Custom adapter containing {@link HueBulb} with information and checkbox to select it for pairing.
 *
 * @author wintermute
 */
public class BulbViewAdapter extends RecyclerView.Adapter<BulbViewAdapter.ViewHolder>
{
    private List<HueBulb> data;
    private Context context;
    private BulbItemClickListener clickListener;

    /**
     * Creates an instance.
     *
     * @param context of implementing activity.
     * @param data to display in adapter.
     */
    public BulbViewAdapter(Context context, List<HueBulb> data)
    {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.hue_bulb_list_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.bulbName.setText(data.get(position).getName());
        holder.bulbType.setText(data.get(position).getType());
        holder.checked.setChecked(data.get(position).isSelected());
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    @Override
    public long getItemId(int position)
    {
        return data.get(position).getId();
    }

    /**
     * Holds the view and provides user action interface.
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView bulbName;
        private TextView bulbType;
        private CheckBox checked;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            LinearLayout row = itemView.findViewById(R.id.hue_bulb_list_row);
            row.setOnClickListener(this);

            this.bulbName = itemView.findViewById(R.id.hue_bulb_list_bulb_name);
            this.bulbType = itemView.findViewById(R.id.hue_bulb_list_bulb_type);
            this.checked = itemView.findViewById(R.id.hue_bulb_list_is_selected);
            checked.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            if (!(v instanceof CheckBox))
            {
                checked.setChecked(!checked.isChecked());
            }
            if (clickListener != null)
            {
                clickListener.onBulbClick(checked.isChecked(), getAdapterPosition());
            }
        }
    }

    /**
     * Sets the click listener.
     *
     * @param clickListener implementing class.
     */
    public void setClickListener(BulbItemClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    /**
     * Provides functionality requirements of implementing this adapter.
     */
    public interface BulbItemClickListener
    {
        void onBulbClick(boolean state, int position);
    }
}
