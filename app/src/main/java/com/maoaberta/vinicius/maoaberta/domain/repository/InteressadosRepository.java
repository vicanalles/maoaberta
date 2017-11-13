package com.maoaberta.vinicius.maoaberta.domain.repository;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maoaberta.vinicius.maoaberta.domain.models.Anuncio;
import com.maoaberta.vinicius.maoaberta.domain.models.Interessados;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vinicius on 13/11/17.
 */

public class InteressadosRepository {

    private final DatabaseReference reference;
    private FirebaseAuth firebaseAuth;

    public InteressadosRepository(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("interessados/");
    }

    public String getUidCurrentUser(){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        } else {
            return "";
        }
    }

    public void salvarOrganizacaoInteressadaAnuncio(Anuncio anuncio, final OnSalvarInteresse onSalvarInteresse) {
        reference.child(anuncio.getId()).child(getUidCurrentUser()).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                onSalvarInteresse.onSalvarInteresseSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onSalvarInteresse.onSalvarInteresseError();
            }
        });
    }

    public void getAnunciosInteressado(final OnGetAllAnunciosInteresseVoluntario onGetAllAnunciosInteresseVoluntario){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Boolean> anuncios = new HashMap<>();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Interessados id = data.getValue(Interessados.class);
                    if(id != null){
                        if(id.equals(getUidCurrentUser())){
                            anuncios.put(id.getChave(), id.getValor());
                        }
                    }
                }
                onGetAllAnunciosInteresseVoluntario.onGetAllAnunciosInteresseVoluntarioSuccess(anuncios);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onGetAllAnunciosInteresseVoluntario.onGetAllAnunciosInteresseVoluntarioError(databaseError);
            }
        });
    }

    public interface OnSalvarInteresse {
        void onSalvarInteresseSuccess();

        void onSalvarInteresseError();
    }

    public interface OnGetAllAnunciosInteresseVoluntario {
        void onGetAllAnunciosInteresseVoluntarioSuccess(HashMap<String, Boolean> anuncios);

        void onGetAllAnunciosInteresseVoluntarioError(DatabaseError databaseError);
    }
}
