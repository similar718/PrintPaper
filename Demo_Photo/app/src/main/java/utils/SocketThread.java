package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import Constant.Contants;

/**
 * Created by pc on 2017/4/25.
 */


public class SocketThread extends Thread {
    private String ip = "156g6836b7.imwork.net";
    private int port = 20152;
    private String TAG = "socket thread";
    private int timeout = 20000;

    public Socket client = null;
    PrintWriter out;
    BufferedReader in;
    public boolean isRun = true;
    Handler inHandler;
    Handler outHandler;
    Context ctx;
    private String TAG1 = "===Send===";
    SharedPreferences sp;

    public SocketThread(Handler handlerin, Context context) {
        inHandler = handlerin;
        ctx = context;
        Log.i(TAG, "创建线程socket");
    }

    /**
     * 连接socket服务器
     */
    public void conn() {

        try {
            initdate();
            Log.i(TAG, "连接中……");
            client = new Socket(ip, port);
            client.setSoTimeout(timeout);// 设置阻塞时间
            Log.i(TAG, "连接成功");
            in = new BufferedReader(new InputStreamReader(
                    client.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    client.getOutputStream())), true);
            Log.i(TAG, "输入输出流获取成功");
        } catch (UnknownHostException e) {
            Log.i(TAG, "连接错误UnknownHostException 重新获取");
            e.printStackTrace();
            conn();
        } catch (IOException e) {
            Log.i(TAG, "连接服务器io错误");
            e.printStackTrace();
        } catch (Exception e) {
            Log.i(TAG, "连接服务器错误Exception" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void initdate() {
        sp = ctx.getSharedPreferences("SP", ctx.MODE_PRIVATE);
        ip = sp.getString("ipstr", ip);
        port = Integer.parseInt(sp.getString("port", String.valueOf(port)));
        Log.i(TAG, "获取到ip端口:" + ip + ";" + port);
    }

    /**
     * 实时接受数据
     */
    @Override
    public void run() {
        conn();
        String line = "";
        while (isRun) {
            try {
                if (client != null) {
                    while ((line = in.readLine()) != null) {
                        Message msg = inHandler.obtainMessage();
                        msg.what= Contants.SocketReceive;
                        msg.obj = line;
                        inHandler.sendMessage(msg);// 结果返回给UI处理
                    }

                } else {
                    Log.i(TAG, "没有可用连接");
                    conn();
                }
            } catch (Exception e) {
                Log.i(TAG, "数据接收错误" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送数据
     *
     * @param mess
     */
    public void Send(String mess) {
        try {
            if (client != null) {
                Log.i(TAG1, "发送" + mess + "至"
                        + client.getInetAddress().getHostAddress() + ":"
                        + String.valueOf(client.getPort()));
                out.println(mess);
                out.flush();
                Log.i(TAG1, "发送成功");
            } else {
                Log.i(TAG, "client 不存在");
                Log.i(TAG, "连接不存在重新连接");
                conn();
            }

        } catch (Exception e) {
            Log.i(TAG1, "send error");
            e.printStackTrace();
        } finally {
            Log.i(TAG1, "发送完毕");

        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        try {
            if (client != null) {
                in.close();
                out.close();
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}