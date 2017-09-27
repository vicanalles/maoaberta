package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.maoaberta.vinicius.maoaberta.domain.models.Voluntario;
import com.maoaberta.vinicius.maoaberta.domain.repository.UsuarioRepository;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.CadastroActivity;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.EsqueceuSenhaActivity;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.MenuPrincipalClienteActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vinicius on 30/08/17.
 */

public class TabLoginCliente extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static int RC_SIGN_IN = 1; //codigo retornado ao selecionar uma conta no google
    private FirebaseAuth mAuth;
    GoogleApiClient mGoogleApiClient;

    @BindView(R.id.edit_text_login_email_cliente)
    EditText edit_text_login_email_cliente;
    @BindView(R.id.edit_text_login_senha_cliente)
    EditText edit_text_login_senha_cliente;
    @BindView(R.id.button_app_login_cliente)
    Button button_app_login_cliente;
    @BindView(R.id.text_view_abrir_cadastro_login_cliente)
    TextView text_view_abrir_cadastro_login_cliente;
    @BindView(R.id.linear_layout_gmail)
    LinearLayout linear_layout_gmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab_login_cliente, container, false);
        ButterKnife.bind(this, v);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();

        text_view_abrir_cadastro_login_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActivityCadastro();
            }
        });

        button_app_login_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailLogin = String.valueOf(edit_text_login_email_cliente.getText());
                String senhaLogin = String.valueOf(edit_text_login_senha_cliente.getText());
                if (!emailLogin.equals("") && !senhaLogin.equals("")) {
                    logarUsuario(emailLogin, senhaLogin);
                } else {
                    campoVazio();
                }

            }
        });

        linear_layout_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        return v;
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
                builder.setMessage(R.string.falha_nos_dados);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        final UsuarioRepository usuarioRepository = new UsuarioRepository();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            AuthResult result = task.getResult();
                            final FirebaseUser user = result.getUser();
                            usuarioRepository.getUserByUid(user.getUid(), new UsuarioRepository.OnGetUserById() {
                                @Override
                                public void onGetUserByIdSuccess(Voluntario voluntario) {
                                    if (voluntario != null) {
                                        if(voluntario.getEmail().equals(user.getEmail())){
                                            abrirMenuPrincipalCliente("2");
                                        }
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
                                        builder.setMessage(R.string.pre_cadastro);
                                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                abrirMenuPrincipalCliente("2");
                                            }
                                        });
                                        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                user.delete();
                                                edit_text_login_email_cliente.setText("");
                                                edit_text_login_senha_cliente.setText("");
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                }

                                @Override
                                public void onGetUserByIdError(String error) {
                                    Log.d("onGetUserByIdError", error);
                                }
                            });
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
                            builder.setMessage(R.string.falha_nos_dados);
                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    edit_text_login_email_cliente.setText("");
                                    edit_text_login_senha_cliente.setText("");
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });
    }

    public void abrirActivityCadastro() {
        Intent intent = new Intent(getContext(), CadastroActivity.class);
        startActivity(intent);
    }

    public void abrirMenuPrincipalCliente(String codigoAtivaCampos) {
        Intent intent = new Intent(getContext(), MenuPrincipalClienteActivity.class);
        intent.putExtra("codigo", codigoAtivaCampos);
        startActivity(intent);
        getActivity().finish();
    }

    public void logarUsuario(final String email, String senha) {
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            alertaDadosIncorretos();
                        } else {
                            abrirMenuPrincipalCliente("1");
                        }
                    }
                });
    }

    private void alertaDadosIncorretos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setMessage(R.string.dados_incorretos);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edit_text_login_senha_cliente.setText("");
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void campoVazio() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setMessage(R.string.preencher_campos);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }
}
