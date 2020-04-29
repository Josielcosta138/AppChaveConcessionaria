package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NOME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String NOME = "NOME";
    public static final String EMAIL = "EMAIL";
    public static final String ID = "ID";

    ///MÉTODO CONSTRUTOR
    public SessionManager(Context context){
        this.context = context;
        sharedPreferences=context.getSharedPreferences(PREF_NOME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }/// session

    //criar uma sessão
    public void createSession (String nome, String email, String id){
        editor.putBoolean(LOGIN, true);
        editor.putString(NOME, nome);
        editor.putString(EMAIL, email);
        editor.putString(ID, id);
        editor.apply();
    }

    ///
    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN,false);
    }

    ///verifica s está logado
    public void checkLogin(){
        if (!this.isLoggin()){
            Intent i = new Intent(context, LoginActivity.class);   /// Itent i Passar informação de uma classa a outra
            context.startActivity(i);  /// Executa a new Intent i
            ((HomeActivity) context).finish();
        }
    }

    ///retorna as informações do usuário
    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>(); /// nome do HashMap = Usuer
        user.put(NOME, sharedPreferences.getString(NOME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(ID, sharedPreferences.getString(ID, null));
        return user;
    }  /// HashMap.Passar 2 Strings

    ///finaliza sessão faz logout
    public void Logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        ((HomeActivity) context).finish();
    }


}/// final classe
