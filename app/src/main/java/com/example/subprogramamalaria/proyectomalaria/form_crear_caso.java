package com.example.subprogramamalaria.proyectomalaria;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
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
public class form_crear_caso extends ActionBarActivity implements OnClickListener {
    //Declaración de Variables
    private DatePicker fecha1, fecha2, fecha3, fecha4;
    private EditText reg_otro, reg_clave, reg_pn, reg_sn, reg_pa, reg_sa, reg_ac, reg_embarazo;
    private String reg_sint="", reg_prueba="", reg_muestra, reg_genero, reg_emba, reg_PDR="";

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //testing on Emulator:
    private static final String CREAR_CASO_URL = "http://proyectomalaria.hol.es/malaria/crear_caso.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_crear_caso);

        //Muestra las preguntas que estan ocultas
        final LinearLayout mostrar = (LinearLayout)findViewById(R.id.ocultar_sintomas);
        final LinearLayout mostrar3 = (LinearLayout)findViewById(R.id.ocultar_res_pdr);
        final LinearLayout mostrar1 = (LinearLayout)findViewById(R.id.oculta_embarazo);
        final LinearLayout mostrar2 = (LinearLayout)findViewById(R.id.oculta_meses);

        //Fecha de Registro del Paciente
        fecha1 = (DatePicker) findViewById(R.id.fecha_registro);

        //Fecha de Inicio de Sintoams
        fecha2 = (DatePicker) findViewById(R.id.fecha_sintomas);

