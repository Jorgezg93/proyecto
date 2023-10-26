package com.dam.proyecto_integrado;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dam.proyecto_integrado.Admin.MainAdmin;
import com.dam.proyecto_integrado.Cliente.ClienteMain;
import com.dam.proyecto_integrado.Repartidor.MainRepartidor;
import com.dam.proyecto_integrado.funciones.Usuario;
import com.dam.proyecto_integrado.funciones.UsuarioSingleton;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;


public class Login extends AppCompatActivity {

    //Variables
    Boolean resultado = false;
    EditText usuario, password;
    Spinner roles;
    String emailUsuario, passwordUsuario, rolUsuario;
    Usuario usuarioConectado;
    int rol, id;


    //Método onCreate donde cogemos las referencias a los componentes de Android
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        usuario = findViewById(R.id.editTextUsuario);
        password = findViewById(R.id.editTextPassword);
        roles = findViewById(R.id.spinnerLogin);
        getRoles();

    }

    //Método que lleva a la actividad de Registro
    public void registrar(View v) {
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }

    //Método que obtiene los valores introducidos y llama al método que comprueba si es correcto
    public void login(View v) throws InterruptedException {
        emailUsuario = usuario.getText().toString().trim();
        passwordUsuario = password.getText().toString().trim();
        switch (roles.getSelectedItem().toString()) {
            case "Repartidor":
                rolUsuario = "repartidores";
                checkPassword(emailUsuario);
                break;
            case "Admin":
                rolUsuario = "administradores";
                checkPassword(emailUsuario);
                break;
            case "Cliente":
                rolUsuario = "clientes";
                checkPassword(emailUsuario);
                break;
            default:
                break;
        }
        System.out.println(resultado);

    }

    //Método que se lanza si el login es correcto y lanza la actividad correspondiente al rol del usuario
    private void loginCorrecto() {
        if (resultado) {
            switch (rol) {
                case 1:
                    Intent intentClient = new Intent(this, ClienteMain.class);
                    startActivity(intentClient);
                    break;
                case 2:
                    Intent intentRepartidor = new Intent(this, MainRepartidor.class);
                    startActivity(intentRepartidor);
                    break;
                case 3:
                    Intent intentAdmin = new Intent(this, MainAdmin.class);
                    startActivity(intentAdmin);
                    break;
                default:
                    break;
            }

        }
    }

    //Método que se lanza si el login es incorrecto y muestra un mensaje por pantalla informativo
    private void loginIncorrecto() {
        Toast toast = Toast.makeText(this, "Usuario/Contraseña no válidos", Toast.LENGTH_LONG);
        toast.show();
        System.out.println();
    }

    //Método que lanza una petición http que busca la correlación entre los datos introducidos y los de la base de datos
    public void checkPassword(String emailUsuario) {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/" + rolUsuario + "/buscarEmail/" + emailUsuario;

        // Crear la solicitud GET
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("email") && response.has("email")) {
                        // Acceder a los datos del objeto JSON
                        String passwordUser = response.getString("password");
                        String emailUser = response.getString("email");

                        id = response.getInt("id");
                        rol = response.getJSONObject("rol").getInt("id");
                        resultado = passwordUser.equals(passwordUsuario) && emailUser.equals(emailUsuario);

                        usuarioConectado = new Usuario(id, passwordUser, response.getString("password"), rol, response.getString("nombre"));
                        UsuarioSingleton.getInstance().setUsuario(usuarioConectado);
                        System.out.println(UsuarioSingleton.getInstance().getUsuario().toString());

                        if (resultado) {
                            loginCorrecto();
                        } else {
                            loginIncorrecto();
                        }
                    }else{
                        loginIncorrecto();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    loginIncorrecto();
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

    //Método que obtiene los roles almacenados para rellenar el spinner
    public void getRoles() {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/roles";

        // Crear la solicitud GET
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<String> elementos = new ArrayList<>();

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String campo = jsonObject.getString("nombre");
                        elementos.add(campo);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Login.this, android.R.layout.simple_list_item_1, elementos);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    roles.setAdapter(adapter);
                    roles.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
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






