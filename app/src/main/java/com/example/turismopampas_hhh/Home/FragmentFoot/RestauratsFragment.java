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


public class RestauratsFragment extends Fragment {

    private ImageView RestivRestaurantednof;
    private ImageView ResivRestaurantetaya;
    private ImageView ResivRestaurantgondolas;
    private ImageView ResivRestaurantlagruta;
    private ImageView ResivRestaurantyonatans;
    private ImageView ResivRestauranttayta;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurats, container, false);

        initComponent(view);
        initListener();
        return view;
    }

    private void initComponent(View view) {

        RestivRestaurantednof = view.findViewById(R.id.ivRestaurantednof);
        ResivRestaurantetaya = view.findViewById(R.id.ivRestaurantetaya);
        ResivRestaurantgondolas = view.findViewById(R.id.ivRestaurantgondolas);
        ResivRestaurantlagruta = view.findViewById(R.id.ivRestaurantlagruta);
        ResivRestaurantyonatans = view.findViewById(R.id.ivRestaurantyonatans);
        ResivRestauranttayta = view.findViewById(R.id.ivRestauranttayta);
    }


    private void initListener() {

        if (RestivRestaurantednof != null) {
            RestivRestaurantednof.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Integer> images= Arrays.asList(R.drawable.dnouno,R.drawable.dnodos,R.drawable.dnotres, R.drawable.dnocuatro);
                    showDialog(getString(R.string.rest_dnofox),getString(R.string.dnofox_ubicacion),images);
                }
            });
        }

        if (ResivRestaurantetaya != null) {
            ResivRestaurantetaya.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Integer> images= Arrays.asList(R.drawable.taya,R.drawable.tayados,R.drawable.tayatres, R.drawable.tayacuatro);
                    showDialog(getString(R.string.rest_taya),getString(R.string.taya_ubicacion),images);
                }
            });
        }

        if (ResivRestaurantgondolas != null) {
            ResivRestaurantgondolas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Integer> images= Arrays.asList(R.drawable.gondouno,R.drawable.gondodos,R.drawable.gondotres, R.drawable.gondocuatro);
                    showDialog(getString(R.string.rest_gondolas),getString(R.string.gondolas_ubicacion),images);
                }
            });
        }

        if (ResivRestaurantlagruta != null) {
            ResivRestaurantlagruta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Integer> images= Arrays.asList(R.drawable.grutauno,R.drawable.grutados,R.drawable.grutatres, R.drawable.grutacuatro);
                    showDialog(getString(R.string.rest_la_gruta),getString(R.string.la_gruta_ubicacion),images);
                }
            });
        }

        if (ResivRestaurantyonatans != null) {
            ResivRestaurantyonatans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Integer> images= Arrays.asList(R.drawable.yonauno,R.drawable.yonados,R.drawable.yonatres, R.drawable.yonacuatro);
                    showDialog(getString(R.string.rest_yonatans),getString(R.string.yonatans_ubicacion),images);
                }
            });
        }

        if (ResivRestauranttayta != null) {
            ResivRestauranttayta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Integer> images= Arrays.asList(R.drawable.taytauno,R.drawable.tayados,R.drawable.tayatres, R.drawable.tayacuatro);
                    showDialog(getString(R.string.rest_tayta),getString(R.string.tayta_ubicacion),images);
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