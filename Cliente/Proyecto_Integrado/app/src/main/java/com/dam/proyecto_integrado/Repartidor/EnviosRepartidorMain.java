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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dam.proyecto_integrado.Cliente.ClienteMain;
import com.dam.proyecto_integrado.Login;
import com.dam.proyecto_integrado.R;
import com.dam.proyecto_integrado.funciones.Usuario;
import com.dam.proyecto_integrado.funciones.UsuarioSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EnviosRepartidorMain extends AppCompatActivity {
    //Variables
    EditText cliente;
    Usuario usuario;
    LinearLayout layout;

    //Método onCreate que almacena las referencias a los componentes de la actividad
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envios_repartidor_main);

        usuario = UsuarioSingleton.getInstance().getUsuario();
        cliente = findViewById(R.id.editTextClienteRepartidor);
        layout = findViewById(R.id.layoutEnviosRepartidor);
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

    //Método que actualiza el layout con los envios
    public void buscarEnviosCliente(View v) {
        layout.removeAllViews();
        getEnvios();
    }

    //Método que lanza una petición HTTP y devuelve todos los envíos asignados al repartidor actual
    public void getEnvios() {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/envios/buscarRepartidorCliente/" + usuario.getId() + "/" + cliente.getText().toString();

        // Crear la solicitud GET
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Button boton = new Button(EnviosRepartidorMain.this);
                        String titulo = "Envio -> ID: " + jsonObject.getString("id") + " - Estado: " + jsonObject.getJSONObject("estado").getString("nombre");
                        boton.setText(titulo);
                        boton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(EnviosRepartidorMain.this, EnviosRepartidorEdit.class);
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
}