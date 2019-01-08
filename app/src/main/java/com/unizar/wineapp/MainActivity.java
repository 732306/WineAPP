package com.unizar.wineapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import android.widget.ArrayAdapter;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private MobileServiceClient conexionServerAPI;
    private Spinner spinner;
    private List<String> listVariety;
    private ArrayAdapter<Variedad> comboAdapter;
    private List<Variedad> lista;

    private SeekBar seekBar_precio,seekBar_calidad,seekBar_region,seekBar_puntuacion;
    private TextView tv_peso_precio, tv_peso_calidad, tv_peso_region, tv_peso_puntuacion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.spinner);

        try {
            conexionServerAPI = new MobileServiceClient("https://winedss2.azurewebsites.net", this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        this.obtenerVariedades();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Button btn_obtener = (Button) findViewById(R.id.btn_obtener);

        btn_obtener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerRecomendacion((Variedad)spinner.getSelectedItem());

            }
        });

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(700);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

        seekBar_calidad = (SeekBar) findViewById(R.id.seekBar_calidad);
        seekBar_precio = (SeekBar) findViewById(R.id.seekBar_precio);
        seekBar_region = (SeekBar) findViewById(R.id.seekBar_region);
        seekBar_puntuacion = (SeekBar) findViewById(R.id.seekBar_puntuacion);

        tv_peso_precio = (TextView) findViewById(R.id.tv_peso_precio);
        tv_peso_calidad = (TextView) findViewById(R.id.tv_peso_calidad);
        tv_peso_region = (TextView) findViewById(R.id.tv_peso_region);
        tv_peso_puntuacion = (TextView) findViewById(R.id.tv_peso_puntuacion);


        seekBar_precio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress=0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
                tv_peso_precio.setText(progress + "/" + seekBar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv_peso_precio.setText(progress + "/" + seekBar.getMax());
            }
        });

        seekBar_calidad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress=0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
                tv_peso_calidad.setText(progress + "/" + seekBar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv_peso_calidad.setText(progress + "/" + seekBar.getMax());
            }
        });

        seekBar_region.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress=0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
                tv_peso_region.setText(progress + "/" + seekBar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv_peso_region.setText(progress + "/" + seekBar.getMax());
            }
        });

        seekBar_puntuacion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress=0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
                tv_peso_puntuacion.setText(progress + "/" + seekBar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv_peso_puntuacion.setText(progress + "/" + seekBar.getMax());
            }
        });

        this.getSupportActionBar().setTitle(R.string.recomendador);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private Variedad[] obtenerVariedades() {
        ListenableFuture<Variedad[]> allVariety = conexionServerAPI.invokeApi("ObtenerVariedades", "", Variedad[].class);

        Futures.addCallback(allVariety, new FutureCallback<Variedad[]>() {
           @Override
            public void onSuccess(Variedad[] result) {


               lista = new ArrayList<Variedad>();
               for (int i=0; i < result.length; i++){
                   lista.add(result[i]);
               }

               comboAdapter = new ArrayAdapter<Variedad>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, lista);
               comboAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
               spinner.setAdapter(comboAdapter);
           }

            @Override
            public void onFailure(Throwable t) {
            }
        });

        return null;
    }

    private Vino obtenerRecomendacion(Variedad variedadSeleccionada){

        System.out.println("Nombre de variedad " + variedadSeleccionada.getNombre());
        final ListenableFuture<Vino[]> listaVinos = conexionServerAPI.invokeApi("ObtenerVinos",variedadSeleccionada.getNombre(), Vino[].class);

        Futures.addCallback(listaVinos,new FutureCallback<Vino[]>() {
            @Override
            public void onFailure(Throwable exc) {
                // Acciones a realizar si la llamada devuelve un error
            }
            @Override
            public void onSuccess(Vino[] vinos) {
                //tv_test.setMovementMethod(new ScrollingMovementMethod());
                String todo="";

                for (int i=0; i < vinos.length; i++){
                    todo+=vinos[i].toString() + "\n";
                }
            }
        });
        return null;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_recomendador) {
            this.getSupportActionBar().setTitle(R.string.recomendador);
        } else if (id == R.id.nav_info) {
            this.getSupportActionBar().setTitle(R.string.info);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
