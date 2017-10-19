package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Organizacao;
import com.maoaberta.vinicius.maoaberta.domain.repository.OrganizacaoRepository;
import com.maoaberta.vinicius.maoaberta.util.CustomPhotoPickerDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vinicius on 23/08/2017.
 */

public class PerfilOrganizacaoActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1598;
    private static final int PERMISSION_WRITE_EXTERNAL = 1594;
    private static final int REQUEST_IMAGE_CAPTURE = 1595;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private OrganizacaoRepository organizacaoRepository;
    private Organizacao ong;

    @BindView(R.id.toolbar_layout_menu_perfil_organizacao)
    Toolbar toolbar_layout_menu_perfil_organizacao;
    @BindView(R.id.relative_layout_image_logo_perfil_organizacao)
    RelativeLayout relative_layout_image_logo_perfil_organizacao;
    @BindView(R.id.image_view_logo_perfil_organizacao)
    CircleImageView image_view_logo_perfil_organizacao;
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
    private ProgressDialog progressDialog;
    private CustomPhotoPickerDialog photoDialog;
    private String mCurrentPhotoPath;
    private Bitmap mImageBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_organizacao);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_layout_menu_perfil_organizacao);

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        organizacaoRepository = new OrganizacaoRepository();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        if (user != null) {
            String uid = user.getUid();
            organizacaoRepository.getOrganizacaoById(uid, new OrganizacaoRepository.OnGetOrganizacaoById() {
                @Override
                public void onGetOrganizacaoByIdSuccess(Organizacao organizacao) {
                    if (organizacao != null) {
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
                        if(organizacao.getPhotoUrl() != null){
                            Glide.with(PerfilOrganizacaoActivity.this).load(organizacao.getPhotoUrl()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                                    .into(image_view_logo_perfil_organizacao);
                            text_view_escolher_foto_perfil_organizacao.setVisibility(View.GONE);
                            text_view_escolher_foto_perfil_organizacao.setEnabled(false);
                        }else{
                            text_view_escolher_foto_perfil_organizacao.setVisibility(View.VISIBLE);
                            text_view_escolher_foto_perfil_organizacao.setEnabled(true);
                        }
                    } else {
                        edit_text_email_perfil_organizacao.setText(user.getEmail());
                    }
                }

                @Override
                public void onGetOrganizacaoByIdError(String error) {
                    Log.d("onGetUserByIdError", error);
                    Toast.makeText(PerfilOrganizacaoActivity.this, "Organização não existe", Toast.LENGTH_LONG).show();
                }
            });
        }

        image_view_logo_perfil_organizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoDialog = new CustomPhotoPickerDialog(PerfilOrganizacaoActivity.this, new CustomPhotoPickerDialog.OnOptionPhotoSelected() {
                    @Override
                    public void onGallery() {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), SELECT_PICTURE);
                        photoDialog.dismiss();
                    }

                    @Override
                    public void onCamera() {
                        if (ContextCompat.checkSelfPermission(PerfilOrganizacaoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(PerfilOrganizacaoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            photoDialog.dismiss();
                            ActivityCompat.requestPermissions(PerfilOrganizacaoActivity.this,
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL);
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                File photoFile = null;
                                try {
                                    photoFile = createImageFile();
                                } catch (IOException e) {
                                    Log.i("TAG", e.getMessage());
                                }

                                if (photoFile != null) {
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                                    photoDialog.dismiss();
                                }
                            }
                        }
                    }
                });
                photoDialog.show();
            }
        });

        button_atualizar_dados_organizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(edit_text_razao_social_perfil_organizacao.getText()).equals("") || String.valueOf(edit_text_nome_fantasia_perfil_organizacao.getText()).equals("") ||
                        String.valueOf(edit_text_cnpj_perfil_organizacao.getText()).equals("") || String.valueOf(edit_text_nome_responsavel_perfil_organizacao.getText()).equals("") ||
                        String.valueOf(edit_text_email_perfil_organizacao.getText()).equals("") || String.valueOf(edit_text_telefone_perfil_organizacao.getText()).equals("")) {
                    alertaCamposNaoPreenchidos();
                } else {
                    image_view_logo_perfil_organizacao.setDrawingCacheEnabled(true);
                    final Bitmap bmap = image_view_logo_perfil_organizacao.getDrawingCache();
                    user = mAuth.getCurrentUser();
                    if (user != null) {
                        organizacaoRepository.getOrganizacaoById(user.getUid(), new OrganizacaoRepository.OnGetOrganizacaoById() {
                            @Override
                            public void onGetOrganizacaoByIdSuccess(Organizacao organizacao) {
                                showProgressDialog("Atualizando dados", "Aguarde enquanto os dados são atualizados");
                                if (organizacao != null) {
                                    ong = new Organizacao();
                                    ong.setCnpj(String.valueOf(edit_text_cnpj_perfil_organizacao.getText()));
                                    ong.setDescricao(String.valueOf(edit_text_descricao_perfil_organizacao.getText()));
                                    ong.setEmail(String.valueOf(edit_text_email_perfil_organizacao.getText()));
                                    ong.setFacebookAccount(String.valueOf(edit_text_facebook_perfil_organizacao.getText()));
                                    ong.setNomeFantasia(String.valueOf(edit_text_nome_fantasia_perfil_organizacao.getText()));
                                    ong.setNomeResponsavel(String.valueOf(edit_text_nome_responsavel_perfil_organizacao.getText()));
                                    ong.setTelefone(String.valueOf(edit_text_telefone_perfil_organizacao.getText()));
                                    ong.setTwitterAccount(String.valueOf(edit_text_twitter_perfil_organizacao.getText()));
                                    ong.setWebSite(String.valueOf(edit_text_site_perfil_organizacao.getText()));
                                    ong.setRazaoSocial(String.valueOf(edit_text_razao_social_perfil_organizacao.getText()));
                                    ong.setEndereco(String.valueOf(edit_text_endereco_perfil_organizacao.getText()));

                                    if (!String.valueOf(edit_text_senha_perfil_organizacao.getText()).equals("")) {
                                        if (String.valueOf(edit_text_senha_perfil_organizacao.getText()).equals(String.valueOf(edit_text_confirmar_senha_perfil_organizacao.getText()))) {
                                            if (edit_text_senha_perfil_organizacao.getText().length() >= 6 && edit_text_confirmar_senha_perfil_organizacao.getText().length() >= 6) {
                                                user.updatePassword(String.valueOf(edit_text_senha_perfil_organizacao.getText())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            organizacaoRepository.atualizarDadosOrganizacao(ong, bmap, new OrganizacaoRepository.OnUpdateOrganizacao() {
                                                                @Override
                                                                public void onUpdateOrganizacaoSuccess(String sucesso) {
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilOrganizacaoActivity.this, R.style.AppTheme));
                                                                    builder.setMessage(R.string.organizacao_atualizada_sucesso);
                                                                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int i) {
                                                                            hideProgressDialog();
                                                                            abrirMenuPerfilOrganizacao();
                                                                        }
                                                                    });
                                                                    AlertDialog dialog = builder.create();
                                                                    dialog.show();
                                                                }

                                                                @Override
                                                                public void onUpdateOrganizacaoError(String error) {
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilOrganizacaoActivity.this, R.style.AppTheme));
                                                                    builder.setMessage(R.string.erro_atualizacao_organizacao);
                                                                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int i) {
                                                                            hideProgressDialog();
                                                                            abrirMenuPerfilOrganizacao();
                                                                        }
                                                                    });
                                                                    AlertDialog dialog = builder.create();
                                                                    dialog.show();
                                                                }
                                                            });
                                                        } else {
                                                            hideProgressDialog();
                                                            Toast.makeText(PerfilOrganizacaoActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            } else {
                                                alertaSenhaCurta();
                                            }
                                        } else {
                                            alertaSenhasDiferentes();
                                        }
                                    } else {
                                        organizacaoRepository.atualizarDadosOrganizacao(ong, bmap, new OrganizacaoRepository.OnUpdateOrganizacao() {
                                            @Override
                                            public void onUpdateOrganizacaoSuccess(String sucesso) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilOrganizacaoActivity.this, R.style.AppTheme));
                                                builder.setMessage(R.string.organizacao_atualizada_sucesso);
                                                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int i) {
                                                        hideProgressDialog();
                                                        abrirMenuPerfilOrganizacao();
                                                    }
                                                });
                                                AlertDialog dialog = builder.create();
                                                dialog.show();
                                            }

                                            @Override
                                            public void onUpdateOrganizacaoError(String error) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilOrganizacaoActivity.this, R.style.AppTheme));
                                                builder.setMessage(R.string.erro_atualizacao_organizacao);
                                                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int i) {
                                                        hideProgressDialog();
                                                        abrirMenuPerfilOrganizacao();
                                                    }
                                                });
                                                AlertDialog dialog = builder.create();
                                                dialog.show();
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onGetOrganizacaoByIdError(String error) {
                                Log.d("onGetUserByIdError", error);
                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilOrganizacaoActivity.this, R.style.AppTheme));
                                builder.setMessage("Não foi possível atualizar os dados da organização. Por favor, tente novamente!");
                                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        hideProgressDialog();
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
                    }
                }
            }
        });
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                image_view_logo_perfil_organizacao.setImageBitmap(mImageBitmap);
                text_view_escolher_foto_perfil_organizacao.setVisibility(View.GONE);
                text_view_escolher_foto_perfil_organizacao.setEnabled(false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                Glide.with(this).load(uri).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(image_view_logo_perfil_organizacao);
                text_view_escolher_foto_perfil_organizacao.setEnabled(false);
                text_view_escolher_foto_perfil_organizacao.setVisibility(View.GONE);
            }
        }
    }

    private void alertaSenhasDiferentes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilOrganizacaoActivity.this, R.style.AppTheme));
        builder.setMessage(R.string.senhas_diferentes);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edit_text_senha_perfil_organizacao.setText("");
                edit_text_confirmar_senha_perfil_organizacao.setText("");
                hideProgressDialog();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void alertaCamposNaoPreenchidos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilOrganizacaoActivity.this, R.style.AppTheme));
        builder.setMessage(R.string.campos_nao_preenchidos);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hideProgressDialog();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void alertaSenhaCurta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilOrganizacaoActivity.this, R.style.AppTheme));
        builder.setMessage(R.string.senha_curta);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hideProgressDialog();
                edit_text_senha_perfil_organizacao.setText("");
                edit_text_confirmar_senha_perfil_organizacao.setText("");
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void abrirMenuPerfilOrganizacao() {
        Intent intent = new Intent(PerfilOrganizacaoActivity.this, PerfilOrganizacaoActivity.class);
        startActivity(intent);
        finish();
    }

    public void showProgressDialog(String title, String content) {
        progressDialog.setTitle(title);
        progressDialog.setMessage(content);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                abrirMenuPrincipal();
                break;
            case R.id.item_exit_menu_principal:
                sairDoApp();
                break;
        }
        return true;
    }

    private void abrirMenuPrincipal() {
        Intent intent = new Intent(PerfilOrganizacaoActivity.this, MenuPrincipalOrganizacaoActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void sairDoApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme));
        builder.setMessage(R.string.sair_app);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                abrirTelaLogin();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void abrirTelaLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
