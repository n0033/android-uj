package pl.nw.zadanie_06.common.local_db

import androidx.room.*
import pl.nw.zadanie_06.models.data.Payment

@Dao
interface PaymentDao {

    @Query("SELECT * FROM user")
    fun getAll(): List<Payment>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg payments: Payment)

    @Update
    suspend fun update(vararg payments: Payment)


}