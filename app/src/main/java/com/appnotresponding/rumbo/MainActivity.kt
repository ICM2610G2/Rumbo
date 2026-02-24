package com.appnotresponding.rumbo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.appnotresponding.rumbo.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
        color = AppTheme.colorScheme.primary,
        style = AppTheme.typography.h1
    )
}

@Preview(showBackground = true, name = "Light Theme")
@Composable
fun LightPreview() {
    AppTheme(darkTheme = false) {
        Greeting("Android")
    }}

@Preview(showBackground = true, name = "Dark Theme", backgroundColor = 0xFF1E1E1E)
@Composable
fun DarkPreview() {
    AppTheme(darkTheme = true) {
        Greeting("Android")
    }
}