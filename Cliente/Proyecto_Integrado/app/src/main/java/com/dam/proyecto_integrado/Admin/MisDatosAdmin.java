package com.dam.proyecto_integrado.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dam.proyecto_integrado.Login;
import com.dam.proyecto_integrado.R;
import com.dam.proyecto_integrado.funciones.Usuario;
import com.dam.proyecto_integrado.funciones.UsuarioSingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class MisDatosAdmin extends AppCompatActivity {
    //Variables
    TextView nombre, apellidos, contrasena, dni, telefono, email;

    //Método onCreate que almacena las referencias a los componentes de la actividad
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_datos_admin);

        nombre = findViewById(R.id.textViewAdminMisDatosNombreText);
        apellidos = findViewById(R.id.textViewAdminMisDatosApellidosText);
        contrasena = findViewById(R.id.textViewAdminMisDatosContraseñaText);
        dni = findViewById(R.id.textViewAdminMisDatosDniText);
        telefono = findViewById(R.id.textViewAdminMisDatosTelefonoText);
        email = findViewById(R.id.textViewAdminMisDatosEmailText);

        Usuario usuario = UsuarioSingleton.getInstance().getUsuario();
        obtenerInfoAdmin(usuario.getId());
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

    //Método que habilita la función de volver
    public void volver(View v) {
        Intent intent = new Intent(this, MainAdmin.class);
        startActivity(intent);
    }

    //Método que obtiene la información del admin y rellena los diferentes campos
    private void obtenerInfoAdmin(int id) {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/administradores/buscar/" + id;

        // Crear la solicitud GET
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Acceder a los datos del objeto JSON
                    nombre.setText(response.getString("nombre"));
                    apellidos.setText(response.getString("apellidos"));
                    email.setText(response.getString("email"));
                    dni.setText(response.getString("dni"));
                    telefono.setText(response.getString("telefono"));
                    contrasena.setText(response.getString("password"));
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
}