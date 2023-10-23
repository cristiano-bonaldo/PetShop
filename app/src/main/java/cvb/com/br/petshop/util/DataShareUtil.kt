package cvb.com.br.petshop.util

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import cvb.com.br.petshop.R

object DataShareUtil {

    fun sendMessageToWhatsApp(activity: Activity, msg: String) {
        val whatsAppAppId = "com.whatsapp"

        val packageManager = activity.packageManager

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.setPackage(whatsAppAppId)
        intent.putExtra(Intent.EXTRA_TEXT, msg)

        if (intent.resolveActivity(packageManager) == null) {
            val btOk = {}
            DialogUtil.showErrorDialog(activity, activity.getString(R.string.share_data_error_whatsapp), btOk)
            return
        }

        activity.startActivity(intent)
    }

    fun sendMessageToEmail(activity: Activity, msg: String) {
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

    fun shareData(activity: Activity, msg: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, msg)
        activity.startActivity(intent)
    }

}