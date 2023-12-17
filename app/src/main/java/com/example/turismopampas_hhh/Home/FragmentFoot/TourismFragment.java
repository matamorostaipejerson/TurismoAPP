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

public class TourismFragment extends Fragment {

    private ImageView imageViewTourismOne;
    private ImageView imageViewTourismTwo;
    private ImageView imageViewTourismThree;
    private ImageView imageViewTourismFour;
    private ImageView imageViewTourismFive;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tourism, container, false);
        initComponets(view);
        initListeners();
        return view;
    }

    private void initComponets(View view) {
        imageViewTourismOne = view.findViewById(R.id.IVTourismOne);
        imageViewTourismTwo = view.findViewById(R.id.IVTourismTwo);
        imageViewTourismThree = view.findViewById(R.id.IVTourismTree);
        imageViewTourismFour = view.findViewById(R.id.IVTourismFour);
        imageViewTourismFive = view.findViewById(R.id.IVTourismFive);

    }

    private void initListeners() {

        if (imageViewTourismOne != null) {

            imageViewTourismOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Integer> images= Arrays.asList(R.drawable.yauri_uno,R.drawable.yauri_tres,R.drawable.yauri_dos, R.drawable.yauri_cuatro);
                    showDialog(getString(R.string.yauricocha),getString(R.string.y_descricion),images);
                }
            });
        }
        if (imageViewTourismTwo != null) {
            imageViewTourismTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Integer> images= Arrays.asList(R.drawable.qha_uno,R.drawable.jados,R.drawable.jatres, R.drawable.jacuatro);
                    showDialog(getString(R.string.R_Qhapaq),getString(R.string.rq_description),images);
                }
            });
        }
        if (imageViewTourismThree != null) {
            imageViewTourismThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Integer> images= Arrays.asList(R.drawable.tabla_uno,R.drawable.tabla_dos,R.drawable.tabla_tres, R.drawable.tabla_cuatro);
                    showDialog(getString(R.string.r_tablachaca),getString(R.string.rt_description),images);
                }
            });
        }
        if (imageViewTourismFour != null) {
            imageViewTourismFour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Integer> images= Arrays.asList(R.drawable.pillo_uno,R.drawable.pillo_dos,R.drawable.pillo_tres, R.drawable.pillo_cuatro);
                    showDialog(getString(R.string.hacienda_s_p),getString(R.string.hsjp_description),images);
                }
            });
        }
        if (imageViewTourismFive != null) {
            imageViewTourismFive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Integer> images= Arrays.asList(R.drawable.lagu_uno,R.drawable.lagu_dos,R.drawable.lagu_tres, R.drawable.lagu_cuatro);
                    showDialog(getString(R.string.cataratas),getString(R.string.cr_description),images);
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