package com.ms.spacecraft.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.ms.spacecraft.R
import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
class Alert {
    companion object {
        fun showMessage(message: String, context: Context) {
            AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.ok_text), null).show()
        }
    }
}