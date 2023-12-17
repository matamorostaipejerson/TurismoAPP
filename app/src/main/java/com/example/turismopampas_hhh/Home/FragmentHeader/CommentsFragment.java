package com.example.turismopampas_hhh.Home.FragmentHeader;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.turismopampas_hhh.R;
import com.example.turismopampas_hhh.comentarios.AdapterMensajes;
import com.example.turismopampas_hhh.comentarios.Mensaje;
import com.example.turismopampas_hhh.data.conexiondb;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class CommentsFragment extends Fragment {
    private CircleImageView fotoPerfil;
    private TextView nombre;
    private RecyclerView rvMensajes;
    private EditText txtMensaje;
    private FloatingActionButton btnEnviarmsg;
    private ImageButton btnEnviarFoto;
    private AdapterMensajes adapter;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storge;
    private StorageReference storageReference;
    private static final int PHOTO_SEND = 1;
    private static final int PHOTO_PERFIL = 2;
    private String fotoPerfilCandena;
    private int userId = -1;
    private String nombreCompleto;

    public void setUserIdComent(int userId) {
        this.userId = userId;
    }


    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a ", Locale.getDefault());
    public String horaActual = dateFormat.format(calendar.getTime());


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("MyApp", "onCreateView: Inflating layout");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        initcomponents(view);
        return view;
    }

    private void initcomponents(View view) {

        fotoPerfil = view.findViewById(R.id.fotoPerfil);
        nombre = view.findViewById(R.id.nombre);
        rvMensajes = view.findViewById(R.id.rvMensajes);
        txtMensaje = view.findViewById(R.id.txtMensaje);
        btnEnviarmsg = view.findViewById(R.id.btnEnviarmsg);
        btnEnviarFoto = view.findViewById(R.id.btnEnviarFoto);
        fotoPerfilCandena = "";

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();//sala de chat
        storge = FirebaseStorage.getInstance();

        adapter = new AdapterMensajes(getContext());
        LinearLayoutManager l = new LinearLayoutManager(requireContext());
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adapter);

        // Crea una conexión a la base de datos y obtiene la base de datos de lectura
        conexiondb db = new conexiondb(getContext());
        SQLiteDatabase readableDB = db.getReadableDatabase();

        // Realiza una consulta a la tabla 'usuarios' para obtener la información del usuario
        Cursor cursor = readableDB.query("usuarios", new String[]{"nombre_completo"}, "id_usuario = ?", new String[]{String.valueOf(userId)}, null, null, null);

        // Verifica si hay algún resultado antes de intentar acceder a los datos
        if (cursor.moveToFirst()) {
            // Obtener el nombre completo del primer (y posiblemente único) resultado
            nombreCompleto = cursor.getString(cursor.getColumnIndex("nombre_completo"));
            nombre.setText(nombreCompleto);
        } else {
            // Manejo de error si no hay resultados en la consulta
            Log.e("MyApp", "No se encontraron resultados para el usuario con ID: " + userId);
        }

        // Cerrar el cursor después de obtener los datos necesarios
        cursor.close();


        btnEnviarmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //enviar mensaje
                databaseReference.push().setValue(new Mensaje(txtMensaje.getText().toString(), nombreCompleto, fotoPerfilCandena, "1", horaActual));
                txtMensaje.setText("");
            }
        });
        btnEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i, "selecciona una foto"), PHOTO_SEND);
            }
        });
        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i, "selecciona una foto"), PHOTO_PERFIL);
            }
        });
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //recibir el mensaje
                Mensaje m = snapshot.getValue(Mensaje.class);
                adapter.addMensaje(m);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setScrollbar() {
        rvMensajes.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_SEND && resultCode == RESULT_OK) {
            Uri u = data.getData();
            storageReference = storge.getReference("imagenes_chat");
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());

            fotoReferencia.putFile(u).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Obtener la URL de descarga directamente desde el taskSnapshot
                    fotoReferencia.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            Mensaje m = new Mensaje("a enviado una foto", downloadUri.toString(), nombreCompleto, fotoPerfilCandena, "2", horaActual);
                            databaseReference.push().setValue(m);
                        }
                    });
                }
            });
        } else if (requestCode == PHOTO_PERFIL && resultCode == RESULT_OK) {
            Uri u = data.getData();
            storageReference = storge.getReference("foto_perfil");
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());

            fotoReferencia.putFile(u).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Obtener la URL de descarga directamente desde el taskSnapshot
                    fotoReferencia.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            fotoPerfilCandena = u.toString();
                            Mensaje m = new Mensaje("a actualizado su foto de perfil", downloadUri.toString(), nombreCompleto, fotoPerfilCandena, "2", horaActual);
                            databaseReference.push().setValue(m);
                            Glide.with(CommentsFragment.this).load(downloadUri.toString()).into(fotoPerfil);
                        }
                    });
                }
            });
        }
    }
}
