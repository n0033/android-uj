package pl.nw.zadanie_06.models.data

import pl.nw.zadanie_06.models.Deserializable
import pl.nw.zadanie_06.models.Serializable
import java.util.*
import kotlin.collections.HashMap

data class Address (
    override val uid: String = UUID.randomUUID().toString(),
    var line1: String,
    var line2: String,
    var postCode: String,
    var city: String
) : EntityBase(), Serializable<Category> {

    override fun toHashMap(): HashMap<String, Any?>{
        return hashMapOf(
            "uid" to uid,
            "line1" to line1,
            "line2" to line2,
            "postCode" to postCode,
            "city" to city
        )
    }

    companion object : Deserializable<Address> {
        override fun fromHashMap(map: HashMap<String, Any>): Address {
            return Address(
                map["uid"] as String,
                map["line1"] as String,
                map["line2"] as String,
                map["postCode"] as String,
                map["city"] as String
            )
        }
    }


}