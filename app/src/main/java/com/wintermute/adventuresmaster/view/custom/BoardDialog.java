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
import com.wintermute.adventuresmaster.view.tools.gm.BoardContentView;
import com.wintermute.adventuresmaster.view.tools.gm.BoardPanel;

/**
 * Custom alert dialog to create folders and {@link BoardContentView} in {@link BoardPanel}.
 *
 * @author wintermute
 */
public class BoardDialog extends DialogFragment
{

    /**
     * This interface listens on clicks when selection item is clicked.
     */
    public interface OnSelectedItem
    {
        void onDialogItemSelected(int selectionIndex);
    }

    /**
     * This interface listens on confirmation of dialog for creating folders or {@link BoardContentView}
     */
    public interface OnCreateBoardConfirm
    {
        void onCreateBoardConfirmed(String name, boolean isContentTable);
    }

    private OnSelectedItem onSelectedItem;
    private OnCreateBoardConfirm onCreateBoardConfirm;
    private Context context;
    private boolean promptForBoardName = false;
    private boolean createContentTable;
    private View view;

    /**
     * Creates an instance.
     *
     * @param context of calling activity.
     * @param promptForBoardName flag to decide if the sub dialog should ask for board name or selection what to
     *     create.
     * @param createContentTable flag to notify if board will be created or an {@link BoardContentView}
     * @param view layout of this dialog.
     */
    public BoardDialog(Context context, boolean promptForBoardName, boolean createContentTable, View view)
    {
        this.context = context;
        this.promptForBoardName = promptForBoardName;
        this.createContentTable = createContentTable;
        this.view = view;
    }

    /**
     * Constructor which is taking the activity context only.
     *
     * @param context of calling application.
     */
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

        if (promptForBoardName)
        {
            builder
                .setView(view)
                .setTitle("Create board: ")
                .setPositiveButton(R.string.board_dialog_confirm, (dialog, which) ->
                {
                    EditText insertBoardName = view.findViewById(R.id.board_dialog_board_name);
                    setOnCreateBoardConfirm((OnCreateBoardConfirm) context);
                    onCreateBoardConfirm.onCreateBoardConfirmed(insertBoardName.getText().toString(),
                        createContentTable);
                })
                .setNegativeButton(R.string.board_dialog_cancel, ((dialog, which) -> dialog.dismiss()));
        } else
        {
            builder.setTitle("Create new: ").setItems(R.array.boardArray, (dialog, which) ->
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
