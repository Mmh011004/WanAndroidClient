package com.example.jetpackmvvm.base.fragment


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.jetpackmvvm.ext.view.getVmClazz
import com.example.jetpackmvvm.network.manager.NetState
import com.example.jetpackmvvm.network.manager.NetworkStateManager


/**
 * 作者　: mmh
 * 时间　: 2021/10/3
 * 描述　:
 */
abstract class BaseVmFragment<VM : BaseViewModel> : Fragment() {
    private val TAG = "BaseVmFragment"
    private val handler = Handler(Looper.getMainLooper())

    //是否第一次加载
    private var isFirst = true

    lateinit var mViewModel: BaseViewModel

    lateinit var mActivity: AppCompatActivity

    /*
    * 当前fragment所绑定的视图布局
    * */
    abstract fun layoutId():Int


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId(),container,false)
    }

    /*
    * 这是当fragment与activity建立联系的时候调用
    * 这是当在fragment中使用context时通常需要getActivity的方式来获取
    * fragment的生命周期第一步onAttach
    * */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }


    /*
    * onViewCreated在onCreateView执行完后立即执行。
    * 注意区分onCreateView 和 onViewCreated */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirst = true
        mViewModel = createViewModel()
        initView(savedInstanceState)
        createObserver()
        registerDefUIChange()
        initData()
    }

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            // 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿
            handler.postDelayed( {
                lazyLoadData()
                //在Fragment中，只有懒加载过了才能开启网络变化监听
                NetworkStateManager.instance.mNetworkStateCallback.observeInFragment(
                    this,
                    Observer {
                        //不是首次订阅时调用方法，防止数据第一次监听错误
                        if (!isFirst) {
                            onNetworkStateChanged(it)
                        }
                    })
                isFirst = false
            },lazyLoadTime())
        }
    }

    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: NetState) {}

    open fun initData(){}

    abstract fun showLoading(message: String = "请求网络中...")

    abstract fun dismissLoading()

    /*
    * 注册UI事件*/
    private fun registerDefUIChange(){
        mViewModel.loadingChange.showDialog.observeInFragment(this, Observer {
            showLoading(it)
        })
        mViewModel.loadingChange.dismissDialog.observeInFragment(this, Observer {
            dismissLoading()
        })
    }

    /*
    * 创建观察者
    * */
    abstract fun createObserver()

    /*
    * 初始化View
    * */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    /**
     * 懒加载
     */
    abstract fun lazyLoadData()

    /**
     * 将非该Fragment绑定的ViewModel添加 loading回调 防止出现请求时不显示 loading 弹窗bug
     * @param viewModels Array<out BaseViewModel>
     */
    protected fun addLoadingObserve(vararg viewModels: BaseViewModel) {
        viewModels.forEach { viewModel ->
            //显示弹窗
            viewModel.loadingChange.showDialog.observeInFragment(this, Observer {
                showLoading(it)
            })
            //关闭弹窗
            viewModel.loadingChange.dismissDialog.observeInFragment(this, Observer {
                dismissLoading()
            })
        }
    }

    /**
     * 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿  bug
     * 这里传入你想要延迟的时间，延迟时间可以设置比转场动画时间长一点 单位： 毫秒
     * 不传默认 300毫秒
     * @return Long
     */
    open fun lazyLoadTime(): Long {
        return 300
    }


}