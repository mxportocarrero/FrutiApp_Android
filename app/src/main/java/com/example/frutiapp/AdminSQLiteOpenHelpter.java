package com.example.frutiapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.support.annotation.Nullable;

public class AdminSQLiteOpenHelpter extends SQLiteOpenHelper {

    public AdminSQLiteOpenHelpter(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creacion de una tabla en lenguaje SQL
        db.execSQL("create table puntaje(nombre text, score int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
