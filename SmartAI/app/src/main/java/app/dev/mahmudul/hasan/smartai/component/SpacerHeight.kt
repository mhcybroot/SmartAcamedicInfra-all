package app.dev.dpi_sai.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun SpacerHeight (height: Int){
    Spacer(modifier = androidx.compose.ui.Modifier.height(height.dp))
}