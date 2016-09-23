package com.example.subprogramamalaria.proyectomalaria;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Subprograma Malaria on 31/08/2015.
 */
public class form_confirmar extends ActionBarActivity implements OnClickListener {
    //Declaración de Variables
    private DatePicker fecha1, fecha2;
    private EditText cod_mh, cont_sex_p, cont_asex_p;
    private String res_mh,tipo_plas, esta_para, cont_sex, cont_asex;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //testing on Emulator:
    private static final String CONFIRMAR_URL = "http://proyectomalaria.hol.es/malaria/confirmar.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_confirmar);

        //Muestra las preguntas que estan ocultas
        final LinearLayout mostrar1 = (LinearLayout)findViewById(R.id.tipo_plas_oculto);
        final LinearLayout mostrar2 = (LinearLayout)findViewById(R.id.estadio_oculto);
        final LinearLayout mostrar3 = (LinearLayout)findViewById(R.id.sexual_oculto);
        final LinearLayout mostrar4 = (LinearLayout)findViewById(R.id.asexual_oculto);
        final LinearLayout mostrar5 = (LinearLayout)findViewById(R.id.par_sex_oculto);
        final LinearLayout mostrar6 = (LinearLayout)findViewById(R.id.par_asex_oculto);

        //Fecha en que Recibio la Muestra Hematica
        fecha1 = (DatePicker)findViewById(R.id.fecha_recibio_mh);

        //Fecha en que le dio Lectura a la Muestra Hematica
        fecha2 = (DatePicker)findViewById(R.id.fecha_lectura_mh);

        //Codigo de la Muestra Hematica
        cod_mh = (EditText)findViewById(R.id.codigo_mh);

