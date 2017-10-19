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
import com.maoaberta.vinicius.maoaberta.BuildConfig;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Voluntario;
import com.maoaberta.vinicius.maoaberta.util.Constants;

import java.io.ByteArrayOutputStream;

/**
 * Created by Vinicius on 10/09/2017.
 */

public class UsuarioRepository {

    private final DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public UsuarioRepository(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("usuarios/");
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

    public void salvarDadosVoluntario(final Voluntario voluntario, Bitmap imagemVoluntario, final OnSaveVoluntario onSaveVoluntario){
        if(imagemVoluntario != null){
            salvarImagemUsuario(imagemVoluntario, getUidCurrentUser(), new OnImageUpload() {
                @Override
                public void onImageUploadSuccess(Uri imageUrl) {
                    voluntario.setPhotoUrl(imageUrl.toString());
                    cadastrarUsuario(voluntario, onSaveVoluntario);
                }

                @Override
                public void onImageUploadError(String error) {
                    onSaveVoluntario.onSaveVoluntarioError(error);
                }
            });
        }else{
            cadastrarUsuario(voluntario, onSaveVoluntario);
        }
    }

    public void salvarImagemUsuario(Bitmap userImage, String userId, final OnImageUpload onImageUpload){

        StorageReference profileImageRef = storageReference.child("images/" + "voluntarios/" + userId + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = profileImageRef.putBytes(data);
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

    public void cadastrarUsuario(final Voluntario voluntario, final OnSaveVoluntario onSaveVoluntario){
        reference.child(getUidCurrentUser()).setValue(voluntario).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.i("CADASTRO", "Voluntário cadastrado com sucesso");
                    onSaveVoluntario.onSaveVoluntarioSuccess(voluntario);
                }else{
                    Log.i("CADASTRO", "Falha no cadastro do voluntário");
                    onSaveVoluntario.onSaveVoluntarioError(task.getException().getMessage());
                }
            }
        });
    }

    public void atualizarDadosVoluntario(final Voluntario voluntario, Bitmap imagemVoluntario, final OnUpdateUsuario onUpdateUsuario){
        if(imagemVoluntario != null){
            salvarImagemUsuario(imagemVoluntario, getUidCurrentUser(), new OnImageUpload() {
                @Override
                public void onImageUploadSuccess(Uri imageUrl) {
                    voluntario.setPhotoUrl(imageUrl.toString());
                    atualizarUser(voluntario, onUpdateUsuario);
                }

                @Override
                public void onImageUploadError(String error) {
                    onUpdateUsuario.onUpdateUsuarioError(error);
                }
            });
        }else{
            atualizarUser(voluntario, onUpdateUsuario);
        }
    }

    public void atualizarUser(Voluntario voluntario, final OnUpdateUsuario onUpdateUsuario){
        reference.child(getUidCurrentUser()).setValue(voluntario).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Log.i("CADASTRO", "Usuário cadastrado com sucesso");
                    onUpdateUsuario.onUpdateUsuarioSuccess(String.valueOf(R.string.atualizacao_usuario_sucesso));
                }else{
                    Log.i("CADASTRO", "Falha no cadastro");
                    onUpdateUsuario.onUpdateUsuarioError(String.valueOf(R.string.erro_atualizar_dados_usuario));
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

    public interface OnGetUserById{
        void onGetUserByIdSuccess(Voluntario voluntario);
        void onGetUserByIdError(String error);
    }

    public interface OnImageUpload{
        void onImageUploadSuccess(Uri imageUrl);
        void onImageUploadError(String error);
    }

    public interface OnSaveVoluntario{
        void onSaveVoluntarioSuccess(Voluntario voluntario);
        void onSaveVoluntarioError(String error);
    }

    public interface OnUpdateUsuario{
        void onUpdateUsuarioSuccess(String sucesso);
        void onUpdateUsuarioError(String error);
    }
}
