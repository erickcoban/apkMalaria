package com.example.subprogramamalaria.proyectomalaria;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.subprogramamalaria.proyectomalaria.JSONParser;
import com.example.subprogramamalaria.proyectomalaria.Menu;
import com.example.subprogramamalaria.proyectomalaria.R;
import com.example.subprogramamalaria.proyectomalaria.Register;

public class Login extends Activity implements OnClickListener {

    private EditText user, pass;
    private Button mSubmit, mSalir, mRegister;
    private Cursor fila;

    private ProgressDialog pDialog;

    // Clase JSONParser
    JSONParser jsonParser = new JSONParser();

    private static final String LOGIN_URL = "http://proyectomalaria.hol.es/malaria/login.php";

    // La respuesta del JSON es
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // setup input fields
        user = (EditText) findViewById(R.id.usuario);
        pass = (EditText) findViewById(R.id.password);

        Consulta();

        // setup buttons
        mSubmit = (Button) findViewById(R.id.login);
        mSalir = (Button) findViewById(R.id.salir);
        //mRegister = (Button) findViewById(R.id.register);

        // register listeners
        mSubmit.setOnClickListener(this);
        //Dato ignorado para no ingresarse
        //mRegister.setOnClickListener(this);

    }

    public void onclicBtnSalir (View view){
        //Source for close app
        finish();
    }

    public void Consulta(){
        //Llama a la tabla last_user y extrae el ultimo registro que se almaceno
        //Se carga la base de datos
        last_user admin = new last_user(Login.this, "admin", null,1);
        SQLiteDatabase db_last = admin.getWritableDatabase();

        //Consulta a la Base de Datos
        Cursor fila = db_last.rawQuery("select username from last_user",null);
        if(fila.moveToLast()){
            user.setText(fila.getString(0));
        }
        db_last.close();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.login:
                new AttemptLogin().execute();
                break;
           /*case R.id.register:
                Intent i = new Intent(this, Register.class);
                startActivity(i);
                break;*/

            default:
                break;
        }
    }

    class AttemptLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            //Se carga la base de datos
            last_user admin = new last_user(Login.this, "admin", null,1);
            SQLiteDatabase db_last = admin.getWritableDatabase();

            int success;
            String usuario = user.getText().toString();
            String password = pass.getText().toString();
            try {
                //Clase para guardar datos en SQLite
                ContentValues last_user = new ContentValues();
                last_user.put("username",usuario);
                last_user.put("password",password);
                db_last.insert("last_user", null, last_user);
                db_last.close();

                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("usuario", usuario));
                params.add(new BasicNameValuePair("pass", password));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
                        params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());
                    // save user data
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(Login.this);
                    Editor edit = sp.edit();
                    edit.putString("usuario", usuario);
                    edit.commit();

                    Intent i = new Intent(Login.this, Menu.class);

                    //Envia el Usuario que ingresa
                    i.putExtra("last_user", user.getText().toString());
                    finish();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
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
            if (file_url != null) {
                Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}