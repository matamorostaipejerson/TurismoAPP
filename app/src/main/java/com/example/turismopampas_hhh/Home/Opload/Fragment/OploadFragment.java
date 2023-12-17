package com.example.turismopampas_hhh.Home.Opload.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.turismopampas_hhh.R;
import com.example.turismopampas_hhh.data.conexiondb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OploadFragment extends Fragment {

    private Bitmap imageBitmap;
    private EditText txt_descrip;
    private ImageView imagen;
    private Button subir, tomarfoto, guardar_contenido;
    private conexiondb dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_opload, container, false);

        txt_descrip = view.findViewById(R.id.descrip_img);

        subir = view.findViewById(R.id.subir_img);
        subir.setOnClickListener(v -> buscarEnGaleria());

        tomarfoto = view.findViewById(R.id.Foto_img);
        tomarfoto.setOnClickListener(v -> abrirCamara());


        guardar_contenido = view.findViewById(R.id.guardar_img);
        guardar_contenido.setOnClickListener(v -> {
            try {
                guardarImagen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        dbHelper = new conexiondb(requireContext());

        imagen = view.findViewById(R.id.ver_imagen3);


        return view;
    }

    private void guardarImagen() throws IOException {
        String description = txt_descrip.getText().toString();

        if (description.isEmpty() || imageBitmap == null) {
            Toast.makeText(requireContext(), "Por favor ingrese los parámetros", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] imageBytes = convertImageToByteArray(imageBitmap);

        long result = dbHelper.insertarimagen(description, imageBytes);

        if (result != -1) {
            Toast.makeText(requireContext(), "Datos guardados en la base de datos", Toast.LENGTH_SHORT).show();
            limpiar();
        } else {
            Toast.makeText(requireContext(), "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] convertImageToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }


    private void limpiar() {
        txt_descrip.setText("");
        imagen.setImageBitmap(null);
        imageBitmap = null;
    }


    private void buscarEnGaleria() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1);
    }

    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagen = getView().findViewById(R.id.ver_imagen3);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imagen.setImageBitmap(imageBitmap);
        } else if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                imageBitmap = (Bitmap) extras.get("data");
                imagen.setImageBitmap(imageBitmap);
            }
        }
    }
}
