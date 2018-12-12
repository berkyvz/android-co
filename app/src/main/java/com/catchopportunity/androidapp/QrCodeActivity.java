package com.catchopportunity.androidapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.catchopportunity.androidapp.api.Api;
import com.catchopportunity.androidapp.client.UserClient;
import com.catchopportunity.androidapp.gui.CaughtActivity;
import com.catchopportunity.androidapp.gui.HomeActivity;
import com.catchopportunity.androidapp.gui.LoginActivity;
import com.catchopportunity.androidapp.gui.ProfileActivity;
import com.catchopportunity.androidapp.gui.SearchActivity;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import retrofit2.Retrofit;

public class QrCodeActivity extends AppCompatActivity {

    private MenuItem item_logout, item_profile, item_home, item_search, item_opp, item_qrReader;


    Retrofit retrofit;
    UserClient userClient;

    private LinearLayout cameraContainer;
    private SurfaceView cameraPreview;
    private TextView txtCamera;
    private Button btnReadAgain;

    BarcodeDetector barcodeetector;
    CameraSource cameraSource;

    final int RequestCameraPermissionID = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        retrofit = Api.getClient();
        userClient = retrofit.create(UserClient.class);

        cameraContainer = findViewById(R.id.cameraContainer);
        cameraPreview = findViewById(R.id.cameraPreview);

        txtCamera = findViewById(R.id.txtCamera);


        barcodeetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        barcodeetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> qrcodes = detections.getDetectedItems();

                if (qrcodes.size() != 0) {
                   
                    final String value = qrcodes.valueAt(0).displayValue.toString();
                    txtCamera.post(new Runnable() {
                        @Override
                        public void run() {
                            txtCamera.setText(value);
                            cameraSource.stop();
                        }
                    });
                }
            }
        });
        cameraSource = new CameraSource.Builder(this, barcodeetector).setRequestedPreviewSize(1200, 1200).setAutoFocusEnabled(true).build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(QrCodeActivity.this, new String[]{Manifest.permission.CAMERA}, RequestCameraPermissionID);
                        return;
                    }


                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();

            }
        });


        btnReadAgain = findViewById(R.id.btnReadAgain);
        btnReadAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });


    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, HomeActivity.class);
                    finish();
                    startActivity(intent);
                    return;
                } else {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                }
            }
            break;


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == item_home.getItemId()) {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
            finish();
        }
        if (item.getItemId() == item_logout.getItemId()) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        if (item.getItemId() == item_opp.getItemId()) {
            Intent i = new Intent(this, CaughtActivity.class);
            startActivity(i);
            finish();
            return true;

        }
        if (item.getItemId() == item_profile.getItemId()) {
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        if (item.getItemId() == item_search.getItemId()) {
            Intent i = new Intent(this, SearchActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        if (item.getItemId() == item_qrReader.getItemId()) {
            Intent i = new Intent(this, QrCodeActivity.class);
            startActivity(i);
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        item_logout = menu.findItem(R.id.menu_logout);
        item_profile = menu.findItem(R.id.menu_profile);
        item_home = menu.findItem(R.id.menu_home);
        item_search = menu.findItem(R.id.menu_search);
        item_opp = menu.findItem(R.id.menu_myOpportunities);
        item_qrReader = menu.findItem(R.id.menu_qrCodeReader);

        item_qrReader.setEnabled(false);

        return true;
    }


}
