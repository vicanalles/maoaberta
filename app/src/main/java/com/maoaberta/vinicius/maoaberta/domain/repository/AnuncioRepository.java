package com.maoaberta.vinicius.maoaberta.domain.repository;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

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
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Anuncio;
import com.maoaberta.vinicius.maoaberta.domain.models.Organizacao;
import com.maoaberta.vinicius.maoaberta.domain.models.Voluntario;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Vinicius on 24/10/2017.
 */

public class AnuncioRepository {

    private final DatabaseReference reference;
    private FirebaseAuth firebaseAuth;

    public AnuncioRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("anuncios/");
    }

    public String getUidCurrentUser() {
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
                if (task.isSuccessful()) {
                    Log.i("CADASTRO", "Anúncio cadastrado com sucesso!");
                    onSaveAnuncio.onSaveAnuncioSuccess("Anúncio criado com sucesso!");
                } else {
                    Log.i("CADASTRO", "Falha no cadastro do anúncio");
                    onSaveAnuncio.onSaveAnuncioError("Falha na criação do anúncio! Por favor, tente novamente!");
                }
            }
        });
    }

    //pega todos os anuncios da organizacao que esta logada
    public void getAllMeusAnunciosOrganizacao(final OnGetAllAnunciosOrganizacao onGetAllAnunciosOrganizacao) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Anuncio> anuncios = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Anuncio anuncio = data.getValue(Anuncio.class);
                    if (anuncio != null) {
                        if (anuncio.getIdProprietario().equals(getUidCurrentUser())) {
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

    //pega todos os anuncios da organizacao selecionada
    public void getAllAnunciosOrganizacao(final Organizacao organizacao, final OnGetAllAnuncios onGetAllAnuncios) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Anuncio> anuncios = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Anuncio anuncio = data.getValue(Anuncio.class);
                    if (anuncio != null) {
                        if (anuncio.getIdProprietario().equals(organizacao.getId())) {
                            anuncio.setId(data.getKey());
                            anuncios.add(anuncio);
                        }
                    }
                }

                onGetAllAnuncios.onGetAllAnunciosSuccess(anuncios);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onGetAllAnuncios.onGetAllAnunciosError(databaseError);
            }
        });
    }

    //pega todos os anuncios que não são do usuário logado
    public void getAllAnuncios(final OnGetAllAnuncios onGetAllAnuncios) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Anuncio> anuncios = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Anuncio anuncio = data.getValue(Anuncio.class);
                    if (!anuncio.getIdProprietario().equals(getUidCurrentUser())) {
                        anuncio.setId(data.getKey());
                        anuncios.add(anuncio);
                    }
                }

                onGetAllAnuncios.onGetAllAnunciosSuccess(anuncios);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onGetAllAnuncios.onGetAllAnunciosError(databaseError);
            }
        });
    }

    public void atualizarAnuncio(Anuncio anuncio, String id, final OnUpdateAnuncio onUpdateAnuncio) {
        reference.child(id).setValue(anuncio).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    onUpdateAnuncio.onUpdateAnuncioSuccess("Anúncio atualizado com sucesso!");
                } else {
                    onUpdateAnuncio.onUpdateAnuncioError("Falha na atualização do anúncio! Por favor, tente novamente!");
                }
            }
        });
    }

    public interface OnSaveAnuncio {
        void onSaveAnuncioSuccess(String sucesso);

        void onSaveAnuncioError(String error);
    }

    public interface OnGetAllAnunciosOrganizacao {
        void onGetAllAnunciosOrganizacaoSuccess(List<Anuncio> anuncios);

        void onGetAllAnunciosOrganizacaoError(DatabaseError databaseError);
    }

    public interface OnGetAllAnuncios {
        void onGetAllAnunciosSuccess(List<Anuncio> anuncios);

        void onGetAllAnunciosError(DatabaseError erro);
    }

    public interface OnUpdateAnuncio {
        void onUpdateAnuncioSuccess(String sucesso);

        void onUpdateAnuncioError(String erro);
    }
}
