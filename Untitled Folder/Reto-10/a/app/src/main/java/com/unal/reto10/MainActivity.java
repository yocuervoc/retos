package com.unal.reto10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Spinner departamenos_spinner;
    private ArrayList<String> departamentoss;
    private ArrayAdapter<String> departamenos_adapter;
    private Spinner municipios_spinner;
    private ArrayList<String> municipioss;
    private ArrayAdapter<String> municipios_adapter;
    private ListView list;
    private ArrayList<String> peajes;
    private ArrayAdapter<String> cod_adapter, prueba;
    private Context context = this;
    private RequestQueue queue;
    private TextView zona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        departamentoss = new ArrayList<>();
        municipioss = new ArrayList<>();
        peajes = new ArrayList<>();
        zona = (TextView)findViewById(R.id.zona_postal);
        queue = Volley.newRequestQueue(this);
        //String url = "https://www.datos.gov.co/resource/jhpq-24h2.json?$select=distinct%20Departamento&$order=Departamento%20ASC";
        String url = "https://www.datos.gov.co/resource/jhpq-24h2.json?$select=distinct%20Departamento&$order=Departamento%20ASC";
        departamenos_spinner = (Spinner) findViewById(R.id.departamentos);
        municipios_spinner = (Spinner) findViewById(R.id.municipios);
        list = findViewById(R.id.list);
        /*
        final JsonArrayRequest departamentos = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject tmp = null;
                            try {
                                tmp = response.getJSONObject(i);
                                departamentoss.add(tmp.getString("Departamento"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        departamenos_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, departamentoss);
                        departamenos_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        departamenos_spinner.setAdapter(departamenos_adapter);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });


        prueba = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, departamentoss);
        list.setAdapter(prueba);
*/
        departamenos_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                municipioss.clear();
                String tmp = (String) parent.getItemAtPosition(pos);
                //String url = "https://www.datos.gov.co/resource/jhpq-24h2.json?$select=distinct%20Municipio&Departamento="+ tmp + "&$order=Municipio%20ASC";
                String url = "https://www.datos.gov.co/resource/w5u4-6ntu.json?departamento="+ departamenos_spinner.getSelectedItem();

                JsonArrayRequest municipios = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject tmp = null;
                                    try {
                                        tmp = response.getJSONObject(i);
                                        municipioss.add(tmp.getString("ciudad"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                municipios_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, municipioss);
                                municipios_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                municipios_spinner.setAdapter(municipios_adapter);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                queue.add(municipios);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        municipios_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                peajes.clear();
                final String tmp = (String) parent.getItemAtPosition(pos);
                //String url = "https://www.datos.gov.co/resource/jhpq-24h2.json?Municipio=" + tmp;
                String url = "https://www.datos.gov.co/resource/w5u4-6ntu.json?ciudad=" + municipios_spinner.getSelectedItem();

                JsonArrayRequest codes = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject tmp = null;
                                    try {
                                        tmp = response.getJSONObject(i);
                                        //zona.setText(tmp.getString("operadora"));

                                        String tmp2 = "año: " + tmp.getString("a_o") + "\n";
                                        tmp2 += "trimestre: " + tmp.getString("trimestre") + "\n";
                                        tmp2 += "departamento: " + tmp.getString("departamento") + "\n";
                                        tmp2 += "ciudad: " + tmp.getString("ciudad") + "\n";
                                        tmp2 += "declaraciones_de_extra_juicio: " + tmp.getString("declaraciones_de_extra_juicio") + "\n";




                                        peajes.add(tmp2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                cod_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, peajes);
                                list.setAdapter(cod_adapter);
                                //prueba = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, municipioss);
                                //list.setAdapter(prueba);

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("REQ", "bad");
                            }
                        });
                queue.add(codes);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //queue.add(departamentos);

    }
}
