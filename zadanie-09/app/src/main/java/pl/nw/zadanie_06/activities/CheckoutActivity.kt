package pl.nw.zadanie_06.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpPost
import com.google.firebase.auth.FirebaseAuth
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pl.nw.zadanie_06.Constants
import pl.nw.zadanie_06.MainActivity
import pl.nw.zadanie_06.common.local_db.LocalDatabase
import pl.nw.zadanie_06.databinding.ActivityCheckoutBinding
import pl.nw.zadanie_06.models.data.Address
import pl.nw.zadanie_06.models.data.Payment
import pl.nw.zadanie_06.models.view.CheckoutViewModel
import pl.nw.zadanie_06.utils.CartUtils
import pl.nw.zadanie_06.utils.StripeUtils
import pl.nw.zadanie_06.utils.UserUtils
import java.time.LocalDateTime

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var db: LocalDatabase
    private lateinit var auth: FirebaseAuth
    private val user = FirebaseAuth.getInstance().currentUser
    private val viewModel: CheckoutViewModel by viewModels {
        val db = LocalDatabase.getInstance(applicationContext)
        UserUtils.ensureAuth(this)
        CheckoutViewModel.Factory(db, user!!.uid)
    }
    private lateinit var payment: Payment
    lateinit var paymentIntentClientSecret: String
    lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    lateinit var paymentSheet: PaymentSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        db = LocalDatabase.getInstance(applicationContext)

        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var price = 0.0
        binding.apply {
            lifecycleScope.launch {
                viewModel.uiState.collect { checkoutState ->
                    price = checkoutState.price
                    binding.checkoutTotalPrice.text = "%.2f $".format(checkoutState.price)
                }
            }
        }

        binding.checkoutButton.setOnClickListener {
            val address = Address(
                line1 = binding.checkoutAddress1Input.text.toString(),
                line2 = binding.checkoutAddress2Input.text.toString(),
                postCode = binding.checkoutPostCodeInput.text.toString(),
                city = binding.checkoutCityInput.text.toString()
            )
            lifecycleScope.launch {
                val cartItems = db.cartDao().findCartByUserId(user!!.uid)!!.items
                payment = Payment(
                    userId = user.uid,
                    timestamp = LocalDateTime.now(),
                    amount = (price * 100).toInt(),
                    items = cartItems,
                    address = address
                )

                StripeUtils.ensureCustomerExists(db, user.uid)
                val stripeCustomer = db.stripeCustomerDao().findByUserId(user.uid)
                Constants.STRIPE_PAYMENT_INTENT_URL.httpPost(
                    listOf(
                        "customer" to stripeCustomer!!.stripeCustomerId,
                        "amount" to payment.amount,
                        "currency" to "usd",
                        "automatic_payment_methods[enabled]" to true
                    )
                ).authentication().basic(Constants.STRIPE_SECRET_KEY, "")
                    .responseObject<StripeUtils.StripePaymentIntent> { _, _, result ->
                        val (body, _) = result
                        paymentIntentClientSecret = body!!.client_secret
                        PaymentConfiguration.init(applicationContext, Constants.STRIPE_PUBLISHABLE_KEY)
                        presentPaymentSheet()
                    }
            }

        }
    }

    private fun presentPaymentSheet() {
        println("paymentinent: $paymentIntentClientSecret")
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret, PaymentSheet.Configuration(
                merchantDisplayName = "Norbert shop", allowsDelayedPaymentMethods = true
            )
        )
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        println("callowana jest ta funkcja")
        println(paymentSheetResult)
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                print("Canceled")
            }
            is PaymentSheetResult.Failed -> {
                print("Error: ${paymentSheetResult.error}")
            }
            is PaymentSheetResult.Completed -> {
                runBlocking {
                    db.paymentDao().insert(payment)
                    CartUtils.flushCart(db, user!!.uid)
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

}