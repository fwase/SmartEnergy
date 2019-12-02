package com.smartenergy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BancoDeDados extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "User.db";
    public static final String SQL_CREATE_TABLE_USUARIOS = "CREATE TABLE Usuarios(" +
          "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, login TEXT NOT NULL, " +
            "senha TEXT NOT NULL, nome TEXT, kw_hora REAL, valor_limite REAL, tempo_segundos INTEGER);";
    public static final String SQL_CREATE_TABLE_CONSUMO =
            "CREATE TABLE Consumo (user_id INTEGER, data_hora TEXT, watts REAL," +
            "FOREIGN KEY (user_id) REFERENCES Usuarios(id));";
    public static final String SQL_POPULATE_TABLES = "INSERT INTO Usuarios " +
            "VALUES (NULL,'admin', '123', 'Ciro', 0.80, 50.0, 0)";
    public static final String SQL_DELETE_TABLE_USUARIOS = "DROP TABLE IF EXISTS Usuarios";
    public static final String SQL_DELETE_TABLE_CONSUMO = "DROP TABLE IF EXISTS Consumo";

    public BancoDeDados(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Executado ao criar o BD
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("ABC","BD INICIOU?");
        db.execSQL(SQL_CREATE_TABLE_USUARIOS);
        Log.d("ABC","CRIOU TABELA USUARIOS!");
        db.execSQL(SQL_CREATE_TABLE_CONSUMO);
        Log.d("ABC","CRIOU TABELA CONSUMO!");
        db.execSQL(SQL_POPULATE_TABLES);
        Log.d("ABC","POPULOU TABELA!");
    }

    // Executado ao atualizar o DB
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE_USUARIOS);
        db.execSQL(SQL_DELETE_TABLE_CONSUMO);
        onCreate(db);
    }
}
