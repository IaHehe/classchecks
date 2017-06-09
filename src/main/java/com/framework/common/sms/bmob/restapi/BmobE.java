/**
 * 
 * Bmob移动后端云服务RestAPI工具扩展类
 * 
 * 依赖BSON提供简单的RestAPI增删改查工具，可直接对表、云函数、支付订单、消息推送进行操作(返回BSONObject)。
 * 使用方法：先初始化initBmob，后调用其他方法即可。
 * 具体使用方法及传参格式详见Bmob官网RestAPI开发文档。
 * http://docs.bmob.cn/restful/developdoc/index.html?menukey=develop_doc&key=develop_restful
 * 
 * @author 金鹰
 * @version V1.0
 * @since 2015-07-07
 * 
 */
package com.framework.common.sms.bmob.restapi;

import com.framework.common.sms.bmob.bson.BSON;
import com.framework.common.sms.bmob.bson.BSONObject;

public class BmobE {
    /**
     * 初始化Bmob
     * 
     * @param appId 填写 Application ID
     * @param apiKey 填写 REST API Key
     * @return 注册结果
     */
    public static boolean initBmob(String appId, String apiKey) {
        return initBmob(appId, apiKey, 10000);
    }

    /**
     * 初始化Bmob
     * 
     * @param appId 填写 Application ID
     * @param apiKey 填写 REST API Key
     * @param timeout 设置超时（1000~20000ms）
     * @return 注册结果
     */
    public static boolean initBmob(String appId, String apiKey, int timeout) {
        return Bmob.initBmob(appId, apiKey, timeout);
    }

    /**
     * 初始化Bmob Master权限
     * 
     * @param masterKey 填写 Master Key
     */
    public static void initMaster(String masterKey) {
        Bmob.initMaster(masterKey);
    }

    /**
     * 查询表全部记录(最多仅查询1000条记录)
     * 
     * @param tableName 表名
     * @return BSONObject
     */
    public static BSONObject findAll(String tableName) {
        return new BSONObject(find(tableName, BSON.CHAR_NULL));
    }

    /**
     * 条件查询表全部记录(最多仅查询1000条记录)
     * 
     * @param tableName 表名
     * @param where 条件JOSN格式
     * @return BSONObject
     */
    public static BSONObject findAll(String tableName, BSONObject where) {
        return find(tableName, where, BSON.CHAR_NULL);
    }

    /**
     * 查询表单条记录
     * 
     * @param tableName 表名
     * @param objectId objectId
     * @return BSONObject
     */
    public static BSONObject findOne(String tableName, String objectId) {
        return resultForBSONObject(Bmob.findOne(tableName, objectId));
    }

    /**
     * 查询表限定数量记录
     * 
     * @param tableName 表名
     * @param limit 查询记录数（1~1000）
     * @return BSONObject
     */
    public static BSONObject find(String tableName, int limit) {
        return find(tableName, new BSONObject(), 0, limit, BSON.CHAR_NULL);
    }

    /**
     * 条件查询表限定数量记录
     * 
     * @param tableName 表名
     * @param where 条件JOSN格式
     * @param limit 查询记录数（1~1000）
     * @return BSONObject
     */
    public static BSONObject find(String tableName, BSONObject where, int limit) {
        return find(tableName, where, 0, limit, BSON.CHAR_NULL);
    }

    /**
     * 条件查询表限定数量记录，返回指定列
     * 
     * @param tableName 表名
     * @param keys 返回列 （例：score,name）
     * @param where 条件JOSN格式
     * @param limit 查询记录数（1~1000）
     * @return BSONObject
     */
    public static BSONObject findColumns(String tableName, String keys, BSONObject where, int limit) {
        return findColumns(tableName, keys, where, 0, limit, BSON.CHAR_NULL);
    }

    /**
     * 查询表区间记录
     * 
     * @param tableName 表名
     * @param skip 跳过记录数
     * @param limit 查询记录数（1~1000）
     * @return BSONObject
     */
    public static BSONObject find(String tableName, int skip, int limit) {
        return find(tableName, new BSONObject(), skip, limit, BSON.CHAR_NULL);
    }

    /**
     * 条件查询表区间记录
     * 
     * @param tableName 表名
     * @param where 条件JOSN格式
     * @param skip 跳过记录数
     * @param limit 查询记录数（1~1000）
     * @return BSONObject
     */
    public static BSONObject find(String tableName, BSONObject where, int skip, int limit) {
        return find(tableName, where, skip, limit, BSON.CHAR_NULL);
    }

    /**
     * 条件查询表区间记录,返回指定列
     * 
     * @param tableName 表名
     * @param keys 返回列 （例：score,name）
     * @param where 条件JOSN格式
     * @param skip 跳过记录数
     * @param limit 查询记录数（1~1000）
     * @return BSONObject
     */
    public static BSONObject findColumns(String tableName, String keys, BSONObject where, int skip, int limit) {
        return findColumns(tableName, keys, where, skip, limit, BSON.CHAR_NULL);
    }

