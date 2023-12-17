package com.example.turismopampas_hhh.CheckIn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.turismopampas_hhh.Login.LoginActivity;
import com.example.turismopampas_hhh.R;
import com.example.turismopampas_hhh.data.conexiondb;
import com.google.android.material.textfield.TextInputEditText;

public class CheckInActivity extends AppCompatActivity {

    private TextInputEditText TieName, TieSurnames, TieUser, TieEmail, TiePasword;
    private Button btnCheckIn;
    private TextView tvError, tvRegisterNow;
    private ProgressBar pbLoading;
    String nombreCompleto, apellidoCompleto, usuario, correo, contrasena;
    private conexiondb dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        dbHelper = new conexiondb(this);

        initializeViews();
        setListeners();
    }

    private void initializeViews() {
        TieName = findViewById(R.id.TieName);
        TieSurnames = findViewById(R.id.TieSurnames);
        TieUser = findViewById(R.id.TieUser);
        TieEmail = findViewById(R.id.TieEmail);
        TiePasword = findViewById(R.id.TiePasword);
        btnCheckIn = findViewById(R.id.btnCheckIn);
        tvError = findViewById(R.id.tverror);
        pbLoading = findViewById(R.id.Loading);
        tvRegisterNow = findViewById(R.id.tvRegisterNow);
    }

    private void setListeners() {
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        tvRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        Intent intent = new Intent(CheckInActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void registerUser() {
        tvError.setVisibility(View.GONE);
        pbLoading.setVisibility(View.VISIBLE);

        nombreCompleto = String.valueOf(TieName.getText()).trim();
        apellidoCompleto = String.valueOf(TieSurnames.getText()).trim();
        usuario = String.valueOf(TieUser.getText()).trim();
        correo = String.valueOf(TieEmail.getText()).trim();
        contrasena = String.valueOf(TiePasword.getText()).trim();

        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String contrasenaPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{12,}$";

        if (nombreCompleto.isEmpty() || apellidoCompleto.isEmpty() || usuario.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            tvError.setText("Por favor completa todos los campos");
            tvError.setVisibility(View.VISIBLE);
            pbLoading.setVisibility(View.GONE);
        } else if (!correo.matches(emailPattern)) {
            tvError.setText("Formato no válido de correo, ingrese otro válido");
            tvError.setVisibility(View.VISIBLE);
            pbLoading.setVisibility(View.GONE);
        } else if (!contrasena.matches(contrasenaPattern)) {
            tvError.setText("el formato de la contraseña no es valida intente con otro");
            tvError.setVisibility(View.VISIBLE);
            pbLoading.setVisibility(View.GONE);
        } else {
            String strengthMessage = getPasswordStrengthMessage(contrasena);
            Toast.makeText(getApplicationContext(), strengthMessage, Toast.LENGTH_SHORT).show();

            boolean existUser = dbHelper.checkIfUserExists(usuario, correo);

            if (existUser) {
                tvError.setText("El correo o usuario ya existe. Por favor elige otro.");
                tvError.setVisibility(View.VISIBLE);
                pbLoading.setVisibility(View.GONE);
            } else {
                long userId = dbHelper.insertUsuario(nombreCompleto, apellidoCompleto, usuario, correo, contrasena);
                if (userId != -1) {
                    conexiondb.setLoggedInUserId((int) userId);
                    Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                    goToLogin();
                } else  {
                    tvError.setText("Error al registrar usuario");
                    tvError.setVisibility(View.VISIBLE);
                    pbLoading.setVisibility(View.GONE);
                }
            }
        }

    }

    private String getPasswordStrengthMessage(String password) {
        String contrasenaPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{12,}$";
        if (password.matches(contrasenaPattern)) {
            return "contraseña fuerte";
        } else if (password.length() >= 8) {
            return "contraseña aceptable";
        } else {
            return "contraseña debil";
        }
    }


    private void goToLogin() {
        Intent intent = new Intent(CheckInActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
