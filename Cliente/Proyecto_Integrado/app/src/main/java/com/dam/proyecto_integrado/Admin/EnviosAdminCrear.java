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
import com.dam.proyecto_integrado.funciones.UsuarioSingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class EnviosAdminCrear extends AppCompatActivity {
    //Variables
    EditText clienteText,repartidorText,empresa,entrega,origen,destino;
    int idUsuario;

    //Método onCreate que almacena las referencias a los componentes de la actividad
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envios_admin_crear);

        idUsuario = UsuarioSingleton.getInstance().getUsuario().getId();
        clienteText = findViewById(R.id.editTextNuevoEnvioCliente);
        repartidorText = findViewById(R.id.editTextNuevoEnvioRepartidor);
        empresa = findViewById(R.id.editTextNuevoEnvioEmpresa);
        entrega = findViewById(R.id.editTextNuevoEnvioEntrega);
        origen = findViewById(R.id.editTextNuevoEnvioOrigen);
        destino = findViewById(R.id.editTextNuevoEnvioDestino);
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
    public void completarRegistro(View v){
        if(empresa.getText().toString().equals("") || entrega.getText().toString().equals("") || origen.getText().toString().equals("") || destino.getText().toString().equals("")){
            Toast toast = Toast.makeText(this, "Es necesario rellenar todos los campos Obligatorios", Toast.LENGTH_LONG);
            toast.show();
        }else{
            String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/envios";

            JSONObject newEnvio= new JSONObject();
            JSONObject cliente = new JSONObject();
            JSONObject repartidor = new JSONObject();
            JSONObject estado = new JSONObject();
            JSONObject administrador = new JSONObject();

            try {
                newEnvio.put("empresaReparto", empresa.getText().toString());
                newEnvio.put("fechaEntrega", entrega.getText().toString());
                newEnvio.put("origen", origen.getText().toString());
                newEnvio.put("destino", destino.getText().toString());
                if(!clienteText.getText().toString().equals("")){
                    cliente.put("id", clienteText.getText().toString());
                    newEnvio.put("cliente", cliente);
                }
                if(!repartidorText.getText().toString().equals("")){
                    repartidor.put("id", repartidorText.getText().toString());
                    newEnvio.put("repartidor", repartidor);
                }
                administrador.put("id", idUsuario);
                newEnvio.put("administrador", administrador);
                estado.put("id", 1);
                newEnvio.put("estado", estado);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Crear la solicitud POST
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, newEnvio, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(EnviosAdminCrear.this, "Nuevo envío creado con éxito", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(EnviosAdminCrear.this, EnviosAdminMain.class);
                    startActivity(intent);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Manejar el error de la solicitud
                            if (error.networkResponse.statusCode == 400) {
                                error.printStackTrace();
                                Toast.makeText(EnviosAdminCrear.this, "Fallo al crear el envío, rellena los campos con valores válidos", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
            );
            // Agregar la solicitud a la cola de solicitudes de Volley
            Volley.newRequestQueue(this).add(request);


        }
    }

    //Método que habilita la opción de cancelar
    public void cancelar(View v){
        Intent intent = new Intent(this, EnviosAdminMain.class);
        startActivity(intent);
    }
}