package cvb.com.br.petshop.util

import android.app.AlertDialog
import android.content.Context
import cvb.com.br.petshop.R
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DialogUtil @Inject constructor(@ActivityContext private val activity: Context) {

    fun showDialog(title: String, message: String,
                   btPositiveTitle: String,
                   btPositiveEvent: (() -> Unit)?,
                   btNegativeTitle: String,
                   btNegativeEvent: (() -> Unit)?) {

        val builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton(btPositiveTitle) { _, _ -> btPositiveEvent?.invoke() }

        builder.setNegativeButton(btNegativeTitle) { _, _ -> btNegativeEvent?.invoke() }

        builder.show()
    }

    fun showErrorRetryDialog(errorMessage: String, btRetryEvent: (() -> Unit), btCancelEvent: (() -> Unit)? = null) {
        val builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
        builder.setIcon(R.drawable.ic_error)
        builder.setTitle(activity.getString(R.string.msg_error_title))
        builder.setMessage(activity.getString(R.string.msg_error, errorMessage))

        builder.setPositiveButton(R.string.bt_retry) { _, _ -> btRetryEvent.invoke() }

        builder.setNegativeButton(R.string.bt_cancelar) { _, _ -> btCancelEvent?.invoke() }

        builder.show()
    }

    fun showErrorDialog(errorMessage: String, btOkEvent: (() -> Unit)) {
        val builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
        builder.setIcon(R.drawable.ic_error)
        builder.setTitle(activity.getString(R.string.msg_error_title))
        builder.setMessage(errorMessage)

        builder.setPositiveButton(R.string.bt_ok) { _, _ -> btOkEvent.invoke() }

        builder.show()
    }

    fun showDataShareDialog(selectOptionListener: ((Int) -> Unit), btCloseListener: (() -> Unit)) {
        val optionList = arrayOf(
            activity.getString(R.string.share_data_option_whatsapp),
            activity.getString(R.string.share_data_option_mail),
            activity.getString(R.string.share_data_option_generic)
        )

        val builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setTitle(activity.getString(R.string.frag_purchase_in_progress_finish_success_title))
        builder.setSingleChoiceItems(optionList, -1) { dialogInterface, option ->
            selectOptionListener.invoke(option)
            // dialogInterface.dismiss()
        }
        builder.setPositiveButton(R.string.bt_fechar) { _, _ -> btCloseListener.invoke()}
        builder.show()
    }
}