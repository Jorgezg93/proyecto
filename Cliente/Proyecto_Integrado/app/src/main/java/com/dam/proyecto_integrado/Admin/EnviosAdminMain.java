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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

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

public class EnviosAdminMain extends AppCompatActivity {
    //Variables
    RadioButton radioEstado, radioCliente, radioRepartidor;
    Spinner estadosSpinner, clienteSpinner, repartidorSpinner;
    String filtro;
    int id;
    LinearLayout layout;

    //Método onCreate que almacena las referencias a los componentes de la actividad
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envios_admin_main);

        estadosSpinner = findViewById(R.id.spinnerEstado);
        clienteSpinner = findViewById(R.id.spinnerCliente);
        repartidorSpinner = findViewById(R.id.spinnerRepartidor);
        radioEstado = findViewById(R.id.radioButtonEstado);
        radioCliente = findViewById(R.id.radioButtonCliente);
        radioRepartidor = findViewById(R.id.radioButtonRepartidor);
        layout = findViewById(R.id.EnviosAdmin);

        getEstados();
        getClientes();
        getRepartidores();


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

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EnviosAdminMain.this, android.R.layout.simple_list_item_1, elementos);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    estadosSpinner.setAdapter(adapter);
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

    //Método que lanza una petición HTTP y devuelve todos los clientes disponibles y rellena el spinner
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
                        String titulo = jsonObject.getString("nombre") + " - " + jsonObject.getString("id");
                        elementos.add(titulo);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EnviosAdminMain.this, android.R.layout.simple_list_item_1, elementos);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    clienteSpinner.setAdapter(adapter);
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

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EnviosAdminMain.this, android.R.layout.simple_list_item_1, elementos);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    repartidorSpinner.setAdapter(adapter);
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

    //Método para buscar el envío según los filtros seleccionados
    private void buscarEnvio() {
        if (radioCliente.isChecked()) {
            String combo = clienteSpinner.getSelectedItem().toString();
            int index = combo.lastIndexOf(" - ");
            id = Integer.parseInt(combo.substring(index + 3));
            filtro = "Cliente/" + id;
            getEnvios();

        } else if (radioEstado.isChecked()) {
            String combo = estadosSpinner.getSelectedItem().toString();
            int index = combo.lastIndexOf(" - ");
            id = Integer.parseInt(combo.substring(index + 3));
            filtro = "Estado/" + id;
            getEnvios();

        } else if (radioRepartidor.isChecked()) {
            String combo = repartidorSpinner.getSelectedItem().toString();
            int index = combo.lastIndexOf(" - ");
            id = Integer.parseInt(combo.substring(index + 3));
            filtro = "Repartidor/" + id;
            getEnvios();

        } else {
            id = 0;
            filtro = "";
            Toast.makeText(EnviosAdminMain.this, "Debes seleccionar al menos un filtro", Toast.LENGTH_LONG).show();
        }
    }

    //Método que actualiza los envíos buscados y los representa en el layout
    private void getEnvios() {
        layout.removeAllViews();
        System.out.println(filtro);
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/envios/buscar" + filtro;
        // Crear la solicitud GET
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Button boton = new Button(EnviosAdminMain.this);
                        String titulo = "-ID:" + jsonObject.getInt("id") + " -Estado: " + jsonObject.getJSONObject("estado").getString("nombre");
                        boton.setText(titulo);
                        boton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(EnviosAdminMain.this, EnviosAdminEdit.class);
                                try {
                                    intent.putExtra("idEnvio", jsonObject.getInt("id"));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                startActivity(intent);
                            }
                        });
                        layout.addView(boton);
                    }
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
                        Toast.makeText(EnviosAdminMain.this, "Debes elegir un filtro para la búsqueda", Toast.LENGTH_LONG).show();
                    }
                }
        );

        // Agregar la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(this).add(request);
    }

    //Método que habilita ir a la actividad para crear envío
    public void crearEnvio(View v) {
        Intent intent = new Intent(EnviosAdminMain.this, EnviosAdminCrear.class);
        startActivity(intent);
    }

    //Método que lanza el método para buscar envíos
    public void buscarEnvios(View v) {
        buscarEnvio();
    }

}