        //Sintomas del Paciente
        final CheckBox sint1 = (CheckBox)findViewById(R.id.fiebre);
        sint1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sint1.isChecked()){
                     reg_sint += " "+"fiebre";
                }
            }
        });
        final CheckBox sint2 = (CheckBox)findViewById(R.id.dolor_de_cabeza);
        sint2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sint2.isChecked()){
                    reg_sint += " "+"dolor_de_cabeza";
                }
            }
        });
        final CheckBox sint3 = (CheckBox)findViewById(R.id.escalofrios);
        sint3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sint3.isChecked()) {
                    reg_sint += " "+"escalofrios";
                }
            }
        });
        final CheckBox sint4 = (CheckBox)findViewById(R.id.dolor_de_cuerpo);
        sint4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sint4.isChecked()){
                    reg_sint += " "+"dolor_de_cuerpo";
                }
            }
        });
        final CheckBox sint5 = (CheckBox)findViewById(R.id.otro);
        sint5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sint5.isChecked()){
                    reg_sint += " "+"otro";
                    mostrar.setVisibility(View.VISIBLE);
                }else {
                    mostrar.setVisibility(View.GONE);
                }
            }
        });

        //Otro Sintoma del Paciente
        reg_otro = (EditText)findViewById(R.id.otro_sintoma);

        //Selecciona el tipo de Prueba que se le hizo al Paciente
        final CheckBox pdr = (CheckBox)findViewById(R.id.prueba_rapida);
        pdr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pdr.isChecked()){
                    reg_prueba += " "+"prueba_rapida";
                    mostrar3.setVisibility(View.VISIBLE);
                }else{
                    mostrar3.setVisibility(View.GONE);
                }
            }
        });
        final CheckBox gg = (CheckBox)findViewById(R.id.gota_gruesa);
        gg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gg.isChecked()){
                    reg_prueba += " "+"gota_gruesa";
                }
            }
        });

        //Tipo de Muestra
        RadioButton RB_diagnostico = (RadioButton)findViewById(R.id.muestra_diagnostico);
        RadioButton RB_control = (RadioButton)findViewById(R.id.muestra_control);
        RadioButton RB_embarazo = (RadioButton)findViewById(R.id.muestra_embarazada);

        View.OnClickListener list = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resu = "";
                switch(view.getId()) {
                    case R.id.muestra_diagnostico:
                        resu = "muestra_diagnostico";
                        mostrar1.setVisibility(View.GONE);
                        break;
                    case R.id.muestra_control:
                        resu = "muestra_control";
                        mostrar1.setVisibility(View.GONE);
                        break;
                    case R.id.muestra_embarazada:
                        resu = "muestra_embarazada";
                        mostrar1.setVisibility(View.VISIBLE);
                        break;
                }
                reg_muestra = resu;
            }
        };
        RB_diagnostico.setOnClickListener(list);
        RB_control.setOnClickListener(list);
        RB_embarazo.setOnClickListener(list);

        //Fecha en que tomo la Muestra Hematica
        fecha3 = (DatePicker)findViewById(R.id.fecha_toma_mh);

        //Clave y No
        reg_clave = (EditText)findViewById(R.id.clave_no);

        //Primer Nombre
        reg_pn = (EditText)findViewById(R.id.primer_nombre);

        //Segundo Nombre
        reg_sn = (EditText)findViewById(R.id.segundo_nombre);

        //Primer Apellido
        reg_pa = (EditText)findViewById(R.id.primer_apellido);

        //Segundo Apellido
        reg_sa = (EditText)findViewById(R.id.segundo_apellido);

        //Apellido Casada
        reg_ac = (EditText)findViewById(R.id.apellido_casada);

        //Genero del Paciente
        RadioButton female = (RadioButton)findViewById(R.id.femenino);
        RadioButton male = (RadioButton)findViewById(R.id.masculino);

        View.OnClickListener list1 = new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                String resu = "";

                switch(view.getId()) {
                    case R.id.femenino:
                        resu = "femenino";
                        mostrar1.setVisibility(View.VISIBLE);
                        break;
                    case R.id.masculino:
                        resu = "masculino";
                        mostrar1.setVisibility(View.GONE);
                        break;
                }
                reg_genero = resu;
            }
        };
        female.setOnClickListener(list1);
        male.setOnClickListener(list1);

        //Fecha de Nacimiento
        fecha4 = (DatePicker)findViewById(R.id.fecha_nacimiento);

        //Paciente Embarazada
        RadioButton n = (RadioButton)findViewById(R.id.item_no);
        RadioButton y = (RadioButton)findViewById(R.id.item_si);

        View.OnClickListener list2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resu = "";
                switch(view.getId()) {
                    case R.id.item_no:
                        resu = "no";
                        mostrar2.setVisibility(View.GONE);
                        break;
                    case R.id.item_si:
                        resu = "si";
                        mostrar2.setVisibility(View.VISIBLE);
                        break;
                }
                reg_emba = resu;
            }
        };
        n.setOnClickListener(list2);
        y.setOnClickListener(list2);

        //Meses de Embarazo
        reg_embarazo = (EditText)findViewById(R.id.meses_embarazo);

        //Resultado de la PDR
        CheckBox itemc = (CheckBox)findViewById(R.id.item_c);
        itemc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                reg_PDR += " "+"c";
            }
        });
        CheckBox item1 = (CheckBox)findViewById(R.id.item_1);
        item1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                reg_PDR += " "+"1";
            }
        });
        CheckBox item2 = (CheckBox)findViewById(R.id.item_2);
        item2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                reg_PDR += " "+"2";
            }
        });

        //envia los datos por medio del botón
        Button btn_crear = (Button)findViewById(R.id.btn_crear_caso);
        btn_crear.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        new CreateUser().execute();

    }

    class CreateUser extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(form_crear_caso.this);
            pDialog.setMessage("Creando el Caso...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub

            //Se crea la base de datos
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(form_crear_caso.this, "administracion", null,1);
            SQLiteDatabase db = admin.getWritableDatabase();

            // Check for success tag
            int success;
            String fecha_registro = fecha1.getYear()+"-"+(fecha1.getMonth()+1)+"-"+fecha1.getDayOfMonth();
            String fecha_sintomas = fecha2.getYear()+"-"+(fecha2.getMonth()+1)+"-"+fecha2.getDayOfMonth();
            String sintomas = reg_sint;
            String otro_sintoma = reg_otro.getText().toString();
            String tipo_prueba = reg_prueba;
            String tipo_muestra = reg_muestra;
            String fecha_toma_mh = fecha3.getYear()+"-"+(fecha3.getMonth()+1)+"-"+fecha3.getDayOfMonth();
            String clave_no = reg_clave.getText().toString();
            String primer_nombre = reg_pn.getText().toString();
            String segundo_nombre = reg_sn.getText().toString();
            String primer_apellido = reg_pa.getText().toString();
            String segundo_apellido = reg_sa.getText().toString();
            String apellido_casada = reg_ac.getText().toString();
            String genero = reg_genero;
            String fecha_nacimiento = fecha4.getYear()+"-"+(fecha4.getMonth()+1)+"-"+fecha4.getDayOfMonth();
            String embarazada = reg_emba;
            String meses_embarazo = reg_embarazo.getText().toString();
            String resultado_pdr = reg_PDR;
            String username_reg = getIntent().getStringExtra("usuario");

            try {
                //Clase para guardar datos en SQLite
                ContentValues registro_caso = new ContentValues();
                registro_caso.put("fecha_registro",fecha_registro);
                registro_caso.put("fecha_sintomas",fecha_sintomas);
                registro_caso.put("sintomas", sintomas);
                registro_caso.put("otro_sintoma", otro_sintoma);
                registro_caso.put("tipo_prueba", tipo_prueba);
                registro_caso.put("tipo_muestra", tipo_muestra);
                registro_caso.put("fecha_toma_mh", fecha_toma_mh);
                registro_caso.put("clave_no", clave_no);
                registro_caso.put("primer_nombre", primer_nombre);
                registro_caso.put("segundo_nombre", segundo_nombre);
                registro_caso.put("primer_apellido", primer_apellido);
                registro_caso.put("segundo_apellido", segundo_apellido);
                registro_caso.put("apellido_casada", apellido_casada);
                registro_caso.put("genero", genero);
                registro_caso.put("fecha_nacimiento", fecha_nacimiento);
                registro_caso.put("embarazada", embarazada);
                registro_caso.put("meses_embarazo", meses_embarazo);
                registro_caso.put("resultado_pdr", resultado_pdr);
                registro_caso.put("username_reg",username_reg);
                db.insert("crear_caso", null, registro_caso);
                db.close();

                //Clase para guardar datos en MySQL
                List params = new ArrayList();
                params.add(new BasicNameValuePair("fecha_registro", fecha_registro));
                params.add(new BasicNameValuePair("fecha_sintomas", fecha_sintomas));
                params.add(new BasicNameValuePair("sintomas",sintomas));
                params.add(new BasicNameValuePair("otro_sintoma",otro_sintoma));
                params.add(new BasicNameValuePair("tipo_prueba",tipo_prueba));
                params.add(new BasicNameValuePair("tipo_muestra",tipo_muestra));
                params.add(new BasicNameValuePair("fecha_toma_mh",fecha_toma_mh));
                params.add(new BasicNameValuePair("clave_no", clave_no));
                params.add(new BasicNameValuePair("primer_nombre",primer_nombre));
                params.add(new BasicNameValuePair("segundo_nombre",segundo_nombre));
                params.add(new BasicNameValuePair("primer_apellido",primer_apellido));
                params.add(new BasicNameValuePair("segundo_apellido",segundo_apellido));
                params.add(new BasicNameValuePair("apellido_casada",apellido_casada));
                params.add(new BasicNameValuePair("genero",genero));
                params.add(new BasicNameValuePair("fecha_nacimiento",fecha_nacimiento));
                params.add(new BasicNameValuePair("embarazada",embarazada));
                params.add(new BasicNameValuePair("meses_embarazo",meses_embarazo));
                params.add(new BasicNameValuePair("resultado_pdr",resultado_pdr));
                params.add(new BasicNameValuePair("username_reg",username_reg));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        CREAR_CASO_URL, "POST", params);

                // full json response
                Log.d("Creando Caso", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Caso Creado!", json.toString());
                    finish();
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Creacion Erronea!", json.getString(TAG_MESSAGE));
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
                Toast.makeText(form_crear_caso.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}
