package com.unizar.wineapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;

/**
 * Clase InfoActivity.
 * Esta clase define la actividad Info que hereda de BaseActivity.
 *
 * En ella se muestra información sobre la aplicación y sus autores.
 *
 * @author: Alejandro y Alberto
 */
public class InfoActivity extends BaseActivity implements Serializable {
    @Override

    /**
     * Se lanza el layout activity_info dentro del content_frame definido en BaseActivity
     * y se modifica el título del ActionBar.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_info, contentFrameLayout);
        getSupportActionBar().setTitle(R.string.info);


    }
}
