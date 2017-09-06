package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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

    private static int RC_SIGN_IN = 1; //codigo retornado ao selecionar uma conta no google
    FirebaseAuth mAuth;

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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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
                if (String.valueOf(nomeCliente.getText()).equals("") || String.valueOf(telefoneCliente.getText()).equals("") ||
                        String.valueOf(emailCliente.getText()).equals("") || String.valueOf(senhaCliente.getText()).equals("") ||
                        String.valueOf(confirmarSenha.getText()).equals("")) {
                    alertaCamposNaoPreenchidos();
                } else {
                    if (String.valueOf(senhaCliente.getText()).equals(String.valueOf(confirmarSenha.getText()))) {
                        Toast.makeText(getActivity(), "Cadastrar Cliente!", Toast.LENGTH_SHORT).show();
                    } else {
                        alertaSenhasDiferentes();
                    }
                }
            }
        });

        return v;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            Intent intent = new Intent(getContext(), MenuPrincipalClienteActivity.class);
            intent.putExtra("nome", account.getDisplayName());
            intent.putExtra("email", account.getEmail());
            intent.putExtra("id", account.getId());
            intent.putExtra("token", account.getIdToken());
            startActivity(intent);
            getActivity().finish();
        } else {
            Toast.makeText(getActivity(), "Falha na Autenticação!", Toast.LENGTH_SHORT).show();
        }
    }

    private void alertaCamposNaoPreenchidos(){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setMessage(R.string.campos_nao_preenchidos);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void alertaSenhasDiferentes(){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setMessage(R.string.senhas_diferentes);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
