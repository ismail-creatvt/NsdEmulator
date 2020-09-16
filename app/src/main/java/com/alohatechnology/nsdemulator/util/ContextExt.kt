package com.alohatechnology.nsdemulator.util

import android.content.Context
import android.content.pm.PackageInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.alohatechnology.nsdemulator.R
import com.alohatechnology.nsdemulator.ui.templates.ResponseTemplate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import kotlin.reflect.KClass

val Context.packageInfo: PackageInfo
    get() = packageManager.getPackageInfo(packageName, 0)

val Context.versionName: String
    get() = packageInfo.versionName

val Context.appName: String
    get() = getString(R.string.app_name)

fun <T : ViewModel> ViewModelStoreOwner.getViewModel(modelClass: KClass<T>): T {
    val provider = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
    return provider.get(modelClass.java)
}

val Context.responseTemplates: ArrayList<ResponseTemplate>
    get() {
        val responseStream = resources.openRawResource(R.raw.command_request_templates)
        val type = object : TypeToken<ArrayList<ResponseTemplate>>() {}.type
        return Gson().fromJson(InputStreamReader(responseStream), type)
    }