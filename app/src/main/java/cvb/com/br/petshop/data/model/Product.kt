package cvb.com.br.petshop.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Long,
    val description: String,
    val weight: String,
    val quantity: Int,
    val amount: Double,
    val imageUrl: String
) : Parcelable
