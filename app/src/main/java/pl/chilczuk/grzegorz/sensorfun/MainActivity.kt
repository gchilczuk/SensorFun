package pl.chilczuk.grzegorz.sensorfun

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Math.abs

class MainActivity : AppCompatActivity(){
    var mSensorManager : SensorManager? = null
    var sensor : Sensor? = null
    var sensorList : List<Sensor>? = null
    var currentSensor = 0
    val sensorEventListener = MySensorEventListener()
    var accuracy = 0.2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = toolbar
        setSupportActionBar(toolbar)
        nextB.setOnClickListener { nextSensor() }
        prevB.setOnClickListener { prevSensor() }
    }

    override fun onResume() {
        super.onResume()
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorList = mSensorManager?.getSensorList(Sensor.TYPE_ALL)
        currentSensor = 0
        sensor = sensorList?.get(currentSensor)
        if (sensor != null) {
            availabilityTV.text = sensor?.name
            availabilityTV.setTextColor(Color.GREEN)
            registerCurrentSensorEventListener()
        } else {
            availabilityTV.text = getString(R.string.unavail)
            availabilityTV.setTextColor(Color.RED)
        }
        setValuesTVToZero()
    }

    override fun onPause() {
        super.onPause()
       unregisterCurrentSensorEventListener()
    }

    fun unregisterCurrentSensorEventListener(){
        mSensorManager?.unregisterListener(sensorEventListener)
        sensorEventListener.reset()
    }

    fun registerCurrentSensorEventListener(){
        mSensorManager?.registerListener(sensorEventListener, sensor,
                SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun nextSensor(){
        if (sensorList != null){
            val len : Int = sensorList?.size as Int
            if (currentSensor >= len-1 ){
                currentSensor = len-1
            } else {
                unregisterCurrentSensorEventListener()
                currentSensor ++
                sensor = sensorList?.get(currentSensor)
                registerCurrentSensorEventListener()
                availabilityTV.text = sensor?.name
                setValuesTVToZero()
            }
        }
    }

    fun prevSensor(){
        if (sensorList != null){
            if (currentSensor == 0 ){
                currentSensor = 0
            } else {
                unregisterCurrentSensorEventListener()
                currentSensor --
                sensor = sensorList?.get(currentSensor)
                registerCurrentSensorEventListener()
                availabilityTV.text = sensor?.name
                setValuesTVToZero()
            }
        }
    }

    fun setValuesTVToZero(){
        valueXTV.text = getString(R.string.zero)
        valueYTV.text = getString(R.string.zero)
        valueZTV.text = getString(R.string.zero)
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
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    inner class MySensorEventListener() : SensorEventListener {
        var prevX = 0f
        var prevY = 0f
        var prevZ = 0f

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            val currX = event?.values?.get(0) as Float
            val currY = event.values?.get(1) as Float
            val currZ = event.values?.get(2) as Float
            if (abs(prevX - currX) > accuracy){
                prevX = currX
                valueXTV.text = currX.toString()
            }
            if (abs(prevY - currY) > accuracy){
                prevY = currY
                valueYTV.text = currY.toString()
            }
            if (abs(prevZ - currZ) > accuracy){
                prevZ = currZ
                valueZTV.text = currZ.toString()
            }
        }

        fun reset(){
            prevX = 0f
            prevY = 0f
            prevZ = 0f
        }
    }
}


fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()