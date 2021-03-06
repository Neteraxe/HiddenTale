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

package qian.xin.library.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static qian.xin.library.util.StringUtil.isNotEmpty;

/*
 * 数据存储工具类
 *
 */
public class DataKeeper {
    private static final String TAG = "DataKeeper";

    public static final String ROOT_SHARE_PREFS_ = "DEMO_SHARE_PREFS_";

    //文件缓存<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    /*
     *
     */
    public static final String fileRootPath = getSDPath() != null ? (getSDPath() + "/hidden.edu/") : null;
    public static final String accountPath = fileRootPath + "account/";
    public static final String audioPath = fileRootPath + "audio/";
    public static final String videoPath = fileRootPath + "video/";
    public static final String imagePath = fileRootPath + "image/";
    public static final String tempPath = fileRootPath + "temp/";
    public static final String shotPath = fileRootPath + "screenshot/";
    //文件缓存>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //存储文件的类型<<<<<<<<<<<<<<<<<<<<<<<<<
    public static final int TYPE_FILE_TEMP = 0;                                //保存保存临时文件
    public static final int TYPE_FILE_IMAGE = 1;                            //保存图片
    public static final int TYPE_FILE_VIDEO = 2;                            //保存视频
    public static final int TYPE_FILE_AUDIO = 3;                            //保存语音
    public static final int TYPE_FILE_SHOT = 4;                             //保留截屏

    //存储文件的类型>>>>>>>>>>>>>>>>>>>>>>>>>

    //不能实例化
    private DataKeeper() {
    }

    private static Application context;

    //获取context，获取存档数据库引用
    public static void init(Application context_) {
        context = context_;

        Log.i(TAG, "init fileRootPath = " + fileRootPath);

        File froot = new File(fileRootPath);
        if (!froot.exists()) {
            froot.mkdir();
        }
        //判断SD卡存在
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            File file = new File(imagePath);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(videoPath);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(audioPath);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(fileRootPath + accountPath);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(tempPath);
            if (!file.exists()) {
                file.mkdir();
            }
        }
    }


    //*外部存储缓存*

    /*
     * 存储缓存文件 返回文件绝对路径
     *
     * @param file 要存储的文件
     * @param type 文件的类型
     *             IMAGE = "imgae";							//图片
     *             VIDEO = "video";							//视频
     *             VOICE = "voice";							//语音
     *             = "voice";							//语音
     * @return 存储文件的绝对路径名 若SDCard不存在返回null
     */
    public static String storeFile(File file, int type) {

        if (!hasSDCard()) {
            return null;
        }
        String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        byte[] data = null;
        try {
            FileInputStream in = new FileInputStream(file);
            data = new byte[in.available()];
            in.read(data, 0, data.length);
            in.close();
        } catch (IOException e) {
            Log.e(TAG, "storeFile  try { FileInputStream in = new FileInputStream(file); ... >>" +
                    " } catch (IOException e) {\n" + e.getMessage());
        }
        return storeFile(data, suffix, type);
    }

    /*
     * @return 存储文件的绝对路径名 若SDCard不存在返回null
     */
    @SuppressLint("DefaultLocale")
    public static String storeFile(byte[] data, String suffix, int type) {

        if (!hasSDCard()) {
            return null;
        }
        String path = null;
        if (type == TYPE_FILE_IMAGE)
            path = imagePath + "IMG_" + Long.toHexString(System.currentTimeMillis()).toUpperCase() + "." + suffix;
        else if (type == TYPE_FILE_VIDEO)
            path = videoPath + "VIDEO_" + Long.toHexString(System.currentTimeMillis()).toUpperCase() + "." + suffix;
        else if (type == TYPE_FILE_AUDIO)
            path = audioPath + "VOICE_" + Long.toHexString(System.currentTimeMillis()).toUpperCase() + "." + suffix;
        else if (type == TYPE_FILE_TEMP)
            path = tempPath + "TEMP_" + Long.toHexString(System.currentTimeMillis()).toUpperCase() + "." + suffix;
        else if (type == TYPE_FILE_SHOT)
            path = shotPath + "SHOT_" + Long.toHexString(System.currentTimeMillis()).toUpperCase() + "." + suffix;
        try {
            assert path != null;
            FileOutputStream out = new FileOutputStream(path);
            out.write(data, 0, data.length);
            out.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "storeFile  try { FileInputStream in = new FileInputStream(file); ... >>" +
                    " } catch (FileNotFoundException e) {\n" + e.getMessage() + "\n\n >> path = null;");
            path = null;
        } catch (IOException e) {
            Log.e(TAG, "storeFile  try { FileInputStream in = new FileInputStream(file); ... >>" +
                    " } catch (IOException e) {\n" + e.getMessage() + "\n\n >> path = null;");
            path = null;
        }
        return path;
    }


    /*
     * jpg
     *
     * @param fileName
     * @return
     */
    public static String getImageFileCachePath(String fileName) {
        return getFileCachePath(TYPE_FILE_IMAGE, fileName, "jpg");
    }

    /*
     * 获取一个文件缓存的路径
     */
    public static String getFileCachePath(int fileType, String fileName, String formSuffix) {

        switch (fileType) {
            case TYPE_FILE_IMAGE:
                return imagePath + fileName + "." + formSuffix;
            case TYPE_FILE_VIDEO:
                return videoPath + fileName + "." + formSuffix;
            case TYPE_FILE_AUDIO:
                return audioPath + fileName + "." + formSuffix;
            case TYPE_FILE_SHOT:
                return shotPath + fileName + "." + formSuffix;
            default:
                return tempPath + fileName + "." + formSuffix;
        }
    }

    /*
     * 若存在SD 则获取SD卡的路径 不存在则返回null
     */
    public static String getSDPath() {
        File sdDir;
        String path = null;
        //判断sd卡是否存在
        boolean sdCardExist = hasSDCard();
        if (sdCardExist) {
            //获取跟目录
            sdDir = Environment.getExternalStorageDirectory();
            path = sdDir.toString();
        }
        return path;
    }

    /*
     * 判断是否有SD卡
     */
    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    //使用SharedPreferences保存 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /*
     * 使用SharedPreferences保存
     *
     * @param path
     * @param mode
     * @param key
     * @param value
     */
    public static void save(String path, int mode, String key, String value) {
        save(context.getSharedPreferences(path, mode), key, value);
    }

    /*
     * 使用SharedPreferences保存
     *
     * @param sdf
     * @param key
     * @param value
     */
    @SuppressLint("ApplySharedPref")
    public static void save(SharedPreferences sdf, String key, String value) {
        if ((sdf == null) || !isNotEmpty(key, false) || (!isNotEmpty(value, false))) {
            Log.e(TAG, "save sdf == null || \n key = " + key + ";\n value = " + value + "\n >> return;");
            return;
        }
        sdf.edit().remove(key).putString(key, value).apply();
    }

    //使用SharedPreferences保存 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}