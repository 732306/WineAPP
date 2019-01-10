package com.unizar.wineapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;

public class InfoActivity extends BaseActivity implements Serializable {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_info, contentFrameLayout);
        getSupportActionBar().setTitle(R.string.info);


    }
}
