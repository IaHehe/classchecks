package com.framework.common.sms.bmob.bson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

public class BSONString {

    public LinkedHashMap<String, Object> getBSON(String str) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(BSON.TAG_MSG, BSON.MSG_ERROR_EMPTY);
        if (str.isEmpty() || str == null)
            return map;
        map.put(BSON.TAG_MSG, BSON.MSG_ERROR_NOTBSON);
        if (str.length() < 2)
            return map;
        str = str.trim();
        if (!(str.startsWith(BSON.CHAR_LEFT_BIG) && str.endsWith(BSON.CHAR_RIGHT_BIG)))
            return map;
        if (str.equals(BSON.CHAR_LEFT_BIG + BSON.CHAR_RIGHT_BIG)) {
            map.put(BSON.TAG_MSG, BSON.MSG_BSON_EMPTY);
            return map;
        }
        str = str.substring(1, str.length() - 1);
        List<String> bsons = checkArraySize(str);
        if (bsons.size() == 1) {
            String[] strs = formatOneBSON(bsons.get(0));
            if (strs == null)
                return map;
            map.put(BSON.TAG_MSG, BSON.MSG_BSON);
            map.put(BSON.TAG_KEY, strs[0]);
            map.put(BSON.TAG_VALUE, strs[1]);
            return map;
        } else {
            for (int i = 0; i < bsons.size(); i++) {
                String[] strs = formatOneBSON(bsons.get(i));
                if (strs == null)
                    return map;
                map.put(strs[0], getValue(strs[1]).get(BSON.TAG_VALUE));
            }
            map.put(BSON.TAG_MSG, BSON.MSG_BSON_ARRAY);
            return map;
        }

    }

    // 分解ObjectArray
    private List<String> checkArraySize(String str) {
        List<String> array = new ArrayList<String>();
        String subStr;
        int flag = 0;
        int start = 0;
        boolean add = true;
        for (int i = 0; i < str.length(); i++) {
            String s = str.substring(i, i + 1);
            if (s.equals(BSON.CHAR_QUOTES)) {
                if (add) {
                    flag++;
                    add = !add;
                } else {
                    flag--;
                    add = !add;
                }
            } else if (s.equals(BSON.CHAR_LEFT_BIG) || s.equals(BSON.CHAR_LEFT_MID)) {
                flag++;
            } else if (s.equals(BSON.CHAR_RIGHT_BIG) || s.equals(BSON.CHAR_RIGHT_MID)) {
                flag--;
            } else if (s.equals(BSON.CHAR_COMMA)) {
                if (flag == 0) {
                    subStr = str.substring(start, i);
                    start = i + 1;
                    array.add(subStr.trim());
                }
            }

        }
        array.add(str.substring(start, str.length()));
        return array;
    }

    // 传入格式 "xxx":xxx
    // 回传String[0] = key , String[1] = value
    private String[] formatOneBSON(String str) {
        str = str.trim();
        if (!str.startsWith(BSON.CHAR_QUOTES))
            return null;
        str = str.substring(1);
        int end = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.substring(i, i + 1).equals(BSON.CHAR_QUOTES)) {
                end = i;
                break;
            }
        }
        String tag = str.substring(0, end);
        str = str.substring(end + 1).trim();
        if (!str.startsWith(BSON.CHAR_RISK))
            return null;
        String value = str.substring(1);
        String[] result = { tag, value };
        return result;
    }

    public LinkedHashMap<String, Object> getValue(String str) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        int type;
        str = str.trim();
        if (str.startsWith(BSON.CHAR_LEFT_MID) && str.endsWith(BSON.CHAR_RIGHT_MID)) {
            // Object[]
            type = BSON.TYPE_ARRAY;
            List<String> array = checkArraySize(str.substring(1, str.length() - 1));
            Object[] objArray = new Object[array.size()];
            for (int i = 0; i < array.size(); i++) {
                LinkedHashMap<String, Object> tMap = getValue(array.get(i));
                objArray[i] = tMap.get(BSON.TAG_VALUE);
            }
            map.put(BSON.TAG_VALUE, objArray);
        } else if (str.startsWith(BSON.CHAR_QUOTES) && str.endsWith(BSON.CHAR_QUOTES)) {
            type = BSON.TYPE_STRING;
            map.put(BSON.TAG_VALUE, str.substring(1, str.length() - 1));
        } else if (str.startsWith(BSON.CHAR_LEFT_BIG) && str.endsWith(BSON.CHAR_RIGHT_BIG)) {
            // BSON or JSON or Date or Object
            map = analyseObject(str, map);
            type = (Integer) map.get(BSON.TAG_TYPE);
            map.put(BSON.TAG_VALUE, map.get(BSON.TAG_VALUE));
        } else if (str.toLowerCase(Locale.getDefault()).equals("true") || str.toLowerCase(Locale.getDefault()).equals("false")) {
            type = BSON.TYPE_BOOLEAN;
            map.put(BSON.TAG_VALUE, Boolean.valueOf(str));
        } else if (str.contains(BSON.CHAR_POINT) || str.contains("e") || str.contains("E")) {
            try {
                if (Float.valueOf(str).isInfinite()) {
                    type = BSON.TYPE_DOUBLE;
                    map.put(BSON.TAG_VALUE, Double.valueOf(str));
                } else {
                    type = BSON.TYPE_FLOAT;
                    map.put(BSON.TAG_VALUE, Float.valueOf(str));
                }
            } catch (Exception e) {
                type = BSON.TYPE_UNKNOW;
                map.put(BSON.TAG_VALUE, str);
            }

        } else if (true) {
            try {
                Integer.valueOf(str);
                type = BSON.TYPE_INTEGER;
                map.put(BSON.TAG_VALUE, Integer.valueOf(str));
            } catch (Exception e) {
                try {
                    type = BSON.TYPE_LONG;
                    map.put(BSON.TAG_VALUE, Long.valueOf(str));
                } catch (Exception ee) {
                    type = BSON.TYPE_UNKNOW;
                    map.put(BSON.TAG_VALUE, str);
                }
            }
        }

        map.put(BSON.TAG_TYPE, type);
        return map;
    }

    private LinkedHashMap<String, Object> analyseObject(String str, LinkedHashMap<String, Object> map) {
        LinkedHashMap<String, Object> oMap = getBSON(str);
        String msg = (String) oMap.get(BSON.TAG_MSG);
        if (msg.equals(BSON.MSG_BSON_ARRAY)) {
            map.put(BSON.TAG_TYPE, BSON.TYPE_BSON_ARRAY);
            BSONObject bson = new BSONObject();
            oMap.remove(BSON.TAG_MSG);
            Iterator<Entry<String, Object>> iter = oMap.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, Object> entry = (Entry<String, Object>) iter.next();
                bson.put(entry.getKey(), entry.getValue());
            }
            map.put(BSON.TAG_VALUE, bson);
            map.put(BSON.TAG_TYPE, BSON.TYPE_BSON_ARRAY);

            return map;
        } else if (msg.equals(BSON.MSG_BSON_EMPTY)) {
            map.put(BSON.TAG_TYPE, BSON.TYPE_BSON);
            map.put(BSON.TAG_VALUE, new BSONObject());
            return map;
        }
        String tag = (String) oMap.get(BSON.TAG_KEY);
        if (!tag.equals(BSON.MSG_ERROR_EMPTY) && !tag.equals(BSON.MSG_ERROR_NOTBSON)) {
            map.put(BSON.TAG_TYPE, BSON.TYPE_BSON);
            BSONObject bson = new BSONObject();
            bson.put(tag, getValue((String) oMap.get(BSON.TAG_VALUE)).get(BSON.TAG_VALUE));
            map.put(BSON.TAG_VALUE, bson);
        } else {
            map.put(BSON.TAG_TYPE, BSON.TYPE_OBJECT);
            map.put(BSON.TAG_VALUE, str);
        }
        return map;
    }

}
