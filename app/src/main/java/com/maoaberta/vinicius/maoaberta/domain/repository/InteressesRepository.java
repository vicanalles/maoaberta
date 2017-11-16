package com.maoaberta.vinicius.maoaberta.domain.repository;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maoaberta.vinicius.maoaberta.domain.models.Anuncio;

/**
 * Created by Vinicius on 16/11/2017.
 */

public class InteressesRepository {

    private final DatabaseReference reference;
    private FirebaseAuth firebaseAuth;

    public InteressesRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("interesses/");
    }

    public String getUidCurrentUser() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        } else {
            return "";
        }
    }

    public void salvarInteresseOrganizacao(Anuncio anuncio, final OnSaveInteresse onSaveInteresse){
        reference.child(anuncio.getId()).child(getUidCurrentUser()).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                onSaveInteresse.onSaveInteresseSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onSaveInteresse.onSaveInteresseError();
            }
        });
    }

    public void removerInteresseOrganizacao(Anuncio anuncio, final OnRemoveInteresse onRemoveInteresse){
        reference.child(anuncio.getId()).child(getUidCurrentUser()).setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                onRemoveInteresse.onRemoveInteresseSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onRemoveInteresse.onRemoveInteresseError();
            }
        });
    }

    public interface OnSaveInteresse{
        void onSaveInteresseSuccess();
        void onSaveInteresseError();
    }

    public interface OnRemoveInteresse{
        void onRemoveInteresseSuccess();
        void onRemoveInteresseError();
    }
}
