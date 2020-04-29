package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.media.MediaCas;
import android.media.session.MediaSessionManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName(); // pega informações dessa classe
    private TextView nome, email; //cria os objetos
    private Button btn_logout;
    String getId;
    SessionManager sessionManeger;
    private static String URL_READ = "http://192.168.137.1/applogin/ler_detalhe.php";
    private static String URL_EDIT = "http://192.168.137.1/applogin/editar_detalhe.php";
    private Menu action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //link com a classe R
        nome = findViewById(R.id.edt_nome_home);
        email = findViewById(R.id.edt_email_home);
        btn_logout = findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManeger.Logout();
            }
        });

        HashMap<String, String> user = sessionManeger.getUserDetail();
        getId = user.get(sessionManeger.ID);

    }//oncreate

    private void getUserDetail(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Carregando informações...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("sucess");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");
                            if (sucess.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String strNome = object.getString("nome").trim();
                                    String strEmail = object.getString("email").trim();

                                    nome.setText(strNome);
                                    email.setText(strEmail);
                                }//for
                            }//if
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }//catch
                    }//onresponse
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(HomeActivity.this,"Erro lendo informações "+
                        error.toString(),Toast.LENGTH_SHORT).show();
            }//onError
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", getId);
                return params;
            }
        };//StringRequest

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest); //aqui passa todas as informaçôes para leitura e tratamento Json

    }//fim getUserDetail

}// class


