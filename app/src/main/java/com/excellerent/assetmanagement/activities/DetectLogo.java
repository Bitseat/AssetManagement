package com.excellerent.assetmanagement.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.camerakit.CameraKitView;
import com.excellerent.assetmanagement.R;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetectLogo extends AppCompatActivity {
    @BindView(R.id.vCamera)
    CameraKitView vCamera;
    @BindView(R.id.ivPreview)
    ImageView ivPreview;
    @BindView(R.id.ivFinalPreview)
    ImageView ivFinalPreview;
    @BindView(R.id.tvClassification)
    TextView tvClassification;
    TextView serial;

    private GtsrbClassifier gtsrbClassifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.detect_logo);
        ButterKnife.bind(this);
        String computer_serial = intent.getStringExtra("serial_num");
        loadGtsrbClassifier();

    }

    private void loadGtsrbClassifier() {
        try {
            gtsrbClassifier = GtsrbClassifier.classifier(getAssets(), GtsrbModelConfig.MODEL_FILENAME);
        } catch (IOException e) {
            Toast.makeText(this, "GTSRB model couldn't be loaded. Check logs for details.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        vCamera.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vCamera.onResume();
    }

    @Override
    protected void onPause() {
        vCamera.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        vCamera.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        vCamera.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnClick(R.id.btnTakePhoto)
    public void onTakePhoto() {
        vCamera.captureImage((cameraKitView, picture) -> {
            onImageCaptured(picture);
        });
    }

    @OnClick(R.id.btnSave)
    public void onSave(View view) {
        Intent intent = new Intent(DetectLogo.this,SaveToDatabaseActivity.class);
        intent.putExtra("logo", tvClassification.getText().toString());
        //intent.putExtra("serial_number", computer_serial);
        startActivity(intent);
        //openNewActivity(intent);
    }

    private void onImageCaptured(byte[] picture) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        Bitmap squareBitmap = ThumbnailUtils.extractThumbnail(bitmap, getScreenWidth(), getScreenWidth());
        ivPreview.setImageBitmap(squareBitmap);

        Bitmap preprocessedImage = ImageUtils.prepareImageForClassification(squareBitmap);
        ivFinalPreview.setImageBitmap(preprocessedImage);

        List<Classification> recognitions = gtsrbClassifier.recognizeImage(preprocessedImage);
        tvClassification.setText(recognitions.toString());

        //serial.setText(computer_serial);


    }

    private int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}
