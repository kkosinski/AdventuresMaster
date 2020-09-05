package com.wintermute.adventuresmaster.view.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.wintermute.adventuresmaster.R;

/**
 * Informs user about result of discovering hue bridge and if any found, prints instruction to establish connection.
 *
 * @author wintermute
 */
public class ConnectHueBridgeDialog extends DialogFragment
{
    /**
     * Handles connection establishment request.
     */
    public interface OnConnectClickedListener
    {
        void onConnectClicked();
    }

    private OnConnectClickedListener onConnectClickedListener;
    private Context context;
    private boolean hueFound;

    /**
     * Creates an instance.
     *
     * @param context of calling activity.
     * @param hueFound flag if hue bridge was found or not.
     */
    public ConnectHueBridgeDialog(Context context, boolean hueFound)
    {
        this.context = context;
        this.hueFound = hueFound;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        try
        {
            onConnectClickedListener = (OnConnectClickedListener) this.context;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(this.context.toString() + " must implement OnSelectedItemListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String title = hueFound ? "Hue bridge found" : "Hue bridge not found";

        builder.setTitle(title);
        if (hueFound)
        {
            builder.setMessage(R.string.hue_discover_dialog_successful_message);
            builder.setPositiveButton(R.string.hue_discover_dialog_successful, (dialog, which) ->
            {
                setOnConnectListener((OnConnectClickedListener) context);
                if (onConnectClickedListener != null) {
                    onConnectClickedListener.onConnectClicked();
                }
            });
        } else
        {
            builder.setMessage(R.string.hue_discover_dialog_failed_message);
            builder.setNegativeButton(R.string.hue_discover_dialog_failed, (dialog, which) -> dialog.dismiss());
        }
        return builder.create();
    }

    private void setOnConnectListener(OnConnectClickedListener onConnectClickedListener) {
        this.onConnectClickedListener = onConnectClickedListener;
    }
}
