package com.unizar.wineapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

/**
 * Clase MainActivity.
 * Esta clase define la actividad principal que hereda de BaseActivity.
 * 
 * En esta actividad se muestran las variedades disponibles y los seekbar para 
 * que el usuario configure los pesos para su recomendación.
 *
 * @author: Alejandro y Alberto
 */
public class MainActivity extends BaseActivity {

    private MobileServiceClient conexionServerAPI;
    private ArrayAdapter<Variedad> comboAdapter;
    private List<Variedad> lista;

    private SeekBar seekBar_precio, seekBar_calidad, seekBar_region, seekBar_puntuacion;
    private TextView tv_peso_precio, tv_peso_calidad, tv_peso_region, tv_peso_puntuacion;
    private Button btn_obtener;
    private ProgressBar bar;
    private Spinner spinner;

    //VARIABLES FINALES
    private final int ALTURA_SPINNER = 700;
    private final String SERVIDOR_AZURE = "https://winedss2.azurewebsites.net";
    private final String API_OBTENER_VARIEDADES = "ObtenerVariedades";
    private final String API_OBTENER_VINOS = "ObtenerVinos";
    private final static String TODAS = "Todas";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.content_main, contentFrameLayout);
        getSupportActionBar().setTitle(R.string.recomendador);

        bar = findViewById(R.id.progressBar);
        spinner = (Spinner) findViewById(R.id.spinner);
        btn_obtener = (Button) findViewById(R.id.btn_obtener);
        seekBar_calidad = (SeekBar) findViewById(R.id.seekBar_calidad);
        seekBar_precio = (SeekBar) findViewById(R.id.seekBar_precio);
        seekBar_region = (SeekBar) findViewById(R.id.seekBar_region);
        seekBar_puntuacion = (SeekBar) findViewById(R.id.seekBar_puntuacion);
        tv_peso_precio = (TextView) findViewById(R.id.tv_peso_precio);
        tv_peso_calidad = (TextView) findViewById(R.id.tv_peso_calidad);
        tv_peso_region = (TextView) findViewById(R.id.tv_peso_region);
        tv_peso_puntuacion = (TextView) findViewById(R.id.tv_peso_puntuacion);

        btn_obtener.setEnabled(false);
        btn_obtener.setClickable(false);

        /*
        Conexión con el servidor AZURE.
         */
        try {
            conexionServerAPI = new MobileServiceClient(SERVIDOR_AZURE, this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        /*
        OnClickListener del botón obtener recomendación.
        Desactiva el botón y muestra un icono de carga mientras se ejecutan las acciones
        para obtener la mejor recomendación.
         */
        btn_obtener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerRecomendacion((Variedad) spinner.getSelectedItem());
            }
        });


        /*Limitación de la altura del spinner al desplegarse.*/
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);
            popupWindow.setHeight(ALTURA_SPINNER);

        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) { }


        onChangeListenerSeekbars();


        obtenerVariedades();

        btn_obtener.setClickable(true);
        btn_obtener.setEnabled(true);
        bar.setVisibility(View.INVISIBLE);
    }

    /**
     * OnChangeListeners de los seekbars
     *
     * Cada seekbar tiene un textView al lado que irá mostrando el progreso de su barra Ejemplo: 5/10.
     */
    private void onChangeListenerSeekbars(){
        seekBar_precio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
                tv_peso_precio.setText(progressValue + "/" + seekBar.getMax());
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

    /**
     * Realiza la llamada al API ObtenerVariedades que le devuelve un vector de objetos
     * Variedad con las variedades almacenadas en la base de datos.
     *
     * En caso de éxito rellena el spinner de la interfaz con dicha lista.
     *
     *
     *
     * @return Devuelve un vector de objetos variedad.
     */
    private void obtenerVariedades() {

        ListenableFuture<Variedad[]> allVariety = conexionServerAPI.invokeApi(API_OBTENER_VARIEDADES, "", Variedad[].class);

        Futures.addCallback(allVariety, new FutureCallback<Variedad[]>() {
            @Override
            public void onSuccess(Variedad[] result) {

                //creamos el ArrayList e introducimos los elementos del vector para
                // introducirlos en el comboAdapter.
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
    }

    /**
     * Realiza la llamada al API ObtenerVinos que devuelve los vinos de la categoría seleccionada.
     *
     *
     * Una vez obtenido el vector lo recorre buscándo el mejor y se lanza la activity de recomendación.
     *
     * @param variedadSeleccionada
     * @return
     */
    private void obtenerRecomendacion(Variedad variedadSeleccionada) {
        bar.setVisibility(View.VISIBLE);

        final ListenableFuture<Vino[]> listaVinos = conexionServerAPI.invokeApi(API_OBTENER_VINOS, variedadSeleccionada.getNombre(), Vino[].class);

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
                float mejorValoracion=algoritmoValoracionVino(vinos[0],vinos,pesoPrecio, pesoCalidad, pesoRegion, pesoPuntuacion);

                //se recorren los vinos buscando el que obtenga mejor resultado ejecutando el algoritmo..
                for (int i=1;i < vinos.length;i++) {
                    float valoracion = algoritmoValoracionVino(vinos[i],vinos,pesoPrecio, pesoCalidad, pesoRegion, pesoPuntuacion);

                    if(valoracion > mejorValoracion){
                        mejorValoracion = valoracion;
                        mejorVino = vinos[i];
                    }
                }


                //Lanza la actividad RecomendacionActivity pasando la información que necesitará.
                Intent anIntent = new Intent(getApplicationContext(), RecomendacionActivity.class);
                anIntent.putExtra("Vino",mejorVino);
                anIntent.putExtra("Valoracion",mejorValoracion);
                anIntent.putExtra("PuntuacionPrecio",funcionValorPrecio(mejorVino));
                anIntent.putExtra("PuntuacionCalidad",funcionValorCalidadPrecio(mejorVino,vinos));
                anIntent.putExtra("PuntuacionRegion",funcionValorRegion(mejorVino));
                anIntent.putExtra("PuntuacionPuntuacion",funcionValorPuntuacion(mejorVino));

                startActivity(anIntent);
                findViewById(R.id.btn_obtener).setEnabled(true);
                drawerLayout.closeDrawers();
                bar.setVisibility(View.INVISIBLE);

            }
        });
        //bar.setVisibility(View.INVISIBLE);
    }

    /**
     * Función valor de la variable precio.
     *
     * Creamos un rango de precios y a cada uno se le otorga una puntuación.
     *
     * Para optimizar el proceso metemos los máximos en un vector y la puntuación será
     * la de la posicion en la que se encontraría el precio del vino.
     *
     * Ejemplo: Un vino con precio de 350€ obtendría un 3.
     *
     * @param vino
     * @return
     */
    private float funcionValorPrecio(Vino vino) {
        Float precio = Float.parseFloat(vino.getPrice());
        int[] maximosIntervalos = {Integer.MAX_VALUE, 1700, 1200, 500, 200, 100, 50, 30, 20, 10, 5};

        for (int i = 0; i < maximosIntervalos.length-1; i++) {

            if (precio > maximosIntervalos[i + 1]) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Función valor de la variable región.
     *
     * Se obtiene el país desde el que se ejecuta la aplicación y se compara
     * con el origen del vino.
     *
     * Si el vino es del país del usuario se le otorga un 10 y sino un 0.
     *
     * El usuario configurará la importancia subjetiva con el peso que asigne a dicha variable.
     *
     *
     * @param vino
     * @return
     */
    private float funcionValorRegion(Vino vino) {
        String paisVino = vino.getCountry();
        int puntuacion=0;

        String paisActual = getApplicationContext().getResources().getConfiguration().locale.getCountry();

        //Traduce el país actual al inglés para compararlo corréctamente con el del vino.
        Locale locale = new Locale("",paisActual);
        paisActual = locale.getDisplayCountry(Locale.ENGLISH);

        if (paisActual.equals(paisVino))
            puntuacion = 10;

        return puntuacion;
    }

    /**
     * Función valor de la variable puntuación, se obtiene la puntuación de la base de datos
     * y se divide entre 10 para que se encuentre en el rango de 0-10.
     *
     * @param vino
     * @return
     */
    private float funcionValorPuntuacion(Vino vino){
        return Float.parseFloat(vino.getPoints())/10;
    }

    /**
     * Utilizamos interpolación lineal o regla de 3.
     *
     * @param vino
     * @param vinos
     * @return
     */
    private float funcionValorCalidadPrecio (Vino vino, Vino[] vinos){
        return (10 * calidadPrecioVino(vino) / (mejorCalidadPrecio(vinos)));
    }

    /**
     * Calcula la relación calidad precio del vino.
     * Divide los puntos otorgados por el taster entre el precio de la botella.
     * @param vino
     * @return
     */
    private float calidadPrecioVino(Vino vino){
        return Math.round(Float.parseFloat(vino.getPoints())) / Float.parseFloat(vino.getPrice());
    }

    /**
     * Recorre el vector de vinos buscando el vino con mejor calidad/precio
     * @param vinos
     * @return
     */
    private float mejorCalidadPrecio(Vino[] vinos){
        float maximo = 0;

        for (Vino vino: vinos) {
            float calidadPrecio = calidadPrecioVino(vino);
            if (calidadPrecio > maximo)
                maximo = calidadPrecio;
        }
        return maximo;
    }

    /**
     * Algoritmo que calcula la valoración de cada vino en función de los resultados
     * de las funciones valor y los pesos otorgados a cada una.
     *
     * @param vino
     * @param vinos
     * @param pesoPrecio
     * @param pesoCalidad
     * @param pesoRegion
     * @param pesoPuntuacion
     * @return
     */
    private float algoritmoValoracionVino(Vino vino, Vino[] vinos, int pesoPrecio, int pesoCalidad, int pesoRegion, int pesoPuntuacion){

        int totalPesos = pesoPrecio + pesoCalidad + pesoRegion + pesoPuntuacion;

        //Si el usuario indicó que todos los pesos son 0 se otorga 25/100 a cada uno.
        if (totalPesos == 0 ) {
             totalPesos = 100;
             pesoPrecio = 25;
             pesoCalidad = 25;
             pesoRegion = 25;
             pesoPuntuacion = 25;
        }

        float puntuacion =  (funcionValorPrecio(vino) * pesoPrecio +
                funcionValorCalidadPrecio(vino,vinos) * pesoCalidad +
                funcionValorRegion(vino) * pesoRegion +
                funcionValorPuntuacion(vino) * pesoPuntuacion )/totalPesos;
        return puntuacion;
    }
}
