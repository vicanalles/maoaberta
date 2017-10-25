package com.maoaberta.vinicius.maoaberta.domain.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Anuncio;

/**
 * Created by Vinicius on 24/10/2017.
 */

public class AnuncioRepository {

    private final DatabaseReference reference;
    private FirebaseAuth firebaseAuth;

    public AnuncioRepository(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("anuncios/");
    }

    public String getUidCurrentUser(){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        } else {
            return "";
        }
    }


    public void salvarDadosAnuncio(Anuncio anuncio, final OnSaveAnuncio onSaveAnuncio) {
        DatabaseReference push = reference.push();
        push.setValue(anuncio).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.i("CADASTRO", "Anúncio cadastrado com sucesso!");
                    onSaveAnuncio.onSaveAnuncioSuccess("Anúncio criado com sucesso!");
                }else{
                    Log.i("CADASTRO", "Falha no cadastro do anúncio");
                    onSaveAnuncio.onSaveAnuncioError("Falha na criação do anúncio! Por favor, tente novamente!");
                }
            }
        });
    }

    public interface OnSaveAnuncio{
        void onSaveAnuncioSuccess(String sucesso);
        void onSaveAnuncioError(String error);
    }
}
