package com.appnotresponding.rumbo.ui.components.molecules.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.ui.components.atoms.RumboButton
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonStyle
import com.appnotresponding.rumbo.ui.theme.RumboTheme


@Composable
fun AuthPrimaryCTA() {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        RumboButton(
            text = "Iniciar sesión",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
        RumboButton(
            text = "Registrarse",
            style  = RumboButtonStyle.Secondary,
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true, name = "Auth CTA - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
fun AuthPrimaryCTADarkPreview() {
    RumboTheme(darkTheme = true) {
        AuthPrimaryCTA()
    }
}

@Preview(showBackground = true, name = "Auth CTA - Light")
@Composable
fun AuthPrimaryCTALightPreview() {
    RumboTheme(darkTheme = false) {
        AuthPrimaryCTA()
    }
}