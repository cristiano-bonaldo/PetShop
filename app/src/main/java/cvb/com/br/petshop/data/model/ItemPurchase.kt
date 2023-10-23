package cvb.com.br.petshop.data.model

data class ItemPurchase(
    val purchaseId: Long,
    val prodId: Long,
    val prodDesc: String,
    val prodPrice: Double,
    val prodUrl: String,
    val quantity: Int
)
