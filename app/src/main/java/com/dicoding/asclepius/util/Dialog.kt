package com.dicoding.asclepius.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.dicoding.asclepius.R

object DialogUtil {

    fun showNoInternetDialog(context: Context, layoutInflater: LayoutInflater) {

        val builder = AlertDialog.Builder(context)
        val dialogView: View = layoutInflater.inflate(R.layout.custom_alert_dialog, null)

        builder.setView(dialogView)
        val dialog = builder.create()

        dialogView.findViewById<Button>(R.id.btnClose).setOnClickListener {
            if (context is Activity) {
                context.finishAffinity()
            }
        }

        dialog.show()

    }

}