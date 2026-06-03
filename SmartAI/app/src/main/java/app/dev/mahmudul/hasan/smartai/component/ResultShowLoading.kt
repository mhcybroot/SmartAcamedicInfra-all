package app.dev.mahmudul.hasan.smartai.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.dev.dpi_sai.component.SpacerHeight
import com.spr.jetpack_loading.components.indicators.PacmanIndicator

@Composable
fun ResultShowLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PacmanIndicator()
        SpacerHeight(height = 100)
        CircularProgressIndicator()
    }
}