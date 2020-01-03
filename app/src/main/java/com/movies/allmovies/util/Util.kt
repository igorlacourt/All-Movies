package com.movies.allmovies.util

import android.app.Activity
import android.content.Context
import android.view.View

object Util {
    fun showRatingDialog(
        context: Context,
        titulo: String, mensagem: String,
        bt1: String, l1: View.OnClickListener,
        bt2: String, l2: View.OnClickListener,
        bt3: String, l3: View.OnClickListener
    ) {
        val dialog = DialogHelper.getInstance(
            context,
            titulo,
            mensagem,
            bt1, l1,
            bt2, l2,
            bt3, l3
        )
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)

        if (context !is Activity || !context.isFinishing)
            dialog.show()
    }
}