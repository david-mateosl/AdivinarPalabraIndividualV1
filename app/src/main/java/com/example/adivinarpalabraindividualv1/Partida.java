package com.example.adivinarpalabraindividualv1;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Partida implements Serializable {


    ArrayList<Palabra> palabras;
    int intentos;
    Palabra palabra;
    char[] palabraEnChar;
    ArrayList<Character> caracteresRepetidos;

    public Partida(ArrayList<Palabra> palabrasConstructor) {
        this.palabras = palabrasConstructor;
    }

    public ArrayList<String> iniciarPalabras() {
        ArrayList<String> aux = new ArrayList<>();
        aux.add("Murcielago");
        aux.add("coche");
        aux.add("java");
        return aux;
    }

    //Inciamos la partida, eligiendo una palabra al azar de la coleccion palabras y la transformamos a un array de chars.
    public String iniciarPartida() {

        try {

            Random elegida = new Random();
            palabra = palabras.get(elegida.nextInt(palabras.size()));
            intentos = palabra.getNombre().length() / 2;

            palabraEnChar = new char[palabra.getNombre().length()];
            for (int i = 0; i < palabra.getNombre().length(); i++) {
                palabraEnChar[i] = '_';
            }
            for (int i = 0; i < palabra.getNombre().length() / 2; i++) {
                int aleatorio = elegida.nextInt(palabra.getNombre().length());
                palabraEnChar[aleatorio] = palabra.getNombre().charAt(aleatorio);
            }

            return String.valueOf(palabraEnChar);

        }catch (IllegalArgumentException iae){
           return "No hay palabras disponibles";
        }
    }
    public void exportarPalabrasTxt(Context contexto) {
        String filename = "palabras.txt";

        try {
            // FileOutputStream fos = contexto.openFileOutput(filename,Context.MODE_PRIVATE);
            //  ObjectOutputStream os = new ObjectOutputStream(fos);
            FileWriter fw = new FileWriter(new File(contexto.getFilesDir(), filename));
            for (int i = 0; i < palabras.size(); i++) {
                //   os.writeObject(palabras.get(i));
                fw.write(palabras.get(i).getNombre());
                fw.write("\n");
                fw.write(palabras.get(i).getDescripcion());
                fw.write("\n");
            }
            // os.flush();
            // os.close();
            fw.flush();
            fw.close();
        } catch (IOException e) {
            Toast.makeText(contexto, "No se pudo escribir el fichero", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Palabra> importarPalabrasTxt(Context contexto) {
        String filename = "palabras.txt";
        String contenido = "";
        palabras.clear();
        Palabra palabraAux;
        try {
            FileReader fr = new FileReader(new File(contexto.getFilesDir(), filename));

            BufferedReader bfr = new BufferedReader(fr);
            while ((contenido = bfr.readLine()) != null) {
                palabraAux=new Palabra();
                System.out.println(contenido);
                palabraAux.setNombre(contenido);
                palabraAux.setDescripcion(bfr.readLine());
                palabras.add(palabraAux);
            }
            fr.close();

        } catch (FileNotFoundException e) {
            Toast.makeText(contexto, "No se ha encontrado el fichero", Toast.LENGTH_SHORT).show();
        } catch (IOException ioe) {
            Toast.makeText(contexto, "Error de entrada salida", Toast.LENGTH_SHORT).show();
        }
        return palabras;
    }

    public void anadirPalabra(Palabra palabraAnadida) {
        palabras.add(palabraAnadida);
    }


    public String adivinar(char letraIntroducida) {


        if (intentos >= 1) {
            if (letraHallada(letraIntroducida)) {
                if (comprobarFinal()) {
                    // volverJugar(true);//Ha ganado si comprobar final devuelve true
                }
            } else {
                intentos--;
            }

        } else {
            //   volverJugar(false);//Ha perdido porque se ha quedado sin intentos

        }


        return String.valueOf(palabraEnChar);
    }

    public Palabra getPalabraResuelta() {
        return palabra;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    public void setPalabra(String palabra) {
        palabraEnChar = palabra.toCharArray();
    }

    public char[] getPalabraEnChar() {
        return palabraEnChar;
    }

    public ArrayList<Palabra> getPalabras() {
        return palabras;
    }

    //Si la letra introducida se encuentra en la palabra, cambia la mÃ¡scara de guiones por la letra en su posicion
    public boolean letraHallada(char letraIntroducida) {
        boolean letrahallada = false;

        for (int i = 0; i < palabraEnChar.length; i++) {
            if (palabra.getNombre().charAt(i) == letraIntroducida) {
                palabraEnChar[i] = letraIntroducida;
                letrahallada = true;
            }

        }
        return letrahallada;
    }

    //Este metodo comprueba que la palabra ha sido resuelta completamente, devuelve true si es asi
    public boolean comprobarFinal() {
        boolean terminado = true;

        for (int i = 0; i < palabraEnChar.length; i++) {
            if (palabraEnChar[i] == '_') {
                terminado = false;
                break;
            }
        }
        return terminado;
    }

    public boolean letrasRepetidas(char caracter) {
        boolean repetida = false;
        if (caracteresRepetidos != null) {
            if (caracteresRepetidos.contains(caracter)) {
                repetida = true;
            }
            caracteresRepetidos.add(caracter);
        }

        return repetida;
    }



}
