package app.dev.mahmudul.hasan.smartai.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieAnim(
        anim: Int, modifier: Modifier = Modifier.height(300.dp).wrapContentWidth()

) {
    Box(
            modifier = modifier,
    ) {
        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(anim))
        LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,

                )
    }
}
