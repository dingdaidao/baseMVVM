# 基于Google MVVM框架的baseMVVM框架
### 	前提说明:
**本人在项目中的一些总结和参考大佬的代码，综合了一个小型的Android vvm框架**
**本框架适用于中小项目，并且对DataBinging有一定的了解。mvvm模式对数据展示累项目的开发十分友好。项目大或者页面逻辑复杂的项目请绕行MVP**
### 主要特性:
``` java
1.加入retrofit2 RxJava
2.使用GoogleArch大礼包，包含 LiveData ，ViewModel，LifeCycle
3.提供 DataBinding 自定义绑定事件（TextView，ImageView，）
4.集成张旭童的一行代码实现RecyclerView [简书链接](https://www.jianshu.com/p/bf8d95dfd60a)
5.基于RxJava的 RxBus，RxUtils
```
## 如何使用:
### 1.activity，xml，和viewodel该怎么写
继承Commonlib下的BaseActivity,并传入DataBinding生成的xml对应的ViewDataBing类和该页面的ViewModel类
``` java
    public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {
```
注意ActivityMainBinding该类是根据activity_main.xml的命名来生成的。我们先来看下activity_main.xml文件
```xml
<!--跟布局用layout包裹-->
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">
    <!--声明xml对应的ViewModel类-->
    <data>

        <variable
            name="mainVM"
            type="com.example.ding.vm.MainViewModel" />
    </data>
    <!--rootview-->
    <android.support.constraint.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">
        <EditText
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{mainVM.weatherLiveData}"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent" />
    </android.support.constraint.ConstraintLayout>
</layout>
```
``` java
@Override
   protected void dealWithAction(Event event) {
      //处理rxbus订阅的信息
   }
```

``` java
@Override
   public int initContentView() {
      return R.layout.activity_main;
   }
@Override
   public int initVariableId() {
      return BR.mainVM;
   }
```
initContentView方法是为了获取该页面的xml，intiVariableId是获取xml对应绑定的ViewModel（MainViewMode）的id
``` java
@Override
   public MainViewModel initViewModel() {
      return new MainViewModel();
   }
```
initViewModel是为了获取对应viewModel的实例
``` java
@Override
    public void initViewObservable() {

    }
```
initViewObservable是为了初始化viewModel层产生数据变化时的监听事件，比如viewModel中一个天气情况的数据weatherLiveData产生了变化，则weatherLiveData.observe(this,(params){
//处理数据变化
});一般来说我们会把数据通过dataBinding直接塞给xml，View层，但也不排除其他复杂一点的操作，在xml中无法处理。所以，可以在initViewObservable中初始化LiveData的监听事件。我们再来看下ViewModel类
``` java
public class MainViewModel extends BaseViewModel {
    private MutableLiveData<String> weatherLiveData = new MutableLiveData<>();

    @Override
    public void onCreate() {
        super.onCreate();
        //页面初始化的一些操作
    }

    public MutableLiveData<String> getWeatherLiveData() {
        return weatherLiveData;
    }
    /**
     * 可不写此方法
     * 使用getShowTxt.setValue(String weatherLiveData)
     *
     * @param weatherLiveData
     */
    public void setWeatherLiveData(String weatherLiveData) {
        this.weatherLiveData.setValue(weatherLiveData);
    }
}
```
在ViewModel类中定义的weatherLiveData属性是继承自LiveData，这是LiveData的介绍[Android Developer LiveData](https://developer.android.com/topic/libraries/architecture/livedata)。LiveData可以个DataBinding连用，实现双向绑定，数据驱动模型。在view层中绑定LiveData，在LiveData中的数据产生变化时，LiveData通过Observe通知到DataBinding绑定的控件，更新显示数据。或者，view产生变化的时候，view绑定的LiveData也会更着页面更新。


### 2.RecycleView的实现
recyclerView的实现参考了张旭童大神的方法，上链接[简书链接](https://www.jianshu.com/p/bf8d95dfd60a)先看下item中xml的实现：
``` java
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="data"
            type="String" />
    </data>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{data}" />
    </LinearLayout>
</layout>
```
定义传入数据的类型String和对应name data（此为固定，因为已经在item.xml中定义了通用id：data，这样在代码中配置adapter的时候就不用传该item的variableId了）。
``` java
        BaseBindingAdapter mAdapter=new BaseBindingAdapter(this,R.layout.main_item)；
        mBinding.r.setAdapter(mAdapter);
```
``` java
        mBinding.r.setAdapter(new BaseBindingAdapter(this,mListData,R.layout.main_item));
```
一两行代码即可配置好RecyclerView的适配器，是不是爽歪歪啊。如果仅仅是普通的展示和点击满足不了你记得需求，你还可以重写onBindViewHolder方法。
``` java
        mAdapter=new BaseBindingAdapter(this,R.layout.main_item){
            //可复写
            @Override
            public void onBindViewHolder(BaseBindingVH holder, int position) {
                super.onBindViewHolder(holder, position);//复写是不可删除
            }
        };
```
在这里你可以根据position对数据或者view做出相应的处理。对于多布局的RecyclerView可以使用BaseMulTypeBindingAdapter类去实现，我就不过多解释了。

最后，这是我的第一篇博客，求轻喷。后续我会继续完善这个项目，并继续更新文章分析Google Architecture Compontents组件库和DataBinding的控件绑定用法。