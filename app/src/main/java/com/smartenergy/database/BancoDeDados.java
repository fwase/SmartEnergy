package com.smartenergy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BancoDeDados extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "User.db";
    public static final String SQL_CREATE_TABLES = "CREATE TABLE Usuarios(" +
          "user_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, login TEXT NOT NULL, " +
            "senha TEXT NOT NULL, nome TEXT, kw_hora REAL, valor_limite REAL)";
    public static final String SQL_POPULATE_TABLES = "INSERT INTO Usuarios " +
            "VALUES (NULL,'admin', '123', 'Ciro Miranda', 1.07, 500.0)";
    public static final String SQL_DELETE_TABLES = "DROP TABLE IF EXISTS Usuarios";

    public BancoDeDados(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Executado ao criar o BD
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("ABC","BD INICIOU?");
        db.execSQL(SQL_CREATE_TABLES);
        Log.i("ABC","CRIOU TABELA!");
        db.execSQL(SQL_POPULATE_TABLES);
        Log.i("ABC","POPULOU TABELA!");
    }

    // Executado ao atualizar o DB
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLES);
        onCreate(db);
    }
}
