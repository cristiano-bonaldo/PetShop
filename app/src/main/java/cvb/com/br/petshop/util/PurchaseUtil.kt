package cvb.com.br.petshop.util

import android.content.Context
import cvb.com.br.petshop.R
import cvb.com.br.petshop.data.model.ItemPurchase
import cvb.com.br.petshop.data.model.Purchase

object PurchaseUtil {

    fun getPurchaseFormatted(context: Context, purchase: Purchase): String {
        /*
        Exemplo:
        Nº de Transação: 10050
        Data e Hora de Criação: 21/10/2023 15:05
        Data e Hora de Confirmação: : 21/10/2023 16:15
        Valor Total R$: 125.00
        Produtos Comprados
        */

        val stringBuilder = StringBuilder()

        val total = getTotalPurchase(purchase.listItemPurchase)

        val infoPurchase = context.getString(R.string.share_data_info_purchase,
            purchase.id.toString(),
            DateTimeUtil.convertTimeMillisToString(purchase.createAt),
            DateTimeUtil.convertTimeMillisToString(purchase.convertedAt),
            StringUtil.formatValue(total))

        stringBuilder.append(infoPurchase)

        /*
        Exemplo:
        -----------
        Item: 1
        Produto: Coleira
        Quantidade: 2
        Valor Unitário 30.00
        Valor R$: 60.00
        */

        purchase.listItemPurchase.forEachIndexed { idx, itemPurchase ->
            val item = idx + 1
            val prodTotal = itemPurchase.prodPrice * itemPurchase.quantity

            val infoItem = context.getString(R.string.share_data_info_item_purchase,
                item.toString(),
                itemPurchase.prodDesc,
                itemPurchase.quantity.toString(),
                StringUtil.formatValue(itemPurchase.prodPrice),
                StringUtil.formatValue(prodTotal)
            )

            stringBuilder.append(infoItem)
        }

        return stringBuilder.toString()
    }

    fun getTotalPurchase(listItemPurchase: List<ItemPurchase>?): Double {
        var total = 0.0
        listItemPurchase?.let { list ->
            if (list.isNotEmpty()) {
                total = list.map { itemPurchase ->
                    itemPurchase.quantity * itemPurchase.prodPrice
                }.reduce { tot, value -> tot + value }
            }
        }

        return total
    }

}