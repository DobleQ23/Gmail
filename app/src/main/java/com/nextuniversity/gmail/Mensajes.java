package com.nextuniversity.gmail;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;


public class Mensajes extends AppCompatActivity {
    ArrayList id = new ArrayList();
    ArrayList contenido= new ArrayList();
    ArrayList factor= new ArrayList();
    ArrayList id_usuario= new ArrayList();
    ArrayList fechahora= new ArrayList();
    Calendar calendar;


    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);
        listView=(ListView) findViewById(R.id.listView);


        String url = "http://192.168.0.79/recomendaciones/visualizacion.php";
        AsyncHttpClient cliente = new AsyncHttpClient();
        RequestParams parametros = new RequestParams();

        cliente.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    JSONArray jsonArray=new JSONArray(new String(responseBody));

                    for(int p = 0; p < jsonArray.length(); p++){
                        id.add(jsonArray.getJSONObject(p).getString("id"));
                        contenido.add(jsonArray.getJSONObject(p).getString("contenido"));
                        factor.add(jsonArray.getJSONObject(p).getString("factor"));
                        id_usuario.add(jsonArray.getJSONObject(p).getString("id_usuario"));
                        fechahora.add(jsonArray.getJSONObject(p).getString("fechahora"));
                        listView.setAdapter(new adaptadorLista6(getApplicationContext()));
                        listView.setDividerHeight(30);
                        //===============inicio  popup======================
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    //===================INICIO CLASE DEL ADAPTADOR DE LISTVIEW======================
    private class adaptadorLista6 extends BaseAdapter {
        Context ctx;
        LayoutInflater layoutInflater;
        TextView TVId, TVContenido,TVId_usuario,TVFechahora;

        public adaptadorLista6(Context applicationContext) {
            this.ctx=applicationContext;
            layoutInflater = (LayoutInflater)ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return id.size();
        }

        @Override
        public Object getItem(int position) {
            return position;

        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewGroup viewGroup=(ViewGroup)layoutInflater.inflate(R.layout.mensajesitems,null);
            TVContenido = (TextView)viewGroup.findViewById(R.id.tvContenido);
            TVContenido.setText(contenido.get(position).toString());
            TVId = (TextView)viewGroup.findViewById(R.id.tvId);
            TVId.setText(id.get(position).toString());
            TVId_usuario = (TextView)viewGroup.findViewById(R.id.tvId_usuario);
            TVId_usuario.setText(id_usuario.get(position).toString());
            TVFechahora = (TextView)viewGroup.findViewById(R.id.tvFechahora);
            TVFechahora.setText(fechahora.get(position).toString());

            return viewGroup;
        }
    }
//===================FIN CLASE DEL ADAPTADOR DEL LISTVIEW======================
}
