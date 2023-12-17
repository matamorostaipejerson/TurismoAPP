package com.example.turismopampas_hhh.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.turismopampas_hhh.Home.Opload.Fragment.Foto;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class conexiondb extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TurismoPampas.db";
    private static final int DATABASE_VERSION = 1;
    private static int loggedInUserId = -1;

    public conexiondb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsuariosTable = "CREATE TABLE usuarios (" +
                "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre_completo TEXT NOT NULL," +
                "correo TEXT NOT NULL," +
                "usuario TEXT NOT NULL," +
                "contrasena TEXT," +
                "apellido_completo TEXT NOT NULL)";

        String createImagenesTable = "CREATE TABLE imagenes (" +
                "id_imagen INTEGER PRIMARY KEY AUTOINCREMENT," +
                "descripcion TEXT," +
                "imagen BLOB," +
                "fecha_subida TEXT," +
                "id_usuario INTEGER NOT NULL," +
                "FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario))";

        db.execSQL(createUsuariosTable);
        db.execSQL(createImagenesTable);
    }

    // Método para establecer el id_usuario actual
    public static void setLoggedInUserId(int userId) {
        loggedInUserId = userId;
    }

    // Método para obtener el id_usuario actual
    public static int getLoggedInUserId() {
        return loggedInUserId;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS imagenes");
        onCreate(db);
    }

    public boolean checkIfUserExists(String usuario, String correo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                "usuarios",
                new String[]{"id_usuario"},
                "usuario = ? OR correo = ?",
                new String[]{usuario, correo},
                null,
                null,
                null
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }


    public long insertUsuario(String nombreCompleto, String apellidoCompleto, String usuario, String correo, String contrasena) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (checkIfUserExists(usuario, correo)) {
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put("nombre_completo", nombreCompleto);
        values.put("apellido_completo", apellidoCompleto);
        values.put("usuario", usuario);
        values.put("correo", correo);
        values.put("contrasena", contrasena);

        long newRowId = db.insert("usuarios", null, values);
        db.close();

        return newRowId;
    }

    public long insertarimagen(String descripcion, byte[] foto) {
        long id = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int idUsuarioActual = getLoggedInUserId();
            ContentValues values = new ContentValues();
            values.put("descripcion", descripcion);

            // Convertir el arreglo de bytes (foto) a un Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(foto, 0, foto.length);

            // Comprimir la imagen y obtener el arreglo de bytes comprimido
            byte[] compressedImage = compressImage(bitmap);
            values.put("imagen", compressedImage);
            values.put("id_usuario", idUsuarioActual);
            id = db.insert("imagenes", null, values);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return id;
    }


    private byte[] compressImage(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // Comprimir la imagen en el flujo de salida (outputStream) con la calidad deseada
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        return outputStream.toByteArray(); // Devolver la representación comprimida como un arreglo de bytes
    }


    public ArrayList<Foto> mostrar_imagenes() {
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<Foto> lista_imagen = new ArrayList<>();
        Foto mostrar = null;
        Cursor cursorfoto = null;
        cursorfoto = db.rawQuery("SELECT *  FROM imagenes", null);

        if (cursorfoto != null && cursorfoto.moveToFirst()) {
            do {
                mostrar = new Foto();
                mostrar.setDescripcion(cursorfoto.getString(cursorfoto.getColumnIndex("descripcion")));
                mostrar.setImg(cursorfoto.getBlob(cursorfoto.getColumnIndex("imagen")));
                lista_imagen.add(mostrar);
            } while (cursorfoto.moveToNext());
        }

        if (cursorfoto != null) {
            cursorfoto.close();
        }
        return lista_imagen;

    }

    @SuppressLint("Range")
    public int getLoggedInUserId(String loggedInEmail, String loggedInPassword) {

        SQLiteDatabase db = this.getReadableDatabase();

        int userId = -1;

        Cursor cursor = db.query("usuarios",
                new String[]{"id_usuario"},
                "correo = ? AND contrasena = ?",
                new String[]{loggedInEmail, loggedInPassword},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex("id_usuario"));
        }

        cursor.close();

        return userId;

    }

    @SuppressLint("Range")
    public String getUsername(int userId) {

        SQLiteDatabase db = this.getReadableDatabase();

        String username = "";

        Cursor cursor = db.query("usuarios",
                new String[]{"usuario"},
                "id_usuario = ?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            username = cursor.getString(cursor.getColumnIndex("usuario"));
        }

        cursor.close();

        return username;

    }
    public String getCorreo(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String correo = "";

        Cursor cursor = db.query(
                "usuarios",
                new String[]{"correo"},
                "id_usuario = ?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            correo = cursor.getString(cursor.getColumnIndex("correo"));
        }

        if (cursor != null) {
            cursor.close();
        }

        return correo;
    }



}
