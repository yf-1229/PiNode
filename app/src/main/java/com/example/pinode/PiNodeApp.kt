import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun PiNodeApp(
    modifier: Modifier = Modifier
) {

}



@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewGridWithDots() {
    Surface(
        modifier = Modifier.padding(16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        PiNodeApp()
    }
}