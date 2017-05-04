package getimageandservice;

/**
 * Created by Administrator on 2016/10/1.
 */
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 读取手机设备信息测试代码
 * http://www.souapp.com 搜应用网
 * song2c@163.com
 * 宋立波
 */
public class PhoneInfo {

    private TelephonyManager telephonyManager;
    /**
     * 国际移动用户识别码
     */
    public String IMSI="";
    private Context cxt;

    public PhoneInfo(Context context) {
        cxt = context;
        telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 获取电话号码
     */
    public String getNativePhoneNumber() {
        String NativePhoneNumber = null;
        checkPermission();
        String number = telephonyManager.getLine1Number();
        if (number!=null && !"".equals(number))
            NativePhoneNumber = telephonyManager.getLine1Number();

        NativePhoneNumber = "";
        return NativePhoneNumber;
    }

    public boolean checkPermission(){
        String[] perms = {Manifest.permission.READ_SMS};
        if (EasyPermissions.hasPermissions(cxt, perms)) {
            return true;
        } else {
            EasyPermissions.requestPermissions((Activity) cxt, "需要权限才能访问，请授权", 1001, perms);
        }
        return false;
    }

    /**
     * 获取手机服务商信息
     */
    public String getProvidersName() {
        String ProvidersName = "N/A";
        try {
            IMSI = telephonyManager.getSubscriberId();
            // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
            System.out.println(IMSI);
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                ProvidersName = "中国移动";
            } else if (IMSI.startsWith("46001")) {
                ProvidersName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                ProvidersName = "中国电信";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ProvidersName;
    }
}