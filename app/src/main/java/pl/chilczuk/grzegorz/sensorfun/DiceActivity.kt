package pl.chilczuk.grzegorz.sensorfun

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
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

        diceTV.text = diceValue.toString()
        sensorEventListener.onShakeListener = this
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
        diceValue = randomGenerator.nextInt(6) + 1
        diceTV.text = diceValue.toString()
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
