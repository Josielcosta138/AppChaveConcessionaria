package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {
        ///DECLARAÇAO DOS OBJETOS

        private EditText nome,email,senha,confirma_senha;
    private Button btn_registrar;
    private ProgressBar loading;
    private static String URL_Registro = "http://192.168.137.1/appLogin/registro.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///CONEXÃO DOS COMPONENTES COM A CLASSE R

        nome = findViewById(R.id.edt_nome);
        email = findViewById(R.id.edit_email);
        senha = findViewById(R.id.edit_senha);
        confirma_senha = findViewById(R.id.confirma_senha);
        loading = findViewById(R.id.loading);

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registro();

            }
        });

    }

    ///DECLACAÇÃO METRODO REGISTRO

    private void Registro() {
        loading.setVisibility(View.VISIBLE);
        btn_registrar.setVisibility(View.GONE);
        final String nome = this.nome.getText().toString().trim(); // TRIM tira os espaços onde o usuario digita no Banco
        final String email = this.email.getText().toString().trim();
        final String senha = this.senha.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Registro, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ///TRY verificaçao de erro
                try {
                    JSONObject jsonObject = new JSONObject(response);    /// JSON retorna resposta do BANCO PHP
                    String sucess = jsonObject.getString("sucess");  /// EQUALS ( FUNÇÃO DE COMPARAÇÃO )
                    if (sucess.equals("1")) {
                        Toast.makeText(RegistroActivity.this, "Registro com Sucesso", Toast.LENGTH_SHORT).show(); /// TOAST MENSAGEM RAPIDA NO RODAPÉ DO APP
                    }
                } catch (JSONException e) {
                    e.printStackTrace(); /// MENSANGEM DE ERRO
                    Toast.makeText(RegistroActivity.this, "Erro!" + e.toString(), Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    btn_registrar.setVisibility(View.VISIBLE);

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegistroActivity.this, "Erro!" + error.toString(), Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
                btn_registrar.setVisibility(View.VISIBLE);

            }
        })
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();
            params.put("nome", nome);
            params.put("email", email);
            params.put("senha", senha);
            return super.getParams();
             }
        };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);


    }
}
