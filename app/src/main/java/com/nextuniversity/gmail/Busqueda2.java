package com.nextuniversity.gmail;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Busqueda2 extends Fragment {

    EditText etDni,etNombres,etApePat,etApeMat;
    Button btnDni,btnNombres;
    String txtDni;
    Activity activity;
    Activity view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_busqueda,container,false);
        //Casteo Botones para agregarle un evento


        btnDni=(Button)view.findViewById(R.id.btnDni);
        btnNombres=(Button)view.findViewById(R.id.btnNombres);


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
        return view;
    }

    public void enviarDni(){
        etDni=(EditText) view.findViewById(R.id.etDni);
        txtDni=etDni.getText().toString();

        if(txtDni.length()<8 || txtDni.length()>8){
            Toast.makeText(getContext(), "Ingrese un Número de DNI Correcto", Toast.LENGTH_SHORT).show();

        }
        else{
            Intent i =new Intent(getContext(),PorDNI.class);
            i.putExtra("dni",txtDni);
            startActivity(i);
        }




    }

    public void enviarNombres(){

    }



}
