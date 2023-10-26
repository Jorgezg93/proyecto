package com.dam.proyecto_integrado.Cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dam.proyecto_integrado.Login;
import com.dam.proyecto_integrado.R;
import com.dam.proyecto_integrado.funciones.Usuario;
import com.dam.proyecto_integrado.funciones.UsuarioSingleton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EnviosCliente extends AppCompatActivity implements OnMapReadyCallback {

    //Variables
    private GoogleMap mMap;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Spinner spinner;
    private int repartidorId, idEnvio, estadoEnvio;
    private TextView id, telefono, estado;
    private final String URL_DATABASE = "https://proyecto-dam-2023-default-rtdb.europe-west1.firebasedatabase.app/";

    /*
    Método onCreate que almacena las referencias a los componentes de la actividad,
    obtiene el usuario actual y busca los envíos asignados a ese cliente.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envios_cliente);

        Intent intent = getIntent();
        idEnvio = intent.getIntExtra("idEnvio", 0);
        id = findViewById(R.id.textViewIdEnvioClienteLocalizacion);
        telefono = findViewById(R.id.textViewTepartidorEnvioCliente);
        estado = findViewById(R.id.textViewEstadoEnvioCliente);
        spinner = findViewById(R.id.spinnerEnvioCliente);

        Usuario usuario = UsuarioSingleton.getInstance().getUsuario();
        getEnvios(usuario.getId());


        if (idEnvio != 0) {
            getDatosEnvio(idEnvio);
        }

        //Iniciamos la conexión con la base de datos en tiempo real
        inicializarFirebase();

        // Obtenemos el mapa y cuando este listo lo colocamos en la actividad
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        // Colocamos la información en los textView cada vez que se seleccione un envio en el spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                idEnvio = Integer.parseInt(selectedItem);
                getDatosEnvio(Integer.parseInt(selectedItem));

                //En caso de que el estado no sea "en tránsito" no se mostrará la ubicación del repartidor en el mapa
                if (estadoEnvio != 2) {
                    Toast.makeText(EnviosCliente.this, "No hay ningún repartidor para tu envío, ya que no se encuentra en tránsito", Toast.LENGTH_LONG).show();
                } else {
                    inicializarFirebase();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Inicializamos la base de datos en tiempo real, y obtenemos, si están disponibles, las coordenadas del repartidor
    private void inicializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance(URL_DATABASE);
        databaseReference = firebaseDatabase.getReference();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("repartidores").child(String.valueOf(repartidorId)).exists()) {
                    double lat = (double) snapshot.child("repartidores").child(String.valueOf(repartidorId)).child("latitud").getValue();
                    double lon = (double) snapshot.child("repartidores").child(String.valueOf(repartidorId)).child("longitud").getValue();
                    System.out.println(lat + lon);
                    setRepartidor(new LatLng(lat, lon));
                } else {
                    if (mMap != null) {
                        mMap.clear();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addValueEventListener(postListener);
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

    //Método que añade diferentes funcionalidades al mapa cuando esté listo
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // Configura las opciones del mapa
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
    }

    //Método que coloca la ubicación del repartidor
    private void setRepartidor(LatLng ubicacionRepartidor) {
        mMap.clear();
        MarkerOptions repartidor = new MarkerOptions().position(ubicacionRepartidor).title("Repartidor");
        mMap.addMarker(repartidor);
    }

    //Método que lanza una petición HTTP y devuelve todos los envíos asignados al cliente actual
    public void getEnvios(int id) {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/envios/buscarCliente/" + id;

        // Crear la solicitud GET
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<Integer> elementos = new ArrayList<>();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        repartidorId = jsonObject.getJSONObject("repartidor").getInt("id");
                        elementos.add(jsonObject.getInt("id"));
                    }
                    ArrayAdapter<Integer> adapter = new ArrayAdapter<>(EnviosCliente.this, android.R.layout.simple_list_item_1, elementos);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

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

    //Método que lanza una petición HTTP y devuelve todos los datos del envío con el id que se pasa como parámetro
    public void getDatosEnvio(int id) {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/envios/buscarId/" + id;

        // Crear la solicitud GET
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("id") == idEnvio) {
                        System.out.println(response.getJSONObject("estado").getString("nombre"));
                        rellenarDatos(String.valueOf(response.getInt("id")), response.getJSONObject("repartidor").getString("telefono"), response.getJSONObject("estado").getString("nombre"));
                    }
                    estadoEnvio = response.getJSONObject("estado").getInt("id");
                    repartidorId = response.getJSONObject("repartidor").getInt("id");
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
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

    //Método auxiliar que rellena los diferentes campos con la información del envío
    private void rellenarDatos(String idEnvio, String telefonoEnvio, String estadoEnvio) {
        id.setText(idEnvio);
        telefono.setText(telefonoEnvio);
        estado.setText(estadoEnvio);
    }
}