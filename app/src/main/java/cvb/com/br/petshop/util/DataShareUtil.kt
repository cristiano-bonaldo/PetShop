package cvb.com.br.petshop.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import cvb.com.br.petshop.R
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DataShareUtil @Inject constructor(@ActivityContext val activity: Context, private val dialogUtil: DialogUtil) {

    fun sendMessageToWhatsApp(msg: String) {
        val whatsAppAppId = "com.whatsapp"

        val packageManager = activity.packageManager

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.setPackage(whatsAppAppId)
        intent.putExtra(Intent.EXTRA_TEXT, msg)

        if (intent.resolveActivity(packageManager) == null) {
            val btOk = {}
            dialogUtil.showErrorDialog(activity.getString(R.string.share_data_error_whatsapp), btOk)
            return
        }

        activity.startActivity(intent)
    }

    fun sendMessageToEmail(msg: String) {
        val subject = activity.getString(R.string.share_data_mail_title)
        val recipientEmail = "" // -> Ex.: "petshop@example.com"

        val gmailAppAppId = "com.google.android.gm"

        val packageManager = activity.packageManager

        val isGmailInstalled = isAppInstalled(packageManager, gmailAppAppId)

        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, msg)

        if (isGmailInstalled) {
            intent.type = "text/html"
            intent.setPackage(gmailAppAppId)
            activity.startActivity(intent)
        } else {
            intent.type = "message/rfc822"
            activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.share_data_mail_select_client)))
        }
    }

    private fun isAppInstalled(packageManager: PackageManager, packageName: String): Boolean {
        return try {
            packageManager.getApplicationInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun shareData(msg: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, msg)
        activity.startActivity(intent)
    }

}