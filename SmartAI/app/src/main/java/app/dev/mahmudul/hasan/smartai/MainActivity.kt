package app.dev.mahmudul.hasan.smartai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import app.dev.mahmudul.hasan.smartai.di.AppModule
import app.dev.mahmudul.hasan.smartai.features.NavGraphs
import app.dev.mahmudul.hasan.smartai.ui.theme.SmartAITheme
import com.ramcosta.composedestinations.DestinationsNavHost
import org.koin.compose.KoinApplication

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartAITheme {
                Surface(color = MaterialTheme.colorScheme.surface) {
                    KoinApplication(
                        application = {
                            modules(AppModule)
                        }
                    ) {
                        DestinationsNavHost(navGraph = NavGraphs.root)
                    }
                }

            }
        }
    }
}

