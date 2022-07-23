package com.ktan

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ktan.binding.ExtrasBinding

internal class KtanLifecycleCallbacks : FragmentManager.FragmentLifecycleCallbacks(),
    Application.ActivityLifecycleCallbacks {

    val savedInstanceMap: MutableMap<Any, Bundle?> = mutableMapOf()

    val ktanBindingMap: MutableMap<Any, MutableList<ExtrasBinding>> = mutableMapOf()

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        savedInstanceMap[activity] = bundle
        if (activity is AppCompatActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                this,
                true
            )
        }
    }

    override fun onActivityStarted(activity: Activity) {
        // do nothing
    }

    override fun onActivityResumed(activity: Activity) {
        // do nothing
    }

    override fun onActivityPaused(activity: Activity) {
        // do nothing
    }

    override fun onActivityStopped(activity: Activity) {
        // do nothing
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
        ktanBindingMap[activity]?.forEach { it.saveInstanceState(bundle) }
    }

    override fun onActivityDestroyed(activity: Activity) {
        savedInstanceMap.remove(activity)
        ktanBindingMap.remove(activity)
        if (activity is AppCompatActivity) {
            activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(
                this
            )
        }
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentCreated(fm, f, savedInstanceState)
        savedInstanceMap[f] = savedInstanceState
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        super.onFragmentSaveInstanceState(fm, f, outState)
        ktanBindingMap[f]?.forEach { it.saveInstanceState(outState) }
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentDestroyed(fm, f)
        savedInstanceMap.remove(f)
        ktanBindingMap.remove(f)
    }

    fun registerKtanBinding(obj: Any, extrasBinding: ExtrasBinding) {
        if (ktanBindingMap[obj]?.add(extrasBinding) == true) {
            return
        }
        ktanBindingMap[obj] = mutableListOf(extrasBinding)
    }
}