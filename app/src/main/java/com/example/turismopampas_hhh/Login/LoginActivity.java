package com.example.turismopampas_hhh.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.turismopampas_hhh.CheckIn.CheckInActivity;
import com.example.turismopampas_hhh.Home.HomeActivity;
import com.example.turismopampas_hhh.R;
import com.example.turismopampas_hhh.data.conexiondb;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText tieCorreo, tieContrasena;
    private Button btnLogIn;
    private ProgressBar loading;
    private TextView tvRegisterNow, tvError;
    private String nombreCompleto;
    private SQLiteDatabase db;
    private int loginAttempts = 0;
    private boolean isBlocked = false;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponents();
        initListeners();

        conexiondb dbHelper = new conexiondb(this);
        db = dbHelper.getWritableDatabase();
    }

    private void initComponents() {
        tieCorreo = findViewById(R.id.TieCorreo);
        tieContrasena = findViewById(R.id.TieContrasena);
        btnLogIn = findViewById(R.id.btnLogIn);
        tvError = findViewById(R.id.tverror);
        loading = findViewById(R.id.Loading);
        tvRegisterNow = findViewById(R.id.tvRegisterNow);
    }

    private void initListeners() {
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUsers();
            }
        });
        tvRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        Intent intent = new Intent(LoginActivity.this, CheckInActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToHome() {
        //se realiza una conexion a la base de datos
        conexiondb db = new conexiondb(this);

        String loggedInEmail = tieCorreo.getText().toString();
        String loggedInPassword = tieContrasena.getText().toString();

        //se recupera el id del usuario
        int userId = db.getLoggedInUserId(loggedInEmail, loggedInPassword);
        //almacena datos ("dato - valor "), que son las preferencias compartidas
        SharedPreferences prefs = getSharedPreferences("mi_app", Context.MODE_PRIVATE);
        //se obtiene un editor para que realice cambios en las preferencias
        SharedPreferences.Editor editor = prefs.edit();
        //un id de tipo entero
        editor.putInt("user_id", userId);
        //finalmente aplica
        editor.apply();

        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void loginUsers() {
        //la funcion se ejecuta cuando el logueo esta bloqueado
        if (isBlocked) {
            tvError.setText("Ha excedido el número de intentos permitidos. Intente nuevamente más tarde.");
            tvError.setVisibility(View.VISIBLE);
            return;
        }
        //se implementa para manejar las excepciones
        try {
            //afecta a la variable y sera invisible
            tvError.setVisibility(View.GONE);
            //afecta a la variable y sera visible
            loading.setVisibility(View.VISIBLE);
            //recuperan texto ingresado por el usuario y sin no es nulo obtiene a la funcion String y con trim() elimina los espacios delante atras
            String correo = tieCorreo.getText() != null ? tieCorreo.getText().toString().trim() : "";
            String contrasena = tieContrasena.getText() != null ? tieContrasena.getText().toString().trim() : "";

            //compara si los campos estan completos
            if (correo.isEmpty() || contrasena.isEmpty()) {
                tvError.setText("Por favor, complete todos los campos.");
                tvError.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                return;
            }
            //realiza una consulta a la base de datos
            Cursor cursor = db.rawQuery("SELECT nombre_completo FROM usuarios WHERE correo = ? AND contrasena = ?",
                    new String[]{correo, contrasena});

            if (cursor != null && cursor.moveToFirst()) {
                loginAttempts = 0;

                int nombre_completo_index = cursor.getColumnIndex("nombre_completo");

                if (nombre_completo_index != -1) {
                    nombreCompleto = cursor.getString(nombre_completo_index);
                    navigateToHome();
                } else {
                    tvError.setText("La columna 'nombre_completo' no existe");
                    tvError.setVisibility(View.VISIBLE);
                }

            } else {
                loginAttempts++;

                if (loginAttempts >= 3) {
                    isBlocked = true;
                    timer = new CountDownTimer(30000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            tvError.setText("Cuenta regresiva: " + millisUntilFinished / 1000 + " segundos");
                        }

                        public void onFinish() {
                            isBlocked = false;
                            loginAttempts = 0;
                            tvError.setText("Cuenta regresiva: 0 segundos");
                            tvError.setVisibility(View.GONE);
                        }
                    }.start();
                }

                tvError.setText("Credenciales inválidas");
                tvError.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        if (timer != null) {
            timer.cancel();
        }
    }
}
