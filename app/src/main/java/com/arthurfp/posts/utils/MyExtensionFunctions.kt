package com.arthurfp.posts.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import retrofit2.Response

fun <T> Response<T>.handleResponse(): NetworkResult<T> {
    return when {
        this.message().toString().contains("timeout") -> {
            NetworkResult.Error("Timeout.")
        }
        this.isSuccessful -> {
            NetworkResult.Success(this.body())
        }
        // TODO: handle more response status errors (e.g., 501, 404, etc) with the proper error message
        else -> {
            NetworkResult.Error("No data found!")
        }
    }
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

fun View.toggleVisibility(): Boolean {
    if (this.isVisible) {
        this.visibility = View.INVISIBLE
        return false
    } else {
        this.visibility = View.VISIBLE
        return true
    }
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}