package com.example.subprogramamalaria.proyectomalaria;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.auth.BasicUserPrincipal;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Subprograma Malaria on 31/08/2015.
 */
public class form_reporte_baf extends ActionBarActivity implements View.OnClickListener {
    //Declaración de Variables
    private DatePicker fecha_reporte;
    private EditText mh_baf, mh_neg, mh_pos, dist_baf, com_baf;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //testing on Emulator:
    private static final String REPORTE_BAF_URL = "http://proyectomalaria.hol.es/malaria/reporte_baf.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_reporte_baf);

        //Fecha en que Reporta los Insumos
        fecha_reporte = (DatePicker) findViewById(R.id.fecha_reporte_baf);

        //Cuantas muestras hemáticas tomo en BAF
        mh_baf = (EditText) findViewById(R.id.cuantas_mh_baf);

        //Cuantas Muestras Hemáticas fueron Negativas
        mh_neg = (EditText) findViewById(R.id.cuantas_mh_negativas);

        //Cuantas Muestras Hemáticas fueron Positivas
        mh_pos = (EditText) findViewById(R.id.cuantas_mh_positivas);

        //Distrito donde realizo el BAF
        dist_baf = (EditText) findViewById(R.id.distrito_baf);

        //Comunidad donde Realizo el BAF
        com_baf = (EditText) findViewById(R.id.comunidad_baf);

        //envia los datos por medio del botón
        Button btn_baf = (Button)findViewById(R.id.btn_reporte_baf);
        btn_baf.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        new CreateBaf().execute();

    }

    class CreateBaf extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(form_reporte_baf.this);
            pDialog.setMessage("Creando Reporte...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            String fecha_reporte_insumos = fecha_reporte.getYear()+"-"+(fecha_reporte.getMonth()+1)+"-"+fecha_reporte.getDayOfMonth();
            String cuantas_mh_baf = mh_baf.getText().toString();
            String cuantas_mh_negativas = mh_neg.getText().toString();
            String cuantas_mh_positivas = mh_pos.getText().toString();
            String distrito_baf = dist_baf.getText().toString();
            String comunidad_baf = com_baf.getText().toString();
            String username_reg = getIntent().getStringExtra("usuario");

            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("fecha_baf", fecha_reporte_insumos));
                params.add(new BasicNameValuePair("cuantas_mh_baf",cuantas_mh_baf));
                params.add(new BasicNameValuePair("cuantas_mh_negativas",cuantas_mh_negativas));
                params.add(new BasicNameValuePair("cuantas_mh_positivas",cuantas_mh_positivas));
                params.add(new BasicNameValuePair("distrito_baf",distrito_baf));
                params.add(new BasicNameValuePair("comunidad_baf",comunidad_baf));
                params.add(new BasicNameValuePair("username_reg",username_reg));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        REPORTE_BAF_URL, "POST", params);

                // full json response
                Log.d("Reportando", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Reporte Enviado!", json.toString());
                    finish();
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Reporte Erroneo!", json.getString(TAG_MESSAGE));
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
                Toast.makeText(form_reporte_baf.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}