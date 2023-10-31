package cvb.com.br.petshop.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cvb.com.br.petshop.data.db.entity.EntityPurchase

@Dao
interface PurchaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entityPurchase: EntityPurchase)

    @Update
    suspend fun update(entityPurchase: EntityPurchase)

    @Query("SELECT * FROM purchase WHERE status = 0 ORDER BY id ASC")
    suspend fun getPurchaseInProgress(): EntityPurchase?
}