package com.example.turismopampas_hhh.Home.FragmentHeader;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turismopampas_hhh.Home.Opload.Fragment.Foto;
import com.example.turismopampas_hhh.Home.Opload.Fragment.ImageAdapter;
import com.example.turismopampas_hhh.R;
import com.example.turismopampas_hhh.data.conexiondb;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView lista_imagenes;
    private ArrayList<Foto> lista;
    private conexiondb dbHelper;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        lista_imagenes = view.findViewById(R.id.lista_imagenes);
        lista_imagenes.setLayoutManager(new LinearLayoutManager(requireContext()));
        dbHelper = new conexiondb(requireContext());

        lista = new ArrayList<>();
        ImageAdapter adapter = new ImageAdapter(dbHelper.mostrar_imagenes());
        lista_imagenes.setAdapter(adapter);

        return view;
    }
}