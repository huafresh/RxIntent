package com.hua.rxintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
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
    private static final int WHAT_START_REQUEST_PERMISSIONS = 1;
    private static final int WHAT_START_REQUEST_INTENT = 2;
    private static final String TAG_RX_INTENT_FRAGMENT = "tag_rx_intent_fragment";
    private static int sRequestCode = 0x100;
    private List<IntentRequest> waitForResult = new ArrayList<>();

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final IntentRequest request = (IntentRequest) msg.obj;
            switch (msg.what) {
                case WHAT_START_REQUEST_PERMISSIONS:
                    final RxPermissions rp = new RxPermissions(RxIntentFragment.this);
                    rp.request(request.getPermissions()).subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean granted) throws Exception {
                            if (granted) {
                                handler.obtainMessage(WHAT_START_REQUEST_INTENT, request)
                                        .sendToTarget();
                            } else {
                                List<String> denied = new ArrayList<>();
                                for (String permission : request.getPermissions()) {
                                    if (!rp.isGranted(permission)) {
                                        denied.add(permission);
                                    }
                                }
                                request.getCallback().onPermissionsDenied(
                                        denied.toArray(new String[denied.size()]));
                            }
                        }
                    });
                    break;
                case WHAT_START_REQUEST_INTENT:
                    request.requestCode = ++sRequestCode;
                    startActivityForResult(request.getIntent(), request.requestCode);
                    waitForResult.add(request);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    static void enqueueRequest(FragmentActivity activity, IntentRequest request) {
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
        IntentRequest request = findRequestByRequestCode(requestCode);
        if (request != null) {
            request.getCallback().onResult(data);
        }
    }

    private IntentRequest findRequestByRequestCode(int requestCode) {
        Iterator<IntentRequest> iterator = waitForResult.iterator();
        while (iterator.hasNext()) {
            IntentRequest request = iterator.next();
            if (request.requestCode == requestCode) {
                iterator.remove();
                return request;
            }
        }
        return null;
    }

    void sendIntentRequest(IntentRequest request) {
        handler.obtainMessage(WHAT_START_REQUEST_PERMISSIONS, request);
    }

}
