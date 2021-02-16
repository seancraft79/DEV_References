package com.sysgen.eom.listener;

@FunctionalInterface
public interface CallBack<T> {
    void onResult(T result);
}
