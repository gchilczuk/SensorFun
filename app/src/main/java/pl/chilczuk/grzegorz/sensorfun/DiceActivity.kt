package pl.chilczuk.grzegorz.sensorfun

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.content_dice.*
import kotlinx.android.synthetic.main.content_sensors_review.*
import java.lang.Thread.sleep
import java.util.Random


class DiceActivity : AppCompatActivity(), OnShakeListener {
    var mSensorManager : SensorManager? = null
    var sensor : Sensor? = null
    val sensorEventListener = MySensorEventListener()
    var diceValue = 0
    val randomGenerator = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        toast(getString(R.string.dice_prompt))
        sensorEventListener.onShakeListener = this

        prepareSpinner()
    }

    fun prepareSpinner(){
        val adapter = ArrayAdapter.createFromResource(this,
                R.array.dice_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dice_spinner.adapter = adapter
        dice_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                SettingsObject.diceType = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("onNothingSelected is not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        dice_spinner.setSelection(SettingsObject.diceType)
    }

    override fun onResume() {
        super.onResume()
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mSensorManager?.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager?.unregisterListener(sensorEventListener)
    }

    override fun onShake(){
        diceValue = randomGenerator.nextInt(SettingsObject.diceMaxValue) + 1
        diceTV.text = diceValue.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_dice, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            val intent = Intent(applicationContext, SettingsActivity::class.java)
            startActivity(intent)
            return true
        } else if (id == R.id.action_sensor){
            val intent = Intent(applicationContext, SensorsReview::class.java)
            startActivity(intent)
            return true
        } else if (id == R.id.action_about){
            val intent = Intent(applicationContext, AboutActivity::class.java)
            toast("Not implemented yet") //startActivity(intent)
            return true
        } else if (id == R.id.action_crypto){
            val intent = Intent(applicationContext, CryptoActivity::class.java)
            toast("Not implemented yet") //startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}

class MySensorEventListener() : SensorEventListener {
    var prevX = 0f
    var prevY = 0f
    var prevZ = 0f
    var lastUpdate = System.currentTimeMillis()
    val SHAKE_THRESHOLD = SettingsObject.SHAKE_THRESHOLD
    var onShakeListener : OnShakeListener? = null

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { }

    override fun onSensorChanged(event: SensorEvent) {
        val currX = event.values.get(0)
        val currY = event.values.get(1)
        val currZ = event.values.get(2)
        val curTime = System.currentTimeMillis()
        if ((curTime - lastUpdate) > 100) {
            val diffTime =(curTime - lastUpdate)
            lastUpdate = curTime;

            val speed = Math.abs(currX + currY + currZ - prevX - prevY - prevZ) / diffTime * 10000

            if (speed > SHAKE_THRESHOLD) {
                onShakeListener?.onShake()
            }
            prevX = currX;
            prevY = currY;
            prevZ = currZ;
        }
    }
}

interface OnShakeListener {
    fun onShake()
}




