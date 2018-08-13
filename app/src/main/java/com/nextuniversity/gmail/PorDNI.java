package com.nextuniversity.gmail;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import com.nextuniversity.gmail.Clases.Popup;

public class PorDNI extends Activity {


    TextView tvNombre,tvDni,tvApePat,tvApeMat,tvEstCivil,tvGradoIns,tvFechNac,tvDepNac,tvProNac,tvDisNac,tvDepDom,tvProDom,tvDisDom,tvDirDom;
    String url;
    public static String getDni;
    AsyncHttpClient client;
    RequestParams params;
    String nom,dni,apePat,apeMat,estCivil,gradoIns,fechNac,depNac,proNac,disNac,depDom,proDom,disDom,dirDom;
    Bundle bn;
    ImageView imgReniec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_por_dni);

        recuperarDniReniec();
        popup();

    }
    public void recuperarDniReniec(){

        final ProgressDialog progreso =new ProgressDialog(PorDNI.this);
        progreso.setMessage("Recuperando Datos...");
        progreso.show();
        imgReniec=(ImageView)findViewById(R.id.imgReniec);

        //Casteo de TextViews
        tvNombre=(TextView)findViewById(R.id.tvNom);
        tvDni=(TextView)findViewById(R.id.tvDni);
        tvApePat=(TextView)findViewById(R.id.tvApePat);
        tvApeMat=(TextView)findViewById(R.id.tvApeMat);
        tvEstCivil=(TextView)findViewById(R.id.tvEstCivil);
        tvGradoIns=(TextView)findViewById(R.id.tvGradoIns);
        tvFechNac=(TextView)findViewById(R.id.tvFecNac);
        tvDepNac=(TextView)findViewById(R.id.tvDepNac);
        tvProNac=(TextView) findViewById(R.id.tvProNac);
        tvDisNac=(TextView)findViewById(R.id.tvDisNac);
        tvDepDom=(TextView)findViewById(R.id.tvDepDom);
        tvProDom=(TextView)findViewById(R.id.tvProDom);
        tvDisDom=(TextView)findViewById(R.id.tvDisDom);
        tvDirDom=(TextView)findViewById(R.id.tvDirDom);

        //Recuperando datos de busqueda
        bn=getIntent().getExtras();
        if(bn!=null){
            getDni=bn.getString("dni");
        }else{
            Log.d("fallo::::","no pasa variable");
        }

        url="http://difo.policia.gob.pe/appPapAndroid/servicio_appReniec.php";
        client =new AsyncHttpClient();
        params =new RequestParams();
        params.put("dni",getDni);

        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                //TODO:Recuperando un ObjectJson debido que el Json del php solo tiene un valor

                String jsonResult=new String(responseBody);
                try {
                    JSONObject json=new JSONObject(jsonResult);

                    //almacenamos el JsonObject en String
                    nom=json.getString("nombres");
                    dni=json.getString("dni");
                    apePat=json.getString("paterno");
                    apeMat=json.getString("materno");
                    estCivil=json.getString("estadoCivil");
                    gradoIns=json.getString("gradoInstruccion");
                    fechNac=json.getString("fechaNacimiento");
                    depNac=json.getString("departamentoNacimiento");
                    proNac=json.getString("provinciaNacimiento");
                    disNac=json.getString("distritoNacimiento");
                    depDom=json.getString("departamentoDomicilio");
                    proDom=json.getString("provinciaDomicilio");
                    disDom=json.getString("distritoDomicilio");
                    dirDom=json.getString("direccionDomicilio");




                    //Asignamos lo recuperado a los TextView
                    tvNombre.setText(nom);
                    tvDni.setText(dni);
                    tvApePat.setText(apePat);
                    tvApeMat.setText(apeMat);
                    tvEstCivil.setText(estCivil);
                    tvGradoIns.setText(gradoIns);
                    tvFechNac.setText(fechNac);
                    tvDepNac.setText(depNac);
                    tvProNac.setText(proNac);
                    tvDisNac.setText(disNac);
                    tvDepDom.setText(depDom);
                    tvProDom.setText(proDom);
                    tvDisDom.setText(disDom);
                    tvDirDom.setText(dirDom);

                    Picasso.get().load("http://difo.policia.gob.pe/imagenes_reniec/"+getDni+".jpg").into(imgReniec);
                    progreso.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("fallo","fallo");
            }
        });

    }
    public void popup(){
        imgReniec=(ImageView)findViewById(R.id.imgReniec);
        imgReniec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PorDNI.this,Popup.class));

            }
        });
    }



}
