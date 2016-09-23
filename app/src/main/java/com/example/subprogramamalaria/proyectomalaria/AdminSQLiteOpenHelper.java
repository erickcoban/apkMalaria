package com.example.subprogramamalaria.proyectomalaria;

/**
 * Created by Subprograma Malaria on 17/07/2015.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    //llamamos al constructor
    public AdminSQLiteOpenHelper(Context context, String nombre, CursorFactory factory, int version) {
        super(context, nombre, factory, version);
    }

    //creamos la tabla Usuarios
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table crear_caso(id_case integer primary key, fecha_registro text , fecha_sintomas text, " +
                "sintomas text, tipo_prueba text, fecha_toma_+mh text, clave_no text, primer_nombre text, segundo_nombre text, " +
                "primer_apellido text, segundo_apellido text, apellido_casada text, genero text, fecha_nacimiento text, " +
                "embarazada text, meses_embarazo integer, resultado_pdr text, username_reg text)");
    }

    //borrar la tabla y crear la nueva tabla
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue) {
        db.execSQL("drop table if exists Usuarios");
        db.execSQL("create table crear_caso(id_case integer primary key, fecha_registro text , fecha_sintomas text," +
                "sintomas text, tipo_prueba text, fecha_toma_mh text, clave_no text, primer_nombre text, segundo_nombre text," +
                "primer_apellido text, segundo_apellido text, apellido_casada text, genero text, fecha_nacimiento text, " +
                "embarazada text, meses_embarazo integer, resultado_pdr text, username_reg text)");
    }
}
