package com.wintermute.adventuresmaster.view.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.view.BoardPanel;

public class BoardDialog extends DialogFragment
{

    public interface OnSelectedItem
    {
        void onDialogItemSelected(int selectionIndex);
    }

    public interface OnCreateBoardConfirm
    {
        void onCreateBoardConfirmed(String name, boolean isContentTable);
    }

    private OnSelectedItem onSelectedItem;
    private OnCreateBoardConfirm onCreateBoardConfirm;
    private Context context;
    private boolean promptBoardName = false;
    private boolean convertToContentTable;
    private View view;

    public BoardDialog(Context context, boolean createBoardOnly, boolean convertToContentTable, View view)
    {
        this.context = context;
        this.promptBoardName = createBoardOnly;
        this.convertToContentTable = convertToContentTable;
        this.view = view;
    }

    public BoardDialog(Context context)
    {
        this.context = context;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        try
        {
            onSelectedItem = (BoardPanel) this.context;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(this.context.toString() + " must implement OnSelectedItemListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (promptBoardName)
        {
            builder
                .setView(view)
                .setTitle("Create board: ")
                .setPositiveButton(R.string.board_dialog_confirm, (dialog, which) ->
                {
                    EditText insertBoardName = view.findViewById(R.id.board_dialog_board_name);
                    setOnCreateBoardConfirm((OnCreateBoardConfirm) context);
                    onCreateBoardConfirm.onCreateBoardConfirmed(insertBoardName.getText().toString(), convertToContentTable);
                })
                .setNegativeButton(R.string.board_dialog_cancel, ((dialog, which) -> dialog.dismiss()));
        } else
        {
            builder.setTitle("Create: ").setItems(R.array.boardArray, (dialog, which) ->
            {
                setOnSelectedItem((BoardPanel) context);
                onSelectedItem.onDialogItemSelected(which);
            }).setNegativeButton("cancel", (dialog, id) -> dialog.dismiss());
        }
        return builder.create();
    }

    private void setOnSelectedItem(OnSelectedItem onSelectedItem)
    {
        this.onSelectedItem = onSelectedItem;
    }

    private void setOnCreateBoardConfirm(OnCreateBoardConfirm onCreateBoardConfirm)
    {
        this.onCreateBoardConfirm = onCreateBoardConfirm;
    }
}
