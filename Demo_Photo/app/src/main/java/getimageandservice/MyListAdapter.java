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
public class MyListAdapter extends BaseAdapter {
    private Activity context;
    private List<ImageEntity> list;

    public MyListAdapter(Activity context, List<ImageEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View itemView = inflater.inflate(R.layout.list_item, null);
        ImageEntity info = list.get(position);
        ImageView imageView = (ImageView) itemView
                .findViewById(R.id.list_image);
        imageView.setImageBitmap(info.getWeatherBitmap());
        return itemView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
