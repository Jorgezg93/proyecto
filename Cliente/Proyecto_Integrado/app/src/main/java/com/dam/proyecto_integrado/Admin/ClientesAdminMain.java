package com.dam.proyecto_integrado.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dam.proyecto_integrado.Login;
import com.dam.proyecto_integrado.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClientesAdminMain extends AppCompatActivity {

    //Variables
    LinearLayout layout;
    Spinner spinnerId;

    //Método onCreate que almacena las referencias a los componentes de la actividad
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_admin_main);

        spinnerId = findViewById(R.id.spinnerClienteId);
        layout = findViewById(R.id.layoutClientesAdmin);

        getClientes();

    }


    //Métodos para implementar el menú personalizado en la zona superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.gestion_envios) {
            Intent intent = new Intent(this, EnviosAdminMain.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.gestion_repar) {
            Intent intent = new Intent(this, RepartidoresAdminMain.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.gestion_clien) {
            Intent intent = new Intent(this, ClientesAdminMain.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.main) {
            Intent intent = new Intent(this, MainAdmin.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.datos) {
            Intent intent = new Intent(this, MisDatosAdmin.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.salir) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    //Método que lanza una petición HTTP y devuelve todos los clientes
    public void getClientes() {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/clientes";

        // Crear la solicitud GET
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<String> elementos = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Button boton = new Button(ClientesAdminMain.this);
                        String titulo = "ID:" + jsonObject.getString("id") + " - Email: " + jsonObject.getString("email");
                        boton.setText(titulo);
                        boton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ClientesAdminMain.this, ClientesAdminEdit.class);
                                try {
                                    intent.putExtra("idCliente", jsonObject.getInt("id"));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                startActivity(intent);
                            }
                        });
                        layout.addView(boton);
                        elementos.add(jsonObject.getString("id"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ClientesAdminMain.this, android.R.layout.simple_list_item_1, elementos);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerId.setAdapter(adapter);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error de la solicitud
                        error.printStackTrace();
                    }
                }
        );

        // Agregar la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(this).add(request);
    }

    //Método que para acceder al cliente según el id seleccionado en el spinner
    public void accederPorId(View v) {
        Intent intent = new Intent(ClientesAdminMain.this, ClientesAdminEdit.class);
        intent.putExtra("idCliente", Integer.parseInt(spinnerId.getSelectedItem().toString()));
        startActivity(intent);
    }

}