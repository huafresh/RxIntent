package com.hua.rxintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import io.reactivex.functions.Consumer;

/**
 * @author hua
 * @version 1.0
 * @date 2018/11/4
 */
@SuppressWarnings("ConstantConditions")
public class RxIntentFragment extends Fragment {
    private static final String TAG_RX_INTENT_FRAGMENT = "tag_rx_intent_fragment";
    private Activity activity;
    private BlockingQueue<IntentRequest> requestQueue = new ArrayBlockingQueue<>(5);
    private boolean running = false;
    private int requestCode = 0x100;
    private SendThread sendThread = new SendThread();

    private class SendThread extends Thread {
        private IntentRequest request;
        private final Object mLock = new Object();

        @Override
        public void run() {
            if (running) {
                try {
                    this.request = null;
                    IntentRequest request = requestQueue.take();
                    request.requestCode = requestCode++;
                    this.request = request;

                    _wait();
                    RxPermissions rxPermissions = new RxPermissions(RxIntentFragment.this);
                    rxPermissions.request(request.getPermissions())
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean b) throws Exception {
                                    _continue();
                                }
                            });

                    _wait();
                    startActivityForResult(request.getIntent(), request.requestCode);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        void _wait() throws InterruptedException {
            synchronized (mLock) {
                mLock.wait();
            }
        }

        void _continue() {
            synchronized (mLock) {
                mLock.notifyAll();
            }
        }


        IntentRequest getRequest() {
            return request;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
        sendThread = new SendThread();
        sendThread.start();
        running = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;
        sendThread.interrupt();
    }

    static void openByFragment(FragmentActivity activity, IntentRequest request) {
        FragmentManager manager = activity.getSupportFragmentManager();
        RxIntentFragment rxFragment = (RxIntentFragment) manager.findFragmentByTag(TAG_RX_INTENT_FRAGMENT);
        if (rxFragment == null) {
            rxFragment = new RxIntentFragment();
            manager.beginTransaction().add(rxFragment, TAG_RX_INTENT_FRAGMENT).commit();
        }
        rxFragment.sendIntentRequest(request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentRequest request = sendThread.getRequest();
        if (request != null && request.requestCode == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                request.getCallback().onResult(data);
            } else {
                request.getCallback().onResult(null);
            }
        }
        sendThread._continue();
    }

    void sendIntentRequest(IntentRequest request) {
        requestQueue.add(request);
    }

}
