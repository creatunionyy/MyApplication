package com.library.core.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

/**
 * *********************************************
 * 
 * File name: XmlParse.java
 * 
 * @Description: XML文件生成与解析工具类：支持对象与集合的生成与解析
 * 
 *               Others:
 * 
 *               Function List:
 * 
 *               History:
 * 
 *               *********************************************
 */

public class XmlParse {
	
	public static String path = /*XHApplication.getApplication().getFilesDir().getPath()
			+ */File.separator + "medical" + File.separator; // 内存路径
	private final static String namespace = null;// 命名空间

	/**
	 * 根据单个对象模型生成XML
	 * 
	 * @param clazz
	 *            对象模型
	 * @param xmlName
	 *            xml文件名称
	 * @param <T>
	 * @return
	 */
	public static <T> boolean parseSingle(T clazz, String xmlName) {
		List<T> list = new ArrayList<T>();
		list.add(clazz);
		return parse(list, null, xmlName, null, null);
	}

	/**
	 * 根据单个对象模型生成XML
	 * 
	 * @param clazz
	 *            对象模型
	 * @param xmlName
	 *            xml文件名称
	 * @param classesName
	 *            对象集合名称
	 * @param codeType
	 *            编码格式
	 * @param <T>
	 * @return
	 */
	public static <T> boolean parseSingle(T clazz, String xmlName,
			String classesName, String codeType) {
		List<T> list = new ArrayList<T>();
		list.add(clazz);
		return parse(list, null, xmlName, classesName, codeType);
	}

	/**
	 * 根据List<对象模型>生成XML
	 * 
	 * @param list
	 *            List<对象模型>
	 * @param xmlName
	 *            xml文件名称
	 * @param <T>
	 * @return
	 */
	public static <T> boolean parse(List<T> list, String xmlName) {
		return parse(list, null, xmlName, null, null);
	}

	/**
	 * 根据List<对象模型>生成XML
	 * 
	 * @param list
	 *            List<对象模型>
	 * @param xmlPath
	 *            xml文件保存路径
	 * @param xmlName
	 *            xml文件名称
	 * @param <T>
	 * @return
	 */
	public static <T> boolean parse(List<T> list, String xmlPath, String xmlName) {
		return parse(list, xmlPath, xmlName, null, null);
	}

