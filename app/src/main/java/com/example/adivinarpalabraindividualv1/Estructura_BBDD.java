package com.example.adivinarpalabraindividualv1;


public final class Estructura_BBDD {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private Estructura_BBDD() {

    }

    /* Inner class that defines the table contents */
    public static final String TABLE_NAME = "Palabras";
    public static final String NOMBRE_COLUMNA1 = "Nombre";
    public static final String NOMBRE_COLUMNA2 = "Descripcion";

    private static final String TEXT_TYPE = " TEXT";
    static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + Estructura_BBDD.TABLE_NAME + " (" + Estructura_BBDD.NOMBRE_COLUMNA1 + " TEXT, "
            + Estructura_BBDD.NOMBRE_COLUMNA2 + TEXT_TYPE + " )";

    static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Estructura_BBDD.TABLE_NAME;


}