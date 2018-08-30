package com.example.commonlib.baserx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

public class RxTimerManager {
    private List<String> mKeyS = new ArrayList<>();
    private Map<String, Disposable> mDisposables = new HashMap<>();

    public Map<String, Disposable> getDisposables() {
        return mDisposables;
    }

    public void setDisposables(String keys, Disposable disposable) {
        mDisposables.put(keys, disposable);
        if (!mKeyS.contains(keys))
            mKeyS.add(keys);
    }

    public boolean clearAll() {
        for (int i = 0; i < mKeyS.size(); i++) {
            clearTag(mKeyS.get(i));
        }
        return true;
    }

    public boolean clearTag(String tag) {
        Disposable di = mDisposables.get(tag);
        if (di != null && !di.isDisposed()) {
            di.dispose();
        }
        return true;
    }
}
