package com.dam.proyecto_integrado.Repartidor;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dam.proyecto_integrado.Login;
import com.dam.proyecto_integrado.R;
import com.dam.proyecto_integrado.funciones.UsuarioSingleton;
import com.dam.proyecto_integrado.funciones.funciones;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class LocalizacionRepartidorMain extends AppCompatActivity implements OnMapReadyCallback {
    //Variables
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100;
    GeoApiContext context;
    Location location;
    String telefono = null;
    int identificador = 0, idCliente = 0, idRepartidor;
    private Polyline polyline;
    ArrayList<LatLng> direcciones = new ArrayList<>();
    ArrayList<LatLng> waypoints = new ArrayList<>();
    private GoogleMap mMap;
    JSONArray envios = new JSONArray();
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    FirebaseDatabase firebaseDatabase;
    Button irEnvio;
    private final String URL_DATABASE = "https://proyecto-dam-2023-default-rtdb.europe-west1.firebasedatabase.app/";

    /*
    Método onCreate que lanza la actividad y prepara el correcto
    funcionamiento del mapa y del uso de la localización
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacion_repartidor_main);


        //Iniciamos la conexión con la base de datos en tiempo real
        FirebaseApp.initializeApp(LocalizacionRepartidorMain.this);
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance(URL_DATABASE);

        //Obtenemos el id de repartidor
        idRepartidor = UsuarioSingleton.getInstance().getUsuario().getId();

        //Deshabilitamos por defecto el botón para acceder al envío por si fuera nulo activarlo después
        irEnvio = findViewById(R.id.buttonIrEnvio);
        irEnvio.setEnabled(false);

        //Obtiene los envios con estado 'En reparto' asociados al repartidor
        getEnvios(idRepartidor);

        // Obtenemos el mapa y cuando este listo lo colocamos en la actividad
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        //Creamos un context para geocodificar direcciones y convertirlas en coordenadas
        context = new GeoApiContext.Builder()
                .apiKey("AIzaSyAb8VdJQxwOFUF4eoeyyanD5FDhhGzufUI")
                .build();

        // Inicializamos el FusedLocationProviderClient para obtener la posición del usuario en este caso el repartidor
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Comprobamos que el usuario otorga permisos de localización
        checkLocationPermission();
        //Lanzamos el método que pedirá las actualizaciones en la localización del usuario
        startLocationUpdates();


        // Configurar el LocationCallback necesario para obtener la localizacion
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                // Obtener la ubicación actual
                location = locationResult.getLastLocation();
                if (location != null) {
                    // Mostrar la ubicación actual en el mapa
                    try {
                        if (identificador != 0) {
                            irEnvio.setEnabled(true);
                        }
                        showCurrentLocationOnMap(location);
                        waypoints.clear();
                    } catch (IOException | InterruptedException | ApiException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
    }

    //++++++++++++++++++++++
    //Map
    //++++++++++++++++++++++
    @Override
    protected void onResume() {
        super.onResume();
        // Verificar los permisos de ubicación y solicitarlos si es necesario
        checkLocationPermission();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Detener las actualizaciones de ubicación
        stopLocationUpdates();
        limpiarFireBase(String.valueOf(UsuarioSingleton.getInstance().getUsuario().getId()));

    }

    //Método para mostrar la ubicación actual
    @SuppressLint({"SetTextI18n", "PotentialBehaviorOverride"})
    private void showCurrentLocationOnMap(Location location) throws IOException, InterruptedException, ApiException, JSONException {
        modificarRepartidor(location);

        //Personalizamos el cursor de destino
        int markerWidth = 100; // Ancho del marcador en píxeles
        int markerHeight = 100; // Alto del marcador en píxeles

        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable drawable = getResources().getDrawable(R.drawable.pngwing_com);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, markerWidth, markerHeight, false);
        BitmapDescriptor customMarker = BitmapDescriptorFactory.fromBitmap(resizedBitmap);

        // Obtener la posición actual
        LatLng ubicacion = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng puntoCercano = null;

        if(direcciones.size()!=0) {
            puntoCercano = funciones.calcularPuntoMasCercano(ubicacion, direcciones);
        }

        if (puntoCercano!=null) {
            //Buscamos la información relacionada con la ubicación de destino actual para mostrarla en el marcador
            for (int i = 0; i < envios.length(); i++) {
                JSONObject envio = envios.getJSONObject(i);
                if (puntoCercano.toString().equals(envio.getString("coordenadas"))) {
                    identificador = envio.getInt("idEnvio");
                    telefono = envio.getString("telefono");
                    idCliente = envio.getInt("idCliente");
                }
            }


            // Crear los marcadores de origen y destino
            MarkerOptions origen = new MarkerOptions()
                    .position(ubicacion)
                    .title("Mi ubicación");

            MarkerOptions destino = new MarkerOptions()
                    .position(puntoCercano)
                    .title("Destino").snippet("Id envio: " + identificador
                            + " Id cliente : " + idCliente
                            + " Teléfono: " + telefono).icon(customMarker);


            // Limpiar los marcadores existentes y agregar los nuevos marcadores
            mMap.clear();
            mMap.addMarker(origen);
            mMap.addMarker(destino);

            //Obtenemos la ruta a seguir
            obtenerRuta(ubicacion, puntoCercano);

            //Vamos borrando y pintando de nuevo la linea de la ruta
            if (polyline != null) {
                polyline.remove();
            }

            if (waypoints.size() >= 2) {
                PolylineOptions polylineOptions = new PolylineOptions().addAll(waypoints).color(Color.BLUE);
                mMap.addPolyline(polylineOptions);
                polyline = mMap.addPolyline(polylineOptions);
            }

            //Almacenamos en la base de datos en tiempo real la nueva ubicación del repartidor
            inicializarFirebase(String.valueOf(UsuarioSingleton.getInstance().getUsuario().getId()), ubicacion);
        }
    }

    //Lanzamos el método que actualiza la posición del repartidor en la base de datos
    private void modificarRepartidor(Location location) {
        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/repartidores/" + idRepartidor + "/" + location.getLatitude() + "/" + location.getLongitude() + "/" + LocalDateTime.now();

        // Crear la solicitud PUT
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

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


    //Método para habilitar las distintas herramientas en el mapa
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // Habilitar los controles de zoom y brújula
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    //Método para comprobar si se han dado los permisos a la aplicación
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Si los permisos no están concedidos, solicitarlos al usuario
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            // Si los permisos están concedidos, empezar a obtener la ubicación
            startLocationUpdates();
        }
    }

    //Método que va a ir actualizando la localizacion del usuario
    private void startLocationUpdates() {
        // Configurar la solicitud de ubicación
        if (fusedLocationProviderClient != null && locationCallback != null) {
            LocationRequest locationRequest = new LocationRequest.Builder(2500)
                    .setIntervalMillis(2500).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build();

            if (fusedLocationProviderClient != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
            }
            // Comprobar los permisos de ubicación nuevamente antes de solicitar actualizaciones de ubicación
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
            }
        }
    }

    //Cuando para la localización limpiamos el registro en la base de datos
    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        limpiarFireBase(String.valueOf(UsuarioSingleton.getInstance().getUsuario().getId()));

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
        if (id == R.id.salirRepar) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.main) {
            Intent intent = new Intent(this, MainRepartidor.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    //Método para obtener los envios y convertir las direcciones en coordenadas
    public void getEnvios(int id) {

        String url = "http://package-tracker-env.eba-q23pvsrc.eu-west-3.elasticbeanstalk.com/envios/buscarRepartidorEstado/" + id + "/2";

        // Crear la solicitud GET
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if(response.length()!=0){
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        JSONObject infoEnvio = new JSONObject();
                        infoEnvio.put("nombre", jsonObject.getJSONObject("cliente").getString("nombre") + " " + jsonObject.getJSONObject("cliente").getString("apellidos"));
                        infoEnvio.put("telefono", jsonObject.getJSONObject("cliente").getString("telefono"));
                        infoEnvio.put("idEnvio", jsonObject.getInt("id"));
                        infoEnvio.put("direccion", jsonObject.getString("destino"));
                        infoEnvio.put("idCliente", jsonObject.getJSONObject("cliente").getInt("id"));
                        infoEnvio.put("coordenadas", getCoordenadas(jsonObject.getString("destino")));

                        envios.put(infoEnvio);
                        System.out.println(envios.toString());

                        //Guardamos en un ArrayList todas las coordenadas de los envíos pendientes
                        direcciones.add(getCoordenadas(jsonObject.getString("destino")));
                    }
                    }else{
                        Toast.makeText(LocalizacionRepartidorMain.this, "Actualmente no tienes envíos pendientes en reparto", Toast.LENGTH_LONG).show();
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

    //Método que devuelve las coordenadas geocodificadas
    private LatLng getCoordenadas(String direccion) {
        Geocoder geocoder = new Geocoder(getApplicationContext(),
                Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(direccion, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = (Address) addressList.get(0);
                StringBuilder sb = new StringBuilder();
                sb.append(address.getLatitude()).append(" ");
                sb.append(address.getLongitude()).append(" ");
                return new LatLng(address.getLatitude(), address.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Método que obtiene la lista de coordenadas que representaran la ruta entre el origen y el destino
    public void obtenerRuta(LatLng origen, LatLng destino) {
        try {

            System.out.println("Aqui se genera la ruta. " + waypoints.size());
            String encodedApiKey = URLEncoder.encode("5b3ce3597851110001cf6248bc640430cbb944e4ade963d78d8ec3b9", "UTF-8");
            String encodedOrigin = URLEncoder.encode(origen.longitude + "," + origen.latitude, "UTF-8");
            String encodedDestination = URLEncoder.encode(destino.longitude + "," + destino.latitude, "UTF-8");

            String apiUrl = "https://api.openrouteservice.org/v2/directions/driving-car?" +
                    "api_key=" + encodedApiKey +
                    "&start=" + encodedOrigin +
                    "&end=" + encodedDestination;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray coordinatesArray = response.getJSONArray("features")
                                        .getJSONObject(0)
                                        .getJSONObject("geometry")
                                        .getJSONArray("coordinates");

                                for (int i = 0; i < coordinatesArray.length(); i++) {
                                    JSONArray coordinate = coordinatesArray.getJSONArray(i);
                                    waypoints.add(new LatLng(coordinate.getDouble(1), coordinate.getDouble(0)));
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // La llamada a la API no fue exitosa, maneja el error apropiadamente
                            // ...
                        }
                    });

            System.out.println("Número de puntos en la ruta: " + waypoints.size());

            Volley.newRequestQueue(this).add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Guardamos en la base de datos de Firebase la posición actual del repartidor
    private void inicializarFirebase(String id, LatLng ubicacion) {

        DatabaseReference databaseReference = firebaseDatabase.getReference("repartidores");

        Map<String, Object> repartidor = new HashMap<>();
        repartidor.put("latitud", ubicacion.latitude);
        repartidor.put("longitud", ubicacion.longitude);

        databaseReference.child(id).setValue(repartidor).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    //Método para eliminar el registro del repartidor en Firebase
    private void limpiarFireBase(String id) {
        DatabaseReference databaseReference = firebaseDatabase.getReference("repartidores");
        databaseReference.child(id).removeValue();
    }

    //Método que lleva a la actividad para editar envíos según el envío actual
    public void irEnvioActual(View v) {
        Intent intent = new Intent(LocalizacionRepartidorMain.this, EnviosRepartidorEdit.class);
        intent.putExtra("idEnvio", identificador);
        startActivity(intent);
    }

    public void refresh(View v){
        recreate(); // Relanza la actividad
    }

}