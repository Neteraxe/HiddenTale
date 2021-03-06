/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package qian.xin.library.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import qian.xin.library.base.BaseApplication;
import qian.xin.library.util.DataKeeper;
import qian.xin.library.util.Log;
import qian.xin.library.util.StringUtil;

import static qian.xin.library.util.JSON.parseArray;
import static qian.xin.library.util.JSON.toJSONString;
import static qian.xin.library.util.StringUtil.isNotEmpty;

/*磁盘缓存管理类
 * @author Lemon
 * @use CacheManager.getInstance().xxxMethod(...);具体参考.BaseListActivity
 */
public class CacheManager {
    private static final String TAG = "CacheManager";

    public static final String CACHE_PATH = DataKeeper.ROOT_SHARE_PREFS_ + "CACHE_PATH";

    public Context context;

    private CacheManager(Context context) {
        this.context = context;
    }

    @SuppressLint("StaticFieldLeak")
    private static CacheManager instance;

    public static CacheManager getInstance() {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    instance = new CacheManager(BaseApplication.getInstance());
                }
            }
        }
        return instance;
    }


    /*
     * @param clazz
     * @return
     */
    public <T> String getClassPath(Class<T> clazz) {
        return clazz == null ? null : CACHE_PATH + clazz.getName();
    }

    /*
     * @param clazz
     * @return
     */
    public <T> String getListPath(Class<T> clazz) {
        String classPath = getClassPath(clazz);
        return isNotEmpty(classPath, true) ? classPath + KEY_LIST : null;
    }

    private SharedPreferences getSharedPreferences(String path) {
        return !isNotEmpty(path, true)
                ? null : context.getSharedPreferences(StringUtil.getTrimedString(path), Context.MODE_PRIVATE);
    }


    /*
     * 数据列表
     */
    public static final String KEY_LIST = "LIST";

    /*
     * 数据分组,自定义
     */
    public static final String KEY_GROUP = "GROUP";
    /*
     * 分组中列表每页最大数量
     */
    public static final int MAX_PAGE_SIZE = 10;

    /*获取列表
     * @param clazz
     * @param start
     * @return
     */
    public <T> List<T> getList(Class<T> clazz, int start, int pageSize) {
        return getList(clazz, null, start, pageSize);
    }

    /*获取列表
     * @param clazz
     * @param group == null ? all : in group
     * @param start < 0 ? all in group : subList(start, end)
     * @param count > 0 ? all in group : subList(start, end)
     * @return
     */
    public <T> List<T> getList(Class<T> clazz, String group, int start, int count) {
        Log.i(TAG, "\n\n<<<<<<<<<<<<<<<<\ngetList  group = " + group + "; start = " + start + "; count = " + count);
        if (count <= 0 || clazz == null) {
            Log.e(TAG, "getList  count <= 0 || clazz == null >> return null;");
            return null;
        }
        Cache<T> cacheList = new Cache<>(clazz, context, getClassPath(clazz) + KEY_LIST);

        if (!isNotEmpty(group, true)) {
            return cacheList.getValueList(start, start + count);
        }

        List<String> idList = getIdList(clazz, group);
        final int totalCount = idList == null ? 0 : idList.size();
        Log.i(TAG, "getList  idList.size() = " + totalCount);
        if (totalCount <= 0) {
            Log.e(TAG, "getList  totalCount <= 0 >> return null;");
            return null;
        }

        if (start >= 0) {
            Log.i(TAG, "getList  start >= 0 >> ");

            int end = start + count;
            if (end > totalCount) {
                end = totalCount;
            }
            Log.i(TAG, "getList  end = " + end);
            if (end <= start) {
                Log.e(TAG, "getList  end <= start >> return null;");
                return null;
            }

            if (start > 0 || end < totalCount) {
                Log.i(TAG, "getList  start > 0 || end < totalCount  >> idList = idList.subList(" + start + "," + end + "); >>");
                idList = idList.subList(start, end);
            }
        }

        List<T> list = new ArrayList<>();
        T data;
        for (String id : idList) {
            data = cacheList.get(id);
            if (data != null) {
                list.add(data);
            }
        }

        Log.i(TAG, "getList  return list; list.size() = " + list.size() + "\n>>>>>>>>>>>>>>>>>>>>>>\n\n");

        return list;
    }

    /*获取单个值
     * @param clazz
     * @param id
     * @return
     */
    public <T> T get(Class<T> clazz, String id) {
        Cache<T> cacheList = clazz == null
                ? null : new Cache<>(clazz, context, getClassPath(clazz) + KEY_LIST);
        return cacheList == null ? null : cacheList.get(id);
    }


    /*获取id列表
     * @param clazz
     * @param group
     * @return
     */
    public <T> List<String> getIdList(Class<T> clazz, String group) {
        SharedPreferences sp = getSharedPreferences(
                getClassPath(clazz) + KEY_GROUP);
        return sp == null ? null : parseArray(sp.getString(StringUtil.getTrimedString(group), null), String.class);
    }

    /*保存列表
     * @param clazz 类
     * @param group 分组
     * @param map 数据表
     * @param start 存储起始位置,[start, start + map.size()]中原有的将被替换. start = start < 0 ? idList.size() : start;
     * @param pageSize 每页大小
     */
    @SuppressLint("ApplySharedPref")
    public <T> void saveList(Class<T> clazz, String group, LinkedHashMap<String, T> map, int start, int pageSize) {
        Log.i(TAG, "\n\n <<<<<<<<<<<<<<<<<\nsaveList  group = " + group + "; start = " + start + "; pageSize = " + pageSize);
        if (clazz == null || map == null || map.size() <= 0) {
            Log.e(TAG, "saveList  clazz == null || map == null || map.size() <= 0 >> return;");
            return;
        }
        final String CLASS_PATH = getClassPath(clazz);

        if (isNotEmpty(group, true)) {
            group = StringUtil.getTrimedString(group);

            Log.i(TAG, "saveList  group = " + group + "; map.size() = " + map.size()
                    + "; start = " + start + "; pageSize = " + pageSize);
            List<String> newIdList = new ArrayList<>(map.keySet());//用String而不是Long，因为订单Order的id超出Long的最大值

            Log.i(TAG, "saveList newIdList.size() = " + newIdList.size() + "; start save <<<<<<<<<<<<<<<<<\n ");


            //保存至分组<<<<<<<<<<<<<<<<<<<<<<<<<
            SharedPreferences sp = getSharedPreferences(CLASS_PATH + KEY_GROUP);
            //			sp.edit().putString(KEY_GROUP, group);
            assert sp != null;
            Editor editor = Objects.requireNonNull(sp).edit();

            Log.i(TAG, "\n saveList pageSize = " + pageSize + " <<<<<<<<");
            //列表每页大小
            if (pageSize > 0) {
                if (pageSize > MAX_PAGE_SIZE) {
                    pageSize = MAX_PAGE_SIZE;
                }
            }
            Log.i(TAG, "\n saveList pageSize = " + pageSize + ">>>>>>>>>");

            //id列表
            List<String> idList = parseArray(sp.getString(group, null), String.class);
            if (idList == null) {
                idList = new ArrayList<>();
            }
            if (start < 0) {
                start = idList.size();
            }
            Log.i(TAG, "\n saveList idList.size() = " + idList.size() + " <<<<<<<<");
            String id;
            for (int i = start; i < start + newIdList.size(); i++) {
                id = newIdList.get(i - start);
                if (id == null || id.isEmpty()) {
                    continue;
                }
                idList.remove(id);//位置发生变化
                if (i < idList.size()) {
                    idList.set(i, id);
                } else {
                    idList.add(id);
                }
            }
            editor.remove(group);
            editor.putString(group, toJSONString(idList));
            editor.apply();

            Log.i(TAG, "\n saveList idList.size() = " + idList.size() + " >>>>>>>>");
        }

        //保存至分组>>>>>>>>>>>>>>>>>>>>>>>>>


        //保存所有数据<<<<<<<<<<<<<<<<<<<<<<<<<
        Cache<T> cache = new Cache<>(clazz, context, CLASS_PATH + KEY_LIST);
        cache.saveList(map);
        //保存所有数据>>>>>>>>>>>>>>>>>>>>>>>>>

        Log.i(TAG, "saveList cache.getSize() = " + cache.getSize() + "; end save \n>>>>>>>>>>>>>>>>>> \n\n");
        //		}

    }

    public <T> void remove(Class<T> clazz, String id) {
        if (clazz == null) {
            Log.e(TAG, "remove  clazz == null >> return;");
            return;
        }
        new Cache<>(clazz, context, getListPath(clazz)).remove(id);
    }

}
