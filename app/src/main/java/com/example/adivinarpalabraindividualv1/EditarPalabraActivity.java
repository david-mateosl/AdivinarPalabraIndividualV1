package com.example.adivinarpalabraindividualv1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

        ArrayAdapter<Palabra> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, palabras);

        ListView listaComp = findViewById(R.id.listaDePalabras);
        listaComp.setAdapter(adaptador);
        listaComp.setOnItemClickListener((adapterView, view, position, l) -> {

            Intent irAformulario = new Intent(getApplicationContext(), FormularioActivity.class);

            irAformulario.putExtra("partida", partida);
            irAformulario.putExtra("palabraForm", partida);
            irAformulario.putExtra("posicionForm", position);

            startActivity(irAformulario);

        });
    }

    public void IrFormulario(View view) {
        Intent i = new Intent(this, FormularioActivity.class);
        i.putExtra("posicionForm", -1);
        i.putExtra("partida", partida);
        startActivity(i);
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
