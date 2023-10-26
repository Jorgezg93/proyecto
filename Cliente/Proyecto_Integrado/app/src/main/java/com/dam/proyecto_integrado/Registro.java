package com.dam.proyecto_integrado;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    //Variables
    EditText nombre, apellidos, telefono, email, dni, direccion, contrasena;

    //Método onCreate que almacena las referencias a los componentes de la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nombre = findViewById(R.id.editTextNombre);
        apellidos = findViewById(R.id.editTextApellidos);
        telefono = findViewById(R.id.editTextTelefono);
        email = findViewById(R.id.editTextEmail);
        dni = findViewById(R.id.editTextDni);
        direccion = findViewById(R.id.editTextDireccion);
        contrasena = findViewById(R.id.editTextTextPassword);

    }

    //Método que realiza una petición HTTP Post con los datos del nuevo cliente si todos los campos se han rellenado correctamente
    public void completarRegistro(View v) {
        if (nombre.getText().toString().equals("") || apellidos.getText().toString().equals("") || telefono.getText().toString().equals("") || email.getText().toString().equals("") || dni.getText().toString().equals("") || contrasena.getText().toString().equals("") || direccion.getText().toString().equals("")) {
            Toast toast = Toast.makeText(this, "Es necesario rellenar todos los campos", Toast.LENGTH_LONG);
            toast.show();
        } else {
            String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/clientes";

            JSONObject newCliente = new JSONObject();
            JSONObject rol = new JSONObject();

            try {
                newCliente.put("nombre", nombre.getText().toString());
                newCliente.put("apellidos", apellidos.getText().toString());
                newCliente.put("telefono", telefono.getText().toString());
                newCliente.put("email", email.getText().toString());
                newCliente.put("dni", dni.getText().toString());
                newCliente.put("direccion", direccion.getText().toString());
                newCliente.put("password", contrasena.getText().toString());
                rol.put("id", 1);
                newCliente.put("rol", rol);

                System.out.println(newCliente);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Crear la solicitud POST
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, newCliente, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(nombre.getText().toString());
                    System.out.println(email.getText().toString());
                    Toast.makeText(Registro.this, "Usuario registrado con éxito", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Registro.this, Login.class);
                    startActivity(intent);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                                // Manejar el error de la solicitud
                                Toast.makeText(Registro.this, "Ya hay un usuario registrado con ese email", Toast.LENGTH_LONG).show();
                                error.printStackTrace();
                            }
                        }
                    }
            );
            // Agregar la solicitud a la cola de solicitudes de Volley
            Volley.newRequestQueue(this).add(request);


        }
    }

    public void Volver(View v){
        Intent intent = new Intent(Registro.this, Login.class);
        startActivity(intent);
    }
}
