package com.library.core.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * *********************************************
 *
 * File name: TypefaceUtil.java
 *
 * @Description: 字体库工具类
 *
 * Others:
 *
 * Function List:
 *
 * History:
 *
 * *********************************************
 */
public class TypefaceUtil {

    //字体库路径
    private static final String path = "fonts/";

    //-----------------------字体库开始--------------------------//

    /**
     * 叶根友毛笔字
     * 字库：在线问诊
     */
    public final static String YeGenYouBrush = path + "yegenyoubrush.ttf";


    //-----------------------字体库结束--------------------------//


    /**
     * 获取字体
     * @param context 上下文
     * @param type 字体库
     * @return
     */
    public static Typeface getFont(Context context,String type){
        return Typeface.createFromAsset(context.getAssets(), type);
    }


}
