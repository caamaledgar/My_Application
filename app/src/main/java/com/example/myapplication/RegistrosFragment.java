package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Model.Usuarios;
import com.example.myapplication.databinding.FragmentRegistrosBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrosFragment extends Fragment {
    //binding
    FragmentRegistrosBinding binding;

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

        /* Deprecade
        TextView mytext = (TextView) view.findViewById(R.id.textIncio);
        TextInputLayout tilNombre = (TextInputLayout) view.findViewById(R.id.txtNombre);
        Button myButton = (Button)  view.findViewById(R.id.button);
         */

        // Conexión al Nodo de la Base de Datos Firebase
        String DB_FB_NODE = "message";
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference(DB_FB_NODE);
        DatabaseReference userRef = dbRef.child("Usuarios");

        // myRef.setValue("Hello, ITChiná 03/10/2022!");

        // Read from the database
        /*   Primera Version
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                Toast.makeText(getActivity(), "Test ItChiná"+ value, Toast.LENGTH_SHORT).show();
                binding.textIncio.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        */

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                myRef.setValue(binding.txtNombre.getEditText().getText().toString());
                Toast.makeText(view.getContext(),  "Registro de Usuario Exitoso", Toast.LENGTH_SHORT).show();
                binding.txtNombre.getEditText().setText("");
                 */
                String nombre = binding.inputNombre.getEditText().getText().toString();
                String correo = binding.inputCorreo.getEditText().getText().toString();
                String imagen = binding.inputImagen.getEditText().getText().toString();


                userRef.push().setValue(nombre);
                userRef.push().setValue(correo);
                userRef.push().setValue(imagen);

                Usuarios usuarios = new Usuarios(userRef.push().getKey(), nombre, correo, imagen);
                userRef.child(usuarios.getUid()).setValue(usuarios);

                binding.inputNombre.getEditText().setText("");
                binding.inputCorreo.getEditText().setText("");
                binding.inputImagen.getEditText().setText("");
                binding.inputNombre.requestFocus();
                Toast.makeText(view.getContext(),  "Registro de Usuario Exitoso", Toast.LENGTH_SHORT).show();

            }
        });

    }
}