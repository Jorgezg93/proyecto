package com.dam.proyecto_integrado.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dam.proyecto_integrado.Login;
import com.dam.proyecto_integrado.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;

public class EnviosAdminEdit extends AppCompatActivity {
    //Variables
    int envioId;
    Spinner spinnerEstado, spinnerRepartidor;
    TextView clienteText, origenText, destinoText, empresaText, entregaText, titulo;

    //Método onCreate que almacena las referencias a los componentes de la actividad
    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envios_admin_edit);

        Intent intent = getIntent();
        envioId = intent.getIntExtra("idEnvio", 0);
        titulo = findViewById(R.id.textViewEnvioTitulo);
        titulo.setText(titulo.getText() + " " + envioId);
        clienteText = findViewById(R.id.textViewClienteAdmin);
        origenText = findViewById(R.id.textViewOrigenAdmin);
        destinoText = findViewById(R.id.textViewDestinoAdmin);
        empresaText = findViewById(R.id.textViewEmpresaAdmin);
        entregaText = findViewById(R.id.textViewEntregaAdmin);
        spinnerEstado = findViewById(R.id.spinnerEstadoAdmin);
        spinnerRepartidor = findViewById(R.id.spinnerRepartidorAdmin);

        getRepartidores();
        getEstados();
        obtenerInfoEnvio(envioId);


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

    //Método que lanza una petición HTTP y rellena los campos con la información actual del envío
    public void obtenerInfoEnvio(int id) {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/envios/buscarId/" + id;

        // Crear la solicitud GET
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response.getString("fechaEntrega"));
                    // Acceder a los datos del objeto JSON
                    clienteText.setText(response.getJSONObject("cliente").getString("id"));
                    origenText.setText(response.getString("origen"));
                    destinoText.setText(response.getString("destino"));
                    empresaText.setText(response.getString("empresaReparto"));
                    entregaText.setText(response.getString("fechaEntrega"));
                } catch (JSONException e) {
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

    //Método que lanza una petición HTTP y devuelve todos los repartidores disponibles y rellena el spinner
    public void getRepartidores() {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/repartidores";

        // Crear la solicitud GET
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<String> elementos = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String titulo = jsonObject.getString("nombre") + " - " + jsonObject.getString("id");
                        elementos.add(titulo);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EnviosAdminEdit.this, android.R.layout.simple_list_item_1, elementos);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerRepartidor.setAdapter(adapter);
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

    //Método que lanza una petición HTTP y devuelve todos los estados disponibles y rellena el spinner
    public void getEstados() {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/estados";

        // Crear la solicitud GET
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<String> elementos = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String campo = jsonObject.getString("nombre") + " - " + jsonObject.getString("id");
                        elementos.add(campo);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EnviosAdminEdit.this, android.R.layout.simple_list_item_1, elementos);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEstado.setAdapter(adapter);
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

    //Método que lanza una petición HTTP y guarda los cambios realizados en el envío
    public void guardarCambios(View v) {
        String[] repartidores = spinnerRepartidor.getSelectedItem().toString().split(" - ");
        String[] estados = spinnerEstado.getSelectedItem().toString().split(" - ");
        int repartidor = Integer.parseInt(repartidores[1]);
        int estado = Integer.parseInt(estados[1]);
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/envios/" + envioId + "/" + estado + "/" + repartidor;

        // Crear la solicitud PUT
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                updateFechaModificacion();
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

        Toast.makeText(this, "Envío modificado con éxito", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, EnviosAdminMain.class);
        startActivity(intent);
    }

    //Método que lanza una petición HTTP y modifica la fecha de modificación del envío
    private void updateFechaModificacion() {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/envios/cambiarModificacion/" + envioId + "/" + LocalDate.now();

        // Crear la solicitud PUT
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

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

    //Método que habilita la opción de volver
    public void volver(View v) {
        Intent intent = new Intent(this, EnviosAdminMain.class);
        startActivity(intent);
    }
}