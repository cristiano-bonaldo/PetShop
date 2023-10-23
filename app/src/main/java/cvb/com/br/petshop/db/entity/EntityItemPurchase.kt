package cvb.com.br.petshop.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "item_purchase", primaryKeys = ["purchase_id","prod_id"])
data class EntityItemPurchase(
    @ColumnInfo(name="purchase_id")
    val purchaseId: Long = 0,

    @ColumnInfo(name="prod_id")
    val prodId: Long,

    @ColumnInfo(name="prod_desc")
    val prodDesc: String,

    @ColumnInfo(name="prod_price")
    val prodPrice: Double,

    @ColumnInfo(name="prod_url")
    val prodUrl: String,

    val quantity: Int
)
