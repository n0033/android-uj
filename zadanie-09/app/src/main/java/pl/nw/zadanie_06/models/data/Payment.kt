package pl.nw.zadanie_06.models.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import pl.nw.zadanie_06.models.Deserializable
import pl.nw.zadanie_06.models.Serializable
import pl.nw.zadanie_06.utils.Converters
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.collections.HashMap



data class Payment(
    override val uid: String = UUID.randomUUID().toString(),
    val userId: String,
    val timestamp: LocalDateTime,
    val amount: Int,
    val address: Address
) : EntityBase(), Serializable<Category> {

    override fun toHashMap(): HashMap<String, Any?>{
        return hashMapOf(
            "uid" to uid,
            "timestamp" to timestamp.toEpochSecond(ZoneOffset.UTC),
            "amount" to amount,
            "address" to address.toHashMap()
        )
    }

    companion object : Deserializable<Payment> {
        override fun fromHashMap(map: HashMap<String, Any>): Payment {
            return Payment(
                map["uid"] as String,
                map["userId"] as String,
                LocalDateTime.ofEpochSecond(map["timestamp"] as Long, 0, ZoneOffset.UTC) as LocalDateTime,
                map["amount"] as Int,
                Address.fromHashMap(map["address"] as HashMap<String, Any>)
            )
        }
    }


}
