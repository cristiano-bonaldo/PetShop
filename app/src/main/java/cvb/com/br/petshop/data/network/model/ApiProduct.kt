package cvb.com.br.petshop.data.network.model

data class ApiProduct(
    val id: Int,
    val description: String,
    val weight: String,
    val quantity: Int,
    val amount: String,
    val imageUrl: String,
)
