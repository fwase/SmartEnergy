package com.smartenergy;

import java.io.Serializable;

public class Usuario implements Serializable {
    public static final long serialVersionUID = 1L;
    private int user_id;
    private String login, senha, name;
    private double kw_hora, valor_limite;

    public Usuario(int user_id, String login, String senha, String name, double kw_hora, double valor_limite) {
        this.user_id = user_id;
        this.login = login;
        this.senha = senha;
        this.name = name;
        this.kw_hora = kw_hora;
        this.valor_limite = valor_limite;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int id) {
        this.user_id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getKw_hora() {
        return kw_hora;
    }

    public void setKw_hora(double kw_hora) {
        this.kw_hora = kw_hora;
    }

    public double getValorLimite() {
        return valor_limite;
    }

    public void setValorLimite(double valor_limite) {
        this.valor_limite = valor_limite;
    }
}
