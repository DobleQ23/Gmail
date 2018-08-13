package com.nextuniversity.gmail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MarioMensaje extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,AdapterView.OnItemSelectedListener {
    //Declaracion de Atributos
    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";
    private final int MY_PERMISSIONS = 100;
    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;
    private String ruta = "";
    private Uri path;
    private File archivo;
    private Bitmap bitmap = null;
    private String id_user;
    ArrayList arrayList;
    Intent intent;


    String [] SpinnerValores = {"SELECCIONA NIVEL :","MUY CRITICA"," SIN ORDEN","CONTRATODO","SEGURO (C)","MUY ESPECIAL"};
    public static Spinner spinner;
    public static String klevert;



    //Localizacion
    private static final String LOGTAG = "android-localizacion";
    private GoogleApiClient apiClient;
    public String latitud;
    public String longitud;

    public LinearLayout miLayout;
    public LinearLayout linearImage;
    LinearLayout.LayoutParams params;
    int item = 0;

    public EditText et1, et2;
    public Button btn1, btn2;
    //public ImageView imagen;
    public TextView txt1, txt2;
    public int prueba;
    //mirutaview;

    //Inactividad
    public Timer timer;


    @SuppressLint("WrongViewCast")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mario_mensaje);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());



        //Castear elementos del layout
        txt1 = (TextView) findViewById(R.id.textView3);
        txt2 = (TextView) findViewById(R.id.textView4);


        //mirutaview = (TextView) findViewById(R.id.ruta);
        et1 = (EditText) findViewById(R.id.editText3);
        et2 = (EditText) findViewById(R.id.editText4);
        btn1 = (Button) findViewById(R.id.button2);
        btn2 = (Button) findViewById(R.id.button3);
        //imagen = (ImageView) findViewById(R.id.imageView);
       //
        //
        //
        // miLayout = (LinearLayout) findViewById(R.id.r_layout2);
        //mirutaview.setText("Archivo seleccionado: Ninguno");



        spinner=(Spinner) findViewById(R.id.spinnerFactor);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MarioMensaje.this, android.R.layout.simple_list_item_1, SpinnerValores);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //--------------Servicio de Google-----------------------
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        linearImage = (LinearLayout)findViewById(R.id.linearImage);
        params = new LinearLayout.LayoutParams(
                200,
                400);

        arrayList = new ArrayList();
        Total.arrayListBitmap = new ArrayList<Bitmap>();


        path = Uri.parse("");
        Intent intentx = getIntent();
        Bundle b = intentx.getExtras();
        if(b!=null)
        {
            id_user = (String) b.get("id");
        }

        if(mayRequestStoragePermission())
        {
            btn1.setEnabled(true);
        }
        else
        {
            btn1.setEnabled(false);
        }
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptions();

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siguienteIntent();

            }
        });
    }

    private void siguienteIntent() {
        intent = new Intent(this, MarioMensajePre.class);
        intent.putExtra("asunto",et1.getText().toString());
        intent.putExtra("contenido", et2.getText().toString());
        intent.putExtra("latitud",latitud);
        intent.putExtra("longitud",longitud);
        intent.putExtra("ruta",arrayList);
        intent.putExtra("id",id_user);
        intent.putExtra("factor",prueba);

        startActivity(intent);

    }


    private void showOptions() {
        final CharSequence[] option = {"Tomar Foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(MarioMensaje.this);
        builder.setTitle("Elige una opcion");
        builder.setItems(option, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                if(option[which]=="Tomar Foto")
                {
                    File carpeta =  new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
                    boolean isDirectoryCreated = carpeta.exists();
                    if(!isDirectoryCreated)
                    {
                        isDirectoryCreated = carpeta.mkdirs();
                    }
                    if(isDirectoryCreated)
                    {
                        Long timestamp = System.currentTimeMillis() / 1000;
                        String imageName = timestamp.toString()+".jpg";
                        ruta = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY + File.separator + imageName;
                        Log.d("ruta imagennnnnnn", ruta);
                        archivo = new File(ruta);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(archivo));
                        startActivityForResult(intent,PHOTO_CODE);
                    }
                }
                else if(option[which]=="Elegir de Galeria")
                {
                    Intent intent =  new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent,"Selecciona app de imagen"), SELECT_PICTURE);
                }
                else

                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("file_path",ruta);
    }

    @Override
    public void onRestoreInstanceState(Bundle saveInstanceState){
        super.onRestoreInstanceState(saveInstanceState);
        ruta = saveInstanceState.getString("file_path");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            ImageView img = new ImageView(getApplicationContext());
            img.setId(100000+item);
            item++;
            img.setLayoutParams(params);
            linearImage.addView(img);

            //Eliminar una imagende las obtenidas
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linearImage.removeView(v);
                    Bitmap bitmapimg = ((BitmapDrawable)((ImageView)v).getDrawable()).getBitmap();
                    int pos = Total.arrayListBitmap.indexOf(bitmapimg);
                    arrayList.remove(pos);
                    Total.arrayListBitmap.remove(bitmapimg);
                }
            });

            switch (requestCode) {
                case PHOTO_CODE:
                    Total.foto=1;
                    MediaScannerConnection.scanFile(this, new String[]{ruta}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String s, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + s + ":");
                            Log.i("ExternalStorage", "-> Uri = " + uri);
                        }
                    });
                    ContentResolver cr = this.getContentResolver();
                    try {
                        bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, Uri.fromFile(archivo));
                        int rotate = 0;
                        ExifInterface exif = new ExifInterface(archivo.getAbsolutePath());
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotate = 90;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotate = 180;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotate = 270;
                                break;
                        }
                        Matrix matriz = new Matrix();
                        matriz.postRotate(rotate);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matriz, true);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //mirutaview.setText("Archivo seleccionado: "+ruta);
                    //imagen.setImageBitmap(bitmap);


                    //img.setImageResource(R.drawable.pnplogo1);
                    img.setImageBitmap(bitmap);


                    Total.arrayListBitmap.add(bitmap);
                    arrayList.add(ruta);


                    break;
                case SELECT_PICTURE:
                    Total.foto=0;



                    Total.datafoto = data;
                    Uri uri = Total.datafoto.getData();


                    //lanza la ruta de la ubicacion del archivo
                    ruta = getRealPathFromURI(uri);



                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        img.setImageBitmap(bitmap);

                        Total.arrayListBitmap.add(bitmap);
                        arrayList.add(ruta);


                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    break;
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String result;
        Cursor cursor = getApplicationContext().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            result = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int indx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(indx);
            cursor.close();
        }
        return result;
    }


    private boolean mayRequestStoragePermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            return true;
        }
        if((checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
        {
            return true;
        }
        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))||(shouldShowRequestPermissionRationale(CAMERA)))
        {
            Snackbar.make(miLayout, "Los permisos son necesarios para poder usar la aplicacion", Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA},MY_PERMISSIONS);
                }
            }).show();
        }
        else
        {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA},MY_PERMISSIONS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permisions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permisions, grantResults);
        switch (requestCode)
        {
            case MY_PERMISSIONS:
                if((grantResults.length == 2) && (grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED))
                {
                    Toast.makeText(MarioMensaje.this, "Permisos Aceptados", Toast.LENGTH_SHORT).show();
                    btn1.setEnabled(true);
                }
                else
                {
                    showExplanetion();
                }
                break;

        }}

    private void showExplanetion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MarioMensaje.this);
        builder.setTitle("Permisos Denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",getPackageName(),null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        builder.show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Conectado correctamente a Google Play Services
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {

            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);

            if(lastLocation!=null)
            {
                latitud = String.valueOf(lastLocation.getLatitude());
                longitud = String.valueOf(lastLocation.getLongitude());
            }
            else
            {
                latitud = "desconocido";
                longitud = "desconocido";
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Se ha interrumpido la conexión con Google Play Services
        Log.e(LOGTAG, "Se ha interrumpido la conexión con Google Play Services");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Se ha producido un error que no se puede resolver automáticamente
        //y la conexión con los Google Play Services no se ha establecido.
        Log.e(LOGTAG, "Error grave al conectar con Google Play Services");
    }


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
        public void run() {
            finish();

        }
    }

}