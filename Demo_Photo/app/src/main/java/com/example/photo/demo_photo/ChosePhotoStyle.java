package com.example.photo.demo_photo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by pc on 2017/4/25.
 */

public class ChosePhotoStyle extends AppCompatActivity implements View.OnClickListener,EasyPermissions.PermissionCallbacks{
    LinearLayout one,two,three,four,five;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chosephotostyle);
        initview();
        initdata();
    }

    private void initdata() {

    }

    private void initview() {
         intent=new Intent(this,EditPhotoActivity.class);
        one= (LinearLayout) findViewById(R.id.linear_one);
        two= (LinearLayout) findViewById(R.id.linear_two);
        three= (LinearLayout) findViewById(R.id.linear_three);
        four= (LinearLayout) findViewById(R.id.linear_four);
        five= (LinearLayout) findViewById(R.id.linear_five);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_one:
                checkPermission();
                break;
            case R.id.linear_two:
                checkPermission();
                break;
            case R.id.linear_three:
                checkPermission();
                break;
            case R.id.linear_four:
                checkPermission();
                break;
            case R.id.linear_five:
                checkPermission();
                break;
        }
    }
    public void checkPermission(){
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            startActivity(intent);
        } else {
            EasyPermissions.requestPermissions(this, "需要权限才能访问，请授权",
                    1001, perms);
        }
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        startActivity(intent);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this,"没有权限，无法继续",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
