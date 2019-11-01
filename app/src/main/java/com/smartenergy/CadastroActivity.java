package com.smartenergy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.smartenergy.database.UsuarioDAO;

public class CadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Fazer cadastro");
        setContentView(R.layout.activity_cadastro);
    }

    public void confirmarCadastro(View view){
        EditText editTextNome = (EditText) findViewById(R.id.editTextNome);
        EditText editTextLogin = (EditText) findViewById(R.id.editTextLogin);
        EditText editTextSenha = (EditText) findViewById(R.id.editTextSenha);
        EditText editTextConfSenha = (EditText) findViewById(R.id.editTextConfSenha);
        EditText editTextKwH = (EditText) findViewById(R.id.editTextKwH);
        EditText editTextValorLim = (EditText) findViewById(R.id.editTextValorLim);

        String nome = editTextNome.getText().toString();
        String login = editTextLogin.getText().toString();
        String senha = editTextSenha.getText().toString();
        String confSenha = editTextConfSenha.getText().toString();

        UsuarioDAO usuarioDAO = new UsuarioDAO(this);
        Usuario usuario;
        if(nome.equals("") || login.equals("") || senha.equals("") || editTextKwH.getText().toString().equals("")
        || editTextValorLim.getText().equals("")){
            Toast.makeText(this, "Não podem haver campos vazios!", Toast.LENGTH_LONG).show();
        }
        else{
            if(!senha.equals(confSenha)) {
                Toast.makeText(this, "Senhas não conferem!", Toast.LENGTH_LONG).show();
            }
            else{
                double kwH = Double.parseDouble(editTextKwH.getText().toString());
                double valorLim = Double.parseDouble(editTextValorLim.getText().toString());

                usuario = new Usuario(0, login, senha, nome, kwH, valorLim);
                if(usuarioDAO.fazerCadastro(usuario)){
                    Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(this, "Login já existente!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
