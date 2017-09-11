package com.maoaberta.vinicius.maoaberta.domain.repository;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maoaberta.vinicius.maoaberta.domain.models.Voluntario;

/**
 * Created by Vinicius on 10/09/2017.
 */

public class UsuarioRepository {

    private final DatabaseReference reference;
    private FirebaseAuth firebaseAuth;

    public UsuarioRepository(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("usuarios/");
    }

    public void cadastrarUsuario(final Voluntario voluntario){
        DatabaseReference push = reference.push();
        push.setValue(voluntario).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Log.i("CADASTRO", "Usuário cadastrado com sucesso");
                }else{
                    Log.i("CADASTRO", "Falha no cadastro");
                }
            }
        });
    }
}
