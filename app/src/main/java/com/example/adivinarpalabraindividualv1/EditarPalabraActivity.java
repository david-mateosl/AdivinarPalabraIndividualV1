package com.example.adivinarpalabraindividualv1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;

public class EditarPalabraActivity extends AppCompatActivity {

    ArrayList<Palabra> palabras = new ArrayList<>();
    Partida partida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listas);

        Intent recogerPartida = getIntent();
        partida = (Partida) recogerPartida.getSerializableExtra("partida");

        if (partida != null) {
            palabras = partida.getPalabras();
        } else {
            Toast.makeText(this, "No se ha recogido ninguna palabra", Toast.LENGTH_LONG).show();
        }

        ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, palabras);

        ListView listaComp = findViewById(R.id.listaDePalabras);
        listaComp.setAdapter(adaptador);
        listaComp.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent irAformulario = new Intent(getApplicationContext(), FormularioActivity.class);

                irAformulario.putExtra("partida", partida);
                irAformulario.putExtra("palabraForm", partida);
                irAformulario.putExtra("posicionForm", position);

                startActivity(irAformulario);

            }

        });
    }


    public void borrar(View view) {
        palabras.clear();
        startActivity(getIntent());
        Toast.makeText(this, "Las palabras fueron borradas", Toast.LENGTH_SHORT).show();

    }


    public void volverL(View view) {
        Intent irMainActivity = new Intent(getApplicationContext(), MainActivity.class);
        irMainActivity.putExtra("partida", partida);
        startActivity(irMainActivity);
    }
}
