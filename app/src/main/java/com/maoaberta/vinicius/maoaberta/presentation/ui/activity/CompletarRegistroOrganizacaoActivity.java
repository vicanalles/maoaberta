package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Organizacao;
import com.maoaberta.vinicius.maoaberta.domain.repository.OrganizacaoRepository;
import com.maoaberta.vinicius.maoaberta.domain.repository.TipoRepository;
import com.maoaberta.vinicius.maoaberta.util.CustomPhotoPickerDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vinicius on 17/10/17.
 */

public class CompletarRegistroOrganizacaoActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1596;
    private static final int REQUEST_IMAGE_CAPTURE = 1595;
    private static final int PERMISSION_WRITE_EXTERNAL = 1594;
    private static final int REQUEST_POSITION = 4569;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private OrganizacaoRepository organizacaoRepository;
    private ProgressDialog progressDialog;
    private Organizacao organizacao;
    private CustomPhotoPickerDialog photoDialog;

    @BindView(R.id.toolbar_layout_menu_completar_registro_organizacao)
    Toolbar toolbar_layout_menu_completar_registro_organizacao;
    @BindView(R.id.relative_layout_image_logo_completar_registro_organizacao)
    RelativeLayout relative_layout_image_logo_completar_registro_organizacao;
    @BindView(R.id.image_view_logo_completar_registro_organizacao)
    CircleImageView image_view_logo_completar_registro_organizacao;
    @BindView(R.id.text_view_escolher_foto_completar_registro_organizacao)
    TextView text_view_escolher_foto_completar_registro_organizacao;
    @BindView(R.id.edit_text_razao_social_completar_registro_organizacao)
    EditText edit_text_razao_social_completar_registro_organizacao;
    @BindView(R.id.edit_text_nome_fantasia_completar_registro_organizacao)
    EditText edit_text_nome_fantasia_completar_registro_organizacao;
    @BindView(R.id.edit_text_cnpj_completar_registro_organizacao)
    EditText edit_text_cnpj_completar_registro_organizacao;
    @BindView(R.id.edit_text_nome_responsavel_completar_registro_organizacao)
    EditText edit_text_nome_responsavel_completar_registro_organizacao;
    @BindView(R.id.edit_text_email_completar_registro_organizacao)
    EditText edit_text_email_completar_registro_organizacao;
    @BindView(R.id.edit_text_enderedo_completar_registro_organizacao)
    EditText edit_text_enderedo_completar_registro_organizacao;
    @BindView(R.id.image_view_add_position)
    ImageView image_view_add_position;
    @BindView(R.id.edit_text_telefone_completar_registro_organizacao)
    EditText edit_text_telefone_completar_registro_organizacao;
    @BindView(R.id.edit_text_descricao_completar_registro_organizacao)
    EditText edit_text_descricao_completar_registro_organizacao;
    @BindView(R.id.button_cadastrar_completar_registro_organizacao)
    Button button_cadastrar_completar_registro_organizacao;
    private String mCurrentPhotoPath;
    private Bitmap mImageBitmap;
    private double latitude = 0;
    private double longitude = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completar_registro_organizacao);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_layout_menu_completar_registro_organizacao);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        organizacaoRepository = new OrganizacaoRepository();
        progressDialog = new ProgressDialog(this);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.pre_cadastro);
        }

        if(user != null){
            edit_text_email_completar_registro_organizacao.setText(user.getEmail());
        }

        image_view_logo_completar_registro_organizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoDialog = new CustomPhotoPickerDialog(CompletarRegistroOrganizacaoActivity.this, new CustomPhotoPickerDialog.OnOptionPhotoSelected() {
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
                        if (ContextCompat.checkSelfPermission(CompletarRegistroOrganizacaoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(CompletarRegistroOrganizacaoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(CompletarRegistroOrganizacaoActivity.this,
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

        image_view_add_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMapsActivity();
            }
        });

        button_cadastrar_completar_registro_organizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_view_logo_completar_registro_organizacao.setDrawingCacheEnabled(true);
                Bitmap bmap = image_view_logo_completar_registro_organizacao.getDrawingCache();
                if(!String.valueOf(edit_text_razao_social_completar_registro_organizacao.getText()).equals("") &&
                        !String.valueOf(edit_text_nome_fantasia_completar_registro_organizacao.getText()).equals("") &&
                        !String.valueOf(edit_text_cnpj_completar_registro_organizacao.getText()).equals("") &&
                        !String.valueOf(edit_text_nome_responsavel_completar_registro_organizacao.getText()).equals("") &&
                        !String.valueOf(edit_text_email_completar_registro_organizacao.getText()).equals("") &&
                        !String.valueOf(edit_text_telefone_completar_registro_organizacao.getText()).equals("") &&
                        !String.valueOf(edit_text_descricao_completar_registro_organizacao.getText()).equals("")){
                    showProgressDialog("Cadastrando Organização", "Aguarde enquanto finalizamos o cadastro");
                    organizacao = new Organizacao();
                    organizacao.setRazaoSocial(edit_text_razao_social_completar_registro_organizacao.getText().toString());
                    organizacao.setNomeFantasia(edit_text_nome_fantasia_completar_registro_organizacao.getText().toString());
                    organizacao.setCnpj(edit_text_cnpj_completar_registro_organizacao.getText().toString());
                    organizacao.setNomeResponsavel(edit_text_nome_responsavel_completar_registro_organizacao.getText().toString());
                    organizacao.setEmail(edit_text_email_completar_registro_organizacao.getText().toString());
                    organizacao.setTelefone(edit_text_telefone_completar_registro_organizacao.getText().toString());
                    organizacao.setDescricao(edit_text_descricao_completar_registro_organizacao.getText().toString());
                    if(latitude != 0 && longitude != 0){
                        organizacao.setLatitude(latitude);
                        organizacao.setLongitude(longitude);
                        organizacao.setHasPosition(true);
                    }else{
                        organizacao.setLatitude(latitude);
                        organizacao.setLongitude(longitude);
                        organizacao.setHasPosition(false);
                    }
                    organizacaoRepository.salvarDadosOrganizacao(organizacao, bmap, new OrganizacaoRepository.OnSaveOrganizacao() {
                        @Override
                        public void onSaveOrganizacaoSuccess(Organizacao organizacao) {
                            TipoRepository tipoRepository = new TipoRepository();
                            tipoRepository.cadastrarTipo(user.getUid(), "tipo", "organizacao", new TipoRepository.OnCadastrarTipo() {
                                @Override
                                public void onCadastrarTipoSuccess(String sucesso) {
                                    Toast.makeText(CompletarRegistroOrganizacaoActivity.this, sucesso, Toast.LENGTH_SHORT).show();
                                    abrirMenuPrincipalOrganizacao();
                                }

                                @Override
                                public void onCadastrarTipoError(String error) {
                                    hideProgressDialog();
                                    Toast.makeText(CompletarRegistroOrganizacaoActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onSaveOrganizacaoError(String error) {
                            Toast.makeText(CompletarRegistroOrganizacaoActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(CompletarRegistroOrganizacaoActivity.this, "Preencha todos os campos para realizar o cadastro!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void abrirMapsActivity() {
        Intent intent = new Intent(CompletarRegistroOrganizacaoActivity.this, MapsActivity.class);
        startActivityForResult(intent, REQUEST_POSITION);
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
                image_view_logo_completar_registro_organizacao.setImageBitmap(mImageBitmap);
                text_view_escolher_foto_completar_registro_organizacao.setVisibility(View.GONE);
                text_view_escolher_foto_completar_registro_organizacao.setEnabled(false);
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else if(requestCode == SELECT_PICTURE && resultCode == RESULT_OK){
            if(data != null){
                Uri uri = data.getData();
                Glide.with(this).load(uri).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                        .into(image_view_logo_completar_registro_organizacao);
                text_view_escolher_foto_completar_registro_organizacao.setVisibility(View.GONE);
                text_view_escolher_foto_completar_registro_organizacao.setEnabled(false);
            }
        }else if(requestCode == REQUEST_POSITION){
            if(resultCode == RESULT_OK){
                if(data != null){
                    Bundle extras = data.getExtras();
                    latitude = (double) extras.get("latitude");
                    longitude = (double) extras.get("longitude");
                    Geocoder geocoder = new Geocoder(CompletarRegistroOrganizacaoActivity.this, Locale.getDefault());
                    try{
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if(addresses != null && addresses.size() != 0){
                            Address returnedAddress = addresses.get(0);
                            String returnString = (returnedAddress.getThoroughfare() != null ?
                                    String.format("%s, ", returnedAddress.getThoroughfare()) : "") +
                                    (returnedAddress.getSubThoroughfare() != null ?
                                            String.format("%s, ", returnedAddress.getSubThoroughfare()) : "") +
                                    (returnedAddress.getSubAdminArea() != null ?
                                            String.format("%s - ", returnedAddress.getSubAdminArea()) : "") +
                                    (returnedAddress.getAdminArea() != null ? returnedAddress.getAdminArea() : "");
                            returnString = returnString.replace("Unnamed Road,", "");
                            edit_text_enderedo_completar_registro_organizacao.setText(returnString);
                            if (returnString.equals(""))
                                edit_text_enderedo_completar_registro_organizacao.setText(R.string.endereco_desconhecido);
                        }else{
                            edit_text_enderedo_completar_registro_organizacao.setText(R.string.endereco_desconhecido);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        edit_text_enderedo_completar_registro_organizacao.setText(R.string.endereco_desconhecido);
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        sairDoApp();
    }

    private void sairDoApp() {
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                abrirTelaLogin();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CompletarRegistroOrganizacaoActivity.this, "Ocorreu um erro. Por favor, tente novamente!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirTelaLogin() {
        Intent intent = new Intent(CompletarRegistroOrganizacaoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void abrirMenuPrincipalOrganizacao() {
        hideProgressDialog();
        Intent intent = new Intent(CompletarRegistroOrganizacaoActivity.this, MenuPrincipalOrganizacaoActivity.class);
        startActivity(intent);
        finish();
    }

    public void showProgressDialog(String title, String content){
        progressDialog.setTitle(title);
        progressDialog.setMessage(content);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideProgressDialog(){
        progressDialog.dismiss();
    }
}
