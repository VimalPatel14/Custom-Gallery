package com.vimal.customgallery;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.nileshp.multiphotopicker.photopicker.activity.PickImageActivity;
import com.tedpark.tedpermission.rx2.TedRx2Permission;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> pathList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.customgallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TedRx2Permission.with(MainActivity.this)
                        .setRationaleTitle("Can we read your storage?")
                        .setRationaleMessage("We need your permission to access your storage and pick image")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .request()
                        .subscribe(permissionResult -> {
                                    if (permissionResult.isGranted()) {
                                        Intent mIntent = new Intent(MainActivity.this, PickImageActivity.class);
                                        mIntent.putExtra(PickImageActivity.KEY_LIMIT_MAX_IMAGE, 60);
                                        mIntent.putExtra(PickImageActivity.KEY_LIMIT_MIN_IMAGE, 3);
                                        startActivityForResult(mIntent, PickImageActivity.PICKER_REQUEST_CODE);

                                    } else {
                                        Toast.makeText(getBaseContext(),
                                                "Permission Denied\n" + permissionResult.getDeniedPermissions().toString(), Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }, throwable -> {
                                }
                        );
            }
        });

  
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (resultCode == -1 && requestCode == PickImageActivity.PICKER_REQUEST_CODE) {
            this.pathList = intent.getExtras().getStringArrayList(PickImageActivity.KEY_DATA_RESULT);
            if (this.pathList != null && !this.pathList.isEmpty()) {
                StringBuilder sb=new StringBuilder("");
                for(int i=0;i<pathList.size();i++) {
                    sb.append("Photo"+(i+1)+":"+pathList.get(i));
                    sb.append("\n");
                }
               Toast.makeText(MainActivity.this,pathList.size()+" Image Selected",Toast.LENGTH_SHORT).show();
            }
        }
    }
}