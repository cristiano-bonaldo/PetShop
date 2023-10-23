package cvb.com.br.petshop.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cvb.com.br.petshop.db.entity.EntityProduct

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entityProduct: EntityProduct)

    @Query("SELECT * FROM product ORDER BY id ASC")
    suspend fun getAll(): List<EntityProduct>
}