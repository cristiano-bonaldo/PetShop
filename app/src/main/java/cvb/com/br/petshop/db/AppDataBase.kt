package cvb.com.br.petshop.db

import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import cvb.com.br.petshop.db.dao.ItemPurchaseDao
import cvb.com.br.petshop.db.dao.ProductDao
import cvb.com.br.petshop.db.dao.PurchaseDao
import cvb.com.br.petshop.db.entity.EntityItemPurchase
import cvb.com.br.petshop.db.entity.EntityProduct
import cvb.com.br.petshop.db.entity.EntityPurchase
import cvb.com.br.petshop.util.Constants

@Database(
    entities = [
        EntityProduct::class,
        EntityPurchase::class,
        EntityItemPurchase::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun purchaseDao(): PurchaseDao
    abstract fun itemPurchaseDao(): ItemPurchaseDao

    companion object {

        val databaseCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Log.i(Constants.TAG, "Database - onCreate")
            }

            override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                super.onDestructiveMigration(db)
                Log.i(Constants.TAG, "Database - onDestructiveMigration")
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                Log.i(Constants.TAG, "Database - onOpen")

                // Delete ALL products from DB
                db.delete("product", null, null)
            }
        }
    }
}