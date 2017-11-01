package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Voluntario;
import com.maoaberta.vinicius.maoaberta.domain.repository.TipoRepository;
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
 * Created by vinicius on 16/10/17.
 */

public class CompletarRegistroVoluntarioActivity extends AppCompatActivity {

    private static final int PERMISSION_WRITE_EXTERNAL = 1594;
    private static final int SELECT_PICTURE = 1596;
    private static final int REQUEST_IMAGE_CAPTURE = 1595;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private UsuarioRepository usuarioRepository;
    private Voluntario voluntario;
    private ProgressDialog progressDialog;
    private CustomPhotoPickerDialog photoDialog;

    @BindView(R.id.relative_layout_image_logo_completar_registro_voluntario)
    RelativeLayout relative_layout_image_logo_completar_registro_voluntario;
    @BindView(R.id.text_view_escolher_foto_completar_registro_voluntario)
    TextView text_view_escolher_foto_completar_registro_voluntario;
    @BindView(R.id.image_view_logo_completar_registro_voluntario)
    CircleImageView image_view_logo_completar_registro_voluntario;
    @BindView(R.id.edit_text_nome_completar_registro_voluntario)
    EditText edit_text_nome_completar_registro_voluntario;
    @BindView(R.id.edit_text_email_completar_registro_voluntario)
    EditText edit_text_email_completar_registro_voluntario;
    @BindView(R.id.edit_text_telefone_completar_registro_voluntario)
    EditText edit_text_telefone_completar_registro_voluntario;
    @BindView(R.id.botao_salvar_completar_registro_voluntario)
    Button botao_salvar_completar_registro_voluntario;
    @BindView(R.id.toolbar_layout_menu_completar_registro_voluntario)
    Toolbar toolbar_layout_menu_completar_registro_voluntario;
    private String mCurrentPhotoPath;
    private Bitmap mImageBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completar_registro_voluntario);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_layout_menu_completar_registro_voluntario);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        usuarioRepository = new UsuarioRepository();
        progressDialog = new ProgressDialog(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.pre_cadastro);
        }

        if (user != null) {
            edit_text_nome_completar_registro_voluntario.setText(user.getDisplayName());
            edit_text_email_completar_registro_voluntario.setText(user.getEmail());
            edit_text_telefone_completar_registro_voluntario.setText(user.getPhoneNumber());
        }

        image_view_logo_completar_registro_voluntario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoDialog = new CustomPhotoPickerDialog(CompletarRegistroVoluntarioActivity.this, new CustomPhotoPickerDialog.OnOptionPhotoSelected() {
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
                        if (ContextCompat.checkSelfPermission(CompletarRegistroVoluntarioActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(CompletarRegistroVoluntarioActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(CompletarRegistroVoluntarioActivity.this,
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL);
                            photoDialog.dismiss();
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if(intent.resolveActivity(getPackageManager()) != null){
                                File photoFile = null;
                                try{
                                    photoFile = createImageFile();
                                }catch(IOException e){
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

        botao_salvar_completar_registro_voluntario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_view_logo_completar_registro_voluntario.setDrawingCacheEnabled(true);
                Bitmap bmap = image_view_logo_completar_registro_voluntario.getDrawingCache();
                showProgressDialog("Cadastrando Voluntário", "Aguarde enquanto finalizamos o cadastro");
                voluntario = new Voluntario();
                voluntario.setNome(edit_text_nome_completar_registro_voluntario.getText().toString());
                voluntario.setEmail(edit_text_email_completar_registro_voluntario.getText().toString());
                voluntario.setTelefone(edit_text_telefone_completar_registro_voluntario.getText().toString());
                usuarioRepository.salvarDadosVoluntario(voluntario, bmap, new UsuarioRepository.OnSaveVoluntario() {
                    @Override
                    public void onSaveVoluntarioSuccess(Voluntario voluntario) {
                        TipoRepository tipoRepository = new TipoRepository();
                        tipoRepository.cadastrarTipo(user.getUid(), "tipo", "voluntario", new TipoRepository.OnCadastrarTipo() {
                            @Override
                            public void onCadastrarTipoSuccess(String sucesso) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(CompletarRegistroVoluntarioActivity.this, R.style.AppTheme));
                                builder.setMessage(sucesso);
                                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        abrirMenuPrincipalCliente();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                            @Override
                            public void onCadastrarTipoError(String error) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(CompletarRegistroVoluntarioActivity.this, R.style.AppTheme));
                                builder.setMessage(error);
                                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        hideProgressDialog();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
                    }

                    @Override
                    public void onSaveVoluntarioError(String error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(CompletarRegistroVoluntarioActivity.this, R.style.AppTheme));
                        builder.setMessage(error);
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try{
                mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                image_view_logo_completar_registro_voluntario.setImageBitmap(mImageBitmap);
                text_view_escolher_foto_completar_registro_voluntario.setVisibility(View.GONE);
                text_view_escolher_foto_completar_registro_voluntario.setEnabled(false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(requestCode == SELECT_PICTURE && resultCode == RESULT_OK){
            if(data != null){
                Uri uri = data.getData();
                Glide.with(this).load(uri).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                        .into(image_view_logo_completar_registro_voluntario);
                text_view_escolher_foto_completar_registro_voluntario.setVisibility(View.GONE);
                text_view_escolher_foto_completar_registro_voluntario.setEnabled(false);
            }
        }
    }

    private void abrirMenuPrincipalCliente() {
        hideProgressDialog();
        Intent intent = new Intent(CompletarRegistroVoluntarioActivity.this, MenuPrincipalClienteActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        sairDoApp();
    }

    private void sairDoApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme));
        builder.setMessage(R.string.sair_app);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        abrirTelaLogin();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(CompletarRegistroVoluntarioActivity.this, R.style.AppTheme));
                        builder.setMessage("Ocorreu um erro. Por favor, tente novamente!!");
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
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
        Intent intent = new Intent(CompletarRegistroVoluntarioActivity.this, LoginActivity.class);
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
