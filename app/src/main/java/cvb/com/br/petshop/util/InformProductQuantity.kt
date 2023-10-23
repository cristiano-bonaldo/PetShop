package cvb.com.br.petshop.util

import android.widget.TextView

object InformProductQuantity {

    const val C_DECREASE_QTD = 0
    const val C_INCREASE_QTD = 1

    fun process(type: Int, textViewQuantity: TextView, event: ((Int) -> Unit)?) {
        var currentQtd = textViewQuantity.text.toString().toInt()

        if (type == C_DECREASE_QTD) {
            if (currentQtd > 0) {
                currentQtd--
            }
        } else {
            if (currentQtd < Constants.LIMIT_QTD_REQUEST) {
                currentQtd++
            }
        }

        textViewQuantity.text = currentQtd.toString()
        event?.invoke(currentQtd)
    }
}