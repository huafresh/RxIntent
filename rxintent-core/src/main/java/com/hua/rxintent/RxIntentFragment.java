package com.hua.rxintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author hua
 * @version 1.0
 * @date 2018/11/4
 */
@SuppressWarnings("ConstantConditions")
public class RxIntentFragment extends Fragment {

    /**
     * 拍照后原始图片存储的名称
     */
    private static final String TEMP_FILE_RELATIVE_PATH = "camera_temp_file.jpg";
    private static final String TAG_RX_INTENT_FRAGMENT = "tag_rx_intent_fragment";
    static final String KEY_INTENT_TYPE = "key_intent_type";
    private Activity activity;
    private BlockingQueue<IntentRequest> requestQueue = new ArrayBlockingQueue<>(5);
    private boolean running = false;
    private SendThread sendThread = new SendThread();

    private class SendThread extends Thread {
        private IntentRequest request;

        @Override
        public void run() {
            while (running) {
                try {
                    this.request = null;
                    IntentRequest request = requestQueue.take();
                    startActivityForResult(this.request.getIntent(), this.request.getType());
                    this.request = request;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        IntentRequest getRequest(){
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
    public void onDestroy() {
        super.onDestroy();
        running = false;
        sendThread.interrupt();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentRequest request = sendThread.getRequest();
        if (request != null) {
            if (resultCode == Activity.RESULT_OK) {
                request.getCallback().onResult(data);
            } else {
                request.getCallback().onResult(null);
            }
        }
    }

    void sendIntentRequest(IntentRequest request) {
        requestQueue.add(request);
    }

}
