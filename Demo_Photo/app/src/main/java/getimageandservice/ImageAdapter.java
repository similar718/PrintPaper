package getimageandservice;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.photo.demo_photo.R;

import java.util.List;

/**
 * Created by Administrator on 2016/10/1.
 */
public class ImageAdapter extends BaseAdapter {
    private List<ImageEntity> list;

    public ImageAdapter(Activity c, List<ImageEntity> lst) {
        mContext = c;
        list=lst;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
             view = inflater.inflate(R.layout.photo_layout, null);
        } else {
            view = convertView;
        }
        ImageEntity info = list.get(position);
        final ImageView imageView = (ImageView) view.findViewById(R.id.photo);
        imageView.setImageBitmap(info.getWeatherBitmap());
        // 给ImageView设置一个Tag，保证异步加载图片时不会乱序
        return view;


    }
    private Activity mContext;
}