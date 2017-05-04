package getimageandservice;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/10/1.
 */
public class ImageEntity {
    /**
     * 代表天气状况的图片
     */
    private Bitmap bitmap;
    public String Path;
    public boolean isDownLoad=false;
    public ImageEntity(Bitmap weatherBitmap, String path) {
        super();
        this.bitmap = weatherBitmap;
        this.Path=path;
    }

    public Bitmap getWeatherBitmap() {
        return bitmap;
    }

    public void setWeatherBitmap(Bitmap weatherBitmap) {
        this.bitmap = weatherBitmap;
    }


}
