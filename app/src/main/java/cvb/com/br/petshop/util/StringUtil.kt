package cvb.com.br.petshop.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object StringUtil {

    fun formatValue(value: Double): String {
        val locale = Locale("pt", "BR")
        val symbols = DecimalFormatSymbols(locale)
        val pattern = "###,###,###.00"

        val decimalFormat = DecimalFormat(pattern, symbols)

        return decimalFormat.format(value)
    }
}