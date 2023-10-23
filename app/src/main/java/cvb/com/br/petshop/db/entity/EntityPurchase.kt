package cvb.com.br.petshop.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cvb.com.br.petshop.data.enum.PurchaseStatusEnum

@Entity(tableName = "purchase")
data class EntityPurchase(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val status: Int = PurchaseStatusEnum.PURCHASE_STATUS_OPEN.status,

    @ColumnInfo(name="created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name="converted_at")
    val convertedAt: Long = 0
)

