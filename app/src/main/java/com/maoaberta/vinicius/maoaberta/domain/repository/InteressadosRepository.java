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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vinicius on 13/11/17.
 */

public class InteressadosRepository {

    private final DatabaseReference reference;
    private FirebaseAuth firebaseAuth;

    public InteressadosRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("interessados/");
    }

    public String getUidCurrentUser() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        } else {
            return "";
        }
    }

    public void salvarOrganizacaoInteressadaAnuncio(Anuncio anuncio, final OnSalvarInteresse onSalvarInteresse) {
        reference.child(getUidCurrentUser()).child(anuncio.getId()).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public void removerInteresseAnuncio(String idAnuncio, final OnRemoverInteresse onRemoverInteresse){
        reference.child(getUidCurrentUser()).child(idAnuncio).setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    onRemoverInteresse.onRemoverInteresseSuccess("Interesse removido com sucesso");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onRemoverInteresse.onRemoverInteresseError("Não foi possível remover seu interesse. Por favor, tente novamente!");
            }
        });
    }

    public void getAnunciosInteressado(final OnGetAllAnunciosInteresseVoluntario onGetAllAnunciosInteresseVoluntario) {
        reference.child(getUidCurrentUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> identificadores = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if(data.getValue().equals(true)){
                        String id = data.getKey();
                        if(id != null){
                            identificadores.add(id);
                        }
                    }
                }
                onGetAllAnunciosInteresseVoluntario.onGetAllAnunciosInteresseVoluntarioSuccess(identificadores);
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

    public interface OnRemoverInteresse{
        void onRemoverInteresseSuccess(String sucesso);
        void onRemoverInteresseError(String error);
    }

    public interface OnGetAllAnunciosInteresseVoluntario {
        void onGetAllAnunciosInteresseVoluntarioSuccess(List<String> anuncios);
        void onGetAllAnunciosInteresseVoluntarioError(DatabaseError databaseError);
    }
}
