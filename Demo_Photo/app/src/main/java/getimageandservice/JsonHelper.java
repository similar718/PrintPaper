package getimageandservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/10/1.
 */
public class JsonHelper {
    public static List<String> getStringList(String jsonstr) throws JSONException {
        List<String> keyListstr = new ArrayList<String>();
        JSONObject dataJson = new JSONObject(jsonstr);
        JSONArray myJsonArray= null;
        try {
            myJsonArray = dataJson.getJSONArray("data");
            String s=myJsonArray.get(0).toString();
            if(myJsonArray.length()==1&&myJsonArray.get(0).toString().equals("[]"))
                return keyListstr;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // JSONArray   myJsonArray = new JSONArray(jsonstr);

        for(int i=0 ; i < myJsonArray.length() ;i++)
        {
            //获取每一个JsonObject对象
            JSONObject myjObject = myJsonArray.getJSONObject(i);
            Iterator it = myjObject.keys();

            while(it.hasNext()){
                keyListstr.add(myjObject.getString(it.next().toString()));
            }



        }
      return   keyListstr;
    }

    public static HashMap<String,String> getStringList(String jsonstr, boolean isGetMoney) throws JSONException {
        HashMap<String,String> keyListstr = new HashMap<String,String>();
        JSONObject dataJson = new JSONObject(jsonstr);
        JSONArray myJsonArray= null;
        try {
            String m=dataJson.get("money").toString();
            keyListstr.put("money",m);
            myJsonArray = dataJson.getJSONArray("data");
        } catch (JSONException e) {

            e.printStackTrace();
            return keyListstr;
        }
        // JSONArray   myJsonArray = new JSONArray(jsonstr);

        for(int i=0 ; i < myJsonArray.length() ;i++)
        {
            //获取每一个JsonObject对象
            JSONObject myjObject = myJsonArray.getJSONObject(i);
            Iterator it = myjObject.keys();

            while(it.hasNext()){
                String key=it.next().toString();
                keyListstr.put(key,myjObject.getString(key));
            }



        }
        return   keyListstr;
    }
}

