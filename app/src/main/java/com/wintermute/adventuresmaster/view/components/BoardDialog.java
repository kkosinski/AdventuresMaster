package com.wintermute.adventuresmaster.view.components;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.view.BoardActivity;

public class BoardDialog extends DialogFragment
{

    public interface OnSelectedItem
    {
        void selectedItem(int selectionIndex);
    }

    public interface OnCreateBoardConfirm
    {
        void createBoard(String name);
    }

    private OnSelectedItem onSelectedItem;
    private OnCreateBoardConfirm onCreateBoardConfirm;
    private Context context;
    private boolean createBoardOnly = false;
    private View view;

    public BoardDialog(Context context, boolean createBoardOnly, View view)
    {
        this.context = context;
        this.createBoardOnly = createBoardOnly;
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
            onSelectedItem = (BoardActivity) this.context;
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


        if (createBoardOnly)
        {
            builder.setView(view).setTitle("Create board: ").setPositiveButton(R.string.board_dialog_confirm, (dialog, which) ->
            {
                EditText insertBoardName = view.findViewById(R.id.board_dialog_board_name);
                setOnCreateBoardConfirm((OnCreateBoardConfirm) context);
                onCreateBoardConfirm.createBoard(insertBoardName.getText().toString());
            }).setNegativeButton(R.string.board_dialog_cancel, ((dialog, which) -> dialog.dismiss()));
        } else
        {
            builder.setTitle("Create: ").setItems(R.array.boardArray, (dialog, which) ->
            {
                setOnSelectedItem((BoardActivity) context);
                onSelectedItem.selectedItem(which);
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
