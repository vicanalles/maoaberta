package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Organizacao;
import com.maoaberta.vinicius.maoaberta.domain.repository.OrganizacaoRepository;
import com.maoaberta.vinicius.maoaberta.domain.repository.TipoRepository;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.MenuPrincipalClienteActivity;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.MenuPrincipalOrganizacaoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius Canalles on 16/08/2017.
 */

public class TabCadastroOrganizacao extends Fragment {

    FirebaseAuth mAuth;

    @BindView(R.id.edit_text_razao_social_organizacao)
    EditText razaoSocial;
    @BindView(R.id.edit_text_nome_fantasia_organizacao)
    EditText nomeFantasia;
    @BindView(R.id.edit_text_cnpj_organizacao)
    EditText cnpjOrganizacao;
    @BindView(R.id.edit_text_nome_responsavel_organizacao)
    EditText nomeResponsavel;
    @BindView(R.id.edit_text_email_organizacao)
    EditText emailOrganizacao;
    @BindView(R.id.edit_text_telefone_organizacao)
    EditText telefoneOrganizacao;
    @BindView(R.id.edit_text_senha_organizacao)
    EditText senhaOrganizacao;
    @BindView(R.id.edit_text_confirmar_senha_organizacao)
    EditText confirmarSenhaOrganizacao;
    @BindView(R.id.button_cadastrar_organizacao)
    Button cadastrarOrganizacao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab_cadastro_organizacao, container, false);
        ButterKnife.bind(this, v);

        mAuth = FirebaseAuth.getInstance();

        cadastrarOrganizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(razaoSocial.getText()).equals("") || String.valueOf(nomeFantasia.getText()).equals("") || String.valueOf(cnpjOrganizacao.getText()).equals("") ||
                        String.valueOf(nomeResponsavel.getText()).equals("") || String.valueOf(emailOrganizacao.getText()).equals("") || String.valueOf(telefoneOrganizacao.getText()).equals("") ||
                        String.valueOf(senhaOrganizacao.getText()).equals("") || String.valueOf(confirmarSenhaOrganizacao.getText()).equals("")){
                    alertaCamposNaoPreenchidos();
                }else{
                    if(String.valueOf(senhaOrganizacao.getText()).equals(String.valueOf(confirmarSenhaOrganizacao.getText()))){
                        if(senhaOrganizacao.getText().length() >= 6 && confirmarSenhaOrganizacao.getText().length() >= 6){
                            createAccountWithEmailAndPassword(String.valueOf(emailOrganizacao.getText()), String.valueOf(senhaOrganizacao.getText()));
                        }else{
                            alertaSenhaCurta();
                        }
                    }else{
                        alertaSenhasDiferentes();
                    }
                }
            }
        });

        return v;
    }

    private void createAccountWithEmailAndPassword(final String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
                            builder.setMessage("Não foi possível salvar a organização. Por favor, tente novamente!");
                            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }else{
                            AuthResult result = task.getResult();
                            FirebaseUser user = result.getUser();
                            Organizacao organizacao = new Organizacao();
                            organizacao.setRazaoSocial(String.valueOf(razaoSocial.getText()));
                            organizacao.setNomeFantasia(String.valueOf(nomeFantasia.getText()));
                            organizacao.setCnpj(String.valueOf(cnpjOrganizacao.getText()));
                            organizacao.setNomeResponsavel(String.valueOf(nomeResponsavel.getText()));
                            organizacao.setEmail(String.valueOf(emailOrganizacao.getText()));
                            organizacao.setTelefone(String.valueOf(telefoneOrganizacao.getText()));
                            OrganizacaoRepository organizacaoRepository = new OrganizacaoRepository();
                            organizacaoRepository.cadastrarOrganizacao(organizacao, user);
                            TipoRepository tipoRepository = new TipoRepository();
                            tipoRepository.cadastrarTipo(user, "tipo", "organizaçao");

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
                            builder.setMessage("Organização cadastrada com sucesso!");
                            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    abrirMenuPrincipalOrganizacao();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });
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

    private void alertaSenhaCurta(){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setMessage(R.string.senha_curta);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void alertaSenhasDiferentes() {
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

    private void abrirMenuPrincipalOrganizacao(){
        Intent intent = new Intent(getContext(), MenuPrincipalOrganizacaoActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
