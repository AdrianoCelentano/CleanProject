package com.clean.domain.util.rx

import io.reactivex.observers.DisposableObserver

open class SimpleSubscriber<ITEM>(
    val onNext: (item: ITEM) -> Unit,
    val onError: (throwable: Throwable) -> Unit
) : DisposableObserver<ITEM>() {

    override fun onComplete() {}

    override fun onNext(item: ITEM) {
        onNext.invoke(item)
    }

    override fun onError(throwable: Throwable) {
        onError.invoke(throwable)
    }
}