    /**
     * 排序查询表记录
     * 
     * @param tableName 表名
     * @param order 排序字段（例：score,-name）
     * @return BSONObject
     */
    public static BSONObject find(String tableName, String order) {
        return find(tableName, new BSONObject(), 0, 1000, order);
    }

    /**
     * 条件排序查询表记录
     * 
     * @param tableName 表名
     * @param where 条件JOSN格式
     * @param order 排序字段（例：score,-name）
     * @return BSONObject
     */
    public static BSONObject find(String tableName, BSONObject where, String order) {
        return find(tableName, where, 0, 1000, order);
    }

    /**
     * 条件排序查询表记录,返回指定列
     * 
     * @param tableName 表名
     * @param keys 返回列 （例：score,name）
     * @param where 条件JOSN格式
     * @param order 排序字段（例：score,-name）
     * @return BSONObject
     */
    public static BSONObject findColumns(String tableName, String keys, BSONObject where, String order) {
        return findColumns(tableName, keys, where, 0, 1000, order);
    }

    /**
     * 排序查询表限定数量记录
     * 
     * @param tableName 表名
     * @param limit 查询记录数（1~1000）
     * @param order 排序字段（例：score,-name）
     * @return BSONObject
     */
    public static BSONObject find(String tableName, int limit, String order) {
        return find(tableName, new BSONObject(), 0, limit, order);
    }

    /**
     * 条件排序查询表限定数量记录
     * 
     * @param tableName 表名
     * @param where 条件JOSN格式
     * @param limit 查询记录数（1~1000）
     * @param order 排序字段（例：score,-name）
     * @return BSONObject
     */
    public static BSONObject find(String tableName, BSONObject where, int limit, String order) {
        return find(tableName, where, 0, limit, order);
    }

    /**
     * 条件排序查询表限定数量记录,返回指定列
     * 
     * @param tableName 表名
     * @param keys 返回列 （例：score,name）
     * @param where 条件JOSN格式
     * @param limit 查询记录数（1~1000）
     * @param order 排序字段（例：score,-name）
     * @return BSONObject
     */
    public static BSONObject findColumns(String tableName, String keys, BSONObject where, int limit, String order) {
        return findColumns(tableName, keys, where, 0, limit, order);
    }

    /**
     * 条件排序查询表区间记录
     * 
     * @param tableName 表名
     * @param where 条件JOSN格式
     * @param skip 跳过记录数
     * @param limit 查询记录数（1~1000）
     * @param order 排序字段（例：score,-name）
     * @return BSONObject
     */
    public static BSONObject find(String tableName, BSONObject where, int skip, int limit, String order) {
        return findColumns(tableName, BSON.CHAR_NULL, where, skip, limit, order);
    }

    /**
     * 条件排序查询表区间记录,返回指定列
     * 
     * @param tableName 表名
     * @param keys 返回列 （例：score,name）
     * @param where 条件JOSN格式
     * @param skip 跳过记录数
     * @param limit 查询记录数（1~1000）
     * @param order 排序字段（例：score,-name）
     * @return BSONObject
     */
    public static BSONObject findColumns(String tableName, String keys, BSONObject where, int skip, int limit, String order) {
        return resultForBSONObject(Bmob.findColumns(tableName, keys, where.toString(), skip, limit, order));
    }

    /**
     * BQL查询表记录
     * 
     * @param BQL SQL语句。例如：select * from Student where name=\"张三\" limit 0,10 order by name
     * @return BSONObject
     */
    public static BSONObject findBQL(String BQL) {
        return findBQL(BQL, BSON.CHAR_NULL);
    }

    /**
     * BQL查询表记录
     * 
     * @param BQL SQL语句。例如：select * from Student where name=? limit ?,? order by name
     * @param value 参数对应SQL中?以,为分隔符。例如"\"张三\",0,10"
     * @return BSONObject
     */
    public static BSONObject findBQL(String BQL, String value) {
        return resultForBSONObject(Bmob.findBQL(BQL, value));
    }

    /**
     * 获取服务器时间
     * 
     * @return BSONObject
     */
    public static BSONObject getServerTime() {
        return resultForBSONObject(Bmob.getServerTime());
    }

    /**
     * 查询表记录数
     * 
     * @param tableName 表名
     * @return 统计值
     */
    public static int count(String tableName) {
        return count(tableName, new BSONObject());
    }

    /**
     * 条件查询记录数
     * 
     * @param tableName 表名
     * @param where 查询条件(BSONObject)
     * @return 统计值
     */
    public static int count(String tableName, BSONObject where) {
        return Bmob.count(tableName, where.toString());
    }

