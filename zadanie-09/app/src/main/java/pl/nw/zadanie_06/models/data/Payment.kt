package pl.nw.zadanie_06.models.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import pl.nw.zadanie_06.utils.Converters
import java.time.LocalDateTime


@Entity(tableName = "payments")
@TypeConverters(value = [Converters::class])
data class Payment (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val timestamp: LocalDateTime,
    val amount: Int,
    val address: Address
)