package com.jj.pelismtv.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jj.pelismtv.AppDelegate
import com.jj.pelismtv.R
import com.jj.pelismtv.ui.MainActivity
import com.jj.pelismtv.utils.ThreadManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class SplashActivity : AppCompatActivity() {
    private lateinit var splashViewModel: SplashScreenViewModel
    private var textStatus: TextView? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashViewModel = ViewModelProvider(this@SplashActivity).get(SplashScreenViewModel::class.java)
       // val text_status:TextView = findViewById(R.id.text_sincronization)

       /* val yourListOfImages = arrayOf(R.drawable.titanic,R.drawable.male2,R.drawable.geminis,R.drawable.badboys,R.drawable.fantasia,R.drawable.batman)
        val random =  Random(System.currentTimeMillis())
        val posOfImage = random.nextInt(yourListOfImages.size)

        splash.setImageResource(yourListOfImages[posOfImage])*/
        textStatus =  findViewById(R.id.text_sincronization)

        if(!AppDelegate.networkStatus(this)){
            AlertDialog.Builder(this@SplashActivity)
                .setMessage("No se detecto conexion a internet en tu dispositivo")
                .setPositiveButton("Ok"){ _, _->
                    exitProcess(0)
                }
                .create()
                .show()

              return
        }
    //    textStatus?.text = "Buscando actualización"


    }

    override fun onResume() {
        super.onResume()
            checkSincronization()
    }

    private fun checkSincronization(){
        GlobalScope.launch(Dispatchers.IO) {
            splashViewModel.importData(textStatus)

        }
        startHome()
    }

    private fun startHome() {
        ThreadManager.executeInMainThread {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}