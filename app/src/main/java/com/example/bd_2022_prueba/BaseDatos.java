package com.example.bd_2022_prueba;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BaseDatos extends SQLiteOpenHelper {

    public BaseDatos(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL("create table usuarios (id integer primary key autoincrement, usuario text, password text)");
       db.execSQL("create table productos (id integer primary key autoincrement, codigo text, nombre text, precio real, Descripcion text)");
       db.execSQL("insert into usuarios values(0,'admin','123')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("create table usuarios (id integer primary key autoincrement, usuario text, password text)");
        db.execSQL("create table productos (id integer primary key autoincrement, codigo text, nombre text, precio real, Descripcion text)");
        db.execSQL("insert into usuarios values(0,'admin','123')");
    }
}
