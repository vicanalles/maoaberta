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
import com.google.firebase.database.ValueEventListener;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Anuncio;

import java.util.ArrayList;
import java.util.List;

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

    public void getAllAnunciosOrganizacao(String uid, final OnGetAllAnunciosOrganizacao onGetAllAnunciosOrganizacao){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Anuncio> anuncios = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Anuncio anuncio = data.getValue(Anuncio.class);
                    if(anuncio != null){
                        if(anuncio.getIdProprietario().equals(getUidCurrentUser())){
                            anuncio.setId(data.getKey());
                            anuncios.add(anuncio);
                        }
                    }
                }
                onGetAllAnunciosOrganizacao.onGetAllAnunciosOrganizacaoSuccess(anuncios);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onGetAllAnunciosOrganizacao.onGetAllAnunciosOrganizacaoError(databaseError);
            }
        });
    }

    public interface OnSaveAnuncio{
        void onSaveAnuncioSuccess(String sucesso);
        void onSaveAnuncioError(String error);
    }

    public interface OnGetAllAnunciosOrganizacao{
        void onGetAllAnunciosOrganizacaoSuccess(List<Anuncio> anuncios);
        void onGetAllAnunciosOrganizacaoError(DatabaseError databaseError);
    }
}
