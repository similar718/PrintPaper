package getimageandservice;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photo.demo_photo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    ArrayList Oneinfomations = new ArrayList<ImageEntity>();
    ArrayList Allinfomations = new ArrayList<ImageEntity>();
    List<Person> Persons=new ArrayList<Person>();
    protected static final int UPDATE_TEXT = 1;
    static final int UPDATE_IMAGE=2;
    static final int UPDATE_ZHANGDAN=3;
    static final int CLEAR_ZHANGDAN=4;
    static final int SHOWMESSAGE=5;
    GridView OnePersonImgListView;
    GridView AllPersonImgListView;
    static Person CurrPerson=null;
    PhoneInfo MyPhone=null ;
    String SendPhoneNum="";
    httpClientUtils hu=null;
   boolean Changed=true;
    ImageSmallAdapter AllPersonListAdapter;
    ImageAdapter OnePersonListAdapter;

    public static final int REQUEST_STORAGE = 17;

    TextView TVdyzj;
    TextView TVdpsr;
    TextView TVdssk;
    TextView TVwlsk;
    TextView TVsyje;
    TextView TVzhye;
     void ChooseCurrPerson() {
         if(Persons.isEmpty())
         {
             if(CurrPerson==null)
                 return;
             CurrPerson=null;
         }
         else
         {
             if(CurrPerson==null)
             {
                 CurrPerson=Persons.get(0);
                 AllPersonImgListView.setSelection(0);
             }
             else
             {
                 String oldname=   CurrPerson.getFileName();
                 int findindex=0;
                 for(Person p:Persons)
                 {
                     if(p.getFileName().equals(oldname))
                     {
                         CurrPerson=p;
                         break;
                     }
                     findindex++;
                 }
                 if(findindex==Persons.size())
                 {
                     CurrPerson=Persons.get(0);
                     AllPersonImgListView.setSelection(0);
                 }
               else
                 {
                     CurrPerson=Persons.get(findindex);
                     AllPersonImgListView.setSelection(findindex);
                 }
             }


         }
         if(CurrPerson!=null) {
             for (ImageEntity ie : CurrPerson.getImgLst()) {
                 Oneinfomations.add(ie);
             }
             OnePersonListAdapter.notifyDataSetChanged();
         }
         else
         {
             Message message = new Message();
             message.what = CLEAR_ZHANGDAN;
             handler.sendMessage(message);//将message对象发送出去
         }


    }
    private Handler handler = new Handler(){
        //处理传递过来的消息
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT:
                    //在这里更新UI操作
                    OnePersonListAdapter.notifyDataSetChanged();
                    AllPersonListAdapter.notifyDataSetChanged();
                break;
                case UPDATE_ZHANGDAN:
                     HashMap<String,String> ImgLst=(HashMap<String,String>)msg.obj;
                    try {
                        TVsyje.setText(ImgLst.get("money"));
                        TVzhye.setText(ImgLst.get("ZHYE"));
                        TVdyzj.setText(ImgLst.get("ZYE"));
                        TVdssk.setText(ImgLst.get("SDSK"));
                        TVdpsr.setText(ImgLst.get("SDSR"));
                        TVwlsk.setText(ImgLst.get("WLSK"));
                    }
                    catch (Exception ex){}
                    break;
                case UPDATE_IMAGE:
                    OnePersonListAdapter.notifyDataSetChanged();
                    AllPersonListAdapter.notifyDataSetChanged();
                    ChooseCurrPerson();
                    //String str=(String)msg.obj;
                    //if(!str.equals(""))
                        //Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
                    break;
                case SHOWMESSAGE:
                   // Toast.makeText(MainActivity.this,msg.obj.toString(),Toast.LENGTH_LONG);
                    break;
                case CLEAR_ZHANGDAN:
                    TVsyje.setText("");
//                    TVzhye.setText("");
//                    TVdyzj.setText("");
//                    TVdssk.setText("");
//                    TVdpsr.setText("");
//                    TVwlsk.setText("");
                    break;
                default:
                    OnePersonListAdapter = new ImageAdapter(getimageandservice.MainActivity.this,Oneinfomations);
                    OnePersonImgListView.setAdapter(OnePersonListAdapter);
                    AllPersonListAdapter =  new ImageSmallAdapter(getimageandservice.MainActivity.this,Allinfomations);
                    AllPersonImgListView.setAdapter(AllPersonListAdapter);
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
                .detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        PhoneInfo MyPhone = new PhoneInfo(getimageandservice.MainActivity.this);

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_STORAGE);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, REQUEST_STORAGE);
        }
        MyPhone.getProvidersName();
       SendPhoneNum=  MyPhone.getNativePhoneNumber();
        if(SendPhoneNum.equals(""))
            SendPhoneNum=MyPhone.IMSI;
        //初始化容器
         TVdyzj= (TextView) findViewById((R.id.dyzj));
         TVdpsr= (TextView) findViewById((R.id.dpsr));
         TVdssk= (TextView) findViewById((R.id.dssk));
         TVwlsk= (TextView) findViewById((R.id.wlsk));
        TVsyje= (TextView) findViewById((R.id.syje));
        TVzhye=(TextView)findViewById(R.id.zhye) ;
        OnePersonImgListView=(GridView)findViewById(R.id.OnePersonImg);
        AllPersonImgListView=(GridView)findViewById(R.id.AllPersonImg);

        AllPersonImgListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(position==-1)
                    return;
                Message message = new Message();
                message.what = CLEAR_ZHANGDAN;
                handler.sendMessage(message);//将message对象发送出去
                Oneinfomations.clear();
                if(position>=Persons.size())
                {
                    CurrPerson=null;
                }
                else
                {
                    CurrPerson=Persons.get(position);
                }
                ChooseCurrPerson();


            }});
        OnePersonListAdapter = new ImageAdapter(getimageandservice.MainActivity.this,Oneinfomations);
        OnePersonImgListView.setAdapter(OnePersonListAdapter);
        AllPersonListAdapter =  new ImageSmallAdapter(getimageandservice.MainActivity.this,Allinfomations);
        AllPersonImgListView.setAdapter(AllPersonListAdapter);

        Init();
    }
     Person FindPersonByName(String path)
    {
       for(Person p:Persons)
       {
           if(p.getImgFolder().equals(path))
               return p;
       }
        return null;
    }
    ImageEntity FindImageEntityByName(String path, Person p)
    {
        for(ImageEntity img:p.getImgLst())
        {
            if(img.Path.equals(path))
                return img;
        }
        return null;
    }
    Timer mTimer;
    TimerTask mTimerTask;
    Timer getZdmTimer;
    TimerTask getZdmTimerTask;

    WiFiUtil wifi=null;
    void Init()
    {
        wifi=new WiFiUtil(getimageandservice.MainActivity.this);

        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override

            public void run() {
                try {
                    try
                    {
                        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        WifiManager wifiMgr = (WifiManager) getimageandservice.MainActivity.this.getSystemService(Context.WIFI_SERVICE);

                        WifiInfo info = wifiMgr.getConnectionInfo();
                        String wifiId = info != null ? info.getSSID() : null;
                        if (!wifiId.equals("\"Photo-88\"")) {
                            // Do whatever
                            wifi.connectWiFi("Photo-88","12345678");

                        }


                    }
                    catch (Exception ex){}

                    getKeList();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };
        //开始一个定时任务
        mTimer.schedule(mTimerTask,0, 9000);
        getZdmTimer= new Timer();
        getZdmTimerTask = new TimerTask() {
            @Override
            public void run() {
                    try {
                        getZd();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        };
        //开始一个定时任务
        getZdmTimer.schedule(getZdmTimerTask,0, 3000);
    }

    void getZd() throws JSONException {
        if(CurrPerson==null)
        {
            Message message = new Message();
            message.what = CLEAR_ZHANGDAN;
            handler.sendMessage(message);//将message对象发送出去
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("com","readaccounts");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String s=SendPhoneNum;
            json.put("phone",s);

        json.put("filename",CurrPerson.getFileName());

        try {
            String jstr=json.toString();
            String s1=   hu.postXml(httpClientUtils.URL,jstr,"");
            HashMap<String,String> ImgLst= JsonHelper.getStringList(s1,true);
            if(ImgLst.size()>0)
            {
                Message message = new Message();
                message.what = UPDATE_ZHANGDAN;
                message.obj=ImgLst;
                handler.sendMessage(message);//将message对象发送出去
            }

//﻿{"com":"readaccounts","state":"OK","geshu":"5","money":"42","data":[{"ZHYE":"0.75",
// "ZYE":"25.00","SDSK":"0","SDSR":"0.75","WLSK":"25"}]}



            System.out.println(s1);
        } catch (Exception e) {
            e.printStackTrace();
        }

}

    public void getKeList() throws IOException {

       // HttpClient httpclient = new DefaultHttpClient();
        StringBuilder MyStringBuilder = new StringBuilder("Your total is ");
      String str=  "{\"com\":\"readfilename\"}";
        JSONObject person = new JSONObject();
        try {
            person.put("com","readfilename");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 第一个键phone的值是数组，所以需要创建数组对象

         hu=new httpClientUtils(getimageandservice.MainActivity.this);
        String s=person.toString();
        try {
            String jsonstr= httpClientUtils.postXml(httpClientUtils.URL,str,"");
          List<String> strlst=  JsonHelper.getStringList(jsonstr);//person
            ;
            List<String> OkImgLst=new ArrayList<String>();
            List<Person> newcopy=new ArrayList<>();
          for(String sl:strlst)
          {
              OkImgLst.clear();
             String path= sl.substring(sl.lastIndexOf("/")+1);
              Person p=null;
                p= FindPersonByName(path);
              if(p==null) {
                  p = new Person();
                  p.setImgFolder(path);
              }
              JSONObject jo = new JSONObject();
              jo.put("com","readPCname");
              jo.put("filename",path);
              String imgstr= httpClientUtils.postXml(httpClientUtils.URL,jo.toString(),"");
              List<String> ImgLst= JsonHelper.getStringList(imgstr);

             for(String im:ImgLst)
             {
                if(im.endsWith(".jpg")||im.endsWith("png")||im.endsWith("bmp"))
                {
                    OkImgLst.add(im);
                }
                 else if(im.endsWith(".jpeg"))
                    OkImgLst.add(im);
                 else if(im.endsWith("bmp"))
                {
                    OkImgLst.add(im);
                }
             }

              List<ImageEntity> imgTempLst=new ArrayList<>();
              for(String url:OkImgLst)
              {
                  ImageEntity ie= FindImageEntityByName(url,p);
                  if(ie==null)
                  {
                      Bitmap b= httpClientUtils.getBitMap(url);
                      if(b!=null)
                      {
                          //System.out.println("取到图片");
                          imgTempLst.add(new ImageEntity(b,url));
                      }
                  }
                  else
                  {
                      imgTempLst.add(ie);
                  }
                  if(imgTempLst.size()==2)
                  {
                      break;
                  }

              }

              p.setFileName(path);
              p.setFilePaths(OkImgLst);
              p.getImgLst().clear();
              p.getImgLst().addAll(imgTempLst);
              newcopy.add(p);
          }
            Persons.clear();
            Persons.addAll(newcopy);
            try
            {
                Allinfomations.clear();
                Oneinfomations.clear();
            for(Person p:Persons)
            {
                    if(p.getImgLst().size()>0)
                        Allinfomations.add(p.getImgLst().get(0));

            }

                Message message = new Message();
                message.what = UPDATE_IMAGE;
                handler.sendMessage(message);//将message对象发送出去



        }
            catch (Exception ex)
            {
                System.out.println(ex);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void kqpcht_Click(View view) {
//        if(CurrPerson==null)
//        {
//            //Toast.makeText(MainActivity.this,"无客户",Toast.LENGTH_SHORT);
//            return;
//        }
        final EditText inputServer = new EditText(this);
        inputServer.setWidth(200);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("密码"+SendPhoneNum).setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (inputServer.getText().toString().equals("123456")) {
                    JSONObject jo = new JSONObject();
                    try {
                        jo.put("com", "PChoutai");

                        String s=SendPhoneNum;
                        jo.put("phone",s);

                        try {
                            String jstr=jo.toString();
                            String s1=   hu.postXml(httpClientUtils.URL,jstr,"");
                            if(s1.contains("OK"))
                            {
                                Toast.makeText(getimageandservice.MainActivity.this,"开启成功", Toast.LENGTH_SHORT);
                            }
                            else
                            {
                                Toast.makeText(getimageandservice.MainActivity.this,"开启失败", Toast.LENGTH_SHORT);
                            }
                            System.out.println(s1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    Toast.makeText(getimageandservice.MainActivity.this,"密码错误", Toast.LENGTH_SHORT);
                }
            }
        });
        builder.show();

}


    public void xjfkan_onclick(View view) {
        if(TVsyje.getText().equals("")||TVsyje.getText()==null)
            return;
        if(CurrPerson==null)
        {

            Message message = new Message();
            message.obj="当前无客户";
            message.what = CLEAR_ZHANGDAN;
            handler.sendMessage(message);//将message对象发送出去
            //Toast.makeText(MainActivity.this,"无客户",Toast.LENGTH_LONG);
            return;
        }

        final EditText inputServer = new EditText(this);
        inputServer.setWidth(200);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("密码"+SendPhoneNum).setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (inputServer.getText().toString().equals("123456")) {
                    JSONObject jo=new JSONObject();

                    try {
                        jo.put("com", "writefilename");
                        jo.put("filename", CurrPerson.getFileName());

                        String s=SendPhoneNum;
                        jo.put("phone",s);


                        try {
                            String jstr=jo.toString();
                            String s1=   hu.postXml(httpClientUtils.URL,jstr,"");

                            System.out.println(s1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    Toast.makeText(getimageandservice.MainActivity.this,"密码错误", Toast.LENGTH_SHORT);
                }
            }
        });
        builder.show();
    }
}
