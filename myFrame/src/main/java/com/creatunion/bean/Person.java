package com.creatunion.bean;

import com.library.core.parse.JsonParse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ZhangZhaoCheng
 *
 * @Date: 2015/11/11
 * @version: 1.0
 * @Description:
 */
public class Person {

    private String name;

    private String age;


    public Person(JSONObject jo) throws JSONException {
        JsonParse.parse(jo, this);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
