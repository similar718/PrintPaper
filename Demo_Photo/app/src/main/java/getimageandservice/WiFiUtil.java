package getimageandservice;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoh on 2016/8/18.
 */
public class WiFiUtil {


    private List<WifiConfiguration> wifiConfigList;

    public WifiManager mWifiManager;

    /**
     *  获取系统Wifi服务   WIFI_SERVICE
     */

    public WiFiUtil(Context context) {
        this.mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        this.myContext=context;
    }

    /**打开Wifi**/
    public void openWiFi() {
        if(!this.mWifiManager.isWifiEnabled()){ //当前wifi不可用
            this.mWifiManager.setWifiEnabled(true);
        }
    }
    /**关闭Wifi**/
    public void closeWiFi() {
        if(mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    /**
     *  得到Scan结果
     */
    public List<ScanResult> getScanResults(){

        return mWifiManager.getScanResults();//得到扫描结果
    }

    /**
     * 获取SSID列表 （去重之后）
     * @return
     */
    public List<String> getSSIDList(){
        List<String> ssidList=new ArrayList<>();
        List<ScanResult> results=mWifiManager.getScanResults();

        for (int i = 0; i <results.size(); i++) {

            if (ssidList.contains(results.get(i).SSID)){

            }else {
                ssidList.add(results.get(i).SSID);
            }

        }
        return ssidList;
    }

    /**
     * 得到Wifi配置好的信息
     */

    public void getConfiguration(){
        wifiConfigList = mWifiManager.getConfiguredNetworks();//得到配置好的网络信息
        for(int i =0;i<wifiConfigList.size();i++){
            Log.i("getConfiguration", wifiConfigList.get(i).SSID);
            Log.i("getConfiguration", String.valueOf(wifiConfigList.get(i).networkId));
        }
    }

    /**
     * 判定指定WIFI是否已经配置好,依据WIFI的地址BSSID,返回NetId
     */

    public int IsConfiguration(String SSID){
        Log.i("IsConfiguration", String.valueOf(wifiConfigList.size()));
        for(int i = 0; i < wifiConfigList.size(); i++){
            Log.i(wifiConfigList.get(i).SSID, String.valueOf( wifiConfigList.get(i).networkId));
            if(wifiConfigList.get(i).SSID.equals(SSID)){//地址相同
                return wifiConfigList.get(i).networkId;
            }
        }
        return -1;
    }

    /**
     * 添加指定WIFI的配置信息,原列表不存在此SSID
     */

    public int AddWifiConfig(List<ScanResult> wifiList, String ssid, String pwd){
        int wifiId = -1;
        for(int i = 0;i < wifiList.size(); i++){
            ScanResult wifi = wifiList.get(i);
            if(wifi.SSID.equals(ssid)){
                WifiConfiguration wifiCong = new WifiConfiguration();
                wifiCong.SSID = "\""+wifi.SSID+"\"";//\"转义字符，代表"
                if(!pwd.equals(""))
                    wifiCong.preSharedKey= "\""+pwd+"\"";
                else
                {
                    //wifiCong.wepKeys[0] = "";
                    wifiCong.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    //wifiCong.wepTxKeyIndex = 0;
                }
                wifiCong.hiddenSSID = false;
                wifiCong.status = WifiConfiguration.Status.ENABLED;
                wifiId = mWifiManager.addNetwork(wifiCong);//将配置好的特定WIFI密码信息添加,添加完成后默认是不激活状态，成功返回ID，否则为-1
                if(wifiId != -1){
                    return wifiId;
                }
            }
        }
        return wifiId;
    }


    /**
     * 连接指定SSID的WIFI
     */
    Context myContext=null;
    public void connectWiFi(String ssId , String password){
        if (TextUtils.isEmpty(ssId)){
            return;
        }
        openWiFi();
        // getConfiguration();

        boolean wifiIsInScope=false;

        for (int i = 0; i <getSSIDList().size() ; i++) {
            if (getSSIDList().get(i).equals(ssId)){
                wifiIsInScope=true;
            };
        }
        if (!wifiIsInScope){
            //Toast.makeText(myContext,"WiFi不在范围内",Toast.LENGTH_SHORT);
            return;
        }
        //else
            //Toast.makeText(myContext,"WiFi连接中...",Toast.LENGTH_SHORT);

        int netId= AddWifiConfig(getScanResults(),ssId,password);
        mWifiManager.enableNetwork(netId,true);

    }


}