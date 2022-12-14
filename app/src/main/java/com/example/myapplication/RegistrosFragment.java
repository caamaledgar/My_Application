package com.example.myapplication;

import static android.view.View.GONE;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.myapplication.Model.Usuarios;
import com.example.myapplication.databinding.FragmentRegistrosBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrosFragment extends Fragment {
    //binding
    FragmentRegistrosBinding binding;

    // Conexión al Nodo de la Base de Datos Firebase
    String DB_FB_NODE = "message";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference(DB_FB_NODE);
    DatabaseReference userRef = dbRef.child("Usuarios");


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegistrosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrosFragment newInstance(String param1, String param2) {
        RegistrosFragment fragment = new RegistrosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_registros, container, false);
        binding = FragmentRegistrosBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);

        binding.ivURLImage.setVisibility(GONE);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertarRegistro(view);
            }
        });

        binding.btnRecyclerRegistros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(view.getContext(), "Click Fragmento", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.recyclerRegistrosFragment);
            }
        });

    }


    public void insertarRegistro(View view){
        String nombre = binding.inputNombre.getEditText().getText().toString();
        String correo = binding.inputCorreo.getEditText().getText().toString();
        String imagen = binding.inputImagen.getEditText().getText().toString();

        Usuarios usuarios = new Usuarios(userRef.push().getKey(),
                nombre, correo, imagen);


        Query userEmailQery = userRef.orderByChild("correo").equalTo(correo).limitToFirst(1);
        userEmailQery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(view.getContext(), "No se puede realizar el Registro. Correo Existente " + snapshot.toString(), Toast.LENGTH_SHORT).show();

                } else {
                    userRef.child(usuarios.getUid()).setValue(usuarios);
                    binding.inputNombre.getEditText().setText("");
                    binding.inputCorreo.getEditText().setText("");
                    binding.inputImagen.getEditText().setText("");
                    binding.inputNombre.requestFocus();
                    Toast.makeText(view.getContext(), "Registro de Usuario Exitoso", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Registro "+usuarios.toString());

                    binding.ivURLImage.setVisibility(View.VISIBLE);
                    Glide.with(binding.ivURLImage.getContext())
                            .load(imagen)
                            .error(R.drawable.ic_launcher_foreground)
                            .apply(new RequestOptions().override(300, 300))
                            .centerCrop()
                            .into(binding.ivURLImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}