        //Resultado de la Muestra Hematica
        RadioButton neg = (RadioButton)findViewById(R.id.negativo);
        RadioButton pos = (RadioButton)findViewById(R.id.positivo);
        View.OnClickListener list = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resu = "";
                switch(view.getId()) {
                    case R.id.negativo:
                        resu = "negativo";
                        mostrar1.setVisibility(View.GONE);
                        mostrar2.setVisibility(View.GONE);
                        break;
                    case R.id.positivo:
                        resu = "positivo";
                        mostrar1.setVisibility(View.VISIBLE);
                        mostrar2.setVisibility(View.VISIBLE);
                        break;
                }
                res_mh = resu;
            }
        };
        neg.setOnClickListener(list);
        pos.setOnClickListener(list);

        //Tipo de Plasmodium
        RadioButton vivax = (RadioButton)findViewById(R.id.p_vivax);
        RadioButton falciparum = (RadioButton)findViewById(R.id.p_falciparum);
        RadioButton asociado = (RadioButton)findViewById(R.id.p_asociado);
        View.OnClickListener list1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resu = "";
                switch(view.getId()) {
                    case R.id.p_vivax:
                        resu = "p_vivax";
                        break;
                    case R.id.p_falciparum:
                        resu = "p_falciparum";
                        break;
                    case R.id.p_asociado:
                        resu = "p_asociado";
                        break;
                }
                tipo_plas = resu;
            }
        };
        vivax.setOnClickListener(list1);
        falciparum.setOnClickListener(list1);
        asociado.setOnClickListener(list1);

        //Estadio del Parasito
        final CheckBox sexual = (CheckBox)findViewById(R.id.sexual);
        sexual.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sexual.isChecked()){
                    esta_para += " "+"sexual";
                    mostrar3.setVisibility(View.VISIBLE);
                }else{
                    mostrar3.setVisibility(View.GONE);
                }
            }
        });
        final CheckBox asexual = (CheckBox)findViewById(R.id.asexual);
        asexual.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(asexual.isChecked()){
                    esta_para += " "+"asexual";
                    mostrar4.setVisibility(View.VISIBLE);
                }else{
                    mostrar4.setVisibility(View.GONE);
                }
            }
        });

        //Conteo Sexual de Parasitos
        RadioButton sexual40 = (RadioButton)findViewById(R.id.menos_40_sexual);
        RadioButton sexualmedia = (RadioButton)findViewById(R.id.media_sexual);
        RadioButton sexual1 = (RadioButton)findViewById(R.id.una_sexual);
        RadioButton sexual2 = (RadioButton)findViewById(R.id.dos_sexual);
        RadioButton sexual3 = (RadioButton)findViewById(R.id.tres_sexual);
        RadioButton sexual4 = (RadioButton)findViewById(R.id.cuatro_sexual);
        View.OnClickListener list2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resu = "";
                switch(view.getId()) {
                    case R.id.menos_40_sexual:
                        resu = "menos_40_sexual";
                        mostrar5.setVisibility(View.VISIBLE);
                        break;
                    case R.id.media_sexual:
                        resu = "media_sexual";
                        mostrar5.setVisibility(View.GONE);
                        break;
                    case R.id.una_sexual:
                        resu = "una_sexual";
                        mostrar5.setVisibility(View.GONE);
                        break;
                    case R.id.dos_sexual:
                        resu = "dos_sexual";
                        mostrar5.setVisibility(View.GONE);
                        break;
                    case R.id.tres_sexual:
                        resu = "tres_sexual";
                        mostrar5.setVisibility(View.GONE);
                        break;
                    case R.id.cuatro_sexual:
                        resu = "cuatro_sexual";
                        mostrar5.setVisibility(View.GONE);
                        break;
                }
                cont_asex = resu;
            }
        };
        sexual40.setOnClickListener(list2);
        sexualmedia.setOnClickListener(list2);
        sexual1.setOnClickListener(list2);
        sexual2.setOnClickListener(list2);
        sexual3.setOnClickListener(list2);
        sexual3.setOnClickListener(list2);
        sexual4.setOnClickListener(list2);

        //Conteo Sexual menor a 40
        cont_sex_p = (EditText)findViewById(R.id.cuantos_sexual);

        //Conteo Sexual de Parasitos
        RadioButton asexual40 = (RadioButton)findViewById(R.id.menos_40_asexual);
        RadioButton asexualmedia = (RadioButton)findViewById(R.id.media_asexual);
        RadioButton asexual1 = (RadioButton)findViewById(R.id.una_asexual);
        RadioButton asexual2 = (RadioButton)findViewById(R.id.dos_asexual);
        RadioButton asexual3 = (RadioButton)findViewById(R.id.tres_asexual);
        RadioButton asexual4 = (RadioButton)findViewById(R.id.cuatro_asexual);
        View.OnClickListener list3 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resu = "";
                switch(view.getId()) {
                    case R.id.menos_40_asexual:
                        resu = "menos_40_asexual";
                        mostrar6.setVisibility(View.VISIBLE);
                        break;
                    case R.id.media_asexual:
                        resu = "media_asexual";
                        mostrar6.setVisibility(View.GONE);
                        break;
                    case R.id.una_asexual:
                        resu = "una_asexual";
                        mostrar6.setVisibility(View.GONE);
                        break;
                    case R.id.dos_asexual:
                        resu = "dos_asexual";
                        mostrar6.setVisibility(View.GONE);
                        break;
                    case R.id.tres_asexual:
                        resu = "tres_asexual";
                        mostrar6.setVisibility(View.GONE);
                        break;
                    case R.id.cuatro_asexual:
                        resu = "cuatro_asexual";
                        mostrar6.setVisibility(View.GONE);
                        break;
                }
                cont_asex = resu;
            }
        };
        asexual40.setOnClickListener(list3);
        asexualmedia.setOnClickListener(list3);
        asexual1.setOnClickListener(list3);
        asexual2.setOnClickListener(list3);
        asexual3.setOnClickListener(list3);
        asexual3.setOnClickListener(list3);
        asexual4.setOnClickListener(list3);

        //Conteo Asexual menor a 40
        cont_sex_p = (EditText)findViewById(R.id.cuantos_asexual);

        //envia los datos por medio del botón
        Button btn_crear = (Button)findViewById(R.id.btn_confimar);
        btn_crear.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        new CreateConfirmar().execute();

    }

    class CreateConfirmar extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(form_confirmar.this);
            pDialog.setMessage("Creando el Caso...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            String fecha_recibio_mh = fecha1.getYear()+"-"+(fecha1.getMonth()+1)+"-"+fecha1.getDayOfMonth();
            String fecha_lectura_mh = fecha2.getYear()+"-"+(fecha2.getMonth()+1)+"-"+fecha2.getDayOfMonth();
            String codigo_mh = cod_mh.getText().toString();
            String resultado_mh = res_mh;
            String tipo_plasmodium = tipo_plas;
            String estadio_parasito = esta_para;
            String conteo_sexual = cont_sex;
            String conteo_sexual_p = cont_sex_p.getText().toString();
            String conteo_asexual = cont_asex;
            String conteo_asexual_p = cont_asex_p.getText().toString();
            String username_reg = getIntent().getStringExtra("usuario");

            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("fecha_registro", fecha_recibio_mh));
                params.add(new BasicNameValuePair("fecha_sintomas", fecha_lectura_mh));
                params.add(new BasicNameValuePair("codigo_mh",codigo_mh));
                params.add(new BasicNameValuePair("resultado_mh",resultado_mh));
                params.add(new BasicNameValuePair("tipo_plasmodium", tipo_plasmodium));
                params.add(new BasicNameValuePair("estadio_parasito", estadio_parasito));
                params.add(new BasicNameValuePair("conteo_sexual", conteo_sexual));
                params.add(new BasicNameValuePair("conteo_sexual_p",conteo_sexual_p));
                params.add(new BasicNameValuePair("conteo_asexual", conteo_asexual));
                params.add(new BasicNameValuePair("conteo_asexual_p", conteo_asexual_p));
                params.add(new BasicNameValuePair("username_reg",username_reg));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        CONFIRMAR_URL, "POST", params);

                // full json response
                Log.d("Confirmando", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Caso Confirmado!", json.toString());
                    finish();
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Confirmacion Erronea!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(form_confirmar.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}
