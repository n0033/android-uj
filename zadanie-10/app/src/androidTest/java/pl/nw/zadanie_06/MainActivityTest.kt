package pl.nw.zadanie_06

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.nw.zadanie_06.common.local_db.LocalDatabase
import pl.nw.zadanie_06.utils.CartUtils

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    lateinit var db: LocalDatabase

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)


    @Before
    fun ensureAuthenticated() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = LocalDatabase.getInstance(context)
        if (FirebaseAuth.getInstance().currentUser == null) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                TestConstants.TEST_USER_LOGIN, TestConstants.TEST_USER_PASSWORD
            )
        }
    }

    @After
    fun tearDown() {
        runBlocking {
            CartUtils.flushCart(db, TestConstants.TEST_USER_ID)
        }
    }


    @Test
    fun checkNavbarElements() {
        val textView = onView(
            allOf(
                withText("SHOP"),
                withParent(
                    allOf(
                        withContentDescription("Shop"),
                        withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("SHOP")))

        val textView2 = onView(
            allOf(
                withText("CART"),
                withParent(
                    allOf(
                        withContentDescription("Cart"),
                        withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("CART")))

        val textView3 = onView(
            allOf(
                withText("CATEGORY"),
                withParent(
                    allOf(
                        withContentDescription("Category"),
                        withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("CATEGORY")))

        val textView4 = onView(
            allOf(
                withText("ADD PRODUCT"),
                withParent(
                    allOf(
                        withContentDescription("Add product"),
                        withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("ADD PRODUCT")))

        val textView5 = onView(
            allOf(
                withText("MY ACCOUNT"),
                withParent(
                    allOf(
                        withContentDescription("My account"),
                        withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView5.check(matches(withText("MY ACCOUNT")))
    }

    @Test
    fun switchToCartView() {
        val tabView = onView(
            allOf(
                withContentDescription("Cart"), childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs), 0
                    ), 1
                ), isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val button = onView(
            allOf(
                withId(R.id.checkout_button),
                withText("CHECKOUT"),
                withParent(
                    allOf(
                        withId(R.id.checkout_button_wrapper),
                        withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))
    }


    @Test
    fun switchToCategoryView() {
        val tabView = onView(
            allOf(
                withContentDescription("Category"), childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs), 0
                    ), 2
                ), isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val recyclerView = onView(
            allOf(
                withParent(
                    allOf(
                        withId(R.id.pager),
                        withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))
                    )
                ), isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))
    }


    @Test
    fun switchToAddProductView() {
        val tabView = onView(
            allOf(
                withContentDescription("Add product"), childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs), 0
                    ), 3
                ), isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val linearLayout = onView(
            allOf(
                withId(R.id.add_product_product_name),
                withParent(withParent(IsInstanceOf.instanceOf(FrameLayout::class.java))),
                isDisplayed()
            )
        )
        linearLayout.check(matches(isDisplayed()))

        val editText = onView(
            allOf(
                withId(R.id.add_product_description_input), withParent(
                    allOf(
                        withId(R.id.add_product_product_description),
                        withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))
                    )
                ), isDisplayed()
            )
        )
        editText.check(matches(isDisplayed()))

        val editText2 = onView(
            allOf(
                withId(R.id.add_product_price_input), withParent(
                    allOf(
                        withId(R.id.add_product_product_price),
                        withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))
                    )
                ), isDisplayed()
            )
        )
        editText2.check(matches(withText("")))

        val spinner = onView(
            allOf(
                withId(R.id.category_spinner), withParent(
                    allOf(
                        withId(R.id.add_product_category_selector),
                        withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))
                    )
                ), isDisplayed()
            )
        )
        spinner.check(matches(isDisplayed()))

        val spinner2 = onView(
            allOf(
                withId(R.id.category_spinner), withParent(
                    allOf(
                        withId(R.id.add_product_category_selector),
                        withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))
                    )
                ), isDisplayed()
            )
        )
        spinner2.check(matches(isDisplayed()))
    }


    @Test
    fun switchToMyAccountView() {
        val tabView = onView(
            allOf(
                withContentDescription("My account"), childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs), 0
                    ), 4
                ), isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val textView = onView(
            allOf(
                withText("My account"), withParent(
                    allOf(
                        withId(R.id.my_account_title_wrapper),
                        withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))
                    )
                ), isDisplayed()
            )
        )
        textView.check(matches(withText("My account")))
    }


    @Test
    fun enterProductDetailsView() {
        val recyclerView = onView(
            allOf(
                withId(R.id.product_list), childAtPosition(
                    withId(R.id.item_list), 0
                )
            )
        )
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, ViewActions.click()
            )
        )

        val button = onView(
            allOf(
                withId(R.id.product_details_back_button),
                withText("BACK"),
                withParent(
                    allOf(
                        withId(R.id.product_details_navbar),
                        withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))
    }

    @Test
    fun addToCart() {
        val recyclerView = onView(
            allOf(
                withId(R.id.product_list), childAtPosition(
                    withId(R.id.item_list), 0
                )
            )
        )
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, ViewActions.click()
            )
        )

        val materialButton = onView(
            allOf(
                withId(R.id.product_details_add_to_cart),
                withText("Add to cart"),
                childAtPosition(
                    childAtPosition(
                        withClassName(Matchers.`is`("android.widget.LinearLayout")), 1
                    ), 2
                ),
                isDisplayed()
            )
        )
        materialButton.perform(ViewActions.click())

        val materialButton2 = onView(
            allOf(
                withId(R.id.product_details_back_button),
                withText("BACK"),
                childAtPosition(
                    allOf(
                        withId(R.id.product_details_navbar), childAtPosition(
                            withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ), 0
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(ViewActions.click())

        val tabView = onView(
            allOf(
                withContentDescription("Cart"), childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs), 0
                    ), 1
                ), isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val textView = onView(
            allOf(
                withId(R.id.cart_item_quantity),
                withText("1"),
                withParent(
                    withParent(
                        IsInstanceOf.instanceOf(
                            RelativeLayout::class.java
                        )
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("1")))
    }


    @Test
    fun decreaseItemAmountInCart() {
        val tabView = onView(
            allOf(
                withContentDescription("Cart"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val tabView2 = onView(
            allOf(
                withContentDescription("Shop"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        tabView2.perform(ViewActions.click())

        val recyclerView = onView(
            allOf(
                withId(R.id.product_list),
                childAtPosition(
                    withId(R.id.item_list),
                    0
                )
            )
        )
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )

        val materialButton = onView(
            allOf(
                withId(R.id.product_details_add_to_cart),
                withText("Add to cart"),
                childAtPosition(
                    childAtPosition(
                        withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        1
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton.perform(ViewActions.click())

        val materialButton2 = onView(
            allOf(
                withId(R.id.product_details_add_to_cart),
                withText("Add to cart"),
                childAtPosition(
                    childAtPosition(
                        withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        1
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(ViewActions.click())

        val materialButton3 = onView(
            allOf(
                withId(R.id.product_details_back_button),
                withText("BACK"),
                childAtPosition(
                    allOf(
                        withId(R.id.product_details_navbar),
                        childAtPosition(
                            withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(ViewActions.click())

        val tabView3 = onView(
            allOf(
                withContentDescription("Cart"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        tabView3.perform(ViewActions.click())

        val materialTextView = onView(
            allOf(
                withId(R.id.cart_item_remove), withText("-"),
                childAtPosition(
                    childAtPosition(
                        withClassName(Matchers.`is`("android.widget.RelativeLayout")),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialTextView.perform(ViewActions.click())

        val textView = onView(
            allOf(
                withId(R.id.cart_item_quantity), withText("1"),
                withParent(
                    withParent(
                        IsInstanceOf.instanceOf(
                            RelativeLayout::class.java
                        )
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("1")))
    }

    @Test
    fun addQuantityTwo() {
        val recyclerView = onView(
            allOf(
                withId(R.id.product_list),
                childAtPosition(
                    withId(R.id.item_list),
                    0
                )
            )
        )
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )

        val materialButton = onView(
            allOf(
                withId(R.id.product_details_add_to_cart),
                withText("Add to cart"),
                childAtPosition(
                    childAtPosition(
                        withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        1
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton.perform(ViewActions.click())

        val materialButton2 = onView(
            allOf(
                withId(R.id.product_details_back_button),
                withText("BACK"),
                childAtPosition(
                    allOf(
                        withId(R.id.product_details_navbar),
                        childAtPosition(
                            withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(ViewActions.click())

        val tabView = onView(
            allOf(
                withContentDescription("Cart"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val materialTextView = onView(
            allOf(
                withId(R.id.cart_item_add), withText("+"),
                childAtPosition(
                    childAtPosition(
                        withClassName(Matchers.`is`("android.widget.RelativeLayout")),
                        1
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialTextView.perform(ViewActions.click())

        val textView = onView(
            allOf(
                withId(R.id.cart_item_quantity), withText("2"),
                withParent(
                    withParent(
                        IsInstanceOf.instanceOf(
                            RelativeLayout::class.java
                        )
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("2")))
    }

    @Test
    fun goToOrdersView() {
        val tabView = onView(
            allOf(
                withContentDescription("My account"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val materialButton = onView(
            allOf(
                withId(R.id.goto_orders_button), withText("Orders"),
                childAtPosition(
                    allOf(
                        withId(R.id.goto_orders_wrapper),
                        childAtPosition(
                            withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton.perform(ViewActions.click())

        val textView = onView(
            allOf(
                withText("Orders"),
                withParent(withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))
    }

    @Test
    fun goToCheckoutView() {
        val tabView = onView(
            allOf(
                withContentDescription("Cart"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val tabView2 = onView(
            allOf(
                withContentDescription("Shop"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        tabView2.perform(ViewActions.click())

        val recyclerView = onView(
            allOf(
                withId(R.id.product_list),
                childAtPosition(
                    withId(R.id.item_list),
                    0
                )
            )
        )
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )

        val materialButton = onView(
            allOf(
                withId(R.id.product_details_add_to_cart),
                withText("Add to cart"),
                childAtPosition(
                    childAtPosition(
                        withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        1
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton.perform(ViewActions.click())

        val materialButton2 = onView(
            allOf(
                withId(R.id.product_details_back_button),
                withText("BACK"),
                childAtPosition(
                    allOf(
                        withId(R.id.product_details_navbar),
                        childAtPosition(
                            withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(ViewActions.click())

        val tabView3 = onView(
            allOf(
                withContentDescription("Cart"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        tabView3.perform(ViewActions.click())

        val materialButton3 = onView(
            allOf(
                withId(R.id.checkout_button), withText("Checkout"),
                childAtPosition(
                    allOf(
                        withId(R.id.checkout_button_wrapper),
                        childAtPosition(
                            withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(ViewActions.click())

        val textView = onView(
            allOf(
                withText("Checkout"),
                withParent(
                    allOf(
                        withId(R.id.checkout_heading),
                        withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))
    }


    @Test
    fun switchToCartThenProducts() {
        val tabView = onView(
            allOf(
                withContentDescription("Cart"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val tabView2 = onView(
            allOf(
                withContentDescription("Shop"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        tabView2.perform(ViewActions.click())

        val linearLayout = onView(
            allOf(
                withId(R.id.item_list),
                isDisplayed()
            )
        )
        linearLayout.check(matches(isDisplayed()))
    }


    @Test
    fun appTraversal() {

        val tabView = onView(
            allOf(
                withContentDescription("Shop"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val textView = onView(
            allOf(
                withId(R.id.product_list_title),
                withText("Shop"),
                withParent(withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))

        val tabView2 = onView(
            allOf(
                withContentDescription("Cart"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        tabView2.perform(ViewActions.click())

        val textView2 = onView(
            allOf(
                withId(R.id.cart_title),
                withText("Cart"),
                withParent(withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))),
                isDisplayed()
            )
        )
        textView2.check(matches(isDisplayed()))

        val button = onView(
            allOf(
                withId(R.id.checkout_button), withText("CHECKOUT"),
                withParent(
                    allOf(
                        withId(R.id.checkout_button_wrapper),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val tabView3 = onView(
            allOf(
                withContentDescription("Category"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        tabView3.perform(ViewActions.click())

        val textView3 = onView(
            allOf(
                withId(R.id.categories_title),
                withText("Categories"),
                withParent(withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))),
                isDisplayed()
            )
        )
        textView3.check(matches(isDisplayed()))

        val tabView4 = onView(
            allOf(
                withContentDescription("Add product"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        tabView4.perform(ViewActions.click())

        val textView4 = onView(
            allOf(
                withText("Add product"),
                withParent(
                    allOf(
                        withId(R.id.add_product_heading),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView4.check(matches(isDisplayed()))

        val textView5 = onView(
            allOf(
                withText("Name"),
                withParent(
                    allOf(
                        withId(R.id.add_product_product_name),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView5.check(matches(isDisplayed()))

        val textView6 = onView(
            allOf(
                withText("Description"),
                withParent(
                    allOf(
                        withId(R.id.add_product_product_description),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView6.check(matches(isDisplayed()))

        val textView7 = onView(
            allOf(
                withText("Price"),
                withParent(
                    allOf(
                        withId(R.id.add_product_product_price),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView7.check(matches(isDisplayed()))

        val textView8 = onView(
            allOf(
                withText("Category"),
                withParent(
                    allOf(
                        withId(R.id.add_product_category_selector),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView8.check(matches(isDisplayed()))

        val button2 = onView(
            allOf(
                withId(R.id.add_product_add_button), withText("ADD"),
                withParent(
                    allOf(
                        withId(R.id.add_product_add_button_wrapper),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        button2.check(matches(isDisplayed()))

        val tabView5 = onView(
            allOf(
                withContentDescription("My account"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        tabView5.perform(ViewActions.click())

        val textView9 = onView(
            allOf(
                withId(R.id.my_account_title),
                withText("My account"),
                withParent(
                    allOf(
                        withId(R.id.my_account_title_wrapper),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView9.check(matches(isDisplayed()))

        val button3 = onView(
            allOf(
                withId(R.id.goto_orders_button), withText("ORDERS"),
                withParent(
                    allOf(
                        withId(R.id.goto_orders_wrapper),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        button3.check(matches(isDisplayed()))

        val materialButton = onView(
            allOf(
                withId(R.id.goto_orders_button), withText("Orders"),
                childAtPosition(
                    allOf(
                        withId(R.id.goto_orders_wrapper),
                        childAtPosition(
                            withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton.perform(ViewActions.click())

        val textView10 = onView(
            allOf(
                withId(R.id.orders_title),
                withText("Orders"),
                withParent(withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))),
                isDisplayed()
            )
        )
        textView10.check(matches(isDisplayed()))
    }


    @Test
    fun verifyCheckoutFields() {
        val tabView0 = onView(
            allOf(
                withContentDescription("Cart"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        tabView0.perform(ViewActions.click())

        val tabView2 = onView(
            allOf(
                withContentDescription("Shop"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        tabView2.perform(ViewActions.click())

        val recyclerView = onView(
            allOf(
                withId(R.id.product_list),
                childAtPosition(
                    withId(R.id.item_list),
                    0
                )
            )
        )
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )

        val materialButton = onView(
            allOf(
                withId(R.id.product_details_add_to_cart), withText("Add to cart"),
                childAtPosition(
                    childAtPosition(
                        withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        1
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton.perform(ViewActions.click())

        val materialButton2 = onView(
            allOf(
                withId(R.id.product_details_back_button), withText("BACK"),
                childAtPosition(
                    allOf(
                        withId(R.id.product_details_navbar),
                        childAtPosition(
                            withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(ViewActions.click())

        val tabView = onView(
            allOf(
                withContentDescription("Cart"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val textView = onView(
            allOf(
                withId(R.id.cart_title), withText("Cart"),
                withParent(withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Cart")))

        val textView2 = onView(
            allOf(
                withId(R.id.cart_item_quantity), withText("1"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.RelativeLayout::class.java))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("1")))

        val button = onView(
            allOf(
                withId(R.id.checkout_button), withText("CHECKOUT"),
                withParent(
                    allOf(
                        withId(R.id.checkout_button_wrapper),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val materialButton3 = onView(
            allOf(
                withId(R.id.checkout_button), withText("Checkout"),
                childAtPosition(
                    allOf(
                        withId(R.id.checkout_button_wrapper),
                        childAtPosition(
                            withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(ViewActions.click())

        val textView3 = onView(
            allOf(
                withText("Checkout"),
                withParent(
                    allOf(
                        withId(R.id.checkout_heading),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("Checkout")))

        val textView4 = onView(
            allOf(
                withId(R.id.checkout_total_price),
                withParent(
                    allOf(
                        withId(R.id.checkout_heading),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView4.check(matches(isDisplayed()))

        val textView5 = onView(
            allOf(
                withText("Address (line 1)"),
                withParent(
                    allOf(
                        withId(R.id.checkout_address_1),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView5.check(matches(isDisplayed()))

        val textView6 = onView(
            allOf(
                withText("Address (line 2)"),
                withParent(
                    allOf(
                        withId(R.id.checkout_address_2),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView6.check(matches(isDisplayed()))

        val textView7 = onView(
            allOf(
                withText("Post code"),
                withParent(
                    allOf(
                        withId(R.id.checkout_post_code),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView7.check(matches(isDisplayed()))

        val textView8 = onView(
            allOf(
                withText("City"),
                withParent(
                    allOf(
                        withId(R.id.checkout_city),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView8.check(matches(isDisplayed()))

        val button2 = onView(
            allOf(
                withId(R.id.pay_with_stripe_button), withText("PAY WITH STRIPE"),
                withParent(
                    allOf(
                        withId(R.id.checkout_button_wrapper),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        button2.check(matches(isDisplayed()))

        val button3 = onView(
            allOf(
                withId(R.id.pay_with_payu_button), withText("PAY WITH PAYU"),
                withParent(
                    allOf(
                        withId(R.id.checkout_button_wrapper),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        button3.check(matches(isDisplayed()))
    }


    @Test
    fun checkoutCheckDropdownText() {
        val tabView = onView(
            allOf(
                withContentDescription("Category"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val tabView2 = onView(
            allOf(
                withContentDescription("Add product"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.pager_tabs),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        tabView2.perform(ViewActions.click())


        val textView = onView(
            allOf(
                withText("Add product"),
                withParent(
                    allOf(
                        withId(R.id.add_product_heading),
                        (IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Add product")))

        val textView2 = onView(
            allOf(
                withText("Name"),
                withParent(
                    allOf(
                        withId(R.id.add_product_product_name),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Name")))

        val textView3 = onView(
            allOf(
                withText("Description"),
                withParent(
                    allOf(
                        withId(R.id.add_product_product_description),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("Description")))

        val textView4 = onView(
            allOf(
                withText("Price"),
                withParent(
                    allOf(
                        withId(R.id.add_product_product_price),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("Price")))

        val textView5 = onView(
            allOf(
                withText("Category"),
                withParent(
                    allOf(
                        withId(R.id.add_product_category_selector),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView5.check(matches(withText("Category")))

        val textView6 = onView(
            allOf(
                withId(R.id.category_dropdown_item), withText("Alcoholic drinks"),
                withParent(
                    allOf(
                        withId(R.id.category_spinner),
                        withParent(withId(R.id.add_product_category_selector))
                    )
                ),
                isDisplayed()
            )
        )
        textView6.check(matches(withText("Alcoholic drinks")))

        val textView7 = onView(
            allOf(
                withId(R.id.category_dropdown_item), withText("Alcoholic drinks"),
                withParent(
                    allOf(
                        withId(R.id.category_spinner),
                        withParent(withId(R.id.add_product_category_selector))
                    )
                ),
                isDisplayed()
            )
        )
        textView7.check(matches(withText("Alcoholic drinks")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent) && view == parent.getChildAt(
                    position
                )
            }
        }
    }
}
