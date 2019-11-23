package com.lacourt.myapplication.idlingresource

import androidx.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A very simple implementation of [IdlingResource].
 *
 *
 * Consider using CountingIdlingResource from espresso-contrib package if you use this class from
 * multiple threads or need to keep a count of pending operations.
 */

class SimpleIdlingResource : IdlingResource {

    @Volatile
    private var mCallback: IdlingResource.ResourceCallback? = null

    // Idleness is controlled with this boolean.
    private val mIsIdleNow = AtomicBoolean(true)

    override fun getName(): String {
        return this.javaClass.name
    }

    override fun isIdleNow(): Boolean {
        return mIsIdleNow.get()
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        mCallback = callback
    }

    /**
     * Sets the new idle state, if isIdleNow is true, it pings the [ResourceCallback].
     * @param isIdleNow false if there are pending operations, true if idle.
     */
    fun setIdleState(isIdleNow: Boolean) {
        mIsIdleNow.set(isIdleNow)
        if (isIdleNow && mCallback != null) {
            mCallback!!.onTransitionToIdle()
        }
    }
}