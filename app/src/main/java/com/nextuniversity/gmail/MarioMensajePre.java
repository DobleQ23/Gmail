package com.nextuniversity.gmail;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class MarioMensajePre extends AppCompatActivity {
    //Declaracion de atributos

    String BASE_URL = "http://difo.policia.gob.pe/proyecto_maguy/";
    private Bitmap bitmap = null;
    private File archivo;
    private String asunto;
    private String contenido;
    private String ruta;
    private Bitmap fotoBitmap;
    private String id_user;
    private String latitud;
    private String longitud;
    public RelativeLayout milayout;
    public EditText et1, et2;
    public ImageView imagen;
    public Button btn_fin;
    private Timer timer;

    ArrayList arregloRuta;
    int factor;
    int repetir =0;
    String subirphp = "/uploadData.php";
    String id_notas ="";
    TextView chencho,chencho2;

    //======================Variables de encriptacion================================
    Button btnEncriptar;
    String cadenaEncriptada,asuntoEncriptada;
    char recorrerCadenaContenido,recorrerCadenaAsunto;
    String convertHX,convertHX2;
    int recuperaAscii,recuperaAscii2;
    int potencia,potencia2;
    String enviarDbContenido,enviarDbAsunto;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mario_mensaje_pre);
        et1 = (EditText) findViewById(R.id.editText5);
        et2 = (EditText) findViewById(R.id.editText6);
        imagen = (ImageView) findViewById(R.id.imageView2);
        btn_fin = (Button) findViewById(R.id.button4);
        milayout = (RelativeLayout) findViewById(R.id.r_layout2);
        chencho=(TextView)findViewById(R.id.txtOri);
        chencho2=(TextView)findViewById(R.id.txtOri2);

        //Recibiendo datos del intent
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null) {
            asunto = (String) b.get("asunto");
            factor=(int) b.get("factor");
            contenido = (String) b.get("contenido");
            //ruta = (String) b.get("ruta");
            id_user = (String) b.get("id");
            latitud = (String) b.get("latitud");
            longitud = (String) b.get("longitud");
            arregloRuta = (ArrayList) b.getStringArrayList("ruta");
            //ruta = Total.arrayListBitmap.get(0);

            ruta = (arregloRuta.get(0)).toString();
            imagen.setImageBitmap((Bitmap) Total.arrayListBitmap.get(0));

        }

        et1.setText(asunto);
        et2.setText(contenido);

        archivo = new File(ruta);

        if(Total.foto==0) {
            Uri uri = Total.datafoto.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imagen.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            ContentResolver cr = this.getContentResolver();
            try {
                bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, Uri.fromFile(archivo));
            } catch (IOException e) {
                e.printStackTrace();
            }
            imagen.setImageBitmap(bitmap);

        }

        btnEncriptar=(Button)findViewById(R.id.button4);
        btnEncriptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //====================Metodo de encriptacion Contenido===============================
                cadenaEncriptada=et2.getText().toString();
                for (int i=0;i<cadenaEncriptada.length();i++){
                    recorrerCadenaContenido=cadenaEncriptada.charAt(i);
                    recuperaAscii=(int)recorrerCadenaContenido;
                    potencia= (int) Math.pow(recuperaAscii,2);
                    convertHX=Integer.toHexString(potencia);
                    if (convertHX.length()==3){
                        chencho.append("0"+convertHX);
                    }else {
                        chencho.append(convertHX);
                    }
                    enviarDbContenido=chencho.getText().toString();
                }

                //==================Metodo de encriptacion Asunto=====================================
                asuntoEncriptada=et1.getText().toString();
                for (int x=0;x<asuntoEncriptada.length();x++){
                    recorrerCadenaAsunto=asuntoEncriptada.charAt(x);
                    recuperaAscii2=(int)recorrerCadenaAsunto;
                    potencia2=(int)Math.pow(recuperaAscii2,2);
                    convertHX2=Integer.toHexString(potencia2);
                    if (convertHX2.length()==3){
                        chencho2.append("0"+convertHX2);
                    }else{
                        chencho2.append(convertHX2);
                    }
                    enviarDbAsunto=chencho2.getText().toString();
                }

                ruta = (arregloRuta.get(0)).toString();
                archivo = new File(ruta);
                uploadData(archivo,arregloRuta.size());
            }
        });
    }

    //==========Inactividad================================
    @Override
    protected void onPause() {
        super.onPause();
        timer = new Timer();
        LogOutTimerTask logoutTimeTask = new LogOutTimerTask();
        timer.schedule(logoutTimeTask, 20000); //Delay=Tiempo de inactividad
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
        public void run()
        {
            finish();
        }
    }

    void subirotras(){
        subirphp = "/uploadData0.php";
        for(int i=1; i<=arregloRuta.size()-1;i++)

        {
            ruta = (arregloRuta.get(i)).toString();
            archivo = new File(ruta);
            // Toast.makeText(this,archivo+"",Toast.LENGTH_LONG).show();

            uploadData(archivo,arregloRuta.size());

        }
    }


    private void uploadData(File archivoSubir,final int repeticiones) {
        final ProgressDialog progreso = new ProgressDialog(MarioMensajePre.this);
        progreso.setMessage("Subiendo Datos ...");
        progreso.show();
        AsyncHttpClient cliente = new AsyncHttpClient();
        RequestParams parametros = new RequestParams();


        parametros.put("asunto", enviarDbAsunto);
        parametros.put("contenido",enviarDbContenido);
        parametros.put("latitud",latitud);
        parametros.put("longitud",longitud);
        parametros.put("id_user", id_user);
        parametros.put("factor",factor);
        parametros.put("id_notas", id_notas);

        try {
            parametros.put("imagen", archivoSubir);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        cliente.post(getApplicationContext(), BASE_URL + subirphp, parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    progreso.dismiss();
                    String respuesta = new String(responseBody);
                    Toast.makeText(getApplicationContext(), "Genial!!! Pasaste de nivel", Toast.LENGTH_SHORT).show();
                    Log.d("respuesta ", respuesta);
                    if (repetir==0) {
                        id_notas = respuesta;
                        //Toast.makeText(getApplicationContext(),id_notas+"",Toast.LENGTH_LONG).show();

                        subirotras();
                    }
                    repetir++;
                    if(repetir == repeticiones){
                        refresh();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Algo paso :( Sigue Intentando...." +
                        ".", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refresh() {
        Intent intentvolver = new Intent(this, MarioMensaje.class);
        intentvolver.putExtra("id",id_user);
        startActivity(intentvolver);
        finish();
    }
}