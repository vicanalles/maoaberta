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
import com.facebook.login.LoginManager;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Voluntario;
import com.maoaberta.vinicius.maoaberta.domain.repository.UsuarioRepository;
import com.maoaberta.vinicius.maoaberta.util.CustomPhotoPickerDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vinicius on 29/08/17.
 */

public class PerfilVoluntarioActivity extends AppCompatActivity {

    private static final int PERMISSION_WRITE_EXTERNAL = 1594;
    private static final int SELECT_PICTURE = 1598;
    private static final int REQUEST_IMAGE_CAPTURE = 1595;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private UsuarioRepository usuarioRepository;
    private String mCurrentPhotoPath;
    private Bitmap mImageBitmap;

    @BindView(R.id.relative_layout_image_logo_perfil_cliente)
    RelativeLayout relative_layout_image_logo_perfil_cliente;
    @BindView(R.id.text_view_escolher_foto_perfil_cliente)
    TextView text_view_escolher_foto;
    @BindView(R.id.image_view_logo_perfil_cliente)
    CircleImageView image_view_logo_perfil_cliente;
    @BindView(R.id.edit_text_nome_perfil_cliente)
    EditText edit_text_nome_perfil_cliente;
    @BindView(R.id.edit_text_email_perfil_cliente)
    EditText edit_text_email_perfil_cliente;
    @BindView(R.id.edit_text_telefone_perfil_cliente)
    EditText edit_text_telefone_perfil_cliente;
    @BindView(R.id.edit_text_senha_perfil_cliente)
    EditText edit_text_senha_perfil_cliente;
    @BindView(R.id.botao_salvar_perfil_cliente)
    Button botao_salvar_perfil_cliente;
    @BindView(R.id.toolbar_layout_menu_perfil_cliente)
    Toolbar toolbar_layout_menu_perfil_cliente;
    private ProgressDialog progressDialog;
    private CustomPhotoPickerDialog photoDialog;
    private Voluntario voluntarioEditado;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cliente);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_layout_menu_perfil_cliente);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        usuarioRepository = new UsuarioRepository();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        if (String.valueOf(user.getProviders()).equals("[google.com]") || String.valueOf(user.getProviders()).equals("[facebook.com]")) {
            edit_text_senha_perfil_cliente.setVisibility(View.GONE);
        } else {
            edit_text_senha_perfil_cliente.setVisibility(View.VISIBLE);
        }

        showProgressDialog("Carregando informações", "Carregando dados do usuário");
        if (user != null) {
            String uid = user.getUid();
            usuarioRepository.getUserByUid(uid, new UsuarioRepository.OnGetUserById() {
                @Override
                public void onGetUserByIdSuccess(Voluntario voluntario) {
                    if (voluntario != null) {
                        voluntarioEditado = voluntario;
                        if (voluntario.getPhotoUrl() != null) {
                            Glide.with(getApplicationContext()).load(voluntario.getPhotoUrl()).diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true).into(image_view_logo_perfil_cliente);
                            text_view_escolher_foto.setEnabled(false);
                            text_view_escolher_foto.setVisibility(View.GONE);
                        } else {
                            text_view_escolher_foto.setEnabled(true);
                            text_view_escolher_foto.setVisibility(View.VISIBLE);
                        }
                        edit_text_nome_perfil_cliente.setText(voluntario.getNome());
                        edit_text_email_perfil_cliente.setText(voluntario.getEmail());
                        edit_text_telefone_perfil_cliente.setText(voluntario.getTelefone());
                    } else {
                        edit_text_nome_perfil_cliente.setText(user.getDisplayName());
                        edit_text_email_perfil_cliente.setText(user.getEmail());
                        botao_salvar_perfil_cliente.setText(R.string.salvar);
                    }
                    hideProgressDialog();
                }

                @Override
                public void onGetUserByIdError(String error) {
                    hideProgressDialog();
                    Log.d("onGetUserByIdError", error);
                }
            });
        }

        image_view_logo_perfil_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoDialog = new CustomPhotoPickerDialog(PerfilVoluntarioActivity.this, new CustomPhotoPickerDialog.OnOptionPhotoSelected() {
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
                        if (ContextCompat.checkSelfPermission(PerfilVoluntarioActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(PerfilVoluntarioActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            photoDialog.dismiss();
                            ActivityCompat.requestPermissions(PerfilVoluntarioActivity.this,
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

        botao_salvar_perfil_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_view_logo_perfil_cliente.setDrawingCacheEnabled(true);
                final Bitmap bmap = image_view_logo_perfil_cliente.getDrawingCache();
                showProgressDialog("Atualizando Dados", "Aguarde enquanto os dados do usuário são atualizados");
                if(edit_text_senha_perfil_cliente.getText().toString().isEmpty()){
                    voluntarioEditado.setNome(edit_text_nome_perfil_cliente.getText().toString());
                    voluntarioEditado.setTelefone(edit_text_telefone_perfil_cliente.getText().toString());
                    usuarioRepository.atualizarDadosVoluntario(voluntarioEditado, bmap, new UsuarioRepository.OnUpdateUsuario() {
                        @Override
                        public void onUpdateUsuarioSuccess(String sucesso) {
                            hideProgressDialog();
                            Toast.makeText(PerfilVoluntarioActivity.this, "Usuário atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onUpdateUsuarioError(String error) {

                        }
                    });
                }else{
                    voluntarioEditado.setNome(edit_text_nome_perfil_cliente.getText().toString());
                    voluntarioEditado.setTelefone(edit_text_telefone_perfil_cliente.getText().toString());
                    user.updatePassword(String.valueOf(edit_text_senha_perfil_cliente.getText()));
                    usuarioRepository.atualizarDadosVoluntario(voluntarioEditado, bmap, new UsuarioRepository.OnUpdateUsuario() {
                        @Override
                        public void onUpdateUsuarioSuccess(String sucesso) {
                            hideProgressDialog();
                            Toast.makeText(PerfilVoluntarioActivity.this, "Usuário atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onUpdateUsuarioError(String error) {

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
                image_view_logo_perfil_cliente.setImageBitmap(mImageBitmap);
                text_view_escolher_foto.setVisibility(View.GONE);
                text_view_escolher_foto.setEnabled(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                try{
                    Glide.with(getApplicationContext()).load(uri).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(image_view_logo_perfil_cliente);
                }catch(Exception e){
                    e.printStackTrace();
                }
                text_view_escolher_foto.setVisibility(View.GONE);
                text_view_escolher_foto.setEnabled(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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
        Intent intent = new Intent(PerfilVoluntarioActivity.this, MenuPrincipalVoluntarioActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        abrirMenuPrincipal();
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
}
