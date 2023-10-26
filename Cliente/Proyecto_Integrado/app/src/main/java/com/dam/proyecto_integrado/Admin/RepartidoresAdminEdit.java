package com.dam.proyecto_integrado.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dam.proyecto_integrado.Login;
import com.dam.proyecto_integrado.R;


import org.json.JSONException;
import org.json.JSONObject;

public class RepartidoresAdminEdit extends AppCompatActivity {
    //Variables
    int idRepartidor;
    TextView idRepar, nombre, apellidos, telefono, email, dni, ubicacion;

    //Método onCreate que almacena las referencias a los componentes de la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repartidores_admin_edit);

        idRepartidor = getIntent().getIntExtra("idRepartidor", 0);
        idRepar = findViewById(R.id.textViewIdAdminRepar);
        nombre = findViewById(R.id.textViewNombreAdminRepar);
        apellidos = findViewById(R.id.textViewApellidosAdminRepar);
        telefono = findViewById(R.id.textViewTelefonoAdminRepar);
        email = findViewById(R.id.textViewEmailAdminRepar);
        dni = findViewById(R.id.textViewDniAdminRepar);
        ubicacion = findViewById(R.id.textViewUbicacionAdminRepar);

        obtenerInfoRepartidor(idRepartidor);

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
        if (id == R.id.datos) {
            Intent intent = new Intent(this, MisDatosAdmin.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.main) {
            Intent intent = new Intent(this, MainAdmin.class);
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
    public void obtenerInfoRepartidor(int id) {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/repartidores/buscar/" + id;

        // Crear la solicitud GET
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Acceder a los datos del objeto JSON
                    idRepar.setText(response.getString("id"));
                    nombre.setText(response.getString("nombre"));
                    apellidos.setText(response.getString("apellidos"));
                    telefono.setText(response.getString("telefono"));
                    email.setText(response.getString("email"));
                    dni.setText(response.getString("dni"));
                    String ubicacionRepartidor = response.getString("latitud") + "/" + response.getString("longitud");
                    ubicacion.setText(ubicacionRepartidor);
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

    //Método que lanza una petición HTTP y elimina el repartidor actual
    public void eliminarRepartidor(View v) {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/repartidores/" + idRepartidor;

        // Crear la solicitud GET
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
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

        Toast.makeText(RepartidoresAdminEdit.this, "Repartidor eliminado con éxito", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(RepartidoresAdminEdit.this, RepartidoresAdminMain.class);
        startActivity(intent);
    }

    //Método que habilita la opción de volver a la actividad anterior
    public void Volver(View V) {
        Intent intent = new Intent(RepartidoresAdminEdit.this, RepartidoresAdminMain.class);
        startActivity(intent);
    }

}