package com.maoaberta.vinicius.maoaberta.domain.repository;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Organizacao;
import com.maoaberta.vinicius.maoaberta.util.Constants;

import java.io.ByteArrayOutputStream;

/**
 * Created by Vinicius on 23/09/2017.
 */

public class OrganizacaoRepository {

    private final DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public OrganizacaoRepository(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("organizacoes/");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl(Constants.FIREBASE_STORAGE_URL);
    }

    public String getUidCurrentUser() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        } else {
            return "";
        }
    }

    public void salvarDadosOrganizacao(final Organizacao organizacao, Bitmap imagemOrganizacao, final OnSaveOrganizacao onSaveOrganizacao){
        if(imagemOrganizacao != null){
            salvarImagemOrganizacao(imagemOrganizacao, getUidCurrentUser(), new OnImageUpload() {
                @Override
                public void onImageUploadSuccess(Uri imageUrl) {
                    organizacao.setPhotoUrl(imageUrl.toString());
                    cadastrarOrganizacao(organizacao, onSaveOrganizacao);
                }

                @Override
                public void onImageUploadError(String error) {
                    onSaveOrganizacao.onSaveOrganizacaoError(error);
                }
            });
        }else{
            cadastrarOrganizacao(organizacao, onSaveOrganizacao);
        }
    }

    public void salvarImagemOrganizacao(Bitmap organizacaoImage, String organizacaoId, final OnImageUpload onImageUpload){

        StorageReference storageRef = storageReference.child("images/" + organizacaoId + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        organizacaoImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onImageUpload.onImageUploadError(e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                onImageUpload.onImageUploadSuccess(downloadUrl);
            }
        });
    }

    public void cadastrarOrganizacao(final Organizacao organizacao, final OnSaveOrganizacao onSaveOrganizacao){
        reference.child(getUidCurrentUser()).setValue(organizacao).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Log.i("CADASTRO", "Organização cadastrada com sucesso");
                    onSaveOrganizacao.onSaveOrganizacaoSuccess(organizacao);
                }else{
                    Log.i("CADASTRO", "Falha no cadastro da organização");
                    onSaveOrganizacao.onSaveOrganizacaoError(task.getException().getMessage());
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

    public void atualizarOrganizacao(Organizacao organizacao, final OnUpdateOrganizacao onUpdateOrganizacao){
        reference.child(getUidCurrentUser()).setValue(organizacao).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Log.i("CADASTRO", "Organização cadastrada com sucesso");
                    onUpdateOrganizacao.onUpdateOrganizacaoSuccess(String.valueOf(R.string.organizacao_atualizada_sucesso));
                }else{
                    Log.i("CADASTRO", "Falha no cadastro");
                    onUpdateOrganizacao.onUpdateOrganizacaoError(String.valueOf(R.string.erro_atualizacao_organizacao));
                }
            }
        });
    }

    public interface OnGetOrganizacaoById{
        void onGetOrganizacaoByIdSuccess(Organizacao organizacao);
        void onGetOrganizacaoByIdError(String error);
    }

    public interface OnSaveOrganizacao{
        void onSaveOrganizacaoSuccess(Organizacao organizacao);
        void onSaveOrganizacaoError(String error);
    }

    public interface OnImageUpload{
        void onImageUploadSuccess(Uri imageUrl);
        void onImageUploadError(String error);
    }

    public interface OnUpdateOrganizacao{
        void onUpdateOrganizacaoSuccess(String sucesso);
        void onUpdateOrganizacaoError(String error);
    }
}
