package com.nextuniversity.gmail;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MarioLogin extends AppCompatActivity {
    //Definimos Atributos

    String BASE_URL = "http://difo.policia.gob.pe/proyecto_maguy/";
    EditText et_user,et_pass;
    Button boton_ingresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mario_login);
        //Castear lo elementos del layout
        //user = (TextView) findViewById(R.id.textView);
        //pass = (TextView) findViewById(R.id.textView2);
        et_user = (EditText) findViewById(R.id.editText);
        et_pass = (EditText) findViewById(R.id.editText2);
        //editText.setHintTextColor(getResources().getColor(R.color.white));
        boton_ingresar = (Button) findViewById(R.id.button);
        boton_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarPhp();
                et_user.setText("");
                et_pass.setText("");
            }
        });

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();


            if (getIntent().getBooleanExtra("atras",true)){
                et_user.setText("");
                et_pass.setText("");
            }
        }
    }

    private void validarPhp() {
        //Creamos un Cliente HTTP
        AsyncHttpClient cliente = new AsyncHttpClient();
        //Establecemos usuario y contraseña como parametros POST
        RequestParams parametros = new RequestParams();
        String usuario = et_user.getText().toString();
        String password = et_pass.getText().toString();
        parametros.put("user",usuario);
        parametros.put("pass",password);
        //Ejecutamos la peticion HTTP
        cliente.post(getApplicationContext(), BASE_URL + "/validarLogin.php", parametros, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200)
                {
                    try {
                        JSONArray jsarray = new JSONArray(new String(responseBody));
                        if (jsarray.length()>0)
                        {
                            Toast.makeText(getApplicationContext(), "¡¡BIENVENIDO!!", Toast.LENGTH_SHORT).show();
                            JSONObject registro = jsarray.getJSONObject(0);
                            String id = registro.getString("idusuarios");
                            Total.idusuarios = id;
                            siguienteIntent(id);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Datos Invalidos", Toast.LENGTH_SHORT).show();
                            Intent i =new Intent(getApplicationContext(),MarioMensaje.class);
                            startService(i);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Error al intentar conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void siguienteIntent(String id) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }


}
