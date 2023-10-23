package cvb.com.br.petshop.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeUtil {

    fun convertTimeMillisToString(time: Long): String {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val netDate = Date(time)
            sdf.format(netDate)
        } catch (e: Exception) {
            Log.e("CRISTIANO", e.toString())
            ""
        }
    }
}