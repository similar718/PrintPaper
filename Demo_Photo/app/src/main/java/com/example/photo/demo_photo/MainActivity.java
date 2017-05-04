package com.example.photo.demo_photo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

/**
 * Created by pc on 2017/4/25.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button pay,takephoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initview();
        initdata();
    }


    private void initdata() {
    }

    private void initview() {
        pay= (Button) findViewById(R.id.pay);
        takephoto= (Button) findViewById(R.id.takephoto);
        pay.setOnClickListener(this);
        takephoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pay:
                Intent intent=new Intent(this,ChosePhotoStyle.class);
                startActivity(intent);
                break;
            case R.id.takephoto:
               // doStartApplicationWithPackageName("us.pinguo.idcamera");
                try {
                    Intent photo = getPackageManager().getLaunchIntentForPackage("us.pinguo.idcamera");
                    startActivity(photo);
                }catch(Exception e){
                    Toast.makeText(this,"请先安装最美证件照",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void doStartApplicationWithPackageName(String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            startActivity(intent);
        }else{
            Toast.makeText(this,"请先安装最美证件照",Toast.LENGTH_SHORT).show();
        }
    }
}
