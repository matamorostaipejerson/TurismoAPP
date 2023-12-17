package com.example.turismopampas_hhh.Home.FragmentFoot.Mapa;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.example.turismopampas_hhh.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class GoogleMapsFragment extends Fragment implements OnMapReadyCallback {

    float distancia=0f;
    //mas datos
    TextView txtCad,txtRun,txtByker,txtDistancia;
    FloatingActionButton btnRuta;
    Polyline poly=null;

    //coordenadas
    String latitudString="";
    String longitudString ="";
    String fCoordenada="";
    String fMyCoordenada = "";
    String myLatitudString = "";
    String myLongitudString = "";

    //Obtener ultoma ubicacion
    private FusedLocationProviderClient fusedLocationClient;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 0;

    private String mParam1;
    private String mParam2;
    private GoogleMap map;

    //retrofit
    private Retrofit retrofit;
    //servicio
    private MyService myService;


    public static GoogleMapsFragment newInstance(String param1, String param2) {
        GoogleMapsFragment fragment = new GoogleMapsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_google_maps, container, false);

        //servici
        myService=new MyService();

        //localizacion
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        // Encuentra las referencias a tus vistas
        LinearLayout btnExploreOptions = view.findViewById(R.id.btnExploreOptions);
        FloatingActionButton btnExplore = view.findViewById(R.id.BtnExplore);

        //Mas datos
        txtDistancia = view.findViewById(R.id.txtDistancia);
        txtCad = view.findViewById(R.id.txtCad);
        txtRun = view.findViewById(R.id.txtRun);
        txtByker = view.findViewById(R.id.txtByker);

        //Botonerws Info
        btnRuta = view.findViewById(R.id.btnRuta);
        LinearLayout btnDistancia = view.findViewById(R.id.btnDistancia);
        LinearLayout btnOptions = view.findViewById(R.id.btnOptions);
        LinearLayout btnCar = view.findViewById(R.id.btnCar);
        LinearLayout btnRun = view.findViewById(R.id.btnRun);
        LinearLayout btnByker = view.findViewById(R.id.btnByker);
        btnRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (map != null) {
                    poly=null;
                    fCoordenada=longitudString+","+latitudString;
                    fMyCoordenada=myLongitudString+","+myLatitudString;
                    MiHilo miHilo= new MiHilo(myService);
                    miHilo.start();
                    btnDistancia.setVisibility(View.VISIBLE);
                    btnCar.setVisibility(View.VISIBLE);
                    btnRun.setVisibility(View.VISIBLE);
                    btnByker.setVisibility(View.VISIBLE);
                    setDatos();
                }
            }
        });

        //Botones ligares
        Button btnYauricocha = view.findViewById(R.id.btnYauricocha);
        Button btnQhapaq = view.findViewById(R.id.btnQhapaq);
        Button btnTablachaca = view.findViewById(R.id.btnTablachaca);
        Button btnPillo = view.findViewById(R.id.btnPillo);
        Button btnCataratas = view.findViewById(R.id.btnCataratas);

        //Yaulicocha
        btnYauricocha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coordenada = new LatLng(-12.381799, -75.056805);
                distancia=52f;
                String nombre = "Laguna Yaulicocha, Pucará";
                lugarMap(coordenada, nombre);
                latitudString = getLatitudString(coordenada);
                longitudString = getLongitudString(coordenada);
                Log.i("lt","latitud"+latitudString);
                Log.i("lt","longitud"+longitudString);
                Log.i("lt", "mi Latitud: " + myLatitudString + ", mi Longitud: "+myLongitudString );
                btnExploreOptions.setVisibility(View.GONE);
                btnDistancia.setVisibility(View.GONE);
                btnCar.setVisibility(View.GONE);
                btnRun.setVisibility(View.GONE);
                btnByker.setVisibility(View.GONE);
                btnOptions.setVisibility(View.VISIBLE);
            }
        });
        //Qhapaq Ñam
        btnQhapaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coordenada = new LatLng(-12.400663, -74.840872);
                distancia=1f;
                String nombre = "Ruta del Qhapaq Ñam";
                lugarMap(coordenada, nombre);
                latitudString = getLatitudString(coordenada);
                longitudString = getLongitudString(coordenada);
                Log.i("lt","latitud"+latitudString);
                Log.i("lt","longitud"+longitudString);
                Log.i("lt", "mi Latitud: " + myLatitudString + ", mi Longitud: "+myLongitudString );
                btnExploreOptions.setVisibility(View.GONE);
                btnDistancia.setVisibility(View.GONE);
                btnCar.setVisibility(View.GONE);
                btnRun.setVisibility(View.GONE);
                btnByker.setVisibility(View.GONE);
                btnOptions.setVisibility(View.VISIBLE);
            }
        });
        //Tablachaca
        btnTablachaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coordenada = new LatLng(-12.464404, -74.786558);
                distancia=52.3f;
                String nombre = "Represa Tablachaca";
                lugarMap(coordenada, nombre);
                latitudString = getLatitudString(coordenada);
                longitudString = getLongitudString(coordenada);
                Log.i("lt","latitud"+latitudString);
                Log.i("lt","longitud"+longitudString);
                Log.i("lt", "mi Latitud: " + myLatitudString + ", mi Longitud: "+myLongitudString );
                btnExploreOptions.setVisibility(View.GONE);
                btnDistancia.setVisibility(View.GONE);
                btnCar.setVisibility(View.GONE);
                btnRun.setVisibility(View.GONE);
                btnByker.setVisibility(View.GONE);
                btnOptions.setVisibility(View.VISIBLE);
            }
        });
        //Pillo
        btnPillo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coordenada = new LatLng(-12.381799, -74.942896);
                distancia=11.2f;
                String nombre = "San Juan de Pillo";
                lugarMap(coordenada, nombre);
                latitudString = getLatitudString(coordenada);
                longitudString = getLongitudString(coordenada);
                Log.i("lt","latitud"+latitudString);
                Log.i("lt","longitud"+longitudString);
                Log.i("lt", "mi Latitud: " + myLatitudString + ", mi Longitud: "+myLongitudString );
                btnExploreOptions.setVisibility(View.GONE);
                btnDistancia.setVisibility(View.GONE);
                btnCar.setVisibility(View.GONE);
                btnRun.setVisibility(View.GONE);
                btnByker.setVisibility(View.GONE);
                btnOptions.setVisibility(View.VISIBLE);
            }
        });
        //Cataratas
        btnCataratas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coordenada = new LatLng(-12.043825, -74.568177);
                distancia=151f;
                String nombre = "Catarata Velapaccha";
                lugarMap(coordenada, nombre);
                latitudString = getLatitudString(coordenada);
                longitudString = getLongitudString(coordenada);
                Log.i("lt","latitud"+latitudString);
                Log.i("lt","longitud"+longitudString);
                Log.i("lt", "mi Latitud: " + myLatitudString + ", mi Longitud: "+myLongitudString );
                btnExploreOptions.setVisibility(View.GONE);
                btnDistancia.setVisibility(View.GONE);
                btnCar.setVisibility(View.GONE);
                btnRun.setVisibility(View.GONE);
                btnByker.setVisibility(View.GONE);
                btnOptions.setVisibility(View.VISIBLE);
            }
        });
        //Botones HOTELES
        Button btnPlaza = view.findViewById(R.id.btnPlaza);
        Button btnConfort = view.findViewById(R.id.btnConfort);
        Button btnJardin = view.findViewById(R.id.btnJardin);
        Button btnAngeles = view.findViewById(R.id.btnAngeles);
        Button btnSurge = view.findViewById(R.id.btnSurge);
        Button btnBlue = view.findViewById(R.id.btnBlue);

        //Plaza
        btnPlaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coordenada = new LatLng(-12.399085, -74.868331);
                distancia=1.7f;
                String nombre = "Hotel Plaza Pampas";
                hotelMap(coordenada, nombre);
                latitudString = getLatitudString(coordenada);
                longitudString = getLongitudString(coordenada);
                Log.i("lt","latitud"+latitudString);
                Log.i("lt","longitud"+longitudString);
                Log.i("lt", "mi Latitud: " + myLatitudString + ", mi Longitud: "+myLongitudString );
                btnExploreOptions.setVisibility(View.GONE);
                btnDistancia.setVisibility(View.GONE);
                btnCar.setVisibility(View.GONE);
                btnRun.setVisibility(View.GONE);
                btnByker.setVisibility(View.GONE);
                btnOptions.setVisibility(View.VISIBLE);
            }
        });
        //CONFORT
        btnConfort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coordenada = new LatLng(-12.398298, -74.867964);
                distancia=1.6f;
                String nombre = "HOTEL D'CONFORT";
                hotelMap(coordenada, nombre);
                latitudString = getLatitudString(coordenada);
                longitudString = getLongitudString(coordenada);
                Log.i("lt","latitud"+latitudString);
                Log.i("lt","longitud"+longitudString);
                Log.i("lt", "mi Latitud: " + myLatitudString + ", mi Longitud: "+myLongitudString );
                btnExploreOptions.setVisibility(View.GONE);
                btnDistancia.setVisibility(View.GONE);
                btnCar.setVisibility(View.GONE);
                btnRun.setVisibility(View.GONE);
                btnByker.setVisibility(View.GONE);
                btnOptions.setVisibility(View.VISIBLE);
            }
        });
        //Jardin
        btnJardin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coordenada = new LatLng(-12.399504478878724, -74.87168642567366);
                distancia=2f;
                String nombre = "Hotel Jardin";
                hotelMap(coordenada, nombre);
                latitudString = getLatitudString(coordenada);
                longitudString = getLongitudString(coordenada);
                Log.i("lt","latitud"+latitudString);
                Log.i("lt","longitud"+longitudString);
                Log.i("lt", "mi Latitud: " + myLatitudString + ", mi Longitud: "+myLongitudString );
                btnExploreOptions.setVisibility(View.GONE);
                btnDistancia.setVisibility(View.GONE);
                btnCar.setVisibility(View.GONE);
                btnRun.setVisibility(View.GONE);
                btnByker.setVisibility(View.GONE);
                btnOptions.setVisibility(View.VISIBLE);
            }
        });
        //ANGELES
        btnAngeles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coordenada = new LatLng(-12.398272600247024, -74.86836631414032);
                distancia=1.6f;
                String nombre = "HOTEL TURÍSTICO \"LOS ANGELES\"";
                hotelMap(coordenada, nombre);
                latitudString = getLatitudString(coordenada);
                longitudString = getLongitudString(coordenada);
                Log.i("lt","latitud"+latitudString);
                Log.i("lt","longitud"+longitudString);
                Log.i("lt", "mi Latitud: " + myLatitudString + ", mi Longitud: "+myLongitudString );
                btnExploreOptions.setVisibility(View.GONE);
                btnDistancia.setVisibility(View.GONE);
                btnCar.setVisibility(View.GONE);
                btnRun.setVisibility(View.GONE);
                btnByker.setVisibility(View.GONE);
                btnOptions.setVisibility(View.VISIBLE);
            }
        });
        //Surge
        btnSurge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coordenada = new LatLng(-12.395813096030908, -74.86664417773683);
                distancia=1.4f;
                String nombre = "Hotel SAM";
                hotelMap(coordenada, nombre);
                latitudString = getLatitudString(coordenada);
                longitudString = getLongitudString(coordenada);
                Log.i("lt","latitud"+latitudString);
                Log.i("lt","longitud"+longitudString);
                Log.i("lt", "mi Latitud: " + myLatitudString + ", mi Longitud: "+myLongitudString );
                btnExploreOptions.setVisibility(View.GONE);
                btnDistancia.setVisibility(View.GONE);
                btnCar.setVisibility(View.GONE);
                btnRun.setVisibility(View.GONE);
                btnByker.setVisibility(View.GONE);
                btnOptions.setVisibility(View.VISIBLE);
            }
        });
        //Blue
        btnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coordenada = new LatLng(-12.398655569848055, -74.86800840514496);
                distancia=1.7f;
                String nombre = "Hotel Blue Moon";
                hotelMap(coordenada, nombre);
                latitudString = getLatitudString(coordenada);
                longitudString = getLongitudString(coordenada);
                Log.i("lt","latitud"+latitudString);
                Log.i("lt","longitud"+longitudString);
                Log.i("lt", "mi Latitud: " + myLatitudString + ", mi Longitud: "+myLongitudString );
                btnExploreOptions.setVisibility(View.GONE);
                btnDistancia.setVisibility(View.GONE);
                btnCar.setVisibility(View.GONE);
                btnRun.setVisibility(View.GONE);
                btnByker.setVisibility(View.GONE);
                btnOptions.setVisibility(View.VISIBLE);
            }
        });

        //Botones RESTAURANTES
        Button btnDnofox = view.findViewById(R.id.btnDnofox);
        Button btnTaya = view.findViewById(R.id.btnTaya);
        Button btnGondolas = view.findViewById(R.id.btnGondolas);
        Button btnGruta = view.findViewById(R.id.btnGruta);
        Button btnYonatans = view.findViewById(R.id.btnYonatans);
        Button btnTayata = view.findViewById(R.id.btnTayata);
        //Dnofox
        btnDnofox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coordenada = new LatLng(-12.400045873332802, -74.86631542904676);
                distancia=1.7f;
                String nombre = "Cafe Dnofox";
                hotelMap(coordenada, nombre);
                latitudString = getLatitudString(coordenada);
                longitudString = getLongitudString(coordenada);
                Log.i("lt","latitud"+latitudString);
                Log.i("lt","longitud"+longitudString);
                Log.i("lt", "mi Latitud: " + myLatitudString + ", mi Longitud: "+myLongitudString );
                btnExploreOptions.setVisibility(View.GONE);
                btnDistancia.setVisibility(View.GONE);
                btnCar.setVisibility(View.GONE);
                btnRun.setVisibility(View.GONE);
                btnByker.setVisibility(View.GONE);
                btnOptions.setVisibility(View.VISIBLE);
            }
        });
        //TAYA
        btnTaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coordenada = new LatLng(-12.399415992751548, -74.8685210696581);
                distancia=1.9f;
                String nombre = "Pollería \"LA TAYA\"";
                hotelMap(coordenada, nombre);
                latitudString = getLatitudString(coordenada);
                longitudString = getLongitudString(coordenada);
                Log.i("lt","latitud"+latitudString);
                Log.i("lt","longitud"+longitudString);
                Log.i("lt", "mi Latitud: " + myLatitudString + ", mi Longitud: "+myLongitudString );
                btnExploreOptions.setVisibility(View.GONE);
                btnDistancia.setVisibility(View.GONE);
                btnCar.setVisibility(View.GONE);
                btnRun.setVisibility(View.GONE);
                btnByker.setVisibility(View.GONE);
                btnOptions.setVisibility(View.VISIBLE);
            }
        });
        //Góndolas
        btnGondolas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coordenada = new LatLng(-12.398487480010465, -74.86912557252096);
                distancia=1.7f;
                String nombre = "Pollos y parrillas \"Góndolas\"";
                hotelMap(coordenada, nombre);
                latitudString = getLatitudString(coordenada);
                longitudString = getLongitudString(coordenada);
                Log.i("lt","latitud"+latitudString);
                Log.i("lt","longitud"+longitudString);
                Log.i("lt", "mi Latitud: " + myLatitudString + ", mi Longitud: "+myLongitudString );
                btnExploreOptions.setVisibility(View.GONE);
                btnDistancia.setVisibility(View.GONE);
                btnCar.setVisibility(View.GONE);
                btnRun.setVisibility(View.GONE);
                btnByker.setVisibility(View.GONE);
                btnOptions.setVisibility(View.VISIBLE);
            }
        });
        //Gruta
        btnGruta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coordenada = new LatLng(-12.390717501656296, -74.87027535460511);
                distancia=1.9f;
                String nombre = "Recreo campestre \"La Gruta\"";
                hotelMap(coordenada, nombre);
                latitudString = getLatitudString(coordenada);
                longitudString = getLongitudString(coordenada);
                Log.i("lt","latitud"+latitudString);
                Log.i("lt","longitud"+longitudString);
                Log.i("lt", "mi Latitud: " + myLatitudString + ", mi Longitud: "+myLongitudString );
                btnExploreOptions.setVisibility(View.GONE);
                btnDistancia.setVisibility(View.GONE);
                btnCar.setVisibility(View.GONE);
                btnRun.setVisibility(View.GONE);
                btnByker.setVisibility(View.GONE);
                btnOptions.setVisibility(View.VISIBLE);
            }
        });
        //Djonatans
        btnYonatans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coordenada = new LatLng(-12.398073870669926, -74.86718883270895);
                distancia=1.5f;
                String nombre = "Chifa Djonatans";
                hotelMap(coordenada, nombre);
                latitudString = getLatitudString(coordenada);
                longitudString = getLongitudString(coordenada);
                Log.i("lt","latitud"+latitudString);
                Log.i("lt","longitud"+longitudString);
                Log.i("lt", "mi Latitud: " + myLatitudString + ", mi Longitud: "+myLongitudString );
                btnExploreOptions.setVisibility(View.GONE);
                btnDistancia.setVisibility(View.GONE);
                btnCar.setVisibility(View.GONE);
                btnRun.setVisibility(View.GONE);
                btnByker.setVisibility(View.GONE);
                btnOptions.setVisibility(View.VISIBLE);
            }
        });
        //Tayta
        btnTayata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coordenada = new LatLng(-12.399361373048928, -74.86634536593496);
                distancia=1.6f;
                String nombre = "Tayta restaurant";
                hotelMap(coordenada, nombre);
                latitudString = getLatitudString(coordenada);
                longitudString = getLongitudString(coordenada);
                Log.i("lt","latitud"+latitudString);
                Log.i("lt","longitud"+longitudString);
                Log.i("lt", "mi Latitud: " + myLatitudString + ", mi Longitud: "+myLongitudString );
                btnExploreOptions.setVisibility(View.GONE);
                btnDistancia.setVisibility(View.GONE);
                btnCar.setVisibility(View.GONE);
                btnRun.setVisibility(View.GONE);
                btnByker.setVisibility(View.GONE);
                btnOptions.setVisibility(View.VISIBLE);
            }
        });


        // Establece un Listener para el clic en el FloatingActionButton
        btnExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnExploreOptions.getVisibility() == View.VISIBLE) {
                    btnExploreOptions.setVisibility(View.GONE);
                } else {
                    btnExploreOptions.setVisibility(View.VISIBLE);
                }
            }
        });
        //

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return view;
    }

    //Calcular datos extra
    private void setDatos(){
        String unidadD = "Km.";
        String unidadA = "hor.";
        String unidadB = "hor.";
        String unidadP = "hor.";
        String distan="";
        String tCar="";
        String tBic="";
        String tPea="";

        // Calcula el tiempo en horas
        double tiempoAuto = distancia / 20;
        double tiempoBici = distancia / 15;
        double tiempoPeaton = distancia / 5;
        // Convierte el tiempo en horas a minutos
        if (distancia < 1) {
            unidadD = "m.";
            distancia *= 1000;
        }
        if (tiempoAuto < 1) {
            unidadA = "min.";
            tiempoAuto *= 60;
        }
        if (tiempoBici < 1) {
            unidadB = "min.";
            tiempoBici *= 60;
        }
        if (tiempoPeaton < 1) {
            unidadP = "min.";
            tiempoPeaton *= 60;
        }
        tCar = String.format("%.2f", tiempoAuto) + unidadA;
        tBic = String.format("%.2f", tiempoBici) + unidadB;
        tPea = String.format("%.2f", tiempoPeaton) + unidadP;
        distan = String.format("%.2f", distancia) + unidadD;
        txtCad.setText(tCar);
        txtByker.setText(tBic);
        txtRun.setText(tPea);
        txtDistancia.setText(distan);
        Log.i("denil","liosto para lansar lsao datos "+distancia+unidadD+"----"+tiempoAuto+unidadA+"---"+tiempoBici+unidadB+"---"+tiempoPeaton+unidadP);
    }
    //Calcular datos extra

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            createMarker();
            enableLocation();
        }
    }

    private void createMarker() {
        LatLng coordenada = new LatLng(-12.399469, -74.868565);
        MarkerOptions marker = new MarkerOptions().position(coordenada).title("Pampas");
        if (map != null) {
            map.addMarker(marker);
            map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(coordenada, 14.7f),
                    4000,
                    null
            );
        }
    }
    //obtener latitud y longitud---------
    public static String getLatitudString(LatLng coordenada) {
        return String.valueOf(coordenada.latitude);
    }

    public static String getLongitudString(LatLng coordenada) {
        return String.valueOf(coordenada.longitude);
    }
    //obtener latitud y longitud---------



    // creacion de marcadores y ubicaiones----------------
    private boolean permissionGranted() {
        return ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void enableLocation() {
        if (map != null && map.getCameraPosition() != null) {
            if (permissionGranted()) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                map.setMyLocationEnabled(true);
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(requireActivity(),location -> {
                            if (location !=null){
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                myLatitudString = String.valueOf(latitude);
                                myLongitudString = String.valueOf(longitude);
                            }
                        } );

            } else {
                requestLocation();
            }
        } else {
            Toast.makeText(getContext(), "El mapa no está inicializado", Toast.LENGTH_SHORT).show();
        }
    }


    private void requestLocation() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(getContext(), "acepta los permisos desde ajustes", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocation();
            } else {
                Toast.makeText(getContext(), "acepta los permisos desde ajustes", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void lugarMap(LatLng coordenada, String titulo) {
        map.clear();
        MarkerOptions marker = new MarkerOptions().position(coordenada).title(titulo);
        if (map != null) {
            map.addMarker(marker);
            map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(coordenada, 14.7f),
                    4000,
                    null
            );
        }
    }

    private void hotelMap(LatLng coordenada, String titulo) {
        map.clear();
        MarkerOptions marker = new MarkerOptions().position(coordenada).title(titulo);
        if (map != null) {
            map.addMarker(marker);
            map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(coordenada, 18f),
                    4000,
                    null
            );
        }
    }

    // creacion de marcadores y ubicaiones----------------
    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }


    //manejo de hilos
    private class MiHilo extends Thread{
        private MyService myService;
        private ApiService apiService;
        public MiHilo(MyService myService) {

            this.myService = myService;
            this.apiService = apiService;
        }
        @Override
        public void run(){

            // Crear instancia de ApiService
            ApiService apiService = myService.getApiService();
            Call<RouteResponse> call = apiService.getRoute("5b3ce3597851110001cf6248e2a30f5dbb0e4ce0a67675d189689e84", fMyCoordenada,fCoordenada);
            call.enqueue(new Callback<RouteResponse>() {
                @Override
                public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                    if (response.isSuccessful()) {
                        Log.i("denil", "entro");
                        drawRoute(response.body());
                        // Manejar el éxito
                        // Puedes acceder a la respuesta con response.body()
                        Log.i("denil", "salio");
                    } else {
                        // Manejar el fallo
                        // Puedes acceder al código de error con response.code() y al mensaje con response.message()
                        Log.i("denil", "no entro");
                    }
                }

                @Override
                public void onFailure(Call<RouteResponse> call, Throwable t) {
                    // Manejar la excepción
                    t.printStackTrace();
                    Log.i("denil", "no se logró");
                }
            });
        }
    }
    private void drawRoute(final RouteResponse routeResponse) {
        PolylineOptions polyLineOptions = new PolylineOptions();
        // Si ya tienes una variable polyLineOptions, no la vuelvas a declarar
        if (routeResponse != null && routeResponse.getFeatures() != null && !routeResponse.getFeatures().isEmpty()) {
            List<List<Double>> coordinatesList = routeResponse.getFeatures().get(0).getGeometry().getCoordinates();
            // Crear una lista plana para almacenar todas las coordenadas
            List<Double> flattenedCoordinates = new ArrayList<>();
            // Iterar sobre las listas internas y agregar las coordenadas a la lista plana
            for (List<Double> coordinates : coordinatesList) {
                flattenedCoordinates.addAll(coordinates);
            }
            // Utilizar la variable existente polyLineOptions si ya está declarada
            if (polyLineOptions == null) {
                polyLineOptions = new PolylineOptions();
            }

            for (int i = 0; i < flattenedCoordinates.size(); i += 2) {
                double latitude = flattenedCoordinates.get(i + 1);
                double longitude = flattenedCoordinates.get(i);

                polyLineOptions.add(new LatLng(latitude, longitude));
            }
            // Obtener el Looper del hilo principal
            Handler mainHandler = new Handler(Looper.getMainLooper());
            // Ejecutar el código en el hilo principal usando el Handler
            PolylineOptions finalPolyLineOptions = polyLineOptions;
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    poly = map.addPolyline(finalPolyLineOptions);
                }
            });
        }
    }
    public class MyService {
        //peticiones a servidor REST
        private Retrofit retrofit;
        public MyService() {
            retrofit = getRetrofit();
        }
        private ApiService getApiService() {
            return retrofit.create(ApiService.class);
        }
    }
    public Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.openrouteservice.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    //manejo de hilos
}