package pl.chilczuk.grzegorz.sensorfun

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import kotlinx.android.synthetic.main.activity_sensors_review.*
import kotlinx.android.synthetic.main.content_sensors_review.*
import java.lang.Math.abs

class SensorsReview : AppCompatActivity() {

    var mSensorManager : SensorManager? = null
    var sensor : Sensor? = null
    var sensorList : List<Sensor>? = null
    var currentSensor = 0
    val sensorEventListener = MySensorEventListener()
    var accuracy = SettingsObject.accurancy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensors_review)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

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

    inner class MySensorEventListener() : SensorEventListener {
        var prevX = 0f
        var prevY = 0f
        var prevZ = 0f

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { }

        override fun onSensorChanged(event: SensorEvent) {
            val currX = event.values.get(0)
            val currY = event.values.get(1)
            val currZ = event.values.get(2)
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
