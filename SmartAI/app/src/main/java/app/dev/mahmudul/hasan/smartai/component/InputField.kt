package app.dev.dpi_sai.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import app.dev.mahmudul.hasan.smartai.R

@Composable
fun InputField(
    name: String,
    value: String,
    isError: Boolean = false,
    supportText: String = "",
    onChange: (value: String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
                .fillMaxWidth(0.8F)
                ,
        value = value,
        onValueChange = {
            onChange(it)
        },

        label = {
            Text(text = "$name",) },
        leadingIcon = {
            Icon(
                Icons.Default.Email,
                contentDescription = "Favorite",
            )
        },
        isError = isError,
        singleLine = true,
        supportingText = { Text(text = "$supportText") }

    )
}
