package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.MenuPrincipalClienteActivity;

import java.util.concurrent.Executor;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius Canalles on 16/08/2017.
 */

public class TabCadastroCliente extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;

    @BindView(R.id.edit_text_nome_cadastro_cliente)
    EditText nomeCliente;
    @BindView(R.id.edit_text_telefone_cadastro_cliente)
    EditText telefoneCliente;
    @BindView(R.id.edit_text_email_cadastro_cliente)
    EditText emailCliente;
    @BindView(R.id.edit_text_senha_cadastro_cliente)
    EditText senhaCliente;
    @BindView(R.id.edit_text_confirmar_senha_cadastro_cliente)
    EditText confirmarSenha;
    @BindView(R.id.button_cadastrar_cliente)
    Button cadastrarCliente;
    @BindView(R.id.linear_layout_gmail)
    LinearLayout linear_layout_gmail;

    GoogleApiClient mGoogleApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab_cadastro_cliente, container, false);
        ButterKnife.bind(this, v);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();

        linear_layout_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        cadastrarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Abrir Menu Principal Cliente", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            Intent intent = new Intent(getContext(), MenuPrincipalClienteActivity.class);
            intent.putExtra("nome", account.getDisplayName());
            intent.putExtra("email", account.getEmail());
            intent.putExtra("id", account.getId());
            intent.putExtra("token", account.getIdToken());
            startActivity(intent);
            getActivity().finish();
        }else{
            Toast.makeText(getActivity(), "Falha na Autenticação!", Toast.LENGTH_SHORT).show();
        }
    }
}
