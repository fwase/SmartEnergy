package com.smartenergy.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.smartenergy.Usuario;

import java.util.HashMap;
import java.util.Map;

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
            Log.i("ABC", "LOGIN FEITO COM SUCESSO");
            if (cursor.moveToNext()) {
                usuario = new Usuario(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getDouble(4),
                        cursor.getDouble(5), cursor.getInt(6));
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
                    "', '" + usuario.getName() + "', " + usuario.getKw_hora() + ", " + usuario.getValorLimite() + ", 0)";
            this.bancoDeDados.execSQL(sqlCmd);
            cursor.close();
            return true;
        }
        catch (SQLException e){
            Log.e("ABC",e.getMessage());
            return false;
        }
    }

    public double consultarConsumo(int user_id, String ano, String mes){
        double somaConsumo = 0.0;

        try {
            String sqlQuery = "SELECT SUM(watts) " +
                    "FROM consumo WHERE data_hora >= '" + ano +
                    "-" + mes + "-01 00:00:00' AND data_hora < " +
                    "datetime('now','start of month','+1 month','-1 day')";
            Log.d("ABC",sqlQuery);
            Cursor cursor = this.bancoDeDados.rawQuery(sqlQuery, null);
            Log.d("ABC", "CURSOR ABERTO");
            if (cursor.moveToNext()) {
                somaConsumo = cursor.getDouble(0);
                Log.d("ABC",Double.toString(somaConsumo));
                /*
                usuario = new Usuario(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getDouble(4),
                        cursor.getDouble(5));
                 */
            }
            cursor.close();
        } catch (SQLException e){
            Log.d("ABC",e.getMessage());
        }
        return somaConsumo;
    }

    public boolean cadastrarConsumo(int user_id, double watts){
        try{
            String sqlCmd = "INSERT INTO Consumo VALUES (" + user_id + ", datetime('now','localtime'), " +
                    watts + ")";
            Log.d("ABC",sqlCmd);
            this.bancoDeDados.execSQL(sqlCmd);
            return true;
        }
        catch (SQLException e){
            Log.d("ABC",e.getMessage());
            return false;
        }
    }

    public boolean atualizarTempo(int user_id, int tempo){
        try{
            String sqlCmd = "UPDATE Usuarios SET tempo_segundos = tempo_segundos + " + tempo + " WHERE id = " +
                    user_id;
            Log.d("ABC",sqlCmd);
            this.bancoDeDados.execSQL(sqlCmd);
            return true;
        }
        catch (SQLException e){
            Log.d("ABC",e.getMessage());
            return false;
        }
    }


}
