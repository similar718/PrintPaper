package com.example.photo.demo_photo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.isseiaoki.simplecropview.CropImageView;
import com.yuyh.library.imgsel.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import utils.SetColorToImage;
import utils.SocketThread;

public class EditImageActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back_iv;
    private Button mNext;
    private ListView lv_image;
    private CropImageView image_edit_iv;
    private ImageButton left_iv;
    private ImageButton up_iv;
    private ImageButton down_iv;
    private ImageButton right_iv;
    private Button big_btn;
    private Button small_btn;
    private Button light_color_btn;
    private Button deep_color_btn;
    private EditText num_et;
    private ImageView image_edit_iv1;
    private List<String> mList = null;
    private String imgurl = "";
    private int math_num = 1;
    private String mBaseUrl = Contants.HttpPostUrlPath;
    Handler mhandler;
    private static int imagetype = 0;
    private Bitmap MBitmap = null;
    private int type = 0;
    SocketThread socketThread;
    ImageLoader loader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        Intent intent = getIntent();
        type = intent.getIntExtra("type",0);

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
                                for (int i=0;i<mList.size();i++){
                                    deleteFile(mList.get(i));//TODO
                                    deleteFile(imgurl);
                                }
                                Intent payIntent = new Intent(EditImageActivity.this, getimageandservice.MainActivity.class);
                                startActivity(payIntent);
                            } else {
                                Log.i("socket", "没有数据返回不更新");
                            }
                        }}
                    else if(msg.what== Contants.HttpUpload){
                        Toast.makeText(EditImageActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        };
        socketThread = new SocketThread(mhandler,this);
        socketThread.start();
    }

    private void initData(){
        if (!checkPermission())
            return;
        // 自定义图片加载器
        loader = new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        };
        mList = new ArrayList<>();
        File  scanner5Directory = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera");
        if (scanner5Directory.isDirectory()) {
            for (File file : scanner5Directory.listFiles()) {
                String name = file.getName();
                String path = file.getAbsolutePath();
                if ((name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"))&&name.startsWith("C360_")) {//C360_
                    mList.add(path);
                }
            }
        }
        PhotoAdapter adapter=new PhotoAdapter(this,mList);
        lv_image.setAdapter(adapter);
        Bitmap bitmap = BitmapFactory.decodeFile(mList.get(0));
        image_edit_iv.setImageBitmap(bitmap);
        MBitmap = bitmap;
    }

    private void initView(){
        mNext = (Button) findViewById(R.id.next);
        back_iv = (ImageView) findViewById(R.id.back_iv);
        lv_image = (ListView) findViewById(R.id.lv_image);
        image_edit_iv = (CropImageView) findViewById(R.id.image_edit_iv);
        left_iv = (ImageButton) findViewById(R.id.left_iv);
        up_iv = (ImageButton) findViewById(R.id.up_iv);
        down_iv = (ImageButton) findViewById(R.id.down_iv);
        right_iv = (ImageButton) findViewById(R.id.right_iv);
        big_btn = (Button) findViewById(R.id.big_btn);
        small_btn = (Button) findViewById(R.id.small_btn);
        light_color_btn = (Button) findViewById(R.id.light_color_btn);
        deep_color_btn = (Button) findViewById(R.id.deep_color_btn);
        num_et = (EditText) findViewById(R.id.num_et);
        image_edit_iv1 = (ImageView) findViewById(R.id.image_edit_iv1);

        if (mList!=null && mList.size()>0) {
            image_edit_iv.setImageBitmap(BitmapFactory.decodeFile(mList.get(0)));
            MBitmap = null;
            MBitmap = BitmapFactory.decodeFile(mList.get(0));
        }

        lv_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image_edit_iv.setImageBitmap(BitmapFactory.decodeFile(mList.get(position)));
                        MBitmap = null;
                        MBitmap = BitmapFactory.decodeFile(mList.get(position));
                    }
                });
            }
        });

        mNext.setOnClickListener(this);
        light_color_btn.setOnClickListener(this);
        deep_color_btn.setOnClickListener(this);
        //上下左右
        left_iv.setOnClickListener(this);
        up_iv.setOnClickListener(this);
        right_iv.setOnClickListener(this);
        down_iv.setOnClickListener(this);
        //放大缩小
        big_btn.setOnClickListener(this);
        small_btn.setOnClickListener(this);
        back_iv.setOnClickListener(this);

        Contants.getscalewh(type);
    }

    public String saveBitmap(Bitmap bm) {
        String pathname = "C360_"+new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date())+".jpg";
        File f = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera", pathname);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            return Environment.getExternalStorageDirectory().toString() + File.separator + "/DCIM/Camera" + File.separator + pathname;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(EditImageActivity.this,"剪切之后的图片保存失败！",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next:
                final Bitmap bitmap = SetColorToImage.addBitmapAll(image_edit_iv.getCroppedBitmap(Contants.MSCALEWIDTH,Contants.MSCALEHEIGHT),null,Contants.mRow,Contants.mColumn);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image_edit_iv.setVisibility(View.GONE);
                        image_edit_iv1.setVisibility(View.VISIBLE);
                        image_edit_iv1.setImageBitmap(bitmap);
                    }
                });
                imgurl = saveBitmap(bitmap);
                math_num = Integer.parseInt(num_et.getText().toString());
                if (imgurl!=null && !"".equals(imgurl))
                    uploadImg();
                    //doPostImage(resulitImage);//TODO
                break;

            case R.id.light_color_btn:
                if (imagetype<0)
                    imagetype = 0;
                imagetype++;
                final Bitmap bitmap1 = SetColorToImage.Brightness(MBitmap,imagetype);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image_edit_iv.setImageBitmap(bitmap1);
                        MBitmap = null;
                        MBitmap = bitmap1;
                    }
                });
                break;
            case R.id.deep_color_btn:
                if (imagetype>0)
                    imagetype = 0;
                imagetype--;
                final Bitmap bitmap2 = SetColorToImage.Brightness(MBitmap,imagetype);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image_edit_iv.setImageBitmap(bitmap2);
                        MBitmap = null;
                        MBitmap = bitmap2;
                    }
                });

            break;
            case R.id.back_iv:
                Intent intent = new Intent(EditImageActivity.this,MainUIActivity.class);
                startActivity(intent);
                break;

            default:
                Toast.makeText(EditImageActivity.this,"请点击截图框使用",Toast.LENGTH_SHORT).show();
        }
    }

    //解码缩放图片(Decode a Scaled Image)
    private void setPic(String bigpath) {
        // Get the dimensions of the View
        int targetW = image_edit_iv.getWidth();
        int targetH = image_edit_iv.getHeight();
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        // 该 值设为true那么将不返回实际的bitmap，也不给其分配内存空间这样就避免内存溢出了。但是允许我们查询图片的信息这其中就包括图片大小信息
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(bigpath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        // Determine how much to scale down the image
        // Math.min求最小值
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        // 设置恰当的inSampleSize可以使BitmapFactory分配更少的空间以消除该错误
        bmOptions.inSampleSize = scaleFactor;
        // 如果inPurgeable设为True的话表示使用BitmapFactory创建的Bitmap,用于存储Pixel的内存空间在系统内存不足时可以被回收
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(bigpath, bmOptions);
        image_edit_iv.setImageBitmap(bitmap);
    }

    private void uploadImg(){
        final File file = new File(imgurl);
        final String url = Contants.HttpPostUrlPath;//"http://guozonghui.51vip.biz/demo/appupload.php";
        final RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart( Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\""+file.getName()+"\""), RequestBody.create(MediaType.parse("image/png"),file)
                ).build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        final OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
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

    public boolean checkPermission(){
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            return true;
        } else {
            EasyPermissions.requestPermissions(this, "需要权限才能访问，请授权",
                    1001, perms);
        }
        return false;
    }

    protected void sendMessageSocket(final File file) {
        socketThread.Send("{Z,up}{DA,"+math_num+"}{PICname,"+file.getName()+"}");
    }
    
    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }
}
