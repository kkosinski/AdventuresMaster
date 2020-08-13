package com.wintermute.adventuresmaster.view;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.adapter.DynamicAdapter;

public class BoardActivity extends AppCompatActivity implements DynamicAdapter.ItemClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_activity);

        //TODO: add logic
    }



    @Override
    public void onItemClick(View view, int position)
    {

    }
}