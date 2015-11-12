package com.library.core.demo;

import com.creatunion.bean.Person;
import com.library.core.parse.JsonParse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ZhangZhaoCheng
 *
 * @Date: 2015/11/11
 * @version: 1.0
 * @Description:
 */
public class JsonParseDemo {


    public JsonParseDemo(){
        List<Person> list = JsonParse.parseArrays(data(), "data", Person.class);
        System.out.println(list.size());
    }

    private JSONObject data() {
        try {
            JSONObject object = new JSONObject();
            object.put("ok", 1);

            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "张三");
            jsonObject.put("age", "16");

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("name", "李四");
            jsonObject1.put("age", "20");

            jsonArray.put(jsonObject);
            jsonArray.put(jsonObject1);

            object.put("data", jsonArray);
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
