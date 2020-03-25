package permissions.dispatcher.ktx.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            val fragment = MainFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNowAllowingStateLoss()
        }
    }
}
