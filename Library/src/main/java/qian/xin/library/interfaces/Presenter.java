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

package qian.xin.library.interfaces;

import android.content.pm.PackageManager;

import java.io.IOException;
import java.net.MalformedURLException;

/*Activity和Fragment的公共逻辑接口
 * @author Lemon
 * @use Activity或Fragment implements Presenter
 */
public interface Presenter {

	String INTENT_TITLE = "INTENT_TITLE";
	String INTENT_ID = "INTENT_ID";
	String INTENT_TYPE = "INTENT_TYPE";
	String INTENT_PHONE = "INTENT_PHONE";
	String INTENT_PASSWORD = "INTENT_PASSWORD";
	String INTENT_VERIFY = "INTENT_VERIFY";
	String INTENT_USER_ID = "INTENT_USER_ID";
	String RESULT_DATA = "RESULT_DATA";
	String ACTION_EXIT_APP = "ACTION_EXIT_APP";

	/*
	 * UI显示方法(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)
	 * @must Activity-在子类onCreate方法内初始化View(setContentView)后调用；Fragment-在子类onCreateView方法内初始化View后调用
	 */
	void initView();
	/*
	 * Data数据方法(存在数据获取或处理代码，但不存在事件监听代码)
	 * @must Activity-在子类onCreate方法内初始化View(setContentView)后调用；Fragment-在子类onCreateView方法内初始化View后调用
	 */
	void initData() throws PackageManager.NameNotFoundException;
	/*
	 * Event事件方法(只要存在事件监听代码就是)
	 * @must Activity-在子类onCreate方法内初始化View(setContentView)后调用；Fragment-在子类onCreateView方法内初始化View后调用
	 */
	void initEvent() throws MalformedURLException, IOException;


	/*
	 * 是否存活(已启动且未被销毁)
	 */
	boolean isAlive();
	/**
	 * 是否在运行
	 */
	boolean isRunning();
}