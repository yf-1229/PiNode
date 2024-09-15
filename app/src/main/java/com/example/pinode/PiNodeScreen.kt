import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pinode.ui.DrawDots
import com.example.pinode.ui.DrawGrid


@Composable
fun PiNodeScreen(
    modifier: Modifier = Modifier
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val verticalLineCount = 5
        val horizontalLineCount = 20
        val strokeWidth = 3f
        DrawGrid(verticalLineCount, horizontalLineCount, strokeWidth)
        DrawDots(verticalLineCount, horizontalLineCount)

    }
}



@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewGridWithDots() {
    Surface(
        modifier = Modifier.padding(16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        PiNodeScreen()
    }
}