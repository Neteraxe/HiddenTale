package qian.xin.library.base;

import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import qian.xin.library.util.CommonUtil;
import qian.xin.library.util.Log;


/*
 * 基础自定义View，适合任何View
 * <br /> 可以用于Adapter内的ItemView，也可以单独作为一个组件使用
 *
 * @param <T> 数据模型(model/JavaBean)类
 * @author Lemon
 * @use extends BaseView<T>, 具体参考.DemoView
 * @see #onDataChangedListener
 * @see #onDestroy
 */
public abstract class BaseView<T> extends RecyclerView.ViewHolder {
    private static final String TAG = "BaseView";

    /*
     * 点击事件
     */
    public interface OnViewClickListener {
    }

    /*
     * 数据改变回调接口
     * (Object) getData() - 改变的数据
     */
    public interface OnDataChangedListener {
    }

    /*
     * 需要在BaseView子类手动回调 onViewClickListener.onViewClick(...)
     */
    public OnViewClickListener onViewClickListener;//数据改变回调监听回调的实例

    public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
    }

    /*
     * 需要在BaseView子类手动回调 onViewClickListener.onDataChanged()
     */
    protected OnDataChangedListener onDataChangedListener;//数据改变回调监听回调的实例


    /*
     * @param context
     * @param layoutResId
     * @see #BaseView(AppCompatActivity, int, ViewGroup)
     */
    public BaseView(AppCompatActivity context, @LayoutRes int layoutResId) {
        this(context, layoutResId, null);
    }

    /*
     * @param context
     * @param layoutResId
     * @param parent      TODO 如果itemView不能占满宽度 或 高度不对，一般是RecyclerView的问题，可通过传parent解决
     */
    public BaseView(AppCompatActivity context, @LayoutRes int layoutResId, ViewGroup parent) {
        this(context, context.getLayoutInflater().inflate(layoutResId, parent, false));
    }

    /*
     * 传入的Activity,可在子类直接使用
     */
    public final AppCompatActivity context;

    /*
     * @param context
     * @param itemView
     */
    public BaseView(AppCompatActivity context, View itemView) {
        super(itemView);
        this.context = context;
    }


    /*
     * 通过id查找并获取控件，使用时不需要强转
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public <V extends View> V findView(int id) {
        return (V) itemView.findViewById(id);
    }

    /*
     * 通过id查找并获取控件，使用时不需要强转
     *
     * @param id
     * @return
     */
    public <V extends View> V findViewById(int id) {
        return findView(id);
    }

    /*
     * 通过id查找并获取控件，并setOnClickListener
     *
     * @param id
     * @param listener
     * @return
     */
    public <V extends View> V findView(int id, OnClickListener listener) {
        V v = findView(id);
        v.setOnClickListener(listener);
        return v;
    }

    /*
     * 通过id查找并获取控件，并setOnClickListener
     *
     * @param id
     * @param listener
     * @return
     */
    public <V extends View> V findViewById(int id, OnClickListener listener) {
        return findView(id, listener);
    }

    public T data = null;
    /*
     * data在列表中的位置
     *
     * @must 只使用bindView(int position, T data)方法来设置position，保证position与data对应正确
     */
    public int position = 0;
    /*
     * 视图类型，部分情况下需要根据viewType使用不同layout，对应Adapter的itemViewType
     */
    public int viewType = 0;
    /*
     * 是否已被选中
     */
    public boolean selected = false;

    /*
     * 创建一个新的View
     *
     * @return
     */
    public View createView() {
        return itemView;
    }

    /*
     * 获取itemView的宽度
     *
     * @return
     * @warn 只能在createView后使用
     */
    public int getWidth() {
        return itemView.getWidth();
    }


    /*
     * 设置并显示内容，建议在子类bindView内this.data = data;
     *
     * @param data     - 传入的数据
     * @param position - data在列表中的位置
     * @param viewType - 视图类型，部分情况下需要根据viewType使用不同layout
     * @warn 只能在createView后使用
     */
    public void bindView(T data, int position, int viewType) {
        this.position = position;
        this.viewType = viewType;
        bindView(data);
    }

    /*
     * 设置并显示内容，建议在子类bindView内this.data = data;
     *
     * @param data_ - 传入的数据
     * @warn 只能在createView后使用
     */
    public void bindView(T data_) {
        if (data_ == null) {
            Log.w(TAG, "bindView data_ == null");
        }
        this.data = data_;

        //不一定要用单选功能，实现也不一定要用这种方式，这里写会影响所有BaseView子类的性能，子类写更好 itemView.setSelected(selected);
    }

    /*
     * 设置可见性
     *
     * @param visibility - 可见性 (View.VISIBLE, View.GONE, View.INVISIBLE);
     * @warn 只能在createView后使用
     */
    public void setVisibility(int visibility) {
        itemView.setVisibility(visibility);
    }


    /*
     * 设置背景
     *
     * @param resId
     * @warn 只能在createView后使用
     */
    public void setBackground(int resId) {
        itemView.setBackgroundResource(resId);
    }


    //	/*性能不好
    //	 * @param id
    //	 * @param s
    //	 */
    //	public void setText(int id, String s) {
    //		TextView tv = (TextView) findViewById(id);
    //		tv.setText(s);
    //	}


    //resources方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    public Resources resources;

    public final Resources getResources() {
        if (resources == null) {
            resources = context.getResources();
        }
        return resources;
    }

    public String getString(int id) {
        return getResources().getString(id);
    }

    public float getDimension(int id) {
        return getResources().getDimension(id);
    }
    //resources方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //show short toast 方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //show short toast 方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //启动新Activity方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /*
     * 打开新的Activity，向左滑入效果
     *
     * @param intent
     */
    public void toActivity(final Intent intent) {
        CommonUtil.toActivity(context, intent);
    }

    /*
     * 打开新的Activity
     *
     * @param intent
     * @param showAnimation
     */
    public void toActivity(final Intent intent, final boolean showAnimation) {
        CommonUtil.toActivity(context, intent, showAnimation);
    }

    /*
     * 打开新的Activity，向左滑入效果
     *
     * @param intent
     * @param requestCode
     */
    public void toActivity(final Intent intent, final int requestCode) {
        CommonUtil.toActivity(context, intent, requestCode);
    }

    /*
     * 打开新的Activity
     *
     * @param intent
     * @param requestCode
     * @param showAnimation
     */
    public void toActivity(final Intent intent, final int requestCode, final boolean showAnimation) {
        CommonUtil.toActivity(context, intent, requestCode, showAnimation);
    }
    //启动新Activity方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /*
     * 销毁并回收内存，建议在对应的View占用大量内存时使用
     *
     * @warn 只能在UI线程中调用
     */
    public void onDestroy() {
        try {
            itemView.destroyDrawingCache();
        } catch (Exception e) {
            Log.w(TAG, "onDestroy  try { itemView.destroyDrawingCache();" +
                    " >> } catch (Exception e) {\n" + e.getMessage());
        }

        onDataChangedListener = null;

        data = null;
        position = 0;

    }

}