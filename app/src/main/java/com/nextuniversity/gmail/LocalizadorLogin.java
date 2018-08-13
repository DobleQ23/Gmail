package com.nextuniversity.gmail;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class LocalizadorLogin extends AppCompatActivity {

    EditText etCip,etCel;
    String cip,cel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizador_login);




        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                recuperarDatos();
            }
        });
    }

    public void recuperarDatos(){
        //En este metodo recupera los datos con el bundle
        etCip=(EditText)findViewById(R.id.txtCip);
        etCel=(EditText)findViewById(R.id.txtCel);

        cip=etCip.getText().toString();
        cel=etCel.getText().toString();

        Intent i =new Intent(this,LocalizadorBotones.class);
        i.putExtra("cip",cip);
        i.putExtra("cel",cel);
        startActivity(i);


    }



}