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
import com.maoaberta.vinicius.maoaberta.domain.models.Organizacao;

/**
 * Created by Vinicius on 23/09/2017.
 */

public class OrganizacaoRepository {

    private final DatabaseReference reference;
    private FirebaseAuth firebaseAuth;

    public OrganizacaoRepository(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("organizacoes/");
    }

    public void cadastrarOrganizacao(Organizacao organizacao, FirebaseUser user){
        reference.child(user.getUid()).setValue(organizacao).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Log.i("CADASTRO", "Organização cadastrada com sucesso");
                }else{
                    Log.i("CADASTRO", "Falha no cadastro da organização");
                }
            }
        });
    }

    public void getOrganizacaoById(final String uid, final OnGetOrganizacaoById onGetOrganizacaoById){
        Query query = reference.child(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Organizacao organizacao = new Organizacao();
                if(dataSnapshot.getKey().equals(uid)){
                    organizacao = dataSnapshot.getValue(Organizacao.class);
                }

                onGetOrganizacaoById.onGetOrganizacaoByIdSuccess(organizacao);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onGetOrganizacaoById.onGetOrganizacaoByIdError(databaseError.getMessage());
            }
        });
    }

    public void atualizarOrganizacao(Organizacao organizacao, FirebaseUser user){
        reference.child(user.getUid()).setValue(organizacao).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Log.i("CADASTRO", "Organização cadastrada com sucesso");
                }else{
                    Log.i("CADASTRO", "Falha no cadastro");
                }
            }
        });
    }

    public interface OnGetOrganizacaoById{
        void onGetOrganizacaoByIdSuccess(Organizacao organizacao);
        void onGetOrganizacaoByIdError(String error);
    }
}
