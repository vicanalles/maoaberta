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
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Voluntario;
import com.maoaberta.vinicius.maoaberta.domain.repository.UsuarioRepository;
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
 * Created by vinicius on 29/08/17.
 */

public class PerfilVoluntarioActivity extends AppCompatActivity {

    private static final int PERMISSION_WRITE_EXTERNAL = 1594;
    private static final int SELECT_PICTURE = 1598;
    private static final int REQUEST_IMAGE_CAPTURE = 1595;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private UsuarioRepository usuarioRepository;
    private Voluntario vol;
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
    @BindView(R.id.edit_text_confirmar_senha_perfil_cliente)
    EditText edit_text_confirmar_senha_perfil_cliente;
    @BindView(R.id.botao_salvar_perfil_cliente)
    Button botao_salvar_perfil_cliente;
    @BindView(R.id.toolbar_layout_menu_perfil_cliente)
    Toolbar toolbar_layout_menu_perfil_cliente;
    private ProgressDialog progressDialog;
    private CustomPhotoPickerDialog photoDialog;

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

        if (String.valueOf(user.getProviders()).equals("[google.com]") || String.valueOf(user.getProviders()).equals("[facebook.com]")) {
            edit_text_senha_perfil_cliente.setVisibility(View.GONE);
            edit_text_confirmar_senha_perfil_cliente.setVisibility(View.GONE);
        } else {
            edit_text_senha_perfil_cliente.setVisibility(View.VISIBLE);
            edit_text_confirmar_senha_perfil_cliente.setVisibility(View.VISIBLE);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        showProgressDialog("Carregando informações", "Carregando dados do usuário");
        if (user != null) {
            String uid = user.getUid();
            usuarioRepository.getUserByUid(uid, new UsuarioRepository.OnGetUserById() {
                @Override
                public void onGetUserByIdSuccess(Voluntario voluntario) {
                    if (voluntario != null) {
                        if(voluntario.getPhotoUrl() != null){
                            Glide.with(PerfilVoluntarioActivity.this).load(voluntario.getPhotoUrl()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(image_view_logo_perfil_cliente);
                            text_view_escolher_foto.setEnabled(false);
                            text_view_escolher_foto.setVisibility(View.GONE);
                        }else{
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
                    Toast.makeText(PerfilVoluntarioActivity.this, R.string.usuario_inexistente, Toast.LENGTH_LONG).show();
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
                        }else{
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if(intent.resolveActivity(getPackageManager()) != null){
                                File photoFile = null;
                                try{
                                    photoFile = createImageFile();
                                }catch (IOException e){
                                    Log.i("TAG", e.getMessage());
                                }

                                if(photoFile != null){
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
                if (String.valueOf(edit_text_nome_perfil_cliente.getText()).equals("") || String.valueOf(edit_text_telefone_perfil_cliente.getText()).equals("") ||
                        String.valueOf(edit_text_email_perfil_cliente.getText()).equals("")) {
                    alertaCamposNaoPreenchidos();
                } else {
                    image_view_logo_perfil_cliente.setDrawingCacheEnabled(true);
                    final Bitmap bmap = image_view_logo_perfil_cliente.getDrawingCache();
                    user = mAuth.getCurrentUser();
                    if (user != null) {
                        usuarioRepository.getUserByUid(user.getUid(), new UsuarioRepository.OnGetUserById() {
                            @Override
                            public void onGetUserByIdSuccess(Voluntario voluntario) {
                                showProgressDialog("Atualizando Dados", "Aguarde enquanto os dados são atualizados");
                                if (voluntario != null) {
                                    vol = new Voluntario();
                                    vol.setNome(String.valueOf(edit_text_nome_perfil_cliente.getText()));
                                    vol.setEmail(voluntario.getEmail());
                                    vol.setTelefone(String.valueOf(edit_text_telefone_perfil_cliente.getText()));

                                    if (!String.valueOf(user.getProviders()).equals("[google.com]")) {
                                        if (!String.valueOf(edit_text_senha_perfil_cliente.getText()).equals("")) {
                                            if (String.valueOf(edit_text_senha_perfil_cliente.getText()).equals(String.valueOf(edit_text_confirmar_senha_perfil_cliente.getText()))) {
                                                if (edit_text_senha_perfil_cliente.getText().length() >= 6 && edit_text_confirmar_senha_perfil_cliente.getText().length() >= 6) {
                                                    user.updatePassword(String.valueOf(edit_text_senha_perfil_cliente.getText())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                usuarioRepository.atualizarDadosVoluntario(vol, bmap, new UsuarioRepository.OnUpdateUsuario() {
                                                                    @Override
                                                                    public void onUpdateUsuarioSuccess(String sucesso) {
                                                                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilVoluntarioActivity.this, R.style.AppTheme));
                                                                        builder.setMessage(R.string.atualizacao_usuario_sucesso);
                                                                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int i) {
                                                                                hideProgressDialog();
                                                                                abrirMenuPerfilVoluntario();
                                                                            }
                                                                        });
                                                                        AlertDialog dialog = builder.create();
                                                                        dialog.show();
                                                                    }

                                                                    @Override
                                                                    public void onUpdateUsuarioError(String error) {
                                                                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilVoluntarioActivity.this, R.style.AppTheme));
                                                                        builder.setMessage(R.string.atualizacao_usuario_sucesso);
                                                                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int i) {
                                                                                hideProgressDialog();
                                                                                abrirMenuPerfilVoluntario();
                                                                            }
                                                                        });
                                                                        AlertDialog dialog = builder.create();
                                                                        dialog.show();
                                                                    }
                                                                });
                                                            }else{
                                                                hideProgressDialog();
                                                                Toast.makeText(PerfilVoluntarioActivity.this, "Erro na atualizacao da senha ", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    alertaSenhaCurta();
                                                }
                                            }else{
                                                alertaSenhasDiferentes();
                                            }
                                        }
                                    }else {
                                        usuarioRepository.atualizarDadosVoluntario(vol, bmap, new UsuarioRepository.OnUpdateUsuario() {
                                            @Override
                                            public void onUpdateUsuarioSuccess(String sucesso) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilVoluntarioActivity.this, R.style.AppTheme));
                                                builder.setMessage(R.string.atualizacao_usuario_sucesso);
                                                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int i) {
                                                        hideProgressDialog();
                                                        abrirMenuPerfilVoluntario();
                                                    }
                                                });
                                                AlertDialog dialog = builder.create();
                                                dialog.show();
                                            }

                                            @Override
                                            public void onUpdateUsuarioError(String error) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilVoluntarioActivity.this, R.style.AppTheme));
                                                builder.setMessage(R.string.atualizacao_usuario_sucesso);
                                                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int i) {
                                                        hideProgressDialog();
                                                        abrirMenuPerfilVoluntario();
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
                            public void onGetUserByIdError(String error) {
                                Log.d("onGetUserByIdError", error);
                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilVoluntarioActivity.this, R.style.AppTheme));
                                builder.setMessage(R.string.erro_recuperacao_dados_usuario);
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

    private File createImageFile() throws IOException{
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
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            try{
                mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                image_view_logo_perfil_cliente.setImageBitmap(mImageBitmap);
                text_view_escolher_foto.setVisibility(View.GONE);
                text_view_escolher_foto.setEnabled(false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(requestCode == SELECT_PICTURE && resultCode == RESULT_OK){
            if(data != null){
                Uri uri = data.getData();
                Glide.with(this).load(uri).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(image_view_logo_perfil_cliente);
                text_view_escolher_foto.setVisibility(View.GONE);
                text_view_escolher_foto.setEnabled(false);
            }
        }
    }

    private void alertaSenhasDiferentes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilVoluntarioActivity.this, R.style.AppTheme));
        builder.setMessage(R.string.senhas_diferentes);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edit_text_senha_perfil_cliente.setText("");
                edit_text_confirmar_senha_perfil_cliente.setText("");
                hideProgressDialog();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void alertaSenhaCurta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilVoluntarioActivity.this, R.style.AppTheme));
        builder.setMessage(R.string.senha_curta);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edit_text_senha_perfil_cliente.setText("");
                edit_text_confirmar_senha_perfil_cliente.setText("");
                hideProgressDialog();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.item_exit_menu_principal:
                sairDoApp();
                break;
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

    private void sairDoApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme));
        builder.setMessage(R.string.sair_app);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoginManager.getInstance().logOut();
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

    private void alertaCamposNaoPreenchidos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilVoluntarioActivity.this, R.style.AppTheme));
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

    private void abrirMenuPerfilVoluntario() {
        Intent intent = new Intent(PerfilVoluntarioActivity.this, PerfilVoluntarioActivity.class);
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
}
