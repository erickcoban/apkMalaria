package com.example.subprogramamalaria.proyectomalaria;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Subprograma Malaria on 31/08/2015.
 */
public class form_mini_bress extends ActionBarActivity implements OnClickListener {
    //Declaración de Variables
    private DatePicker fecha1, fecha2, fecha3, fecha4, fecha5, fecha6, fecha7;
    private EditText cloro_150mg, prima_15mg, prima_5mg,aceta_80mg, aceta_500mg, pdr, laminillas, lancetas, algodon, alcohol, obser ;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //testing on Emulator:
    private static final String MINI_BRESS_URL = "http://proyectomalaria.hol.es/malaria/mini_bress.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_mini_bress);

        //Fecha en que Reporta los Insumos
        fecha1 = (DatePicker) findViewById(R.id.fecha_registro_insumos);

        //Escribe cuanto tiene de Cloroquina 150mg
        cloro_150mg = (EditText) findViewById(R.id.tengo_cloroquina_150mg);
        //Fecha de Vencimiento de la Cloroquina 150mg
        fecha2 = (DatePicker) findViewById(R.id.vence_cloroquina_150mg);

        //Escribe cuanto tiene de Primaquina 15mg
        prima_15mg = (EditText) findViewById(R.id.tengo_primaquina_15mg);
        //Fecha de Vencimiento de Primaquina 15mg
        fecha3 = (DatePicker) findViewById(R.id.vence_primaquina_15mg);

        //Escribe cuanto tiene de Primaquina 5mg
        prima_5mg = (EditText) findViewById(R.id.tengo_primaquina_5mg);
        //Fecha de Vencimiento de Primaquina 5mg
        fecha4 = (DatePicker) findViewById(R.id.vence_primaquina_5mg);

        //Escribe cuanto tiene de Acetaminofen 80mg
        aceta_80mg = (EditText) findViewById(R.id.tengo_acetaminofen_80mg);
        //Fecha de Vencimiento de Acetaminofen 80mg
        fecha5 = (DatePicker) findViewById(R.id.vence_acetaminofen_80mg);

        //Escribe cuanto tiene de Acetaminofen 500mg
        aceta_500mg = (EditText) findViewById(R.id.tengo_acetaminofen_500mg);
        //Fecha de Vencimiento de Acetaminofen 500mg
        fecha6 = (DatePicker) findViewById(R.id.vence_acetaminofen_500mg);

        //Escribe cuanto tiene de PDR
        pdr = (EditText) findViewById(R.id.tengo_pdr);
        //Fecha de Vencimiento de PDR
        fecha7 = (DatePicker) findViewById(R.id.vence_pdr);

        //Escribe cuanto tiene de Laminillas
        laminillas = (EditText) findViewById(R.id.tengo_laminillas);

        //Escribe cuanto tiene de Lancetas
        lancetas = (EditText) findViewById(R.id.tengo_lancetas);

        //Escribe cuanto tiene de Algodon
        algodon = (EditText) findViewById(R.id.tengo_algodon);

        //Escribe cuanto tiene de Alcohol
        alcohol = (EditText) findViewById(R.id.tengo_alcohol);

        //Escribe las Observaciones Detectadas
        obser = (EditText) findViewById(R.id.observaciones);

        //envia los datos por medio del botón
        Button btn_bress = (Button)findViewById(R.id.btn_reporte_insumos);
        btn_bress.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        new CreateBress().execute();

    }

    class CreateBress extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(form_mini_bress.this);
            pDialog.setMessage("Creando el Reporte...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            String fecha_registro_insumos = fecha1.getYear()+"-"+(fecha1.getMonth()+1)+"-"+fecha1.getDayOfMonth();
            String tengo_cloroquina_150mg = cloro_150mg.getText().toString();
            String vence_cloroquina_150mg = fecha2.getYear()+"-"+(fecha2.getMonth()+1)+"-"+fecha2.getDayOfMonth();
            String tengo_primaquina_15mg = prima_15mg.getText().toString();
            String vence_primaquina_15mg = fecha3.getYear()+"-"+(fecha3.getMonth()+1)+"-"+fecha3.getDayOfMonth();
            String tengo_primaquina_5mg = prima_5mg.getText().toString();
            String vence_primaquina_5mg = fecha4.getYear()+"-"+(fecha4.getMonth()+1)+"-"+fecha4.getDayOfMonth();
            String tengo_acetaminofen_80mg = aceta_80mg.getText().toString();
            String vence_acetaminofen_80mg = fecha5.getYear()+"-"+(fecha5.getMonth()+1)+"-"+fecha5.getDayOfMonth();
            String tengo_acetaminofen_500mg = aceta_500mg.getText().toString();
            String vence_acetaminofen_500mg = fecha6.getYear()+"-"+(fecha6.getMonth()+1)+"-"+fecha6.getDayOfMonth();
            String tengo_pdr = pdr.getText().toString();
            String vence_pdr = fecha7.getYear()+"-"+(fecha7.getMonth()+1)+"-"+fecha7.getDayOfMonth();
            String tengo_laminillas = laminillas.getText().toString();
            String tengo_lancetas = lancetas.getText().toString();
            String tengo_algodon = algodon.getText().toString();
            String tengo_alcohol = alcohol.getText().toString();
            String observaciones = obser.getText().toString();
            String username_reg = getIntent().getStringExtra("usuario");

            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("fecha_registro_insumos", fecha_registro_insumos));
                params.add(new BasicNameValuePair("tengo_cloroquina_150mg",tengo_cloroquina_150mg));
                params.add(new BasicNameValuePair("vence_cloroquina_150mg",vence_cloroquina_150mg));
                params.add(new BasicNameValuePair("tengo_primaquina_15mg", tengo_primaquina_15mg));
                params.add(new BasicNameValuePair("vence_primaquina_15mg", vence_primaquina_15mg));
                params.add(new BasicNameValuePair("tengo_primaquina_5mg",tengo_primaquina_5mg));
                params.add(new BasicNameValuePair("vence_primaquina_5mg",vence_primaquina_5mg));
                params.add(new BasicNameValuePair("tengo_acetaminofen_80mg",tengo_acetaminofen_80mg));
                params.add(new BasicNameValuePair("vence_acetaminofen_80mg",vence_acetaminofen_80mg));
                params.add(new BasicNameValuePair("tengo_acetaminofen_500mg",tengo_acetaminofen_500mg));
                params.add(new BasicNameValuePair("vence_acetaminofen_500mg",vence_acetaminofen_500mg));
                params.add(new BasicNameValuePair("tengo_pdr",tengo_pdr));
                params.add(new BasicNameValuePair("vence_pdr", vence_pdr));
                params.add(new BasicNameValuePair("tengo_laminillas",tengo_laminillas));
                params.add(new BasicNameValuePair("tengo_lancetas",tengo_lancetas));
                params.add(new BasicNameValuePair("tengo_algodon",tengo_algodon));
                params.add(new BasicNameValuePair("tengo_alcohol",tengo_alcohol));
                params.add(new BasicNameValuePair("observaciones",observaciones));
                params.add(new BasicNameValuePair("username_reg",username_reg));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        MINI_BRESS_URL, "POST", params);

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
                Toast.makeText(form_mini_bress.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}