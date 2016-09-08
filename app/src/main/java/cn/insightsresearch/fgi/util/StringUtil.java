package cn.insightsresearch.fgi.util;

import java.util.List;

/**
 * Created by Administrator on 2016/9/7.
 */
public class StringUtil {

    public static String formatList(List<?> list , String dot) {
        if(dot ==null || "".equals(dot)){ dot = ",";  }
        StringBuilder b = new StringBuilder();
        boolean flag = false;
        for(Object o : list) {
            if (flag) {
                b.append(dot);
            }else {
                flag=true;
            }
            b.append(o);
        }
        return b.toString();
    }

    public static String getType(int i){
        switch (i){
            case 1:
                return "(单选)";
            case 2:
                return "(多选)";
            case 3:
                return "(问答)";
            default:
                return "（？）";
        }
    }

}
