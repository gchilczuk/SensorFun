package pl.chilczuk.grzegorz.sensorfun

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.content_dice.*
import kotlinx.android.synthetic.main.content_settings.*

object SettingsObject{
    var accurancy = 0f
    var SHAKE_THRESHOLD = 1000
    var diceMaxValue = 6;
    var diceType = 1L
        set(value) {
            when(value){
                0L -> diceMaxValue = 4
                1L -> diceMaxValue = 6
                2L -> diceMaxValue = 8
                3L -> diceMaxValue = 10
                4L -> diceMaxValue = 12
                5L -> diceMaxValue = 20
                6L -> diceMaxValue = 100
            }
            field = value
        }
    var sensiticity = 1L
    set(value) {
        when(value){
            0L -> SHAKE_THRESHOLD = 300
            1L -> SHAKE_THRESHOLD = 1000
            2L -> SHAKE_THRESHOLD = 3000
        }
        field = value
    }
}

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        prepareDiceTypeSpinner()
        prepareDiceSensitivitySpinner()
    }

    fun prepareDiceTypeSpinner(){
        val adapter = ArrayAdapter.createFromResource(this,
                R.array.dice_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dice_type_spinner.adapter = adapter
        dice_type_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                SettingsObject.diceType = id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("onNothingSelected is not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        dice_type_spinner.setSelection(1)

    }

    fun prepareDiceSensitivitySpinner(){
        val adapter = ArrayAdapter.createFromResource(this,
                R.array.dice_sensitivity, android.R.layout.simple_spinner_dropdown_item)
        dice_sensitivity_spinner.adapter = adapter
        dice_sensitivity_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                SettingsObject.sensiticity = id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("onNothingSelected is not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        dice_sensitivity_spinner.setSelection(1)
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_dice){
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
        } else if (id == R.id.action_crypto){
            val intent = Intent(applicationContext, CryptoActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

}
