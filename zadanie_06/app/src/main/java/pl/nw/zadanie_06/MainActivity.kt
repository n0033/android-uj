package pl.nw.zadanie_06

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import pl.nw.zadanie_06.Constants.COLLECTION
import pl.nw.zadanie_06.databinding.ActivityMainBinding
import pl.nw.zadanie_06.models.Category
import pl.nw.zadanie_06.models.Product
import pl.nw.zadanie_06.repository.RealtimeDatabase


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val category = Category(name = "Alcoholic drinks");
        val categoryRepository = RealtimeDatabase<Category>()
        categoryRepository.create("${COLLECTION["category"]}/${category.uid}", category)

        val productRepository = RealtimeDatabase<Product>()
        val product = Product(name = "Beer", description = "Refreshing IPA 5%", price = 1.70)


        val categoryReference = RealtimeDatabase<Category>().read(
            COLLECTION["category"]!!
        )

        val categoryList = mutableListOf<Category>()
        val categoryListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (categorySnapshot in dataSnapshot.children) {
                    categoryList.add(categorySnapshot.getValue<Category>()!!)
                }
                for (el in categoryList) {
                    if (el.name == "Alcoholic drinks") {
                        product.categoryId = el.uid;
                        productRepository.create("${COLLECTION["product"]}/${product.uid}", product)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", error.toException())
            }
        }

        categoryReference.addValueEventListener(categoryListener);





        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}