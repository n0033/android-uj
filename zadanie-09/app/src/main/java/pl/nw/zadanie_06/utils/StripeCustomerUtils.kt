package pl.nw.zadanie_06.utils

import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpPost
import kotlinx.coroutines.runBlocking
import pl.nw.zadanie_06.Constants
import pl.nw.zadanie_06.common.local_db.LocalDatabase
import pl.nw.zadanie_06.models.data.StripeCustomer

class StripeCustomerUtils {

    data class CustomerId(var id: String = "")

    companion object {
        suspend fun ensureCustomerExists(db: LocalDatabase, userId: String) {
            runBlocking {
                var stripeCustomer = db.stripeCustomerDao().findByUserId(userId)
                if (stripeCustomer == null) {
                    Constants.STRIPE_CREATE_CUSTOMER_URL.httpPost().authentication()
                        .basic(Constants.STRIPE_SECRET_KEY, "")
                        .responseObject<CustomerId> { _, _, result ->
                            val (cid, _) = result
                            stripeCustomer =
                                StripeCustomer(userId = userId, stripCustomerId = cid!!.id)
                            runBlocking {
                                db.stripeCustomerDao().insert(stripeCustomer!!)
                            }
                        }
                }
            }
        }
    }
}