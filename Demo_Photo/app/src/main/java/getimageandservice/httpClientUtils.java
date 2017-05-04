
package getimageandservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class httpClientUtils implements Runnable {
    /**
     * 对于Android4.0之上的环境下，不能在主线程中访问网络 所以这里另新建了一个实现了Runnable接口的Http访问类
     */
    private String username;
    private String password;
private static Context MainContext;
  public static String URL="http://192.168.2.41/demo/moblie.php";
   // public static  String URL="http://192.168.2.4/demo/moblie.php";
   // public static  String URL="http://192.168.1.41/demo/moblie.php";
    //public static  String URL="http://192.168.1.4/demo/moblie.php";
    //public static String PreURL="http://156g6836b7.imwork.net/";
  // public static  String URL="http://156g6836b7.imwork.net/demo/moblie.php";
    public httpClientUtils(String username, String password) {
        // 初始化用户名和密码
        this.username = username;
        this.password = password;
    }
    public httpClientUtils(Context context){
        MainContext=context;
    }
    @Override
    public void run() {
        // 设置访问的Web站点
       /* String path = "http://192.168.1.103:1231/loginas.ashx";
        // 设置Http请求参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);

        String result = sendHttpClientPost(path, params, "utf-8");
        // 把返回的接口输出
        System.out.println(result);*/
    }
    public static Bitmap getBitMap(String imageUrl)
    {
       // int index=imageUrl.indexOf("demo");
        // String  str= PreURL+ imageUrl.substring(index);
       String str=imageUrl;
        //httpGet连接对象
        HttpGet httpRequest = new HttpGet(str);
        //取得HttpClient 对象11111
        HttpClient httpclient = new DefaultHttpClient();
        try {
            //请求httpClient ，取得HttpRestponse
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                //取得相关信息 取得HttpEntiy
                HttpEntity httpEntity = httpResponse.getEntity();
                //获得一个输入流
                InputStream is = httpEntity.getContent();
                System.out.println(is.available());
                System.out.println("Get, Yes!");
                BitmapFactory.Options opts=new BitmapFactory.Options();
                opts.inTempStorage = new byte[1000 * 1024];
                opts.inPreferredConfig = Bitmap.Config.RGB_565;
                opts.inPurgeable = true;
                opts.inSampleSize =2;
                opts.inInputShareable = true;

                Bitmap bitmap = BitmapFactory.decodeStream(is,null, opts);
                is.close();
                return bitmap;
                //iv.setImageBitmap(bitmap);
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            Toast.makeText(MainContext,"网络连接不正常", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Toast.makeText(MainContext,"网络连接不正常", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return null;
    }
    public static String postXml(String path, String xml, String encoding) throws Exception {
        encoding= HTTP.UTF_8;
        byte[] data = xml.getBytes(encoding);

        java.net.URL url = new URL(path);

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();

        conn.setRequestMethod("POST");

        conn.setDoOutput(true);

        conn.setRequestProperty("Content-Type", "text/xml; charset="+ encoding);

        conn.setRequestProperty("Content-Length", String.valueOf(data.length));

        conn.setConnectTimeout(5 * 1000);

        OutputStream outStream = conn.getOutputStream();

        outStream.write(data);

        outStream.flush();

        outStream.close();

        if(conn.getResponseCode()==200){

            return readStream(conn.getInputStream());

        }else
        {
            Toast.makeText(MainContext,"网络连接不正常", Toast.LENGTH_LONG).show();
            return null;
        }



    }
    /**

     * 读取流

     * @param inStream

     * @return 字节数组

     * @throws Exception

     */

    public static String readStream(InputStream inStream) throws Exception
    {

        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];

        int len = -1;

        while( (len=inStream.read(buffer)) != -1)

        {

            outSteam.write(buffer, 0, len);

        }

       // String s=  outSteam.toString();
        outSteam.close();

        inStream.close();
    /*
   JsonData jd = JsonMapper.ToObject(json);
                JsonData jdItems = jd[listName];
                foreach (JsonData item in jdItems)
                {
                    for (int i = 0; i < item.Count; i++)
                    {
                        list.Add(item[i].ToString());
                    }
                }
     */
        String str=new String(outSteam.toByteArray(),"UTF-8");
        System.out.println(str);;
        return str;

    }


    /**
     * 发送Http请求到Web站点
     *
     * @param path
     *            Web站点请求地址
     * @param map
     *            Http请求参数


     * @return Web站点响应的字符串
     */
    public String sendHttpClientPost(String path, Map<String, String> map) {

        List<NameValuePair> list = new ArrayList<NameValuePair>();
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                // 解析Map传递的参数，使用一个键值对对象BasicNameValuePair保存。
                list.add(new BasicNameValuePair(entry.getKey(), entry
                        .getValue()));
            }
        }
        try {

            // 实现将请求 的参数封装封装到HttpEntity中。
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, HTTP.UTF_8);
            // 使用HttpPost请求方式
            HttpPost httpPost = new HttpPost(path);
            // 设置请求参数到Form中。
            httpPost.setEntity(entity);
            // 实例化一个默认的Http客户端
            DefaultHttpClient client = new DefaultHttpClient();
            // 执行请求，并获得响应数据
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            HttpResponse httpResponse = client.execute(httpPost);

            // 判断是否请求成功，为200时表示成功，其他均问有问题。
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                // 通过HttpEntity获得响应流
                InputStream inputStream = httpResponse.getEntity().getContent();
             String strResult = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
                return changeInputStream(inputStream, HTTP.UTF_8);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(MainContext,"网络连接不正常", Toast.LENGTH_LONG).show();
        } catch (ClientProtocolException e) {
            Toast.makeText(MainContext,"网络连接不正常", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(MainContext,"网络连接不正常", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 把Web站点返回的响应流转换为字符串格式
     *
     * @param inputStream
     *            响应流
     * @param encode
     *            编码格式
     * @return 转换后的字符串
     */
    private String changeInputStream(InputStream inputStream, String encode) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result = "";
        if (inputStream != null) {
            try {
                while ((len = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, len);
                }
                result = new String(outputStream.toByteArray(), encode);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MainContext,"网络连接不正常", Toast.LENGTH_LONG).show();
            }
        }
        return result;
    }
}

