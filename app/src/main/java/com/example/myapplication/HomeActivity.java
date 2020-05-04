package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaCas;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName(); // pega informações dessa classe
    private TextView nome, email; //cria os objetos
    private Button btn_logout, btn_editar_photo;
    String getId;
    SessionManager sessionManeger;
    private static String URL_READ = "http://192.168.137.1/applogin/ler_detalhe.php";
    private static String URL_EDIT = "http://192.168.137.1/applogin/editar_detalhe.php";
    private Menu action;
    Bitmap bitmap;
    ImageView imagem_perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //link com a classe R D ARIQUIVO XML DESING COM ESSE ARQUIVO JAVA
        nome = findViewById(R.id.edt_nome_home);
        email = findViewById(R.id.edt_email_home);
        btn_logout = findViewById(R.id.btn_logout);
        btn_editar_photo = findViewById(R.id.btn_editar_foto);
        imagem_perfil = findViewById(R.id.imagem_perfil);
        
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManeger.Logout();
            }
        });

        btn_editar_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escolherFoto();
            }
        });

        HashMap<String, String> user = sessionManeger.getUserDetail();
        getId = user.get(sessionManeger.ID);

    }//oncreate

    private void getUserDetail(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Carregando informações...");
        progressDialog.show();

        ///POST IRA FAZER A CONEXÃO COM O BANCO, ATRAVES DA URL_READ QUE É O IP

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString()); /// ON RESPONSE IRA PEGAR A RESPOOSTA DA STRING

                        try {
                            JSONObject jsonObject = new JSONObject(response);  /// JASON, IRA LER A MENSAGEM VINDA DO BANCO, PEGANDO ATRAVÉS DO GET
                            String sucess = jsonObject.getString("sucess"); /// BUSCA NA CARTA DE SUCESS NO BANCO EM PHP, CONVERTENTO PARA JASON
                            JSONArray jsonArray = jsonObject.getJSONArray("read"); /// AQUI RECEBE DO BANCO O OBJETO READ (email, senha)
                            if (sucess.equals("1")) {
                                /// AQUI IRA VER OQUE TEM DENTRO DA LISTA DO OBJETO READ DO BANCO       (Nome , email)

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i); ///array i = 0  IRA PERCORRER A LISTA Object

                                    String strNome = object.getString("nome").trim(); /// STRING VAI RECEBER NOME DE UM USUARIO NA POSICAO 0
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
                        error.toString(),Toast.LENGTH_SHORT).show(); /// Toast MENSAGEM PEQUENA QUE APARECE PRO USUÁRIO
            }//onError
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>(); /// MAP IRA FAZER O MAPEAMENTO DE DUAS STRING
                params.put("id", getId); ///IRA FAZER UMA LEITURA DO ID
                return params;
            }
        };//StringRequest

        RequestQueue requestQueue = Volley.newRequestQueue(this); /// NOVA REQUESIÇAO
        requestQueue.add(stringRequest); //aqui passa todas as informaçôes para leitura e tratamento Json

    }//fim getUserDetail

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetail();
        // onResume
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_acao, menu);

        action = menu;
        action.findItem(R.id.menu_salvar).setVisible(false);
        return true;
        // return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_editar:
                nome.setFocusableInTouchMode(true);
                email.setFocusableInTouchMode(true);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(nome, InputMethodManager.SHOW_IMPLICIT);

                action.findItem(R.id.menu_editar).setVisible(false);
                action.findItem(R.id.menu_salvar).setVisible(true);

                return  true;
            case R.id.menu_salvar:

                SaveEditDetail();

                action.findItem(R.id.menu_editar).setVisible(true);
                action.findItem(R.id.menu_salvar).setVisible(false);

                nome.setFocusableInTouchMode(false);
                email.setFocusableInTouchMode(false);
                nome.setFocusable(false);
                email.setFocusable(false);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }//switch

    }//onOptionsItemSelected

    private void SaveEditDetail(){

        final String nome = this.nome.getText().toString().trim(); /// TRIM RETIRA OS ESPAÇOS DA DIGITAÇAO DO USUÁRIO
        final  String email = this.email.getText().toString().trim();
        final String id = getId;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Salvando...");
        progressDialog.show();

        //passar as informaçoes para o arquivo PHP e pedier uma resposta dele.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();//DESAPAREÇA MSG
                        // new JSONObject(response) METODO CONSTRUTOR/ RESPONSE VAI RECEBER RESPSTA
                        try {
                            JSONObject jasonObject = new JSONObject(response);
                            String sucess = jasonObject.getString("sucess");

                            if (sucess.equals("1")) {
                                Toast.makeText(HomeActivity.this, "Sucesso!", Toast.LENGTH_SHORT).show();
                                sessionManeger.createSession(nome, email, id);
                            }//if

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(HomeActivity.this, "ERRO AO SALVAR!" + e.toString(), Toast.LENGTH_SHORT).show();
                        }//catch
                    }//onResponse
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(HomeActivity.this, "ERRO AO SALVAR!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }//OnerroResponseListener
                }//o

        )//FIM stringRequest
        {//Parans ou parametros de requisaçao

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nome", nome);
                params.put("email", email);
                params.put("id", id);
                return params;
            }
        };//StringRequest

        RequestQueue requestQueue = Volley.newRequestQueue( this);
        requestQueue.add(stringRequest);

    }//SavaeEdidtDatail

    //escolher foto
   private void escolherFoto(){
       Intent intent = new Intent();
       intent.setType("imagem/*");
       intent.setAction(Intent.ACTION_GET_CONTENT);
       startActivityForResult(Intent.createChooser(intent, "selecione a foto!"), 1);
   }// escolher foto

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if de verificao se o usuario selecionou a imagem e eu certo
        if (requestCode == 1 && resultCode == RESULT_OK  && data != null && data.getData()
         != null){
            Uri filePath = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imagem_perfil.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }// fim do catch
            UploadPicture(getId, getStringImage(bitmap));
            
        }//if
    }//on ActivityResult

    private Object getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream ByteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ByteArrayOutputStream);

        byte[] imageByteArray = ByteArrayOutputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodeImage;

    }//getStringImage
    UploadPicture(getId, getStringImage(bitmap));

}// class


