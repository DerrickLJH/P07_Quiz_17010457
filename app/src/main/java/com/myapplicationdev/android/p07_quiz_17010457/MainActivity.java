package com.myapplicationdev.android.p07_quiz_17010457;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnRetrieve;
    TextView tvData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRetrieve = findViewById(R.id.btnRetrieve);
        tvData = findViewById(R.id.tvData);
        int permissionCheck = PermissionChecker.checkSelfPermission(
                MainActivity.this, Manifest.permission.READ_SMS);

        if (permissionCheck != PermissionChecker.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS},0);

            return;
        }

        btnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://sms");

                String[] reqCols = new String[]{"address","body"};
                String filter = "address LIKE ? AND body LIKE?";
                String[] args = {"%55%","%RP"};
                ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(uri, reqCols, filter, args, null);
                String smsBody = "";
                if (cursor.moveToFirst()){
                    do{
                        String address = cursor.getString(0);
                        String body = cursor.getString(1);
                        smsBody += address + " " + body;
                    } while (cursor.moveToNext());
                }
                tvData.setText(smsBody);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    btnRetrieve.performClick();
                } else {
                    Toast.makeText(MainActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
