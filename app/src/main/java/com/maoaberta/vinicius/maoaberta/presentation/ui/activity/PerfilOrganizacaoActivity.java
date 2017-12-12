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
import android.os.StrictMode;
import android.provider.MediaStore;
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
    @BindView(R.id.button_atualizar_dados_organizacao)
    Button button_atualizar_dados_organizacao;
    private ProgressDialog progressDialog;
    private CustomPhotoPickerDialog photoDialog;
    private String mCurrentPhotoPath;
    private Bitmap mImageBitmap;
    private Organizacao organizacaoEditada;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_organizacao);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_layout_menu_perfil_organizacao);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

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
                        organizacaoEditada = organizacao;
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
                            Glide.with(getApplicationContext()).load(organizacao.getPhotoUrl()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
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
                image_view_logo_perfil_organizacao.setDrawingCacheEnabled(true);
                final Bitmap bmap = image_view_logo_perfil_organizacao.getDrawingCache();
                showProgressDialog("Atualizando Dados", "Aguarde enquanto os dados do usuário são atualizados");
                organizacaoEditada.setNomeFantasia(edit_text_nome_fantasia_perfil_organizacao.getText().toString());
                organizacaoEditada.setRazaoSocial(edit_text_razao_social_perfil_organizacao.getText().toString());
                organizacaoEditada.setCnpj(edit_text_cnpj_perfil_organizacao.getText().toString());
                organizacaoEditada.setDescricao(edit_text_descricao_perfil_organizacao.getText().toString());
                organizacaoEditada.setEndereco(edit_text_endereco_perfil_organizacao.getText().toString());
                organizacaoEditada.setFacebookAccount(edit_text_facebook_perfil_organizacao.getText().toString());
                organizacaoEditada.setNomeResponsavel(edit_text_nome_responsavel_perfil_organizacao.getText().toString());
                organizacaoEditada.setTelefone(edit_text_telefone_perfil_organizacao.getText().toString());
                organizacaoEditada.setTwitterAccount(edit_text_twitter_perfil_organizacao.getText().toString());
                organizacaoEditada.setWebSite(edit_text_site_perfil_organizacao.getText().toString());

                if(edit_text_senha_perfil_organizacao.getText().toString().isEmpty()){
                    organizacaoRepository.atualizarDadosOrganizacao(organizacaoEditada, bmap, new OrganizacaoRepository.OnUpdateOrganizacao() {
                        @Override
                        public void onUpdateOrganizacaoSuccess(String sucesso) {
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(), "Usuário atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onUpdateOrganizacaoError(String error) {

                        }
                    });
                }else{
                    user.updatePassword(String.valueOf(edit_text_senha_perfil_organizacao.getText()));
                    organizacaoRepository.atualizarDadosOrganizacao(organizacaoEditada, bmap, new OrganizacaoRepository.OnUpdateOrganizacao() {
                        @Override
                        public void onUpdateOrganizacaoSuccess(String sucesso) {
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(), "Usuário atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onUpdateOrganizacaoError(String error) {

                        }
                    });
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                Glide.with(getApplicationContext()).load(uri).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(image_view_logo_perfil_organizacao);
                text_view_escolher_foto_perfil_organizacao.setEnabled(false);
                text_view_escolher_foto_perfil_organizacao.setVisibility(View.GONE);
            }
        }
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
        }
        return true;
    }

    private void abrirMenuPrincipal() {
        Intent intent = new Intent(getApplicationContext(), MenuPrincipalOrganizacaoActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
