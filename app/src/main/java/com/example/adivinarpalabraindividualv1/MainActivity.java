package com.example.adivinarpalabraindividualv1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView intentos, palabraAadivinar, numeroDePalabras, palabraDescripcion;
    Button nuevo, adivinar;
    EditText letra;
    Partida partida;
    ArrayList<Palabra> arrayInicial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayInicial = new ArrayList<>();

        nuevo = findViewById(R.id.nuevo);
        adivinar = findViewById(R.id.adivinar);
        palabraDescripcion = findViewById(R.id.descripcionF);
        intentos = findViewById(R.id.intentos);
        palabraAadivinar = findViewById(R.id.palabraF);
        numeroDePalabras = findViewById(R.id.palabrasDisponibles);

        if (getIntent().hasExtra("partida")) {
            Intent recogerPartida = getIntent();
            partida = (Partida) recogerPartida.getSerializableExtra("partida");
        } else {
            arrayInicial.add(new Palabra("Murcielago", "Mamifero volador"));
            arrayInicial.add(new Palabra("Coche", "Vehiculo común"));
            arrayInicial.add(new Palabra("Platano", "Fruta amarilla"));
            partida = new Partida(arrayInicial);
        }

        numeroDePalabras.setText("Numero de palabras disponibles: " + partida.palabras.size());
        palabraAadivinar.setText(String.valueOf(partida.iniciarPartida()));

        if (palabraAadivinar.getText().equals("No hay palabras disponibles")) {

            palabraDescripcion.setText("");
            nuevo.setEnabled(false);
            adivinar.setEnabled(false);

        } else {
            palabraDescripcion.setText(String.valueOf(partida.palabra.getDescripcion()));
        }

        intentos.setText(partida.getIntentos() + "");


    }


    /*  @Override
      protected void onResume() {
          super.onResume();
          SharedPreferences datosGuardar = PreferenceManager.getDefaultSharedPreferences(this);
         int intentosActualizar = datosGuardar.getInt("intentos", partida.getIntentos());
          intentos.setText(String.valueOf(intentosActualizar));
          String palabraGuardada = datosGuardar.getString("palabraGuardada","");
          palabraAadivinar.setText(palabraGuardada);
          partida.setPalabra(palabraGuardada);
          partida.setIntentos(intentosActualizar);
      }

      //sobreescrimos metodo onPause de la actividad para que guarde los datos en un Bundle
      @Override
      protected void onPause() {
          super.onPause();
          SharedPreferences datosGuardar = PreferenceManager.getDefaultSharedPreferences(this);
          SharedPreferences.Editor miEditor = datosGuardar.edit();
          miEditor.putString("palabraGuardada",String.valueOf(palabraAadivinar.getText()));
          miEditor.putInt("intentos",partida.getIntentos());
          miEditor.apply();

      }
  */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.mostrarPalabra://En este caso simplemente hago un Toast llamando al getter que me he creado en la clase partida para mostrar la palabra
                Toast.makeText(MainActivity.this, "La palabra es: " + partida.getPalabraResuelta(), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.listaPalabras:
                enviarPalabras();
                return true;
            case R.id.importar:
                partida.palabras = partida.importarPalabrasTxt(this);
                Toast.makeText(this, "Partida Cargada con exito", Toast.LENGTH_SHORT).show();
                numeroDePalabras.setText("Numero de palabras disponibles: " + String.valueOf(partida.palabras.size()));
                palabraAadivinar.setText(String.valueOf(partida.iniciarPartida()));
                return true;
            case R.id.exportar:
                partida.exportarPalabrasTxt(this);
                Toast.makeText(this, "Partida guardada con exito", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.importarSQL:
                importarPalabrasSQL();
                return true;
            case R.id.exportarSQL:
                exportarPalabrasSQL();
                return true;
            case R.id.salir:
                Toast.makeText(this, "Hasta luego!", Toast.LENGTH_SHORT).show();
                finishAffinity();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    public void importarPalabrasSQL() {

        partida.palabras.clear();

        BBDD_Helper helper = new BBDD_Helper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {Estructura_BBDD.NOMBRE_COLUMNA1, Estructura_BBDD.NOMBRE_COLUMNA2};


        try {

            Cursor cursor = db.query(Estructura_BBDD.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    null,              // The columns for the WHERE clause
                    null,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
            );

            while (cursor.moveToNext()) {
                @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex(Estructura_BBDD.NOMBRE_COLUMNA1));
                @SuppressLint("Range") String descripcion = cursor.getString(cursor.getColumnIndex(Estructura_BBDD.NOMBRE_COLUMNA2));
                partida.palabras.add(new Palabra(nombre, descripcion));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        numeroDePalabras.setText("Numero de palabras disponibles: " + partida.palabras.size());

    }

    public void exportarPalabrasSQL() {

        BBDD_Helper helper = new BBDD_Helper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();


        for (Palabra palabras : partida.palabras) {

            values.put(Estructura_BBDD.NOMBRE_COLUMNA1, palabras.getNombre());
            values.put(Estructura_BBDD.NOMBRE_COLUMNA2, palabras.getDescripcion());
            db.insert(Estructura_BBDD.TABLE_NAME, null, values);

        }

    }

    public void enviarPalabras() {
        Intent irAlista = new Intent(getApplicationContext(), EditarPalabraActivity.class);

        irAlista.putExtra("partida", partida);

        startActivity(irAlista);
    }

    public void jugar(View view) {
        reiniciar();
    }

    public void reiniciar() {
        palabraAadivinar.setText(partida.iniciarPartida());
        palabraDescripcion.setText(String.valueOf(partida.palabra.getDescripcion()));
        intentos.setText("" + partida.getIntentos());
    }

    public void adivinar(View vista) {

        letra = findViewById(R.id.letra);
        EditText palabra = findViewById(R.id.ingresarPalabraCorrecta);

        if (palabra.getText().toString().equals("") && letra.getText().toString().equals("")) {
            Toast.makeText(this, "Por favor introduzca Valores en los campos", Toast.LENGTH_SHORT).show();
        } else {
            if (!palabra.getText().toString().equals("")) {
                letra.setText("");
            }
            if (!letra.getText().toString().equals("")) {
                if (partida.getIntentos() >= 1) {

                    try {

                        palabraAadivinar.setText(partida.adivinar(String.valueOf(letra.getText()).charAt(0)));
                        intentos.setText(partida.getIntentos() + "");
                        letra.setText("");
                        if (partida.comprobarFinal()) {
                            letra.setText("");
                            volverJugarPartidaGanada().show();
                            reiniciar();
                        }
                        if (partida.getIntentos() < 1) {
                            if (!partida.comprobarFinal()) {
                                letra.setText("");
                                volverJugarPartidaPerdida().show();
                                reiniciar();
                            }
                        }
                    } catch (IndexOutOfBoundsException ioe) {
                        Toast.makeText(this, "Introduce una letra!", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                if (partida.palabra.getNombre().equals(palabra.getText().toString())) {
                    palabra.setText("");
                    volverJugarPartidaGanada().show();
                    reiniciar();
                } else {
                    int intentos = Integer.parseInt(this.intentos.getText().toString());
                    partida.setIntentos(intentos - 1);
                    this.intentos.setText(partida.getIntentos() + "");
                    if (partida.getIntentos() == 0) {
                        volverJugarPartidaPerdida().show();
                        reiniciar();
                    }
                }
            }
        }
    }

   /* public AlertDialog.Builder anadirPalabraConCuadroDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText palabraDialogNombre = new EditText(this);
        final EditText palabraDialogDescripcion = new EditText(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        palabraDialogNombre.setInputType(InputType.TYPE_CLASS_TEXT);
        palabraDialogDescripcion.setInputType(InputType.TYPE_CLASS_TEXT);

        palabraDialogNombre.setHint("Introduce el nombre");
        palabraDialogDescripcion.setHint("Introduce la descripcion");
        layout.addView(palabraDialogNombre);
        layout.addView(palabraDialogDescripcion);
        builder.setView(layout);


        if (palabraDialogNombre.getText().toString() != "") {

            builder.setTitle("Añade una palabra: ").setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    partida.anadirPalabra(new Palabra(String.valueOf(palabraDialogNombre.getText()), String.valueOf(palabraDialogDescripcion.getText())));
                    numeroDePalabras.setText("Numero de palabras disponibles: " + String.valueOf(partida.getPalabras().size()));
                }
            }).setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        } else {
            Toast.makeText(MainActivity.this, "Introduce una palabra", Toast.LENGTH_SHORT).show();
        }
        return builder;

    } */

    public AlertDialog.Builder volverJugarPartidaPerdida() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Fin de la partida, perdiste.").setMessage("La palabra era: " + partida.palabra).setPositiveButton("Volver a jugar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("Cerrar aplicacion", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Sigue practicando!", Toast.LENGTH_LONG).show();
                finishAffinity();
            }
        });
        return builder;
    }

    public AlertDialog.Builder volverJugarPartidaGanada() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Felicidades, has ganado!!").setMessage("La palabra era: " + partida.palabra).setPositiveButton("Volver a jugar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("Cerrar aplicacion", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Vuelve pronto!", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        });
        return builder;
    }

}