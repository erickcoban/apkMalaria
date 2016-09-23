package com.example.subprogramamalaria.proyectomalaria;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * Created by Subprograma Malaria on 07/10/2015.
 */
public class last_user extends SQLiteOpenHelper {
    //llamamos al constructor
    public last_user(Context context, String user, CursorFactory factory, int version) {
        super(context, user, factory, version);
    }

    //creamos la tabla Usuarios
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table last_user(username text primary key, password text)");
    }

    //borrar la tabla y crear la nueva tabla
    @Override
    public void onUpgrade(SQLiteDatabase db_last, int versionAnte, int versionNue) {
        db_last.execSQL("drop table if exists Usuarios");
        db_last.execSQL("create table last_user(username text primary key, password text)");
    }
}
