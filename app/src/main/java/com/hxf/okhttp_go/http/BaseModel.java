package com.hxf.okhttp_go.http;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentLinkedDeque;

public abstract class BaseModel {

    private ReferenceQueue<IModelListener> mReferenceQueue;
    private ConcurrentLinkedDeque<WeakReference<IModelListener>> mWeakListenerArrayList;


    public BaseModel() {
        mReferenceQueue = new ReferenceQueue<>();
        mWeakListenerArrayList = new ConcurrentLinkedDeque<>();
    }


    public void register(IModelListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (this) {
            //每次注册前都先移除掉被系统回收的对象
            Reference<? extends IModelListener> releaseListener = null;
            while ((releaseListener = mWeakListenerArrayList.poll()) != null) {
                mWeakListenerArrayList.remove(releaseListener);
            }
            for (WeakReference<IModelListener> weakListener : mWeakListenerArrayList) {
                IModelListener listenerItem = weakListener.get();
                if (listener == listenerItem) {
                    return;
                }
            }

            WeakReference<IModelListener> weakListener = new WeakReference<>(listener, mReferenceQueue);
            mWeakListenerArrayList.add(weakListener);
        }
    }

    public void unRegister(IModelListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (this) {
            for (WeakReference<IModelListener> weakListener : mWeakListenerArrayList) {
                IModelListener listenerItem = weakListener.get();
                if (listener == listenerItem) {
                    mWeakListenerArrayList.remove(listener);
                    return;
                }
            }
        }
    }

    protected void sendMessageToUI(final int errCode, final String prompt, final boolean isEmpty, final boolean isFirstPage,
                                   final boolean hasNextPage) {
        synchronized (this) {
            for (WeakReference<IModelListener> weakListener : mWeakListenerArrayList) {
                IModelListener listenerItem = weakListener.get();
                if (listenerItem != null) {
                    listenerItem.onLoadFinish(BaseModel.this, errCode, prompt, isEmpty, isFirstPage, hasNextPage);
                }
            }
        }
    }
}
