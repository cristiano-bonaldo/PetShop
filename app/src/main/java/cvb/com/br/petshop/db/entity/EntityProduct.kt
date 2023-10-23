package cvb.com.br.petshop.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class EntityProduct(

    @PrimaryKey(autoGenerate = false)
    val id: Long,

    val description: String = "",

    val weight: String = "",

    val quantity: Int = 0,

    val amount: Double = 0.00,

    @ColumnInfo(name="image_url")
    val imageUrl: String = "",

    @ColumnInfo(name="created_at")
    val createdAt: Long = System.currentTimeMillis()
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EntityProduct

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

