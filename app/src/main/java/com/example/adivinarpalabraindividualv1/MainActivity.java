package com.example.adivinarpalabraindividualv1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView intentos, palabraAadivinar, numeroDePalabras, palabraDescripcion;
    Button nuevo,adivinar;
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

        if (getIntent().hasExtra("partida")) {
            Intent recogerPartida = getIntent();
            partida = (Partida) recogerPartida.getSerializableExtra("partida");
        } else {
            arrayInicial.add(new Palabra("Murcielago", "Mamifero volador"));
            arrayInicial.add(new Palabra("Coche", "Vehiculo común"));
            arrayInicial.add(new Palabra("Platano", "Fruta amarilla"));
            partida = new Partida(arrayInicial);
        }

        palabraDescripcion = findViewById(R.id.descripcionF);
        intentos = findViewById(R.id.intentos);
        palabraAadivinar = findViewById(R.id.palabraF);
        numeroDePalabras = findViewById(R.id.palabrasDisponibles);

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
            case R.id.salir:
                Toast.makeText(this, "Hasta luego!", Toast.LENGTH_SHORT).show();
                finishAffinity();
                return true;
            default:
                return super.onOptionsItemSelected(item);

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
            } catch (IndexOutOfBoundsException ioe) {
                Toast.makeText(this, "Introduce una letra!", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (!partida.comprobarFinal()) {
                letra.setText("");
                volverJugarPartidaPerdida().show();
                reiniciar();
            }
        }
    }

    public AlertDialog.Builder anadirPalabraConCuadroDialogo() {
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

    }

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
                finish();
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
                finish();
            }
        });
        return builder;
    }

}