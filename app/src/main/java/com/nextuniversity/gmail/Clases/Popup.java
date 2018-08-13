package com.nextuniversity.gmail.Clases;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.nextuniversity.gmail.PorDNI;
import com.squareup.picasso.Picasso;

import com.nextuniversity.gmail.R;
import com.nextuniversity.gmail.PorDNI;

public class Popup extends Activity {

    String getDni= PorDNI.getDni;
    ImageView imgReniec2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        imgReniec2=(ImageView)findViewById(R.id.imgReniec2);

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width =dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int) (width*.8),(int)(height*.6));

        Picasso.get().load("http://difo.policia.gob.pe/imagenes_reniec/"+getDni+".jpg").into(imgReniec2);


    }
}
