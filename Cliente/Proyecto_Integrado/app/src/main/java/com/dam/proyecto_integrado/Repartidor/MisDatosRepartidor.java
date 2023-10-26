package com.dam.proyecto_integrado.Repartidor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dam.proyecto_integrado.Cliente.ClienteMain;
import com.dam.proyecto_integrado.Login;
import com.dam.proyecto_integrado.R;
import com.dam.proyecto_integrado.funciones.Usuario;
import com.dam.proyecto_integrado.funciones.UsuarioSingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class MisDatosRepartidor extends AppCompatActivity {
    //Variables
    TextView nombre, apellidos, contrasena, dni, telefono, email;

    //Método onCreate que almacena las referencias a los componentes de la actividad
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_datos_repartidor);

        nombre = findViewById(R.id.textViewRepartidorMisDatosNombreText);
        apellidos = findViewById(R.id.textViewRepartidorMisDatosApellidosText);
        contrasena = findViewById(R.id.textViewRepartidorMisDatosContraseñaText);
        dni = findViewById(R.id.textViewRepartidorMisDatosDniText);
        telefono = findViewById(R.id.textViewRepartidorMisDatosTelefonoText);
        email = findViewById(R.id.textViewRepartidorMisDatosEmailText);

        Usuario usuario = UsuarioSingleton.getInstance().getUsuario();
        obtenerInfoRepartidor(usuario.getId());
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

    //Método que habilita la función de volver
    public void volver(View v) {
        Intent intent = new Intent(this, MainRepartidor.class);
        startActivity(intent);
    }

    //Método que obtiene la información del cliente y rellena los diferentes campos
    private void obtenerInfoRepartidor(int id) {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/repartidores/buscar/" + id;

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