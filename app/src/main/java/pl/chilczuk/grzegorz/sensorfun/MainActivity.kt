package pl.chilczuk.grzegorz.sensorfun

import android.content.Context
import android.content.Intent
import android.media.audiofx.BassBoost
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        StartSensorOverviewB.setOnClickListener {
            val intent = Intent(applicationContext, SensorsReview::class.java)
            startActivity(intent)
        }

        StartDiceB.setOnClickListener {
            val intent = Intent(applicationContext, DiceActivity::class.java)
            startActivity(intent)
        }

        StartSettingsB.setOnClickListener {
            val intent = Intent(applicationContext, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
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
        }

        return super.onOptionsItemSelected(item)
    }
}

object SettingsObject{
    var accurancy = 0f
    var SHAKE_THRESHOLD = 800
}

fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()