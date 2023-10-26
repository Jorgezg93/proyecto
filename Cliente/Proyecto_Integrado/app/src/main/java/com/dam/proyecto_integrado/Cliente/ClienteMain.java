package com.dam.proyecto_integrado.Cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dam.proyecto_integrado.Admin.MainAdmin;
import com.dam.proyecto_integrado.Login;
import com.dam.proyecto_integrado.R;
import com.dam.proyecto_integrado.funciones.Usuario;
import com.dam.proyecto_integrado.funciones.UsuarioSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClienteMain extends AppCompatActivity {
    //Variables
    LinearLayout layout;
    Usuario usuario;
    TextView titulo;

    //Método onCreate donde cogemos las referencias a los componentes de Android
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_main);

        layout = findViewById(R.id.layoutEnviosCliente);
        usuario = UsuarioSingleton.getInstance().getUsuario();
        titulo = findViewById(R.id.textViewTituloClienteMain);

        getEnvios();

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

    //Método que obtiene los envíos asociados al cliente usuario
    @SuppressLint("SetTextI18n")
    public void getEnvios() {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/envios/buscarCliente/" + usuario.getId();

        //Colocamos el titulo en la zona superior de la actividad
        titulo.setText(titulo.getText() + " " + usuario.getNombre());
        // Crear la solicitud GET
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    //Recorremolinos los envíos y vamos rellenando la lista en botones
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Button boton = new Button(ClienteMain.this);
                        String titulo = "Envio" + jsonObject.getString("id") + " - Estado: " + jsonObject.getJSONObject("estado").getString("nombre");
                        System.out.println(titulo);

                        boton.setText(titulo);
                        boton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ClienteMain.this, EnviosCliente.class);
                                try {
                                    intent.putExtra("idEnvio", jsonObject.getInt("id"));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                startActivity(intent);
                            }
                        });
                        //Finalmente añadimos los botones al layout contenedor
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