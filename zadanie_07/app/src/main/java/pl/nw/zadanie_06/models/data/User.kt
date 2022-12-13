package pl.nw.zadanie_06.models.data

import pl.nw.zadanie_06.models.Deserializable
import pl.nw.zadanie_06.models.Serializable
import java.util.*

class User (
    override val uid: String = UUID.randomUUID().toString(),
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var cartId: String? = null,
) : EntityBase(), Serializable<User> {

    override fun toHashMap(): HashMap<String, Any?>{
        return hashMapOf(
            "uid" to uid,
            "first_name" to firstName,
            "last_name" to lastName,
            "email" to email,
            "cartId" to cartId
        )
    }

    companion object : Deserializable<User> {
        override fun fromHashMap(map: HashMap<String, Any>): User {
            return User(
                map["uid"] as String,
                map["first_name"] as String,
                map["email"] as String,
                map["cartId"] as String
            )
        }
    }
}