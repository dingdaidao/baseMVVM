package com.example.commonlib.baserx;

import android.app.Activity;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * @author Army
 * @version V_1.0.0
 * @date 2017/4/29
 * @description
 */
public class RxUtils {
    private static final int DISPLAYONCE = 1;
/*

    */
/**
     * Activity里面统一Observable处理
     *
     * @param activity     BaseActivity对象
     * @param observable   需要耗时操作的Observable
     * @param isNeedDialog 是否需要显示加载框
     * @return 处理后的Observable
     *//*

    public static <T> Flowable<T> getActivityObservable(BaseActivity activity, Flowable<T> observable,
                                                        boolean isNeedDialog) {
        return observable.compose(rxSchedulerHelper(isNeedDialog ? activity : null));
    }

    */
/**
     * fragment里面统一Observable处理
     *
     * @param fragment     BaseFragment对象
     * @param observable   需要耗时操作的Observable
     * @param isNeedDialog 是否需要显示加载框
     * @return 处理后的Observable
     *//*

    public static <T> Flowable<T> getFragmentObservable(BaseFragment fragment, Flowable<T> observable,
                                                        boolean isNeedDialog) {
        return observable.compose(rxSchedulerHelper(isNeedDialog ? fragment.getActivity() : null));
    }
*/


    /**
     * 统一线程处理
     */
    public static <T> FlowableTransformer<T, T> rxSchedulerHelper(Activity activity) {    //compose简化线程
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .doOnSubscribe(subscription -> {
                    if (activity != null && Looper.myLooper() == Looper.getMainLooper()) {

                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 全在子线程
     */
    public static <T> FlowableTransformer<T, T> rxThreadHelper() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

/*
    public static <T> FlowableTransformer<T, T> rxSchedulerHelperOnce(Activity activity, int flag) {    //compose简化线程
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .doOnSubscribe(subscription -> {
                    if (activity != null && Looper.myLooper() == Looper.getMainLooper()) {
                        if (!DialogManager.getInstance().isShow()) {
                            if (flag != DISPLAYONCE) {
                                DialogManager.getInstance().createLoadingDialog(activity, activity.getString(R.string.common_onloading)).show();
                            }
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }*/

    public static Flowable<Integer> countDown(int count) {
        return Flowable.interval(1, TimeUnit.SECONDS)
                .take(count + 1).map(aLong -> count - aLong.intValue());
    }

    /*private static AllClickInvocationHandler invocationHandler = null;

    public static void click(View v, Consumer<Object> onNext, boolean... isNeedJudgeLogin) {
        Consumer consumer;
        if (isNeedJudgeLogin != null && isNeedJudgeLogin.length > 0 && isNeedJudgeLogin[0]) {
            if (invocationHandler == null) {
                invocationHandler = new AllClickInvocationHandler();
            }
            consumer = (Consumer) invocationHandler.bind(onNext);
        } else {
            consumer = onNext;
        }
        RxView.clicks(v).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(consumer);
    }

    public static void longClick(View v, Consumer<Object> onNext) {
        RxView.longClicks(v).subscribe(onNext);
    }

    private static class AllClickInvocationHandler implements InvocationHandler {

        private Object object = null;

        public Object bind(Object obj) {
            object = obj;
            return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
           if (Global.isLogin) {
                return method.invoke(object, args);
            } else {
                Activity topActivity = ActivityManager.getInstance().getTopActivity();
                Context context;
                Intent intent = new Intent();
                if (topActivity == null) {
                    context = Global.application;
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    context = topActivity;
                }
                intent.setClassName(context, Global.loginActivity);
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
            return null;
        }
    }*/

    public static Observable<Boolean> meetMultiConditionDo(Function<Object[], Boolean> combiner, TextView... tvs) {
        if (tvs != null && tvs.length > 0) {
            List<Observable<CharSequence>> observableList = new ArrayList<>();
            for (int i = 0; i < tvs.length; i++) {
                observableList.add(RxTextView.textChanges(tvs[i]).skip(1));
            }
            return Observable.combineLatest(observableList, combiner);
        }
        return null;
    }


    //定时任务以及循环任务
    private static Disposable mDisposable;

    /**
     * milliseconds毫秒后执行next操作
     *
     * @param milliseconds
     * @param next
     */
    public static Disposable timer(long milliseconds, final IRxNext next) {
        final Disposable[] disposable = new Disposable[1];
        Observable.timer(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable di) {
                        disposable[0] = di;
                        mDisposable = di;
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        if (next != null) {
                            next.doNext(number);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        //取消订阅
                        cancel(disposable[0]);
                    }

                    @Override
                    public void onComplete() {
                        //取消订阅
                        cancel(disposable[0]);
                    }
                });
        return disposable[0];
    }

    /**
     * 每隔milliseconds毫秒后执行next操作
     *
     * @param milliseconds
     * @param next
     */
    public static Disposable interval(long milliseconds, final IRxNext next) {
        final Disposable[] disposable = new Disposable[1];
        Observable.interval(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable di) {
                        disposable[0] = di;
                        mDisposable = di;
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        if (next != null) {
                            next.doNext(number);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return disposable[0];
    }

    /**
     * 每隔milliseconds毫秒后执行next操作
     *
     * @param hour
     * @param minute
     * @param second
     * @param next
     * @return
     */
    public static Disposable interval(int hour, int minute, int second, final IRxNext next) {
        final Disposable[] disposable = new Disposable[1];
        Calendar calendar = Calendar.getInstance();
        long nowTime = calendar.getTimeInMillis();
        /*** 定制每日6:00执行方法 ***/
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        //如果第一次执行定时任务的时间 小于 当前的时间
        //此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。循环执行的周期则以当前时间为准
        if (calendar.getTime().before(new Date())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        long startTime = calendar.getTimeInMillis(); //第一次执行定时任务的时间
        //如果第一次执行定时任务的时间 小于 当前的时间
        //此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。循环执行的周期则以当前时间为准
        Observable.interval(startTime - nowTime, 86400000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable di) {
                        disposable[0] = di;
                        mDisposable = di;
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        if (next != null) {
                            next.doNext(number);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return disposable[0];
    }


    /**
     * 取消最新的一条订阅
     */
    public static void cancel() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    /**
     * 取消指定订阅
     *
     * @param mDisposable
     */
    public static void cancel(Disposable mDisposable) {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    public interface IRxNext {
        void doNext(long number);
    }

    public static <T> ObservableTransformer<T, T> io_main() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static void longClick(View v, Consumer<Object> onNext) {
        RxView.longClicks(v).subscribe(onNext);
    }


//    public static void click(View v, Consumer<Object> onNext, boolean... isNeedJudgeLogin) {
//        Consumer consumer;
//        if (isNeedJudgeLogin != null && isNeedJudgeLogin.length > 0 && isNeedJudgeLogin[0]) {
//            if (invocationHandler == null) {
//                invocationHandler = new AllClickInvocationHandler();
//            }
//            consumer = (Consumer) invocationHandler.bind(onNext);
//        } else {
//            consumer = onNext;
//        }
//        RxView.clicks(v).throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe(consumer);
//    }


}
