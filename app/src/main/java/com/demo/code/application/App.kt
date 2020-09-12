package com.demo.code.application

import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import com.demo.code.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    /** A tree which logs important information for crash reporting.  */
    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, @NonNull message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
            FakeCrashLibrary.log(priority, tag, message)
            if (t != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(t)
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(t)
                }
            }
        }
    }

    /** Not a real crash reporting library!  */
    class FakeCrashLibrary private constructor() {
        companion object {
            fun log(priority: Int, tag: String?, message: String?) {
                // TODO add log entry to circular buffer.
            }

            fun logWarning(t: Throwable?) {
                // TODO report non-fatal warning.
            }

            fun logError(t: Throwable?) {
                // TODO report non-fatal error.
            }
        }

        init {
            throw AssertionError("No instances.")
        }
    }
}