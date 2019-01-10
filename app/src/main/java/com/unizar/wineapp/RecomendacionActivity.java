package com.unizar.wineapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RecomendacionActivity extends BaseActivity {
    private Vino mejorVino;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        View vista = getLayoutInflater().inflate(R.layout.activity_recomendacion, contentFrameLayout);
        getSupportActionBar().setTitle(R.string.vino_recomendado);


        //setContentView(R.layout.activity_recomendacion);


        mejorVino = (Vino) getIntent().getSerializableExtra("Vino");
        float valoracion = (float)getIntent().getSerializableExtra("Valoracion");

        TextView tv_titulo = (TextView) vista.findViewById(R.id.tv_titulo_vino);
        TextView tv_descripcion = (TextView) vista.findViewById(R.id.tv_descripcion_vino);
        TextView tv_precio = (TextView) vista.findViewById(R.id.tv_precio_vino);
        TextView tv_pais = (TextView) vista.findViewById(R.id.tv_pais_vino);
        TextView tv_provincia = (TextView) vista.findViewById(R.id.tv_provincia_vino);
        TextView tv_catador = (TextView) vista.findViewById(R.id.tv_catador_vino);
        TextView tv_puntuacion = (TextView) vista.findViewById(R.id.tv_puntuacion_vino);
        TextView tv_valoracion = (TextView) vista.findViewById(R.id.tv_valor_funcion);

        tv_titulo.setText(mejorVino.getTitle().toString());
        tv_descripcion.setText((mejorVino.getDescription()));
        tv_precio.setText(mejorVino.getPrice());
        tv_pais.setText(mejorVino.getCountry());
        tv_provincia.setText(mejorVino.getProvince());
        tv_catador.setText(mejorVino.getTaster());
        tv_puntuacion.setText(mejorVino.getPoints());
        tv_valoracion.setText(Float.toString(valoracion));


    }

}
