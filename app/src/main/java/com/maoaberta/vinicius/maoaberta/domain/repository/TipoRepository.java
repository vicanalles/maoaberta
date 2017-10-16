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

    public void cadastrarTipo(String uid, String tipo, String valor, final OnCadastrarTipo onCadastrarTipo){
        reference.child(uid).child(tipo).setValue(valor).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Log.i("CADASTRO", "Usuário cadastrado com sucesso");
                    onCadastrarTipo.onCadastrarTipoSuccess("Cadastro realizado com sucesso!");
                }else{
                    Log.i("CADASTRO", "Falha no cadastro");
                    onCadastrarTipo.onCadastrarTipoError("Erro ao realizar o cadastro. Por favor, tente novamente!");
                }
            }
        });
    }

    public void getTipoById(final String uid, final OnGetTipoById onGetTipoById){
        Query query = reference.child(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tipoUser = null;
                if(dataSnapshot.getKey().equals(uid)){
                    tipoUser = dataSnapshot.child("tipo").getValue(String.class);
                    Log.i("USER", "Usuário");
                }else{

                }

                onGetTipoById.onGetTipoByIdSuccess(tipoUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onGetTipoById.onGetTipoByIdError(databaseError.getMessage());
            }
        });
    }

    public interface OnGetTipoById{
        void onGetTipoByIdSuccess(String tipo);
        void onGetTipoByIdError(String error);
    }

    public interface OnCadastrarTipo{
        void onCadastrarTipoSuccess(String sucesso);
        void onCadastrarTipoError(String error);
    }
}
