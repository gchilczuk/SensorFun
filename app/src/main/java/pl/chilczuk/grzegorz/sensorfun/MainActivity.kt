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
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Math.cos
import java.lang.Math.sin

class MainActivity : AppCompatActivity(), SensorEventListener{
    val NS2S = 1.0f / 1000000000.0f
    val deltaRotationVector = arrayOf(0f,0f, 0f, 0f)
    var timestamp = 0L
    var mSensorManager : SensorManager? = null
    var sensor : Sensor? = null
    var sensorList : List<Sensor>? = null
    var currentSensor = 0

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
        sensor = sensorList?.get(0)
        if (sensor != null) {
            availabilityTV.text = sensor?.name
            availabilityTV.setTextColor(Color.GREEN)
            headerTV.visibility = View.VISIBLE
            valueXTV.visibility = View.VISIBLE
            valueYTV.visibility = View.VISIBLE
            valueZTV.visibility = View.VISIBLE
        } else {
            availabilityTV.text = "Sensor is " + getString(R.string.unavail)
            availabilityTV.setTextColor(Color.RED)
            headerTV.visibility = View.INVISIBLE
            valueXTV.visibility = View.INVISIBLE
            valueYTV.visibility = View.INVISIBLE
            valueZTV.visibility = View.INVISIBLE
        }
        var sensorid = 0
        while(sensorid < sensorList?.size as Int){
            mSensorManager?.registerListener(object : SensorEventListener {
                val number = sensorid
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//                    TODO("not implementedd") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onSensorChanged(event: SensorEvent?) {
                    if(currentSensor == number) {
                        valueXTV.text = "X: " + event?.values?.get(0)
                        valueYTV.text = "Y: " + event?.values?.get(1)
                        valueZTV.text = "Z: " + event?.values?.get(2)
                    }
                }
            }, sensorList?.get(sensorid), SensorManager.SENSOR_DELAY_NORMAL)
            sensorid ++
        }
    }

    fun nextSensor(){
        if (sensorList != null){
            val len : Int = sensorList?.size as Int
            if (currentSensor >= len-1 ){
                nextB.isActivated = false
                currentSensor = len-1
            } else {
                currentSensor ++
                sensor = sensorList?.get(currentSensor)
                toast(sensor?.name as CharSequence)
                availabilityTV.text = sensor?.name+" is "+ getString(R.string.avail)
                if (currentSensor == len-1 ){
                    nextB.isActivated = false
                }
            }
        }
    }

    fun prevSensor(){
        if (sensorList != null){
            val len : Int = sensorList?.size as Int
            if (currentSensor <= 0 ){
                nextB.isActivated = false
                currentSensor = 0
            } else {
                currentSensor --
                sensor = sensorList?.get(currentSensor)
                toast(sensor?.name as CharSequence)
                availabilityTV.text = sensor?.name+" is "+ getString(R.string.avail)
                if (currentSensor == 0 ){
                    nextB.isActivated = false
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mSensorManager?.unregisterListener(this)
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

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//        TODO("No idea waht is that") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSensorChanged(event: SensorEvent){
        val EPSILON = 0.001
        var axisX = event.values[0]
        var axisY = event.values[1]
        var axisZ = event.values[2]

        valueXTV.text = "X: "+axisX
        valueYTV.text = "Y: "+axisY
        valueZTV.text = "Z: "+axisZ
        /*if (timestamp != 0L){
            val dT = (event.timestamp - timestamp) * NS2S
            // Axis of the rotation sample, not normalized yet.


            // Calculate the angular speed of the sample
            val omegaMagnitude = (axisX * axisX + axisY * axisY + axisZ * axisZ)

            // Normalize the rotation vector if it's big enough to get the axis
            // (that is, EPSILON should represent your maximum allowable margin of error)
            if (omegaMagnitude > EPSILON) {
                axisX /= omegaMagnitude
                axisY /= omegaMagnitude
                axisZ /= omegaMagnitude
            }

            // Integrate around this axis with the angular speed by the timestep
            // in order to get a delta rotation from this sample over the timestep
            // We will convert this axis-angle representation of the delta rotation
            // into a quaternion before turning it into the rotation matrix.
            val thetaOverTwo = omegaMagnitude * dT / 2.0f as Double
            val sinThetaOverTwo = sin(thetaOverTwo) as Float
            val cosThetaOverTwo = cos(thetaOverTwo) as Float
            deltaRotationVector[0] = sinThetaOverTwo * axisX
            deltaRotationVector[1] = sinThetaOverTwo * axisY
            deltaRotationVector[2] = sinThetaOverTwo * axisZ
            deltaRotationVector[3] = cosThetaOverTwo

        }
        timestamp = event.timestamp
        val deltaRotationMatrix = FloatArray(9)
        SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector as FloatArray)*/
    }
}


fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()