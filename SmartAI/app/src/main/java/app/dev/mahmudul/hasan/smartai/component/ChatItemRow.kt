package app.dev.mahmudul.hasan.smartai.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.dev.dpi_sai.component.SpacerHeight
import app.dev.mahmudul.hasan.smartai.features.course.home.data.MessageItemModel


@Composable
fun ChatItemRow(chatItem: MessageItemModel, photo: String, uid: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle click on chat item */ }
            .padding(16.dp),
    ) {
        SpacerHeight(height = 10)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center,

                ) {
                SpacerHeight(height = 10)
                Text(
                    text = "@${chatItem.senderName}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (chatItem.uid == uid) Color.Blue else Color.Black
                )
                SpacerHeight(height = 10)
                Text(
                    text = if (chatItem.uid == uid) "${chatItem.message} " else "${chatItem.message}",
                    style = MaterialTheme.typography.bodyLarge
                )
                SpacerHeight(height = 20)
            }
            Text(
                text = chatItem.time,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Bottom)
            )
            Spacer(modifier = Modifier.width(20.dp))

        }
        SpacerHeight(height = 10)
    }
}
