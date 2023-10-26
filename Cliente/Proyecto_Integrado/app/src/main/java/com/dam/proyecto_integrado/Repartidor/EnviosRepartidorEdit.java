package com.dam.proyecto_integrado.Repartidor;

import androidx.appcompat.app.AppCompatActivity;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dam.proyecto_integrado.Cliente.ClienteMain;
import com.dam.proyecto_integrado.Login;
import com.dam.proyecto_integrado.R;
import com.dam.proyecto_integrado.funciones.Usuario;
import com.dam.proyecto_integrado.funciones.UsuarioSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;

public class EnviosRepartidorEdit extends AppCompatActivity {

    //Variables
    int idEnvio;
    Spinner estados;
    Usuario usuario;
    TextView cliente, repartidor, origen, destino, entrega, empresa, titulo;

    /*
    Método onCreate que almacena las referencias a los componentes de la actividad,
    obtiene el envío actual y busca su información.
     */
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envios_repartidor_edit);

        usuario = UsuarioSingleton.getInstance().getUsuario();
        idEnvio = getIntent().getIntExtra("idEnvio", 0);
        estados = findViewById(R.id.spinnerEstadoRepartidorEdit);

        titulo = findViewById(R.id.textViewReparEnvioTitulo);
        cliente = findViewById(R.id.textViewClienteEnvioRepar);
        repartidor = findViewById(R.id.textViewReparEnvioRepar);
        origen = findViewById(R.id.textViewOrigenEnvioRepar);
        destino = findViewById(R.id.textViewDestinoEnvioRepar);
        entrega = findViewById(R.id.textViewEntregaEnvioRepar);
        empresa = findViewById(R.id.textViewEmpresaEnvioRepar);

        titulo.setText(titulo.getText().toString() + " " + idEnvio);
        obtenerInfoEnvio(idEnvio);
        getEstados();
    }

    //Métodos para implementar el menú personalizado en la zona superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_repar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.enviosRepar) {
            Intent intent = new Intent(this, EnviosRepartidorMain.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.localizacionRepar) {
            Intent intent = new Intent(this, LocalizacionRepartidorMain.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.datosRepar) {
            Intent intent = new Intent(this, MisDatosRepartidor.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.main) {
            Intent intent = new Intent(this, MainRepartidor.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.salirRepar) {
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

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EnviosRepartidorEdit.this, android.R.layout.simple_list_item_1, elementos);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    estados.setAdapter(adapter);
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

    //Método que lanza una petición HTTP y rellena los campos con la información actual del envío
    public void obtenerInfoEnvio(int idEnvio) {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/envios/buscarId/" + idEnvio;

        // Crear la solicitud GET
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Acceder a los datos del objeto JSON
                    cliente.setText(response.getJSONObject("cliente").getString("id"));
                    repartidor.setText(response.getJSONObject("repartidor").getString("id"));
                    origen.setText(response.getString("origen"));
                    destino.setText(response.getString("destino"));
                    empresa.setText(response.getString("empresaReparto"));
                    entrega.setText(response.getString("fechaEntrega"));
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

    //Método que lanza una petición HTTP y guarda los cambios realizados en el envío
    public void guardarCambios(View v) {
        String[] estadosSpinner = estados.getSelectedItem().toString().split(" - ");
        int estado = Integer.parseInt(estadosSpinner[1]);

        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/envios/" + idEnvio + "/" + estado;

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

        Toast.makeText(EnviosRepartidorEdit.this, "Envío modificado con éxito", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(EnviosRepartidorEdit.this, EnviosRepartidorMain.class);
        startActivity(intent);
    }

    //Método que lanza una petición HTTP y modifica la fecha de modificación del envío
    private void updateFechaModificacion() {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/envios/cambiarModificacion/" + idEnvio + "/" + LocalDate.now();

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
        Intent intent = new Intent(this, EnviosRepartidorMain.class);
        startActivity(intent);
    }

}