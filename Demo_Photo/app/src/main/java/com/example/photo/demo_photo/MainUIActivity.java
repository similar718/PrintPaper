package com.example.photo.demo_photo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yuyh.library.imgsel.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import utils.FileUtils;

public class MainUIActivity extends AppCompatActivity {

    private Button mPayBtn;
    private Button mCameraBtn;
    //private File file;
    //private ImageView iv;
    //private String mFilePath;
    //private String mFileName;

   // private static final int MSG_TAKE_PHOTO = 1;
    //private static final int CAMERA_JAVA_REQUEST_CODE = 2;
    ImageLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPayBtn = (Button) findViewById(R.id.pay_btn);
        mCameraBtn = (Button) findViewById(R.id.camera_btn);
       // iv = (ImageView) findViewById(R.id.iv);
//        createfile();
        //mFilePath = FileUtils.getFileDir() + File.separator;

        mPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainUIActivity.this, getimageandservice.MainActivity.class);
                startActivity(intent);
            }
        });
        mCameraBtn .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission())
                    return;
                if (getfilesize().size() > 0) {
                    Intent intent = new Intent(MainUIActivity.this, SelectSizeActivity.class);
                    startActivity(intent);
                } else {
                    try {
                        Intent photo = getPackageManager().getLaunchIntentForPackage("us.pinguo.idcamera");
                        startActivity(photo);
                    } catch (Exception e) {
                        Toast.makeText(MainUIActivity.this, "请先安装最美证件照", Toast.LENGTH_SHORT).show();
                    }
                }
                //jump2Camera();//TODO
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public boolean checkPermission(){
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            return true;
        } else {
            EasyPermissions.requestPermissions(this, "需要权限才能访问，请授权", 1001, perms);
        }
        return false;
    }

//    public void jump2Camera() {
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            File path = new File(mFilePath);
//            if (!path.exists()) {
//                path.mkdirs();
//            }
//            mFileName = "C360_"+new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date())+".jpg";
//            File file = new File(path, mFileName);
//            if (file.exists()) {
//                file.delete();
//            }
//            FileUtils.startActionCapture(this,file,MSG_TAKE_PHOTO);
//        } else {
//            L.e("main sdcard not exists");
//        }
//    }

//    private void createfile(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_JAVA_REQUEST_CODE);
//        File file = new File(Environment.getExternalStorageDirectory()+"/printpaper");
//        if (!file.exists()){
//            file.mkdir();
//        }
//    }

//    /**
//     * 从sd卡获取图片资源
//     * @return
//     */
//    private List<String> getImagePathFromSD() {
//        // 图片列表
//        List<String> imagePathList = new ArrayList<String>();
//        // 得到sd卡内image文件夹的路径   File.separator(/)
//        String filePath = Environment.getExternalStorageDirectory().toString() + File.separator
//                + "printpaper";
//        // 得到该路径文件夹下所有的文件
//        File fileAll = new File(filePath);
//        File[] files = fileAll.listFiles();
//        // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
//        for (int i = 0; i < files.length; i++) {
//            File file = files[i];
//            //if (checkIsImageFile(file.getPath()) && file.getPath().contains("C360_"))
//            imagePathList.add(file.getPath());
//        }
//        // 返回得到的图片列表
//        return imagePathList;
//    }


    public List<String> getfilesize (){
        loader = new ImageLoader() {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    };
        List<String> list=new ArrayList<>();
        File  scanner5Directory = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera");
        if (scanner5Directory.isDirectory()) {
            for (File file : scanner5Directory.listFiles()) {
                String name = file.getName();
                String path = file.getAbsolutePath();
                if ((name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"))&&name.startsWith("C360_")) {//C360_
                    list.add(path);
                }
            }
        }
        return list;
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode==1){
//            if (getfilesize().size()>0) {//TODO
//                Intent intent = new Intent(MainUIActivity.this, SelectSizeActivity.class);
//                startActivity(intent);
//            } else {
//                Toast.makeText(MainUIActivity.this,"没有保存图片，请点击拍照！",Toast.LENGTH_SHORT).show();
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}
