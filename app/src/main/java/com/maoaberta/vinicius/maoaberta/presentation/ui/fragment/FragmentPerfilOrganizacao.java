package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Organizacao;
import com.maoaberta.vinicius.maoaberta.domain.repository.OrganizacaoRepository;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.MenuPrincipalOrganizacaoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius on 23/08/2017.
 */

public class FragmentPerfilOrganizacao extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private OrganizacaoRepository organizacaoRepository;
    private Organizacao ong;

    @BindView(R.id.relative_layout_image_logo_perfil_organizacao)
    RelativeLayout relative_layout_image_logo_perfil_organizacao;
    @BindView(R.id.image_view_logo_perfil_organizacao)
    ImageView image_view_logo_perfil_organizacao;
    @BindView(R.id.text_view_escolher_foto_perfil_organizacao)
    TextView text_view_escolher_foto_perfil_organizacao;
    @BindView(R.id.edit_text_razao_social_perfil_organizacao)
    EditText edit_text_razao_social_perfil_organizacao;
    @BindView(R.id.edit_text_nome_fantasia_perfil_organizacao)
    EditText edit_text_nome_fantasia_perfil_organizacao;
    @BindView(R.id.edit_text_cnpj_perfil_organizacao)
    EditText edit_text_cnpj_perfil_organizacao;
    @BindView(R.id.edit_text_nome_responsavel_perfil_organizacao)
    EditText edit_text_nome_responsavel_perfil_organizacao;
    @BindView(R.id.edit_text_email_perfil_organizacao)
    EditText edit_text_email_perfil_organizacao;
    @BindView(R.id.edit_text_telefone_perfil_organizacao)
    EditText edit_text_telefone_perfil_organizacao;
    @BindView(R.id.edit_text_endereco_perfil_organizacao)
    EditText edit_text_endereco_perfil_organizacao;
    @BindView(R.id.edit_text_facebook_perfil_organizacao)
    EditText edit_text_facebook_perfil_organizacao;
    @BindView(R.id.edit_text_twitter_perfil_organizacao)
    EditText edit_text_twitter_perfil_organizacao;
    @BindView(R.id.edit_text_site_perfil_organizacao)
    EditText edit_text_site_perfil_organizacao;
    @BindView(R.id.edit_text_descricao_perfil_organizacao)
    EditText edit_text_descricao_perfil_organizacao;
    @BindView(R.id.edit_text_senha_perfil_organizacao)
    EditText edit_text_senha_perfil_organizacao;
    @BindView(R.id.edit_text_confirmar_senha_perfil_organizacao)
    EditText edit_text_confirmar_senha_perfil_organizacao;
    @BindView(R.id.button_atualizar_dados_organizacao)
    Button button_atualizar_dados_organizacao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil_organizacao, container, false);
        ButterKnife.bind(this, view);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        organizacaoRepository = new OrganizacaoRepository();

        if(user != null){
            String uid = user.getUid();
            organizacaoRepository.getOrganizacaoById(uid, new OrganizacaoRepository.OnGetOrganizacaoById() {
                @Override
                public void onGetOrganizacaoByIdSuccess(Organizacao organizacao) {
                    if(organizacao != null){
                        edit_text_razao_social_perfil_organizacao.setText(organizacao.getRazaoSocial());
                        edit_text_nome_fantasia_perfil_organizacao.setText(organizacao.getNomeFantasia());
                        edit_text_cnpj_perfil_organizacao.setText(organizacao.getCnpj());
                        edit_text_nome_responsavel_perfil_organizacao.setText(organizacao.getNomeResponsavel());
                        edit_text_email_perfil_organizacao.setText(organizacao.getEmail());
                        edit_text_telefone_perfil_organizacao.setText(organizacao.getTelefone());
                        edit_text_endereco_perfil_organizacao.setText(organizacao.getEndereco());
                        edit_text_facebook_perfil_organizacao.setText(organizacao.getFacebookAccount());
                        edit_text_twitter_perfil_organizacao.setText(organizacao.getTwitterAccount());
                        edit_text_site_perfil_organizacao.setText(organizacao.getWebSite());
                        edit_text_descricao_perfil_organizacao.setText(organizacao.getDescricao());
                    }else{
                        edit_text_email_perfil_organizacao.setText(user.getEmail());
                    }
                }

                @Override
                public void onGetOrganizacaoByIdError(String error) {
                    Log.d("onGetUserByIdError", error);
                    Toast.makeText(getActivity(), "Usuário não existe", Toast.LENGTH_LONG).show();
                }
            });
        }

        button_atualizar_dados_organizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    private void alertaCamposNaoPreenchidos() {
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

    private void alertaSenhaCurta() {
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

    private void abrirMenuPrincipal() {
        Intent intent = new Intent(getContext(), MenuPrincipalOrganizacaoActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