	/**
	 * 根据List<对象模型>生成XML
	 * 
	 * @param list
	 *            List<对象模型>
	 * @param xmlPath
	 *            xml文件保存路径
	 * @param xmlName
	 *            xml文件名称
	 * @param classesName
	 *            对象集合名称
	 * @param codeType
	 *            编码格式
	 * @param <T>
	 * @return
	 */
	public static <T> boolean parse(List<T> list, String xmlPath,String xmlName, String classesName, String codeType) {
		if (list == null || list.size() <= 0) {
			return false;
		}
		File file = null;
		if (xmlPath == null || "".equals(xmlPath)) {
			// file = new
			// File(Environment.getExternalStorageDirectory(),xmlName);
	//		file = new File(/*XHApplication.getApplication().getFilesDir()*/, xmlName);
		} else {
			file = new File(xmlPath, xmlName);
		}
		final String className = list.get(0).getClass().getSimpleName();
		classesName = classesName == null ? className + "s" : classesName;
		codeType = codeType == null ? "utf-8" : codeType;
		XmlSerializer serializer = Xml.newSerializer();// xml文件生成器
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			// 为xml生成器设置输出流和字符编码
			serializer.setOutput(fos, codeType);
			// 开始文档，参数分别为字符编码和是否保持独立
			serializer.startDocument(codeType, true);
			// 开始标签,参数分别为：命名空间和标签名
			serializer.startTag(namespace, classesName);
			for (Object obj : list) {
				serializer.startTag(namespace, className);
				Field fields[] = obj.getClass().getDeclaredFields();
				for (Field field : fields) {
					final String fieldName = field.getName();
					field.setAccessible(true);
					serializer.startTag(namespace, fieldName);
					serializer.text(String.valueOf(field.get(obj)));
					serializer.endTag(namespace, fieldName);
				}
				serializer.endTag(namespace, className);
			}
			serializer.endTag(namespace, classesName);
			serializer.endDocument();// 结束xml文档
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 根据对象模型解析XML
	 * 
	 * @param c
	 *            对象模型
	 * @param xmlName
	 *            xml文件名称
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> pullXml(Class<T> c, String xmlName) {
		return pullXml(c, null, xmlName, null, null);
	}

	/**
	 * 根据对象模型解析XML
	 * 
	 * @param c
	 *            对象模型
	 * @param xmlPath
	 *            xml文件保存路径
	 * @param xmlName
	 *            xml文件名称
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> pullXml(Class<T> c, String xmlPath, String xmlName) {
		return pullXml(c, xmlPath, xmlName, null, null);
	}

	/**
	 * 根据对象模型解析XML
	 * 
	 * @param c
	 *            对象模型
	 * @param xmlPath
	 *            xml文件保存路径
	 * @param xmlName
	 *            xml文件名称
	 * @param classesName
	 *            对象集合名称
	 * @param codeType
	 *            编码格式
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> pullXml(Class<T> c,String xmlPath,String xmlName,String classesName,String codeType) {
        try {
            File file = null;
            if(xmlPath == null || "".equals(xmlPath)){
//                file = new File(Environment.getExternalStorageDirectory(),xmlName);
          //      file = new File(XHApplication.getApplication().getFilesDir(),xmlName);
            }else{
                file = new File(xmlPath,xmlName);
            }
            final String className = c.getSimpleName();
            classesName = classesName == null ? className+"s" : classesName;
            codeType = codeType == null ? "utf-8" : codeType;
            List<T> list = null;
            T t = null;
            Object obj = Class.forName(c.getName()).newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            XmlPullParser parser = Xml.newPullParser();// 获取xml解析器
            FileInputStream fis = new FileInputStream(file);
            parser.setInput(fis, codeType);// 参数分别为输入流和字符编码
            int type = parser.getEventType();
            while (type != XmlPullParser.END_DOCUMENT) {// 如果事件不等于文档结束事件就继续循环
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (classesName.equals(parser.getName())) {
                            list = new ArrayList<T>();
                        } else if (className.equals(parser.getName())) {
                            t = c.newInstance();
                        } else {
                            setField(t,fields,parser.getName(),parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (className.equals(parser.getName())) {
                            list.add(t);
                            t = null;
                        }
                        break;
                }
                type = parser.next();// 继续下一个事件
            }
            return list;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 设置成员变量值
     * @param t 泛型对象
     * @param fields 成员变量集合
     * @param nodeName 节点名称
     * @param nodeValue 节点值
     * @param <T>
     * @throws Exception
     */
    private static <T> void setField(T t,Field[] fields,String nodeName,String nodeValue) throws Exception {
        for (Field field : fields) {
            final String fieldName = field.getName();
            if (fieldName.equals(nodeName)) {
                final Class<?> type = field.getType();
                field.setAccessible(true);
                if (type.equals(String.class)) {
                    field.set(t, nodeValue);
                }else if(type.equals(int.class) || type.equals(Integer.class)){
                    field.set(t, Integer.parseInt(nodeValue));
                }else if(type.equals(boolean.class) || type.equals(Boolean.class)){
                    field.set(t, "1".equals(nodeValue) || "true".equals(nodeValue));
                }else if(type.equals(long.class) || type.equals(Long.class)){
                    field.set(t, Long.parseLong(nodeValue));
                }else if(type.equals(double.class) || type.equals(Double.class)){
                    field.set(t, Double.parseDouble(nodeValue));
                }else if(type.equals(float.class) || type.equals(Float.class)){
                    field.set(t, Float.parseFloat(nodeValue));
                }
            }
        }
    }

}
