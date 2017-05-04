package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.List;
import com.example.photo.demo_photo.R;
import utils.ImageTool;
/**
 * Created by pc on 2017/3/22.
 */

public class PhotoAdapter extends BaseAdapter {
    Context context;
    List<String> list;
    public PhotoAdapter(Context context,List<String> list){
        this.context=context;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.photo_item, parent, false);
            holder = buildeViewholder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        bindViewData(holder, convertView, position);
        return convertView;
    }

    public class ViewHolder{
        ImageView photo;
    }
    public ViewHolder buildeViewholder(View convertView){
        ViewHolder viewHolder=new ViewHolder();
        viewHolder.photo= (ImageView) convertView.findViewById(R.id.photo);
        return viewHolder;
    }
    private void bindViewData(final ViewHolder holder, View convertView, final int position) {
        ImageTool.DefaultImageViewIcon(context, list.get(position),
                holder.photo);

    }
}
