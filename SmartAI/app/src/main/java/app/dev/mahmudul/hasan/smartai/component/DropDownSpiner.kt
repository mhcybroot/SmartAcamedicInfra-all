package app.dev.dpi_sai.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownSpiner(
    items: List<String>,
    selecedItem: MutableState<String>,

    ) {
    var expanded = remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier.fillMaxWidth(

        ),
        contentAlignment = Alignment.Center,
    ) {


        ExposedDropdownMenuBox(

            expanded = expanded.value,
            onExpandedChange = {
                expanded.value = !expanded.value
            }
        ) {
            TextField(
                value = selecedItem.value,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent, // Remove underline when focused
                    unfocusedIndicatorColor = Color.Transparent // Remove underline when not focused
                ),
                onValueChange = {},
                readOnly = true,
                textStyle = TextStyle(textAlign = TextAlign.Center),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                modifier = Modifier.fillMaxWidth(0.8F).menuAnchor()
            )



            DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }, modifier = Modifier.fillMaxWidth(0.8F) ) {
                items.forEach { item ->
                    DropdownMenuItem({ dropDownItem(item = item) }, onClick = {
                        selecedItem.value = item
                        expanded.value = false
                    },)
                }

            }
        }
    }

}


@Composable
fun dropDownItem(item: String) {
    Text(text = item, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
}

