package dhruvi.patel.cashstockapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler as AndroidOsHandler

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val updateHandler = AndroidOsHandler()
        val runnable = Runnable {
            startActivity(Intent(this, MainActivity::class.java))
        }

        updateHandler.postDelayed(runnable, 3000)

    }
}