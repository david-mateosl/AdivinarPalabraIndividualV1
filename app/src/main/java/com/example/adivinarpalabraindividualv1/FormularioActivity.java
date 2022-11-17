package com.example.adivinarpalabraindividualv1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FormularioActivity extends AppCompatActivity {

    ArrayList<Palabra> palabras = new ArrayList<>();
    int posicionF;
    Partida partida;
    EditText nombre, descripcion;
    Boolean semaforo = true;
    Button botonInsetarSQL, botonBorrarSQL, botonActualizarSQL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formularios);

        botonInsetarSQL = findViewById(R.id.botonInsertarSQL);
        botonBorrarSQL = findViewById(R.id.botonBorrarSQL);
        botonActualizarSQL = findViewById(R.id.botonActualizarSQL);
        nombre = findViewById(R.id.textoNombre);
        descripcion = findViewById(R.id.textoDescripcion);

        Intent recogerPartida = getIntent();
        partida = (Partida) recogerPartida.getSerializableExtra("partida");

        if (partida != null) {
            if (!partida.palabras.isEmpty()) {
                palabras = partida.getPalabras();
            } else {
                palabras = new ArrayList<>();
            }
        } else {
            Toast.makeText(this, "No se ha recogido ninguna palabra", Toast.LENGTH_LONG).show();
        }

        Bundle datos = getIntent().getExtras();

        if (datos != null) {
            if (palabras.size() != 0) {
                posicionF = datos.getInt("posicionForm");
                nombre.setText(partida.getPalabras().get(posicionF).getNombre());
                descripcion.setText(partida.getPalabras().get(posicionF).getDescripcion());
            } else {
                posicionF = 0;
            }
        }

        BBDD_Helper helper = new BBDD_Helper(this);

        botonInsetarSQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase db = helper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(Estructura_BBDD.NOMBRE_COLUMNA1, nombre.getText().toString());
                values.put(Estructura_BBDD.NOMBRE_COLUMNA2, descripcion.getText().toString());

                long newRowId = db.insert(Estructura_BBDD.TABLE_NAME, null, values);

                Toast.makeText(FormularioActivity.this, "Se guardó el registro con clave: " +
                        newRowId, Toast.LENGTH_SHORT).show();

            }
        });

        botonBorrarSQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase db = helper.getWritableDatabase();

                String selection = Estructura_BBDD.NOMBRE_COLUMNA1 + " LIKE ?";
                String[] selectionArgs = {nombre.getText().toString()};
                int deletedRows = db.delete(Estructura_BBDD.TABLE_NAME, selection, selectionArgs);

                Toast.makeText(FormularioActivity.this, "Se borro el registro",
                        Toast.LENGTH_SHORT).show();


            }
        });

        botonActualizarSQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase db = helper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(Estructura_BBDD.NOMBRE_COLUMNA2, descripcion.getText().toString());

                String selection = Estructura_BBDD.NOMBRE_COLUMNA1 + " LIKE ?";
                String[] selectionArgs = {nombre.getText().toString()};

                int count = db.update(
                        Estructura_BBDD.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);

                Toast.makeText(FormularioActivity.this, "Se modifico la palabra", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void borrarPalabra(View view) {

        if (semaforo && !nombre.getText().toString().equals("")
                && !descripcion.getText().toString().equals("")) {
            if (palabras.contains(nombre.getText().toString())
                    && palabras.contains(descripcion.getText().toString())) {
                palabras.remove(posicionF);
                Toast.makeText(this, "Se borró la palabra: " + nombre.getText(), Toast.LENGTH_SHORT).show();
                borrarCamposPalabra();
                semaforo = false;
            }
        } else {
            Toast.makeText(this, "Error campos en blanco", Toast.LENGTH_SHORT).show();
        }

    }

    public void modificarPalabra(View view) {

        if (semaforo && !nombre.getText().toString().equals("")
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
                partida.palabras.add(new Palabra(nombre.getText().toString(), descripcion.getText().toString()));
            Toast.makeText(this, "La palabra: " + nombre.getText().toString()
                    + " se añadió correctamente", Toast.LENGTH_SHORT).show();
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
