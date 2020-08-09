package com.wintermute.adventuresmaster.view;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.wintermute.adventuresmaster.R;

public class BoardActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        System.out.println();
        Toast.makeText(this, getIntent().getStringExtra("type"), Toast.LENGTH_SHORT).show();
    }
}