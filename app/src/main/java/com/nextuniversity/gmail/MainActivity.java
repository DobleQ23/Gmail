package com.nextuniversity.gmail;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;


    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //CAMBIAR EL ACTIONBAR POR TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        FloatingActionButton newMessage = (FloatingActionButton) findViewById(R.id.new_message);
        if (newMessage != null) {
            newMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Intentas crear un nuevo mensaje.", Snackbar.LENGTH_LONG).show();
                }
            });
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toogle);
        toogle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){



                case R.id.nav_inbox:
                    Intent nav_inbox= new Intent(this,MarioMensaje.class);
                    startActivity(nav_inbox);
                    break;
                case R.id.nav_social:
                    Intent nav_social= new Intent(this,LocalizadorLogin.class);
                    startActivity(nav_social);
                    break;

                case R.id.nav_promotions:
                    Intent nav_promotions= new Intent(this,Reporte.class);
                    startActivity(nav_promotions);
                    break;


                case R.id.nav_updates:
                    Intent nav_updates= new Intent(this,Busqueda.class);
                    startActivity(nav_updates);

                    break;

                case R.id.nav_promotions1:
                    Intent nav_updates1= new Intent(this,Reporte.class);
                    startActivity(nav_updates1);

                    break;

                case R.id.nav_inbox1:
                    Intent nav_inbox1= new Intent(this,Mensajes.class);
                    startActivity(nav_inbox1);
                break;
                default:

                    //OTROS ITEMS DEL NAVIGATION DRAWER
                    break;
            }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
