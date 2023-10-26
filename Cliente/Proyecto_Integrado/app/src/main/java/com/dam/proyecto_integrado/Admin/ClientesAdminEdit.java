package com.dam.proyecto_integrado.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dam.proyecto_integrado.Login;
import com.dam.proyecto_integrado.R;

import org.json.JSONException;
import org.json.JSONObject;


public class ClientesAdminEdit extends AppCompatActivity {
    //Variables
    int idCliente;
    TextView idText, nombre, apellidos, telefono, email, dni, direccion;

    //Método onCreate que almacena las referencias a los componentes de la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_admin_edit);

        idCliente = getIntent().getIntExtra("idCliente", 0);
        idText = findViewById(R.id.textViewIdAdminCliente);
        nombre = findViewById(R.id.textViewNombreAdminCliente);
        apellidos = findViewById(R.id.textViewApellidosAdminCliente);
        telefono = findViewById(R.id.textViewTelefonoAdminCliente);
        email = findViewById(R.id.textViewEmailAdminCliente);
        dni = findViewById(R.id.textViewDniAdminCliente);
        direccion = findViewById(R.id.textViewUbicacionAdminCliente);

        obtenerInfoCliente(idCliente);
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
        }if (id == R.id.gestion_repar) {
            Intent intent = new Intent(this, RepartidoresAdminMain.class);
            startActivity(intent);
            return true;
        }if (id == R.id.gestion_clien) {
            Intent intent = new Intent(this, ClientesAdminMain.class);
            startActivity(intent);
            return true;
        }if (id == R.id.datos) {
            Intent intent = new Intent(this, MisDatosAdmin.class);
            startActivity(intent);
            return true;
        }if (id == R.id.main) {
            Intent intent = new Intent(this, MainAdmin.class);
            startActivity(intent);
            return true;
        } if (id == R.id.salir) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

    //Método que lanza una petición HTTP y rellena los campos con la información actual del cliente
    public void obtenerInfoCliente(int id) {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/clientes/buscar/" + id;

        // Crear la solicitud GET
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Acceder a los datos del objeto JSON
                    idText.setText(response.getString("id"));
                    nombre.setText(response.getString("nombre"));
                    apellidos.setText(response.getString("apellidos"));
                    telefono.setText(response.getString("telefono"));
                    email.setText(response.getString("email"));
                    dni.setText(response.getString("dni"));
                    direccion.setText(response.getString("direccion"));
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

    //Método que lanza una petición HTTP y elimina el cliente actual
    public void eliminarCliente(View v) {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/repartidores/" + idCliente;

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

        Toast.makeText(ClientesAdminEdit.this, "Cliente eliminado con éxito", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ClientesAdminEdit.this, ClientesAdminMain.class);
        startActivity(intent);
    }

    //Método que habilita la opción de volver
    public void Volver(View V){
        Intent intent = new Intent(ClientesAdminEdit.this, ClientesAdminMain.class);
        startActivity(intent);
    }

}