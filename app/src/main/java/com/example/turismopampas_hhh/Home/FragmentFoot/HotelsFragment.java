package com.example.turismopampas_hhh.Home.FragmentFoot;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.turismopampas_hhh.R;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HotelsFragment extends Fragment {

    private ImageView hotelPlazaImageView;
    private ImageView hotelDConfortImageView;
    private ImageView hotelJardinImageView;
    private ImageView hotelAngelesImageView;
    private ImageView hotelSurgeImageView;
    private ImageView hotelBlueImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotels, container, false);

        initComponents(view);
        initListeners();
        return view;
    }

    private void initComponents(View view) {

        hotelPlazaImageView = view.findViewById(R.id.HotelPlaza);
        hotelDConfortImageView = view.findViewById(R.id.HotelDConfort);
        hotelJardinImageView = view.findViewById(R.id.Hoteljardin);
        hotelAngelesImageView = view.findViewById(R.id.Hotelangeles);
        hotelSurgeImageView = view.findViewById(R.id.Hotelsurge);
        hotelBlueImageView = view.findViewById(R.id.Hotelblue);
    }


    private void initListeners() {

        if (hotelPlazaImageView != null) {

            hotelPlazaImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Integer> images= Arrays.asList(R.drawable.plazauno,R.drawable.plazados,R.drawable.plazatres, R.drawable.plazacuatro, R.drawable.plazacinco);
                    showDialog(getString(R.string.hotel_plaza), getString(R.string.hp_ubicacion), images);
                }
            });
        }

        if (hotelDConfortImageView != null) {
            hotelDConfortImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Integer> images= Arrays.asList(R.drawable.dcouno,R.drawable.dcodos,R.drawable.dcotres, R.drawable.dcocuatro, R.drawable.dcocinco);
                    showDialog(getString(R.string.hotel_d_confort), getString(R.string.hc_ubicacion), images);
                }
            });
        }

        if (hotelJardinImageView != null) {
            hotelJardinImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Integer> images= Arrays.asList(R.drawable.jauno,R.drawable.jados,R.drawable.jatres, R.drawable.jacuatro, R.drawable.jacinco);
                    showDialog(getString(R.string.hotel_jardin), getString(R.string.hj_ubicacion), images);
                }
            });
        }

        if (hotelAngelesImageView != null) {
            hotelAngelesImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Integer> images= Arrays.asList(R.drawable.anuno,R.drawable.andos,R.drawable.antres, R.drawable.ancuatro, R.drawable.ancinco);
                    showDialog(getString(R.string.hotel_angeles), "Descripción del Hotel Los angeles", images);
                }
            });
        }

        if (hotelSurgeImageView != null) {
            hotelSurgeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Integer> images= Arrays.asList(R.drawable.surguno,R.drawable.surdos,R.drawable.surtres, R.drawable.surcuatro, R.drawable.surcinco);
                    showDialog(getString(R.string.hotel_surge), "Descripción del Hotel Plaza", images);
                }
            });
        }

        if (hotelBlueImageView != null) {
            hotelBlueImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Integer> images= Arrays.asList(R.drawable.bluuno,R.drawable.bludos,R.drawable.blutres, R.drawable.blucuatro, R.drawable.blucinco);
                    showDialog(getString(R.string.hotel_blue), "Descripción del Hotel Plaza", images);
                }
            });
        }
    }

    private void showDialog(String hotelName, String hotelDescription, List<Integer> images) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.activity_dialog);

        TextView nameTextView = dialog.findViewById(R.id.tvNombre);
        TextView descriptionTextView = dialog.findViewById(R.id.tvUbicacion);
        ImageCarousel imageCarousel = dialog.findViewById(R.id.icCarucel);

        nameTextView.setText(hotelName);
        descriptionTextView.setText(hotelDescription);

        List<CarouselItem> items = new ArrayList<>();
        for (int imageResId : images) {
            items.add(new CarouselItem(imageResId, ""));
        }
        imageCarousel.addData(items);

        final Handler handler= new Handler();
        Timer timer= new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int currentPage=0;
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPage== images.size()){
                            currentPage=0;
                        }
                        imageCarousel.setCurrentPosition(currentPage);
                        currentPage ++;
                    }
                });

            }
        },0,3000);

        dialog.show();
    }



}
