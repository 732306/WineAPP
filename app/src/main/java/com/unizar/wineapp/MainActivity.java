package com.unizar.wineapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
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
import java.util.Locale;

public class MainActivity extends BaseActivity {

    private MobileServiceClient conexionServerAPI;
    private Spinner spinner;
    private List<String> listVariety;
    private ArrayAdapter<Variedad> comboAdapter;
    private List<Variedad> lista;
    private SeekBar seekBar_precio, seekBar_calidad, seekBar_region, seekBar_puntuacion;
    private TextView tv_peso_precio, tv_peso_calidad, tv_peso_region, tv_peso_puntuacion;

    private final static String TODAS = "Todas";
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.content_main, contentFrameLayout);
        getSupportActionBar().setTitle(R.string.recomendador);

        bar = findViewById(R.id.progressBar);

        spinner = (Spinner) findViewById(R.id.spinner);

        try {
            conexionServerAPI = new MobileServiceClient("https://winedss2.azurewebsites.net", this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        bar.setVisibility(View.VISIBLE);
        this.obtenerVariedades();
        bar.setVisibility(View.INVISIBLE);

        findViewById(R.id.btn_obtener).setEnabled(true);
        findViewById(R.id.btn_obtener).setClickable(true);


        Button btn_obtener = (Button) findViewById(R.id.btn_obtener);

        btn_obtener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.btn_obtener).setEnabled(false);
                obtenerRecomendacion((Variedad) spinner.getSelectedItem());
            }
        });

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(700);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
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
            int progress = 0;

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
            int progress = 0;

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
            int progress = 0;

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
            int progress = 0;

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



    }

    private Variedad[] obtenerVariedades() {
        ListenableFuture<Variedad[]> allVariety = conexionServerAPI.invokeApi("ObtenerVariedades", "", Variedad[].class);

        Futures.addCallback(allVariety, new FutureCallback<Variedad[]>() {
            @Override
            public void onSuccess(Variedad[] result) {


                lista = new ArrayList<Variedad>();
                lista.add(new Variedad(TODAS,2008));

                for (int i = 0; i < result.length; i++) {
                    lista.add(result[i]);
                }


                comboAdapter = new ArrayAdapter<Variedad>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, lista);
                comboAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(comboAdapter);
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });

        return null;
    }

    private Vino obtenerRecomendacion(Variedad variedadSeleccionada) {

        bar.setVisibility(View.VISIBLE);
        final ListenableFuture<Vino[]> listaVinos = conexionServerAPI.invokeApi("ObtenerVinos", variedadSeleccionada.getNombre(), Vino[].class);

        Futures.addCallback(listaVinos, new FutureCallback<Vino[]>() {
            @Override
            public void onFailure(Throwable exc) {
                // Acciones a realizar si la llamada devuelve un error
            }

            @Override
            public void onSuccess(Vino[] vinos) {


                int pesoPrecio, pesoCalidad, pesoRegion, pesoPuntuacion;

                pesoPrecio = seekBar_precio.getProgress();
                pesoCalidad = seekBar_calidad.getProgress();
                pesoRegion = seekBar_region.getProgress();
                pesoPuntuacion = seekBar_puntuacion.getProgress();

                Vino mejorVino = vinos[0];
                float mejorValoracion=funcionValorVinos(vinos[0],vinos,pesoPrecio, pesoCalidad, pesoRegion, pesoPuntuacion);

                for (int i=1;i < vinos.length;i++) {
                    float valoracion = funcionValorVinos(vinos[i],vinos,pesoPrecio, pesoCalidad, pesoRegion, pesoPuntuacion);

                    if(valoracion > mejorValoracion){
                        mejorValoracion = valoracion;
                        mejorVino = vinos[i];
                    }
                }

                bar.setVisibility(View.INVISIBLE);

                Intent anIntent = new Intent(getApplicationContext(), RecomendacionActivity.class);
                anIntent.putExtra("Vino",mejorVino);
                anIntent.putExtra("Valoracion",mejorValoracion);

                startActivity(anIntent);
                findViewById(R.id.btn_obtener).setEnabled(true);
                drawerLayout.closeDrawers();

                //System.err.println("MAX Puntuacion: " + mejorCalidadPrecio(vinos) + " Calidad Precio del vino: " + calidadPrecioVino(vinos[0]) + " Puntuación de calidad: " + puntuacionCalidadPrecio(vinos[0],vinos));

            }
        });
        return null;
    }

    private float puntuacionPrecio(Vino vino) {
        Float precio = Float.parseFloat(vino.getPrice());
        int[] maximosIntervalos = {Integer.MAX_VALUE, 1700, 1200, 500, 200, 100, 50, 30, 20, 10, 5};

        for (int i = 0; i < maximosIntervalos.length-1; i++) {

            if (precio > maximosIntervalos[i + 1])
                return i;
        }
        return 0;
    }

    private float puntuacionRegion(Vino vino) {
        String paisVino = vino.getCountry();
        int puntuacion=0;

        String paisActual = getApplicationContext().getResources().getConfiguration().locale.getCountry();

        Locale locale = new Locale("",paisActual);

        paisActual = locale.getDisplayCountry(Locale.ENGLISH);

        if (paisActual.equals(paisVino))
            puntuacion = 10;

        return puntuacion;
    }

    private float puntuacionPuntuacion(Vino vino){
        return Float.parseFloat(vino.getPoints())/10;
    }

    private float calidadPrecioVino(Vino vino){
        return Math.round(Float.parseFloat(vino.getPoints())) / Float.parseFloat(vino.getPrice());
    }

    private float mejorCalidadPrecio(Vino[] vinos){
        float maximo = 0;

        for (Vino vino: vinos) {
            float calidadPrecio = calidadPrecioVino(vino);
            if (calidadPrecio > maximo)
                maximo = calidadPrecio;
        }
        return maximo;
    }

    private float puntuacionCalidadPrecio (Vino vino, Vino[] vinos){
        return (10 * calidadPrecioVino(vino) / (mejorCalidadPrecio(vinos)));
    }

    private float funcionValorVinos(Vino vino, Vino[] vinos, int pesoPrecio, int pesoCalidad, int pesoRegion, int pesoPuntuacion){

        int totalPuntos = pesoPrecio + pesoCalidad + pesoRegion + pesoPuntuacion;

        if (totalPuntos == 0 ) {
             totalPuntos = 100;
             pesoPrecio = 25;
             pesoCalidad = 25;
             pesoRegion = 25;
             pesoPuntuacion = 25;
        }

        float puntuacion =  (puntuacionPrecio(vino) * pesoPrecio +
                puntuacionCalidadPrecio(vino,vinos) * pesoCalidad +
                puntuacionRegion(vino) * pesoRegion +
                puntuacionPuntuacion(vino) * pesoPuntuacion )/totalPuntos;
        //System.err.println("Vino: " + vino.toString() +  "Pesos : "+ pesoPrecio + " " + pesoCalidad + " " + pesoRegion + " " + pesoPuntuacion + " PuntPrecio " +puntuacionPrecio(vino) + " puntCalidad " +puntuacionCalidadPrecio(vino,vinos)+ " PuntRegion: " + puntuacionRegion(vino) + " puntPuntuacion " + puntuacionPuntuacion(vino)+" PUNTUACION:"+puntuacion );
        return puntuacion;
    }



    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    */
}
