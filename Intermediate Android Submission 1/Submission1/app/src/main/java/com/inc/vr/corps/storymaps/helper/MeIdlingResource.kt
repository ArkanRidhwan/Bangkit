package com.inc.vr.corps.storymaps.helper

import androidx.test.espresso.idling.CountingIdlingResource

object IdlingResource {
    private const val RESOURCE = "GLOBAL"
    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}

inline fun <T> wrapEspressoIdleResource(function: () -> T): T {
    IdlingResource.increment()
    return try {
        function()
    } finally {
        IdlingResource.decrement()
    }
}