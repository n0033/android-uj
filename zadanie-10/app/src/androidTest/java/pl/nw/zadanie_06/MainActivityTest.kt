package pl.nw.zadanie_06


import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.runBlocking
import net.bytebuddy.asm.Advice.Local
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.nw.zadanie_06.common.local_db.LocalDatabase
import pl.nw.zadanie_06.models.data.User
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
        runBlocking {
            if (FirebaseAuth.getInstance().currentUser == null) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    TestConstants.TEST_USER_LOGIN, TestConstants.TEST_USER_PASSWORD
                )
            }
        }
    }

    @After
    fun tearDown() {
        runBlocking {
            CartUtils.flushCart(db, TestConstants.TEST_USER_ID)
        }
    }

    @Test
    fun switchToCartView() {
        val tabView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withContentDescription("Cart"), childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.pager_tabs), 0
                    ), 1
                ), ViewMatchers.isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val button = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.checkout_button),
                ViewMatchers.withText("CHECKOUT"),
                ViewMatchers.withParent(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.checkout_button_wrapper),
                        ViewMatchers.withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))
                    )
                ),
                ViewMatchers.isDisplayed()
            )
        )
        button.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    @Test
    fun switchToCategoryView() {
        val tabView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withContentDescription("Category"), childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.pager_tabs), 0
                    ), 2
                ), ViewMatchers.isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val recyclerView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withParent(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.pager),
                        ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))
                    )
                ), ViewMatchers.isDisplayed()
            )
        )
        recyclerView.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    @Test
    fun switchToAddProductView() {
        val tabView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withContentDescription("Add product"), childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.pager_tabs), 0
                    ), 3
                ), ViewMatchers.isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val linearLayout = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.add_product_product_name),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(FrameLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        linearLayout.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val editText = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.add_product_description_input), ViewMatchers.withParent(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.add_product_product_description),
                        ViewMatchers.withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))
                    )
                ), ViewMatchers.isDisplayed()
            )
        )
        editText.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val editText2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.add_product_price_input), ViewMatchers.withParent(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.add_product_product_price),
                        ViewMatchers.withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))
                    )
                ), ViewMatchers.isDisplayed()
            )
        )
        editText2.check(ViewAssertions.matches(ViewMatchers.withText("")))

        val spinner = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.category_spinner), ViewMatchers.withParent(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.add_product_category_selector),
                        ViewMatchers.withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))
                    )
                ), ViewMatchers.isDisplayed()
            )
        )
        spinner.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val spinner2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.category_spinner), ViewMatchers.withParent(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.add_product_category_selector),
                        ViewMatchers.withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))
                    )
                ), ViewMatchers.isDisplayed()
            )
        )
        spinner2.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    @Test
    fun switchToMyAccountView() {
        val tabView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withContentDescription("My account"), childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.pager_tabs), 0
                    ), 4
                ), ViewMatchers.isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val textView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("My account"), ViewMatchers.withParent(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.my_account_title_wrapper),
                        ViewMatchers.withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))
                    )
                ), ViewMatchers.isDisplayed()
            )
        )
        textView.check(ViewAssertions.matches(ViewMatchers.withText("My account")))
    }


    @Test
    fun enterProductDetailsView() {
        val recyclerView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.product_list), childAtPosition(
                    ViewMatchers.withId(R.id.item_list), 0
                )
            )
        )
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, ViewActions.click()
            )
        )

        val button = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.product_details_back_button),
                ViewMatchers.withText("BACK"),
                ViewMatchers.withParent(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.product_details_navbar),
                        ViewMatchers.withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))
                    )
                ),
                ViewMatchers.isDisplayed()
            )
        )
        button.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun addToCart() {
        val recyclerView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.product_list), childAtPosition(
                    ViewMatchers.withId(R.id.item_list), 0
                )
            )
        )
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, ViewActions.click()
            )
        )

        val materialButton = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.product_details_add_to_cart),
                ViewMatchers.withText("Add to cart"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")), 1
                    ), 2
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialButton.perform(ViewActions.click())

        val materialButton2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.product_details_back_button),
                ViewMatchers.withText("BACK"),
                childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.product_details_navbar), childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ), 0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialButton2.perform(ViewActions.click())

        val tabView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withContentDescription("Cart"), childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.pager_tabs), 0
                    ), 1
                ), ViewMatchers.isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val textView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.cart_item_quantity),
                ViewMatchers.withText("1"),
                ViewMatchers.withParent(
                    ViewMatchers.withParent(
                        IsInstanceOf.instanceOf(
                            RelativeLayout::class.java
                        )
                    )
                ),
                ViewMatchers.isDisplayed()
            )
        )
        textView.check(ViewAssertions.matches(ViewMatchers.withText("1")))
    }


    @Test
    fun decreaseItemAmountInCart() {
        val tabView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withContentDescription("Cart"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.pager_tabs),
                        0
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val tabView2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withContentDescription("Shop"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.pager_tabs),
                        0
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        tabView2.perform(ViewActions.click())

        val recyclerView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.product_list),
                childAtPosition(
                    ViewMatchers.withId(R.id.item_list),
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

        val materialButton = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.product_details_add_to_cart),
                ViewMatchers.withText("Add to cart"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        1
                    ),
                    2
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialButton.perform(ViewActions.click())

        val materialButton2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.product_details_add_to_cart),
                ViewMatchers.withText("Add to cart"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        1
                    ),
                    2
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialButton2.perform(ViewActions.click())

        val materialButton3 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.product_details_back_button),
                ViewMatchers.withText("BACK"),
                childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.product_details_navbar),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialButton3.perform(ViewActions.click())

        val tabView3 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withContentDescription("Cart"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.pager_tabs),
                        0
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        )
        tabView3.perform(ViewActions.click())

        val materialTextView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.cart_item_remove), ViewMatchers.withText("-"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.RelativeLayout")),
                        1
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialTextView.perform(ViewActions.click())

        val textView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.cart_item_quantity), ViewMatchers.withText("1"),
                ViewMatchers.withParent(
                    ViewMatchers.withParent(
                        IsInstanceOf.instanceOf(
                            RelativeLayout::class.java
                        )
                    )
                ),
                ViewMatchers.isDisplayed()
            )
        )
        textView.check(ViewAssertions.matches(ViewMatchers.withText("1")))
    }

    @Test
    fun addQuantityTwo() {
        val recyclerView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.product_list),
                childAtPosition(
                    ViewMatchers.withId(R.id.item_list),
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

        val materialButton = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.product_details_add_to_cart),
                ViewMatchers.withText("Add to cart"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        1
                    ),
                    2
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialButton.perform(ViewActions.click())

        val materialButton2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.product_details_back_button),
                ViewMatchers.withText("BACK"),
                childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.product_details_navbar),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialButton2.perform(ViewActions.click())

        val tabView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withContentDescription("Cart"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.pager_tabs),
                        0
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val materialTextView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.cart_item_add), ViewMatchers.withText("+"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.RelativeLayout")),
                        1
                    ),
                    2
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialTextView.perform(ViewActions.click())

        val textView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.cart_item_quantity), ViewMatchers.withText("2"),
                ViewMatchers.withParent(
                    ViewMatchers.withParent(
                        IsInstanceOf.instanceOf(
                            RelativeLayout::class.java
                        )
                    )
                ),
                ViewMatchers.isDisplayed()
            )
        )
        textView.check(ViewAssertions.matches(ViewMatchers.withText("2")))
    }


    @Test
    fun addTwoDifferentProductsToCart() {
        val recyclerView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.product_list),
                childAtPosition(
                    ViewMatchers.withId(R.id.item_list),
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

        val materialButton = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.product_details_add_to_cart),
                ViewMatchers.withText("Add to cart"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        1
                    ),
                    2
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialButton.perform(ViewActions.click())

        val materialButton2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.product_details_back_button),
                ViewMatchers.withText("BACK"),
                childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.product_details_navbar),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialButton2.perform(ViewActions.click())

        val recyclerView2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.product_list),
                childAtPosition(
                    ViewMatchers.withId(R.id.item_list),
                    0
                )
            )
        )
        recyclerView2.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                ViewActions.click()
            )
        )

        val materialButton3 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.product_details_add_to_cart),
                ViewMatchers.withText("Add to cart"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        1
                    ),
                    2
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialButton3.perform(ViewActions.click())

        val materialButton4 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.product_details_back_button),
                ViewMatchers.withText("BACK"),
                childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.product_details_navbar),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialButton4.perform(ViewActions.click())

        val tabView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withContentDescription("Cart"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.pager_tabs),
                        0
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val textView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.cart_item_name), ViewMatchers.withText("Apple juice"),
                ViewMatchers.withParent(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.cart_item_first_column),
                        ViewMatchers.withParent(IsInstanceOf.instanceOf(RelativeLayout::class.java))
                    )
                ),
                ViewMatchers.isDisplayed()
            )
        )
        textView.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val textView2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.cart_item_name), ViewMatchers.withText("Orange Juice"),
                ViewMatchers.withParent(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.cart_item_first_column),
                        ViewMatchers.withParent(IsInstanceOf.instanceOf(RelativeLayout::class.java))
                    )
                ),
                ViewMatchers.isDisplayed()
            )
        )
        textView2.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun goToCheckoutView() {
        val recyclerView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.product_list),
                childAtPosition(
                    ViewMatchers.withId(R.id.item_list),
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

        val materialButton = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.product_details_add_to_cart),
                ViewMatchers.withText("Add to cart"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        1
                    ),
                    2
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialButton.perform(ViewActions.click())

        val materialButton2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.product_details_back_button),
                ViewMatchers.withText("BACK"),
                childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.product_details_navbar),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialButton2.perform(ViewActions.click())

        val tabView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withContentDescription("Cart"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.pager_tabs),
                        0
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        )
        tabView.perform(ViewActions.click())

        val materialButton3 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.checkout_button), ViewMatchers.withText("Checkout"),
                childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.checkout_button_wrapper),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            2
                        )
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialButton3.perform(ViewActions.click())

        val textView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("Checkout"),
                ViewMatchers.withParent(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.checkout_heading),
                        ViewMatchers.withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))
                    )
                ),
                ViewMatchers.isDisplayed()
            )
        )
        textView.check(ViewAssertions.matches(ViewMatchers.withText("Checkout")))

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
