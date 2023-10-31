package cvb.com.br.petshop.domain.model

data class Purchase(
    val id: Long,
    val status: Int,
    val createAt: Long,
    val convertedAt: Long,

    var listItemPurchase: List<ItemPurchase> = listOf()
)
