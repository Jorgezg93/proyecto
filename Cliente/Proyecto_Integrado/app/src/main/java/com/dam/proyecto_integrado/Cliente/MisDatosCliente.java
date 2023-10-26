package com.dam.proyecto_integrado.Cliente;

import androidx.appcompat.app.AppCompatActivity;

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
import com.dam.proyecto_integrado.Login;
import com.dam.proyecto_integrado.R;
import com.dam.proyecto_integrado.funciones.Usuario;
import com.dam.proyecto_integrado.funciones.UsuarioSingleton;

import org.json.JSONException;
import org.json.JSONObject;


public class MisDatosCliente extends AppCompatActivity {
    //Variables
    TextView nombre, apellidos, direccion, contrasena, dni, telefono, email;

    //Método onCreate que almacena las referencias a los componentes de la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_datos_cliente);

        nombre = findViewById(R.id.textViewClienteMisDatosNombreText);
        apellidos = findViewById(R.id.textViewClienteMisDatosApellidosText);
        direccion = findViewById(R.id.textViewClienteMisDatosDireccionText);
        contrasena = findViewById(R.id.textViewClienteMisDatosContraseñaText);
        dni = findViewById(R.id.textViewClienteMisDatosDniText);
        telefono = findViewById(R.id.textViewClienteMisDatosTelefonoText);
        email = findViewById(R.id.textViewClienteMisDatosEmailText);

        Usuario usuario = UsuarioSingleton.getInstance().getUsuario();
        obtenerInfoCliente(usuario.getId());
    }

    //Métodos para implementar el menú personalizado en la zona superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cliente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.localizacionCliente) {
            Intent intent = new Intent(this, EnviosCliente.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.datosCliente) {
            Intent intent = new Intent(this, MisDatosCliente.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.salirCliente) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.main) {
            Intent intent = new Intent(this, ClienteMain.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    //Método que habilita la función de volver
    public void volver(View v) {
        Intent intent = new Intent(MisDatosCliente.this, ClienteMain.class);
        startActivity(intent);
    }

    //Método que obtiene la información del cliente y rellena los diferentes campos
    private void obtenerInfoCliente(int id) {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/clientes/buscar/" + id;

        // Crear la solicitud GET
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Acceder a los datos del objeto JSON
                    nombre.setText(response.getString("nombre"));
                    apellidos.setText(response.getString("apellidos"));
                    direccion.setText(response.getString("direccion"));
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