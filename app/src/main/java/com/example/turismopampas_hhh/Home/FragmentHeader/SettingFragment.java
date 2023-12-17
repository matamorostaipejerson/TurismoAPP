package com.example.turismopampas_hhh.Home.FragmentHeader;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.turismopampas_hhh.Home.HomeActivity;
import com.example.turismopampas_hhh.R;
import com.example.turismopampas_hhh.data.conexiondb;
import com.google.android.material.button.MaterialButton;

public class SettingFragment extends Fragment {

    // Declaración de variables para elementos de la interfaz de usuario
    private EditText etNombre, etApellido, etUsuario, etCorreo, etContrasena;
    private Button btnGuardar;
    private MaterialButton btnEliminar;

    // Variable para almacenar el ID de usuario
    private int userId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el diseño del fragmento y lo devuelve
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Inicializa las referencias a los elementos de la interfaz de usuario
        etNombre = view.findViewById(R.id.nombreSetting);
        etApellido = view.findViewById(R.id.apellidoSetting);
        etUsuario = view.findViewById(R.id.usuarioSetting);
        etCorreo = view.findViewById(R.id.correoSetting);
        etContrasena = view.findViewById(R.id.contrasenaSetting);

        setUserInfo();

        // Establece un oyente de clics para el botón que llama al método `updateUserInfo()` al hacer clic
        btnGuardar = view.findViewById(R.id.btnGuardarSetting);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateUserConfirm();
            }
        });

        btnEliminar = view.findViewById(R.id.btnEliminarSetting);

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteUserConfirm();
            }
        });

        return view;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Método para establecer la información del usuario desde la base de datos
    private void setUserInfo() {
        // Verifica si el ID de usuario es válido
        if (userId <= 0) {
            return;
        }

        // Crea una conexión a la base de datos y obtiene la base de datos de lectura
        conexiondb db = new conexiondb(getContext());
        SQLiteDatabase readableDB = db.getReadableDatabase();

        // Comprueba si la conexión a la base de datos se realizó correctamente
        if (readableDB == null) {
            return;
        }

        // Realiza una consulta a la tabla 'usuarios' para obtener la información del usuario
        Cursor cursor = readableDB.query("usuarios",
                new String[]{"nombre_completo", "apellido_completo", "usuario", "correo", "contrasena"},
                "id_usuario = ?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        // Comprueba si el cursor no es nulo
        if (cursor != null) {
            // Comprueba si el cursor tiene datos
            if (cursor.moveToFirst()) {
                // Extrae la información del usuario del cursor y la establece en los campos de texto de la interfaz de usuario
                @SuppressLint("Range") String nombreCompleto = cursor.getString(cursor.getColumnIndex("nombre_completo"));
                @SuppressLint("Range") String apellidoCompleto = cursor.getString(cursor.getColumnIndex("apellido_completo"));
                @SuppressLint("Range") String usuario = cursor.getString(cursor.getColumnIndex("usuario"));
                @SuppressLint("Range") String correo = cursor.getString(cursor.getColumnIndex("correo"));
                @SuppressLint("Range") String contrasena = cursor.getString(cursor.getColumnIndex("contrasena"));

                etNombre.setText(nombreCompleto);
                etApellido.setText(apellidoCompleto);
                etUsuario.setText(usuario);
                etCorreo.setText(correo);
                etContrasena.setText(contrasena);
            }

            // Cierra el cursor
            cursor.close();
        }
    }


    private void showUpdateUserConfirm() {
        String correoIngresado = etCorreo.getText().toString();
        String contrasenaIngresada = etContrasena.getText().toString();

        if (isValidCredentials(correoIngresado, contrasenaIngresada)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Actualizar Usuario");
            builder.setMessage("¿Está seguro de que desea actualizar la información de este usuario?");

            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    updateUserInfo();
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Toast.makeText(getContext(), "Correo o contraseña inválidos", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidCredentials(String correo, String contrasena) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String contrasenaPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{12,}$";

        return correo.matches(emailPattern) && contrasena.matches(contrasenaPattern);
    }


    private void updateUserInfo() {
        String nombreCompleto = etNombre.getText().toString();
        String apellidoCompleto = etApellido.getText().toString();
        String usuario = etUsuario.getText().toString();
        String correo = etCorreo.getText().toString();
        String contrasena = etContrasena.getText().toString();

        conexiondb db = new conexiondb(getContext());
        SQLiteDatabase writableDB = db.getWritableDatabase();

        if (writableDB == null) {
            Log.d("UpdateUserInfo","WritableDB es nulo");
            return;
        }

        ContentValues values = new ContentValues();
        // Establece los valores del objeto `ContentValues` con la información del usuario proporcionada por el usuario
        values.put("nombre_completo", nombreCompleto);
        values.put("apellido_completo", apellidoCompleto);
        values.put("usuario", usuario);
        values.put("correo", correo);
        values.put("contrasena", contrasena);
        values.put("id_usuario",userId);
        Log.d("UpdateUserInfo", "Valores a actualizar: " + values.toString());

        int rowsUpdated = writableDB.update("usuarios", values, "id_usuario = ?",
                new String[]{String.valueOf(userId)});

        if (rowsUpdated > 0) {
            Toast.makeText(getContext(), "Información actualizada correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "No se pudo actualizar la información", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteUserConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        builder.setTitle("Eliminar Usuario");
        builder.setMessage("¿Está seguro de que desea eliminar este usuario?");

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteUser();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteUser() {
        conexiondb db = new conexiondb(getContext());
        SQLiteDatabase writableDB = db.getWritableDatabase();

        if (writableDB == null) {
            Log.d("DeleteUserInfo", "WritableDB es nulo");
            return;
        }

        int rowsDeleted = writableDB.delete("usuarios", "id_usuario = ?", new String[]{String.valueOf(userId)});

        if (rowsDeleted > 0) {
            Toast.makeText(getContext(), "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show();

            ((HomeActivity) requireActivity()).logout();
        } else {
            Toast.makeText(getContext(), "No se pudo eliminar el usuario", Toast.LENGTH_SHORT).show();
        }
    }
}
