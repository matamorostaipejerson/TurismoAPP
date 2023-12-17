package com.example.turismopampas_hhh.Home.Opload.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turismopampas_hhh.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.FotoViewHolder> {

    ArrayList<Foto> Listafotos;
    public ImageAdapter(ArrayList<Foto> Listafotos){
        this.Listafotos = Listafotos;
    }

    @NonNull
    @Override
    public FotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,null,false);
        return new FotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FotoViewHolder holder, int position) {

        Foto imageModel = Listafotos.get(position);
        holder.ver_descrip.setText(imageModel.getDescripcion());
        // Convertir el array de bytes de la imagen a Bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageModel.getImg(), 0, imageModel.getImg().length);
        // Mostrar la imagen en el ImageView
        holder.imagen_ver.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return Listafotos.size();
    }
    public class FotoViewHolder extends RecyclerView.ViewHolder {
        TextView ver_descrip;
        ImageView imagen_ver;
        public FotoViewHolder(@NonNull View itemView) {
            super(itemView);

            ver_descrip = itemView.findViewById(R.id.ver_desccrip);
            imagen_ver = itemView.findViewById(R.id.imagen_ver);

        }
    }
}



