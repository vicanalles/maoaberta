package com.maoaberta.vinicius.maoaberta.domain.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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

    public void cadastrarUsuario(final Voluntario voluntario, FirebaseUser user){
        reference.child(user.getUid()).setValue(voluntario).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public void getUserByUid(final String uid, final OnGetUserById onGetUserById ){
        Query query = reference.child(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Voluntario voluntario = new Voluntario();
                if(dataSnapshot.getKey().equals(uid)){
                    voluntario = dataSnapshot.getValue(Voluntario.class);
                }

                onGetUserById.onGetUserByIdSuccess(voluntario);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onGetUserById.onGetUserByIdError(databaseError.getMessage());
            }
        });
    }

    public void atualizarUser(Voluntario voluntario, FirebaseUser user){
        reference.child(user.getUid()).setValue(voluntario).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public interface OnGetUserById{
        void onGetUserByIdSuccess(Voluntario voluntario);
        void onGetUserByIdError(String error);
    }
}
