package com.smartenergy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.smartenergy.database.UsuarioDAO;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void fazerLogin(View view){
        Intent intent = new Intent(this, ConsumoActivity.class);

        EditText editTextLogin = (EditText) findViewById(R.id.textLogin);
        EditText editTextSenha = (EditText) findViewById(R.id.textSenha);

        String login = editTextLogin.getText().toString();
        String senha = editTextSenha.getText().toString();

        UsuarioDAO usuarioDAO = new UsuarioDAO(this);
        Usuario usuario = usuarioDAO.fazerLogin(login, senha);

        if(usuario != null){
            intent.putExtra("usuario", usuario);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Usuário e/ou senha inválidos!",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void fazerCadastro(View view){
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }
}
