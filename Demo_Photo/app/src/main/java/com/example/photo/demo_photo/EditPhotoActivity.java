package com.example.photo.demo_photo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.inphase.okhttputils.L;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapter.PhotoAdapter;
import Bean.UpLoadImgBean;
import Constant.Contants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.EasyPermissions;
import utils.SocketThread;

public class EditPhotoActivity extends AppCompatActivity implements View.OnClickListener,EasyPermissions.PermissionCallbacks{
    EditText img_url;
    Button chose_upload;
    ImageLoader loader;
    ImgSelConfig config;
    String imgurl;
    Handler mhandler;
    SocketThread socketThread;
    GridView grideview;
    List<String>list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editphoto);
        initView();
        initData();

        mhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                try {
                    if(msg.what== Contants.SocketReceive){
                    if (msg.obj != null) {
                        String s = msg.obj.toString();
                        if (s.trim().length() > 0) {
                            Log.i("socket", "mhandler接收到"+s);

                        } else {
                            Log.i("socket", "没有数据返回不更新");
                        }
                    }}
                    else if(msg.what== Contants.HttpUpload){
                        Toast.makeText(EditPhotoActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        };
        socketThread = new SocketThread(mhandler,this);
        socketThread.start();
    }

    private void initData() {
        // 自定义图片加载器
        loader = new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        };
        list=new ArrayList<>();
        File  scanner5Directory = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera");
        if (scanner5Directory.isDirectory()) {
            for (File file : scanner5Directory.listFiles()) {
                String name = file.getName();
                String path = file.getAbsolutePath();
                if ((name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"))&&name.startsWith("C360_")) {
                    list.add(path);
                }
            }
        }

        PhotoAdapter adapter=new PhotoAdapter(this,list);
        grideview.setAdapter(adapter);
    }

    private void initView() {
        img_url= (EditText) findViewById(R.id.img_url);
        chose_upload= (Button) findViewById(R.id.chose_upload);
        chose_upload.setOnClickListener(this);
        grideview= (GridView) findViewById(R.id.grideview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chose_upload:
                checkPermission();
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 选择结果回调
        if (requestCode ==1005 && resultCode == RESULT_OK && data != null) {
            imgurl=data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT).get(0);
            Toast.makeText(this,"当前选择图片的路径："+imgurl,Toast.LENGTH_LONG).show();
            uploadImg();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        chosephoto();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this,"没有权限，如要继续请授权",Toast.LENGTH_LONG);
    }
    public void checkPermission(){
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            chosephoto();
        } else {
            EasyPermissions.requestPermissions(this, "需要权限才能访问，请授权",
                    1001, perms);
        }
    }
    private void chosephoto(){
        // 配置选项
        config = new ImgSelConfig.Builder(this,loader)
                // 是否多选
                .multiSelect(true)
                // 确定按钮背景色
                .btnBgColor(Color.TRANSPARENT)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 确定按钮文字颜色
                .btnTextColor(Color.WHITE)
                // 返回图标ResId
                // .backResId(R.drawable.returnkey)
                .title("图片")
                .titleColor(Color.WHITE)
                .titleBgColor(Color.parseColor("#3F51B5"))
                .cropSize(1, 1, 200, 200)
                .needCrop(true)
                // 第一个是否显示相机
                .needCamera(true)
                // 最大选择图片数量
               .maxNum(2)
                .build();
        // 跳转到图片选择器
        ImgSelActivity.startActivity(this,config,1005);
    }
    private void uploadImg(){
        final File file = new File(imgurl);
        final String url = "http://guozonghui.51vip.biz/demo/appupload.php";
        final RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
        .addPart( Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\""+file.getName()+"\""), RequestBody.create(MediaType.parse("image/png"),file)
        ).build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient  = httpBuilder
                //设置超时
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = mhandler.obtainMessage();
                msg.what= Contants.HttpUpload;
                msg.obj = "上传图片失败，请检查网络是否可用以及图片是否正确";
                mhandler.sendMessage(msg);// 结果返回给UI处理
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.e("info",response.body().string());
                Gson gson=new Gson();
                UpLoadImgBean upLoadImgBean=gson.fromJson(response.body().string(), UpLoadImgBean.class);
                if(upLoadImgBean.getState()!=null){
                if(upLoadImgBean.getState().equals("OK")) {
                    Message msg = mhandler.obtainMessage();
                    msg.what= Contants.HttpUpload;
                    msg.obj = "上传图片成功";
                    mhandler.sendMessage(msg);// 结果返回给UI处理
                    sendMessageSocket(file);
                }else{
                    Message msg = mhandler.obtainMessage();
                    msg.what= Contants.HttpUpload;
                    msg.obj = "上传图片失败，请重新上传";
                    mhandler.sendMessage(msg);// 结果返回给UI处理
                }
            }
            }
        });
    }

    protected void sendMessageSocket(final File file) {
        socketThread.Send("{Z,up}{DA,1}{PICname,"+file.getName()+"}");
    }
}
