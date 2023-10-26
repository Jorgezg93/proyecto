package com.dam.proyecto_integrado.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class RepartidoresAdminCrear extends AppCompatActivity {
    //Variables
    EditText nombre, apellidos, dni, telefono, email, password;

    //Método onCreate que almacena las referencias a los componentes de la actividad
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repartidores_admin_crear);

        nombre = findViewById(R.id.editTextNuevoReparNombre);
        apellidos = findViewById(R.id.editTextNuevoReparApellidos);
        dni = findViewById(R.id.editTextNuevoRepartidorDni);
        telefono = findViewById(R.id.editTextNuevoReparTelefono);
        email = findViewById(R.id.editTextNuevoReparEmail);
        password = findViewById(R.id.editTextNuevoReparContrasena);
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

    //Método que coge toda la información de los diferentes campos y realiza la petición post correspondiente
    public void completarRegistro(View v) {
        if (nombre.getText().toString().equals("") || apellidos.getText().toString().equals("") || dni.getText().toString().equals("") || telefono.getText().toString().equals("") || password.getText().toString().equals("") || email.getText().toString().equals("")) {
            Toast toast = Toast.makeText(this, "Es necesario rellenar todos los campos Obligatorios", Toast.LENGTH_LONG);
            toast.show();
        } else {
            String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/repartidores";

            JSONObject newRepartidor = new JSONObject();
            JSONObject rol = new JSONObject();

            try {
                newRepartidor.put("nombre", nombre.getText().toString());
                newRepartidor.put("apellidos", apellidos.getText().toString());
                newRepartidor.put("email", email.getText().toString());
                newRepartidor.put("password", password.getText().toString());
                newRepartidor.put("telefono", telefono.getText().toString());
                newRepartidor.put("dni", dni.getText().toString());
                rol.put("id", 2);
                newRepartidor.put("rol", rol);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Crear la solicitud POST
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, newRepartidor, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(RepartidoresAdminCrear.this, "Repartidor registrado con éxito", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RepartidoresAdminCrear.this, RepartidoresAdminMain.class);
                    startActivity(intent);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Manejar el error de la solicitud
                            if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                                error.printStackTrace();
                                Toast.makeText(RepartidoresAdminCrear.this, "Fallo al crear el envío, rellena los campos con valores válidos", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
            );
            // Agregar la solicitud a la cola de solicitudes de Volley
            Volley.newRequestQueue(this).add(request);


        }
    }

    //Método que habilita la opción de cancelar
    public void cancelar(View v) {
        Intent intent = new Intent(this, RepartidoresAdminMain.class);
        startActivity(intent);
    }
}