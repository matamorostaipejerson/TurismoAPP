package com.example.turismopampas_hhh.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.turismopampas_hhh.Home.FragmentFoot.HotelsFragment;
import com.example.turismopampas_hhh.Home.FragmentFoot.MapsFragment;
import com.example.turismopampas_hhh.Home.FragmentFoot.RestauratsFragment;
import com.example.turismopampas_hhh.Home.FragmentFoot.TourismFragment;
import com.example.turismopampas_hhh.Home.FragmentHeader.AboutFragment;
import com.example.turismopampas_hhh.Home.FragmentHeader.CommentsFragment;
import com.example.turismopampas_hhh.Home.FragmentHeader.HomeFragment;
import com.example.turismopampas_hhh.Home.FragmentHeader.SettingFragment;
import com.example.turismopampas_hhh.Home.FragmentHeader.ShareFragment;
import com.example.turismopampas_hhh.Home.Opload.Fragment.OploadFragment;
import com.example.turismopampas_hhh.IntroductionActivity;
import com.example.turismopampas_hhh.R;
import com.example.turismopampas_hhh.data.conexiondb;
import com.example.turismopampas_hhh.databinding.ActivityHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    ActivityHomeBinding binding;
    FloatingActionButton nav_agregar;
    TextView nombreCompleto, correoCompleto;

    @Override
    protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HotelsFragment());

        nombreCompleto=findViewById(R.id.nombreCompleto);
        correoCompleto=findViewById(R.id.correoCompleto);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkLoggedInUser();

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        nav_agregar = findViewById(R.id.nav_agregar);

        nav_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new OploadFragment());
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.nav_Hotels) {
                replaceFragment(new HotelsFragment());
            } else if (itemId == R.id.nav_Restaurants) {
                replaceFragment(new RestauratsFragment());
            } else if (itemId == R.id.nav_mapa) {
                replaceFragment(new MapsFragment());
            } else if (itemId == R.id.nav_tourism) {
                replaceFragment(new TourismFragment());
            }
            return true;

        });
    }

    @Override

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        } else if (itemId == R.id.nav_setting) {
            openSettings();

        } else if (itemId == R.id.nav_share) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShareFragment()).commit();
        } else if (itemId == R.id.nav_about) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
        } else if (itemId == R.id.nav_comments) {
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CommentsFragment()).commit();
            openCmments();
        } else if (itemId == R.id.nav_logout) {
            logout();
            Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openSettings() {
        SharedPreferences prefs = getSharedPreferences("mi_app", Context.MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);
        SettingFragment SettingFragment = new SettingFragment();
        SettingFragment.setUserId(userId);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,SettingFragment).commit();

    }
    private void openCmments() {
        SharedPreferences prefs = getSharedPreferences("mi_app", Context.MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);
        CommentsFragment CommentsFragment = new CommentsFragment();
        CommentsFragment.setUserIdComent(userId);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,CommentsFragment).commit();

    }

    private void checkLoggedInUser() {
        SharedPreferences prefs = getSharedPreferences("mi_app", Context.MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        if(userId == -1) {
            Toast.makeText(this, "No hay una sesión activa", Toast.LENGTH_SHORT).show();
        } else {
            conexiondb db = new conexiondb(this);
            String username = db.getUsername(userId);
           // String correo = db.getCorreo(userId);

            Toast.makeText(this, "Hola " + username, Toast.LENGTH_SHORT).show();
//            nombreCompleto.setText(username);
//            correoCompleto.setText(correo);

            SettingFragment settingFragment = new SettingFragment();
            settingFragment.setUserId(userId);
            CommentsFragment commentsFragment = new CommentsFragment();
            commentsFragment.setUserIdComent(userId);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, settingFragment).commit();
        }
    }



    public void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        SharedPreferences prefsApp = getSharedPreferences("mi_app", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorApp = prefsApp.edit();
        editorApp.putInt("user_id",-1);
        boolean successClearApp = editorApp.commit();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        boolean success = editor.commit();
        Log.d("Logout", "Changes committed: " + success);

        Intent intent = new Intent(HomeActivity.this, IntroductionActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            showExitConfirmationDialog();
        }
    }

    private void replaceFragment(Fragment fragment) {
        SharedPreferences prefs = getSharedPreferences("mi_app", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "");

        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }


    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Salir de la aplicación")
                .setMessage("¿Estás seguro de que quieres salir?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }


    protected void onDestroy() {
        super.onDestroy();
    }
}

