package com.example.subprogramamalaria.proyectomalaria;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Subprograma Malaria on 14/07/2015.
 */
public class Menu extends ActionBarActivity {

    //Declaramos las Variables
    ListViewAdapter adapter;
    String[] menu = {"Notificar Caso", "Mini Bress"};
    int[] img = {R.drawable.icono1, R.drawable.icono3};

    Button btn_close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);

        //Asigna el user ingresa a la aplicacion
        TextView txt_user = (TextView)findViewById(R.id.last_user);
        final String lastuser = getIntent().getStringExtra("last_user");
        //Muestra el Usuario que ingreso
        txt_user.setText("Usuario: "+lastuser);


        final ListView lista = (ListView) findViewById(R.id.listView);
        adapter = new ListViewAdapter(this, menu, img);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int posicion, long l) {
               //Toast.makeText(getApplicationContext(), "presiono " + i, Toast.LENGTH_SHORT).show();

                switch (posicion){
                    case 0 :
                       Intent i = new Intent(getApplicationContext(), form_crear_caso.class);
                        //Envia el Usuario que ingresa
                        i.putExtra("usuario",lastuser);
                        startActivity(i);
                        break;
                    case 1:
                        i = new Intent(getApplicationContext(), form_mini_bress.class);
                        //Envia el Usuario que ingresa
                        i.putExtra("usuario",lastuser);
                        startActivity(i);
                        break;
                }
            }
        });

        //Inicalizamos el Botón Cerrar
        btn_close = (Button) findViewById(R.id.btnClose);

    }

    public void onclicBtnClose (View view){
        //Source for close app
        finish();
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
    }
}