    /**
     * 修改记录
     * 
     * @param tableName 表名
     * @param objectId objectId
     * @param paramContent BSONObject
     * @return BSONObject
     */
    public static BSONObject update(String tableName, String objectId, BSONObject paramContent) {
        return resultForBSONObject(Bmob.update(tableName, objectId, paramContent.toString()));
    }

    /**
     * 插入记录
     * 
     * @param tableName 表名
     * @param paramContent BSONObject
     * @return BSONObject
     */
    public static BSONObject insert(String tableName, BSONObject paramContent) {
        return resultForBSONObject(Bmob.insert(tableName, paramContent.toString()));
    }

    /**
     * 删除记录
     * 
     * @param tableName 表名
     * @param objectId objectId
     * @return BSONObject
     */
    public static BSONObject delete(String tableName, String objectId) {
        return resultForBSONObject(Bmob.delete(tableName, objectId));
    }

    /**
     * 查询支付订单
     * 
     * @param payId 交易编号
     * @return BSONObject
     */
    public static BSONObject findPayOrder(String payId) {
        return resultForBSONObject(Bmob.findPayOrder(payId));
    }

    /**
     * 推送消息
     * 
     * @param data BSONObject 详细使用方法参照
     *            http://docs.bmob.cn/restful/developdoc/index.html?menukey=develop_doc&key=develop_restful#index_消息推送简介
     * @return BSONObject
     */
    public static BSONObject pushMsg(BSONObject data) {
        return resultForBSONObject(Bmob.pushMsg(data.toString()));
    }

    /**
     * 调用云端代码
     * 
     * @param funcName 云函数名
     * @param paramContent BSONObject格式参数
     * @return BSONObject
     */
    public static BSONObject callFunction(String funcName, BSONObject paramContent) {
        return resultForBSONObject(Bmob.callFunction(funcName, paramContent.toString()));
    }

    /**
     * 复合查询-或
     * 
     * @param where1 BSONObject条件一
     * @param where2 BSONObject条件二
     * @return 复合或BSONObject
     */
    public static BSONObject whereOr(BSONObject where1, BSONObject where2) {
        return whereForBSONObject(Bmob.whereOr(where1.toString(), where2.toString()));
    }

    /**
     * 复合查询-与
     * 
     * @param where1 BSONObject条件一
     * @param where2 BSONObject条件二
     * @return 复合与BSONObject
     */
    public static BSONObject whereAnd(BSONObject where1, BSONObject where2) {
        return whereForBSONObject(Bmob.whereAnd(where1.toString(), where2.toString()));
    }

    /**
     * 操作符-小于
     * 
     * @param value 目标值
     * @return 复合小于BSONObject
     */
    public static BSONObject whereLess(int value) {
        return whereForBSONObject(Bmob.whereLess(value));
    }

    /**
     * 操作符-小于
     * 
     * @param value 支持日期类型BSONObject
     * @return 复合小于BSONObject
     */
    public static BSONObject whereLess(BSONObject value) {
        return whereForBSONObject(Bmob.whereLess(value.toString()));
    }

    /**
     * 操作符-小于等于
     * 
     * @param value 目标值
     * @return 复合小于等于BSONObject
     */
    public static BSONObject whereLessEqual(int value) {
        return whereForBSONObject(Bmob.whereLessEqual(value));
    }

    /**
     * 操作符-小于等于
     * 
     * @param value 支持日期类型BSONObject
     * @return 复合小于等于BSONObject
     */
    public static BSONObject whereLessEqual(BSONObject value) {
        return whereForBSONObject(Bmob.whereLessEqual(value.toString()));
    }

    /**
     * 操作符-大于
     * 
     * @param value 目标值
     * @return 复合大于BSONObject
     */
    public static BSONObject whereGreate(int value) {
        return whereForBSONObject(Bmob.whereGreate(value));
    }

    /**
     * 操作符-大于
     * 
     * @param value 支持日期类型BSONObject
     * @return 复合大于BSONObject
     */
    public static BSONObject whereGreate(BSONObject value) {
        return whereForBSONObject(Bmob.whereGreate(value.toString()));
    }

    /**
     * 操作符-大于等于
     * 
     * @param value 目标值
     * @return 复合大于等于BSONObject
     */
    public static BSONObject whereGreateEqual(int value) {
        return whereForBSONObject(Bmob.whereGreateEqual(value));
    }

    /**
     * 操作符-大于等于
     * 
     * @param value 支持日期类型BSONObject
     * @return 复合大于等于BSONObject
     */
    public static BSONObject whereGreateEqual(BSONObject value) {
        return whereForBSONObject(Bmob.whereGreateEqual(value.toString()));
    }

    /**
     * 操作符-不等于
     * 
     * @param value 目标值
     * @return 复合不等于BSONObject
     */
    public static BSONObject whereNotEqual(int value) {
        return whereForBSONObject(Bmob.whereNotEqual(value));
    }

