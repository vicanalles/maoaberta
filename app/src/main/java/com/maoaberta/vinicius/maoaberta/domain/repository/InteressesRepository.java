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
import com.maoaberta.vinicius.maoaberta.domain.models.Voluntario;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinicius on 16/11/2017.
 */

public class InteressesRepository {

    private final DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private String pessoa;

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

    public void getInteressadosAnuncio(Anuncio anuncio, final OnGetInteressadosAnuncio onGetInteressadosAnuncio){
        reference.child(anuncio.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> pessoasInteressadas = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    pessoa = data.getKey();
                    if(pessoa != null){
                        pessoasInteressadas.add(pessoa);
                    }
                }
                onGetInteressadosAnuncio.onGetInteressadosAnuncioSuccess(pessoasInteressadas);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onGetInteressadosAnuncio.onGetInteressadosAnuncioError(databaseError);
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

    public interface OnGetInteressadosAnuncio{
        void onGetInteressadosAnuncioSuccess(List<String> pessoas);
        void onGetInteressadosAnuncioError(DatabaseError databaseError);
    }
}
