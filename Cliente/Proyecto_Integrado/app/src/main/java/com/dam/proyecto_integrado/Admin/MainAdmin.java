package com.dam.proyecto_integrado.Admin;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dam.proyecto_integrado.Login;
import com.dam.proyecto_integrado.R;
import com.dam.proyecto_integrado.funciones.Usuario;
import com.dam.proyecto_integrado.funciones.UsuarioSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainAdmin extends AppCompatActivity {
    //Variables
    LinearLayout layout;
    Usuario usuario;

    //Método onCreate que almacena las referencias a los componentes de la actividad y buscamos los envíos asociados al administrador
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        layout = findViewById(R.id.MainAdminLayout);
        usuario = UsuarioSingleton.getInstance().getUsuario();

        getEnvios();
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

    //Método que lanza una petición HTTP y devuelve todos los envíos asignados al administrador actual
    public void getEnvios() {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/envios/buscarAdministrador/" + usuario.getId();

        // Crear la solicitud GET
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Button boton = new Button(MainAdmin.this);
                        String titulo = "ID: " + jsonObject.getString("id") + " - Estado: " + jsonObject.getJSONObject("estado").getString("nombre");
                        boton.setText(titulo);
                        boton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainAdmin.this, EnviosAdminEdit.class);
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
                    }
                }
        );

        // Agregar la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(this).add(request);
    }

    //Método que lanza el envío de un email a la cuenta de soporte (prueba)
    public void soporte(View v) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String destinatario = "jzamgon002@iesfuengirola1.es";
        String asunto = "";
        String cuerpo = "";

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{destinatario});
        intent.putExtra(Intent.EXTRA_SUBJECT, asunto);
        intent.putExtra(Intent.EXTRA_TEXT, cuerpo);

        startActivity(Intent.createChooser(intent, "Enviar correo electronico"));
    }

}