    /**
     * 操作符-不等于
     * 
     * @param value 支持日期类型BSONObject
     * @return 复合不等于BSONObject
     */
    public static BSONObject whereNotEqual(BSONObject value) {
        return whereForBSONObject(Bmob.whereNotEqual(value.toString()));
    }

    /**
     * 操作符-包含
     * 
     * @param value 目标数组值(例：new int[]{1,3,5,7})
     * @return 复合包含BSONObject
     */
    public static BSONObject whereIn(int[] value) {
        return whereForBSONObject(Bmob.whereIn(value));
    }

    /**
     * 操作符-包含
     * 
     * @param value 目标数组值(例：new String[]{"张三","李四","王五"})
     * @return 复合包含BSONObject
     */
    public static BSONObject whereIn(String[] value) {
        return whereForBSONObject(Bmob.whereIn(value));
    }

    /**
     * 操作符-包含
     * 
     * @param value 目标数组值(例："1,3,5,7")
     * @return 复合包含BSONObject
     */
    public static BSONObject whereIn(String value) {
        return whereForBSONObject(Bmob.whereIn(value));
    }

    /**
     * 操作符-不包含
     * 
     * @param value 目标数组值(例：new int[]{1,3,5,7})
     * @return 复合不包含BSONObject
     */
    public static BSONObject whereNotIn(int[] value) {
        return whereForBSONObject(Bmob.whereNotIn(value));
    }

    /**
     * 操作符-不包含
     * 
     * @param value 目标数组值(例：new String[]{"张三","李四","王五"})
     * @return 复合不包含BSONObject
     */
    public static BSONObject whereNotIn(String[] value) {
        return whereForBSONObject(Bmob.whereNotIn(value));
    }

    /**
     * 操作符-不包含
     * 
     * @param value 目标数组值(例："\"张三\",\"李四\",\"王五\"")
     * @return 复合不包含BSONObject
     */
    public static BSONObject whereNotIn(String value) {
        return whereForBSONObject(Bmob.whereNotIn(value));
    }

    /**
     * 操作符-存在
     * 
     * @param value 布尔值
     * @return 复合存在BSONObject
     */
    public static BSONObject whereExists(boolean value) {
        return whereForBSONObject(Bmob.whereExists(value));
    }

    /**
     * 操作符-全包含
     * 
     * @param value 目标值
     * @return 复合全包含BSONObject
     */
    public static BSONObject whereAll(String value) {
        return whereForBSONObject(Bmob.whereAll(value));
    }

    /**
     * 操作符-区间包含
     * 
     * @param greatEqual 是否大于包含等于
     * @param greatValue 大于的目标值
     * @param lessEqual 是否小于包含等于
     * @param lessValue 小于的目标值
     * @return 复合区间包含BSONObject 例：查询[1000,3000), whereIncluded(true,1000,false,3000)
     */
    public static BSONObject whereIncluded(boolean greatEqual, int greatValue, boolean lessEqual, int lessValue) {
        return whereIncluded(greatEqual, String.valueOf(greatValue), lessEqual, String.valueOf(lessValue));
    }

    /**
     * 操作符-区间包含
     * 
     * @param greatEqual 是否大于包含等于
     * @param greatValue 大于的目标值
     * @param lessEqual 是否小于包含等于
     * @param lessValue 小于的目标值
     * @return 复合区间包含BSONObject 例：查询[1000,3000), whereIncluded(true,"1000",false,"3000")
     */
    public static BSONObject whereIncluded(boolean greatEqual, String greatValue, boolean lessEqual, String lessValue) {
        return whereForBSONObject(Bmob.whereIncluded(greatEqual, greatValue, lessEqual, lessValue));
    }

    /**
     * 操作符-正则表达式
     * 
     * @param regexValue
     * @return 复合正则表达式BSONObject
     */
    public static BSONObject whereRegex(String regexValue) {
        return whereForBSONObject(Bmob.whereRegex(regexValue));
    }

    public static int getTimeout() {
        return Bmob.getTimeout();
    }

    public static void setTimeout(int timeout) {
        Bmob.setTimeout(timeout);
    }

    // 查询结果转换成BSONObject
    // 结果可能返回null
    private static BSONObject resultForBSONObject(String result) {
        BSONObject bson = null;
        if (result.equals(Bmob.MSG_UNREGISTERED) || result.contains(Bmob.MSG_NOT_FOUND) || result.contains(Bmob.MSG_ERROR)) {
            BSON.Warn(result);
        } else {
            bson = new BSONObject(result);
        }
        return bson;
    }

    // 条件转换成BSONObject
    private static BSONObject whereForBSONObject(String where) {
        BSONObject bson = null;
        bson = new BSONObject(where);
        return bson;
    }

}
