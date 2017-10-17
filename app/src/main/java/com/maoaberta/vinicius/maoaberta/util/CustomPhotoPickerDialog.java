package com.maoaberta.vinicius.maoaberta.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.maoaberta.vinicius.maoaberta.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vinicius on 17/10/17.
 */

public class CustomPhotoPickerDialog extends Dialog {

    private OnOptionPhotoSelected mOnOptionPhotoSelected;

    public CustomPhotoPickerDialog(@NonNull Context context, OnOptionPhotoSelected onOptionPhotoSelected) {
        super(context);
        this.mOnOptionPhotoSelected = onOptionPhotoSelected;
    }

    public CustomPhotoPickerDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected CustomPhotoPickerDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_photo_picker, null);
        setContentView(view);

        CustomPhotoPickerHolder holder = new CustomPhotoPickerHolder(view);
        holder.mRelativeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnOptionPhotoSelected.onCamera();
            }
        });
        holder.mRelativeGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnOptionPhotoSelected.onGallery();
            }
        });
    }

    public class CustomPhotoPickerHolder{
        @BindView(R.id.relative_gallery)
        RelativeLayout mRelativeGallery;
        @BindView(R.id.relative_camera)
        RelativeLayout mRelativeCamera;

        CustomPhotoPickerHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnOptionPhotoSelected{
        void onGallery();
        void onCamera();
    }
}
