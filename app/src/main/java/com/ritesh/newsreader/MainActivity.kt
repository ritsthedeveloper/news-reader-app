package com.ritesh.newsreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ritesh.newsreader.common.ui.navigation.NewsReaderContainer
import com.ritesh.newsreader.ui.theme.NewsReaderTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * @AndroidEntryPoint generates an individual Hilt component for each Android class in your project.
 * These components can receive dependencies from their respective parent classes.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsReaderTheme {
                // A surface container using the 'background' color from the theme
//                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//                    NewsReaderContainer()
//                }

                Surface {
                    NewsReaderContainer()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NewsReaderPreview() {
    NewsReaderTheme {
        NewsReaderContainer()
    }
}