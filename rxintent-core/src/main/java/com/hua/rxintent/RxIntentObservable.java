package com.hua.rxintent;

import android.content.Intent;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author hua
 * @version 2018/11/7 17:15
 */

public class RxIntentObservable {

    private Observable<Intent> source;
    private IInterceptor after;
    private Intent intent;

    public RxIntentObservable(Observable<Intent> source) {
        this.source = source;
    }

    public RxIntentObservable before(IInterceptor before){
        before.intercept(intent);
        return this;
    }
    public RxIntentObservable after(IInterceptor after){
        this.after = after;
        return this;
    }

    public Observable subscribe(Consumer<Intent> consumer){
        source.map(new Function<Intent, Intent>() {
            @Override
            public Intent apply(Intent intent) throws Exception {
                after.intercept(intent);
                return intent;
            }
        }).subscribe(consumer);
        return source;
    }
}
