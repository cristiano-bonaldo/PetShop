package cvb.com.br.petshop.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cvb.com.br.petshop.db.entity.EntityItemPurchase

@Dao
interface ItemPurchaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entityItemPurchase: EntityItemPurchase)

    @Delete
    suspend fun delete(entityItemPurchase: EntityItemPurchase)

    @Update
    suspend fun update(entityItemPurchase: EntityItemPurchase)

    @Query("SELECT * FROM item_purchase WHERE purchase_id = :purchaseId ORDER BY prod_desc ASC")
    suspend fun getByPurchase(purchaseId: Long): List<EntityItemPurchase>

    @Query("SELECT * FROM item_purchase WHERE purchase_id = :purchaseId AND prod_id = :prodId")
    suspend fun getByPurchaseAndProduct(purchaseId: Long, prodId: Long): EntityItemPurchase?
}