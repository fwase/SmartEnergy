package com.smartenergy.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.smartenergy.Usuario;

public class UsuarioDAO {
    private SQLiteDatabase bancoDeDados;

    // Abre conexão com o BD
    public UsuarioDAO(Context context){
        this.bancoDeDados = (new BancoDeDados(context)).getWritableDatabase();
    }

    public Usuario fazerLogin(String login, String senha){
        Usuario usuario = null;

        try {
            String sqlQuery = "SELECT * FROM Usuarios WHERE login='" + login +
                    "' AND senha='" + senha + "'";
            Log.i("ABC",sqlQuery);
            Cursor cursor = this.bancoDeDados.rawQuery(sqlQuery, null);
            Log.i("ABC", "CURSOR ABERTO");
            if (cursor.moveToNext()) {
                usuario = new Usuario(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getDouble(4),
                        cursor.getDouble(5));
            }
            cursor.close();
        } catch (SQLException e){
            Log.e("ABC",e.getMessage());
        }
        return usuario;
    }

    public boolean fazerCadastro(Usuario usuario){
        try{
            String sqlQuery = "SELECT * FROM Usuarios WHERE login='" + usuario.getLogin() +
                    "'";
            Cursor cursor = this.bancoDeDados.rawQuery(sqlQuery, null);
            if(cursor.moveToNext()){
                return false;
            }
            String sqlCmd = "INSERT INTO Usuarios VALUES (NULL, '" + usuario.getLogin() + "', '" + usuario.getSenha() +
                    "', '" + usuario.getName() + "', " + usuario.getKw_hora() + ", " + usuario.getValorLimite() + ")";
            this.bancoDeDados.execSQL(sqlCmd);
            cursor.close();
            return true;
        }
        catch (SQLException e){
            Log.e("ABC",e.getMessage());
            return false;
        }
    }
}
