package Constant;

/**
 * Created by pc on 2017/4/25.
 */

public class Contants {
    public static final int SocketConect=1;
    public static final int SocketSend=2;
    public static final int SocketReceive=3;
    public static final int HttpUpload=4;

    public static int MSCALEWIDTH = 0;
    public static int MSCALEHEIGHT = 0;
    public static String HttpPostUrlPath = "http://192.168.2.41/demo/Debug/SYS/photo/";
    public static int mRow = 0;
    public static int mColumn = 0;

    public static void getscalewh(int type){
        switch (type){
            case 1: //25*35  295*413  11.8 11.8
                MSCALEWIDTH = 295;
                MSCALEHEIGHT = 413;
                mRow = 4;
                mColumn = 3;
                break;
            case 2://33*48  390*567  11.8181  11.8125
                MSCALEWIDTH = 390;
                MSCALEHEIGHT = 567;
                mRow = 3;
                mColumn = 2;
                break;
            case 3://35*45  413*531
                MSCALEWIDTH = 413;
                MSCALEHEIGHT = 531;
                mRow = 3;
                mColumn = 2;
                break;
            case 4://40*60  472*708
                MSCALEWIDTH = 472;
                MSCALEHEIGHT = 708;
                mRow = 2;
                mColumn = 2;
                break;
            case 5://50*50 590*590
                mRow = 1;
                mColumn = 1;
                MSCALEWIDTH = 590;
                MSCALEHEIGHT = 590;
                break;

            default:
                mRow = 1;
                mColumn = 1;
                MSCALEWIDTH = 400;
                MSCALEHEIGHT = 400;
                break;
        }
    }
}
