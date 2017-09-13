package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Voluntario;
import com.maoaberta.vinicius.maoaberta.domain.repository.UsuarioRepository;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius on 03/09/2017.
 */

public class FragmentMenuAnunciosCliente extends Fragment {

    private FirebaseAuth mAuth = null;
    private FirebaseUser user = null;
    private UsuarioRepository usuarioRepository;

    @BindView(R.id.text_view_anuncios_fragment_cliente)
    TextView text_view_anuncios_fragment_cliente;

    public FragmentMenuAnunciosCliente(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_anuncios_cliente, container, false);
        ButterKnife.bind(this, view);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        usuarioRepository = new UsuarioRepository();

        if(user != null){
            String uidUser = user.getUid();
            usuarioRepository.getUserByUid(uidUser, new UsuarioRepository.OnGetUserById() {
                @Override
                public void onGetUserByIdSuccess(Voluntario voluntario) {
                    Voluntario vol = new Voluntario();
                    vol.setId(voluntario.getId());
                    vol.setNome(voluntario.getNome());
                    vol.setEmail(voluntario.getEmail());
                    vol.setTelefone(voluntario.getTelefone());
                    text_view_anuncios_fragment_cliente.setText("Seja bem vindo " + vol.getNome());
                }

                @Override
                public void onGetUserByIdError(String error) {
                    Log.d("onGetUserByIdError", error);
                    Toast.makeText(getActivity(), "Usuário não existe", Toast.LENGTH_LONG).show();
                }
            });
        }
        return view;
    }
}
