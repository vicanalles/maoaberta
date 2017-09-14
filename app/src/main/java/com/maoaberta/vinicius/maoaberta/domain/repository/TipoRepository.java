package com.maoaberta.vinicius.maoaberta.domain.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by vinicius on 14/09/17.
 */

public class TipoRepository {

    private final DatabaseReference reference;
    private FirebaseAuth firebaseAuth;

    public TipoRepository(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("tipo/");
    }

    public void cadastrarTipo(FirebaseUser user, String tipo, String valor){
        reference.child(user.getUid()).child(tipo).setValue(valor).addOnCompleteListener(new OnCompleteListener<Void>() {
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
