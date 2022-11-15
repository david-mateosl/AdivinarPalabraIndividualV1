package com.example.adivinarpalabraindividualv1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FormularioActivity extends AppCompatActivity {

    ArrayList<Palabra> palabras = new ArrayList<>();
    int posicionF;
    Partida partida;
    EditText nombre, descripcion;
    Boolean cemaforoNovea = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formularios);
        Intent recogerPartida = getIntent();
        partida = (Partida) recogerPartida.getSerializableExtra("partida");

        if (partida != null) {
            palabras = partida.getPalabras();
        } else {
            Toast.makeText(this, "No se ha recogido ninguna palabra", Toast.LENGTH_LONG).show();
        }

        Bundle datos = getIntent().getExtras();
        posicionF = datos.getInt("posicionForm");

        nombre = findViewById(R.id.textoNombre);
        nombre.setText(partida.getPalabras().get(posicionF).getNombre());

        descripcion = findViewById(R.id.textoDescripcion);
        descripcion.setText(partida.getPalabras().get(posicionF).getDescripcion());

    }

    public void borrarPalabra(View view) {

        if (cemaforoNovea) {

            palabras.remove(posicionF);
            Toast.makeText(this, "Se borró la palabra: " + nombre.getText(), Toast.LENGTH_SHORT).show();
            borrarCamposPalabra();
            cemaforoNovea = false;
        }

    }

    public void modificarPalabra(View view) {

        if (cemaforoNovea && !nombre.getText().toString().equals("")
                && !descripcion.getText().toString().equals("")) {

            if (palabras.contains(nombre.getText().toString())) {
                palabras.get(posicionF).setNombre(nombre.getText().toString());
                palabras.get(posicionF).setDescripcion(descripcion.getText().toString());
                Toast.makeText(this, "La palabra" + palabras.get(posicionF).getNombre() +
                        " se modifico corrrectamente", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "No se puede modificar una" +
                        " palabra a una palabra que ya existe", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No se puede modificar una" +
                    " palabra dejando un campo en blanco", Toast.LENGTH_SHORT).show();
        }
    }

    public void añadirPalabra(View view) {

        if (!nombre.getText().toString().equals("") &&
                !descripcion.getText().toString().equals("")) {
            if (!palabras.contains(nombre.getText().toString()))
                palabras.add(new Palabra(nombre.getText().toString(), descripcion.getText().toString()));
        }
    }

    public void borrarCamposPalabra() {

        nombre.setText("");
        descripcion.setText("");
    }

    public void volver(View view) {
        Intent irListas = new Intent(getApplicationContext(), EditarPalabraActivity.class);
        irListas.putExtra("partida", partida);
        startActivity(irListas);
    }

}
