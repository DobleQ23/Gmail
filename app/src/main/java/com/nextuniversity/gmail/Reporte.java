package com.nextuniversity.gmail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;


public class Reporte extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] SpinnerValores = {"SELECCIONA EVENTO :","MARCHA ANTICORRUPCION","MARCHA FIESTRAS PATRIAS","MARCHA CONTRA LA CONTAMINACION","MARCHA CONTRA LA VIOLENCIA A LA MUJER"};
    public static Spinner spinner;
    public static String klevert;
    public String latitud;
    public String longitud;
    private int id_user= 123123 ;
    String Date;

    public String contenido;
    public Button btn2;
    public int prueba;
    public Timer timer;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);
        calendar= Calendar.getInstance();
        simpleDateFormat= new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date=simpleDateFormat.format(calendar.getTime());

        spinner=(Spinner) findViewById(R.id.spinnerFactor);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Reporte.this, android.R.layout.simple_list_item_1, SpinnerValores);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        //=================================================================================================


        btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                contenido = ((EditText) findViewById(R.id.eT)).getText().toString();
                String url="http://192.168.0.79/recomendaciones/insertarReporte.php";
                AsyncHttpClient cliente =new AsyncHttpClient();
                RequestParams parametros=new RequestParams();

                parametros.put("latitud",latitud);
                parametros.put("longitud",longitud);
                parametros.put("contenido",contenido);
                parametros.put("factor",prueba);
                parametros.put("id_usuario",id_user);
                parametros.put("fechahora",Date);
                Toast.makeText(getApplicationContext(),contenido+"----"+prueba+"-----"+Date+"-----"+id_user, Toast.LENGTH_LONG).show();

                refresh();
                cliente.get(url,parametros,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                        if(jsonArray.length()!=0){
                            for (int o = 0; o <= jsonArray.length() ; o++) {
                                JSONObject jsonObject = null;

                                try {
                                    jsonObject = jsonArray.getJSONObject(o);
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("contenido", jsonObject.getString("contenido"));
                                    map.put("factor",jsonObject.getString("factor"));
                                    map.put("id_usuario",jsonObject.getString("id_usuario"));
                                    map.put("latitud",jsonObject.getString("latitud"));
                                    map.put("longitud",jsonObject.getString("longitud"));
                                    map.put("fechahora",jsonObject.getString("fechahora"));

                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), "El correo o la contraseña son incorrectos", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "El correo o la contraseña son incorrectos", Toast.LENGTH_SHORT).show();
                        }

                    }
                    public void onFailure(Throwable arg) {
                        Toast.makeText(getApplicationContext(), "Error al intentar validar", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void refresh() {
        Intent intentvolver = new Intent(this, Reporte.class);
        intentvolver.putExtra("id_usuario",id_user);
        startActivity(intentvolver);
        finish();
    }


    //=================================================================================================
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView texto =(TextView) view;
        klevert=texto.getText().toString();

        prueba=spinner.getSelectedItemPosition();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    protected void onPause() {
        super.onPause();
        timer = new Timer();
        Reporte.LogOutTimerTask logoutTimeTask = new Reporte.LogOutTimerTask();
        timer.schedule(logoutTimeTask, 90000);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    private class LogOutTimerTask extends TimerTask {
        @Override
        public void run() {
            finish();

        }
    }

}