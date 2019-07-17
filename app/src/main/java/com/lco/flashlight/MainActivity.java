package com.lco.flashlight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button bt_enable;
    ImageView img;
    private static final int CAMERA_REQUEST = 50;
    private boolean flashlightstatus = false;
    private boolean hascameraflash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_enable = findViewById(R.id.button);
        img = findViewById(R.id.img_light);
        hascameraflash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        boolean isEnabled = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
        bt_enable.setEnabled(!isEnabled);
        bt_enable.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]
                        {Manifest.permission.CAMERA}, CAMERA_REQUEST);


            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hascameraflash) {
                    if (flashlightstatus)
                        flashlightOff();
                    else
                        flashlightOn();
                } else {
                    Toast.makeText(MainActivity.this, "No flash available on your device",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void flashlightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
            flashlightstatus = true;
            img.setImageResource(R.drawable.img2);
        } catch (CameraAccessException e) {
        }
    }

    private void flashlightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
            flashlightstatus = false;
            img.setImageResource(R.drawable.image);
        } catch (CameraAccessException e) {
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    bt_enable.setEnabled(false);
                    bt_enable.setText("Camera Enabled");
                    img.setEnabled(true);
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied for the Camera", Toast.LENGTH_SHORT).show();
                    img.setEnabled(false);
                }
                break;
        }
    }
}