package com.nextuniversity.gmail;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Busqueda extends AppCompatActivity {

    EditText etDni,etNombres,etApePat,etApeMat;
    Button btnDni,btnNombres;
    String txtDni;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);
        //Casteo Botones para agregarle un evento
        btnDni=(Button)findViewById(R.id.btnDni);
        btnNombres=(Button)findViewById(R.id.btnNombres);


        btnDni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarDni();
            }
        });

        btnNombres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarNombres();
            }
        });

    }

    public void enviarDni(){
        etDni=(EditText)findViewById(R.id.etDni);
        txtDni=etDni.getText().toString();

        if(txtDni.length()<8 || txtDni.length()>8){
            Toast.makeText(this, "Ingrese un Número de DNI Correcto", Toast.LENGTH_SHORT).show();

        }
        else{
            Intent i =new Intent(this,PorDNI.class);
            i.putExtra("dni",txtDni);
            startActivity(i);
        }




    }

    public void enviarNombres(){

    }



}