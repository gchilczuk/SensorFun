package pl.chilczuk.grzegorz.sensorfun

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.content_crypto.*

class CryptoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crypto)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        fragment1.view?.setBackgroundColor(Color.RED)
        fragment2.view?.setBackgroundColor(Color.GREEN)
        fragment3.view?.setBackgroundColor(Color.BLUE)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_crypto, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            val intent = Intent(applicationContext, SettingsActivity::class.java)
            startActivity(intent)
            return true
        } else if (id == R.id.action_dice){
            val intent = Intent(applicationContext, DiceActivity::class.java)
            startActivity(intent)
            return true
        } else if (id == R.id.action_sensor){
            val intent = Intent(applicationContext, SensorsReview::class.java)
            startActivity(intent)
            return true
        } else if (id == R.id.action_about){
            val intent = Intent(applicationContext, AboutActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

}
