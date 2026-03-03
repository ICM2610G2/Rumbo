package com.appnotresponding.rumbo.ui.templates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.ui.components.molecules.map.LocateMe
import com.appnotresponding.rumbo.ui.components.molecules.map.WriteDropNote
import com.appnotresponding.rumbo.ui.components.organisms.common.MainTopBar
import com.appnotresponding.rumbo.ui.components.organisms.common.Nav
import com.appnotresponding.rumbo.ui.components.organisms.map.ViewDropNote
import com.appnotresponding.rumbo.ui.theme.RumboTheme

@Composable
fun MapTemplate(user: User){
    Scaffold(
        topBar = { MainTopBar(user) },
        floatingActionButton = {
            Column(
                modifier = Modifier.height(120.dp).width(45.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ){
                WriteDropNote {  }
                LocateMe {  }
            }
        },
        bottomBar = { Nav() }
    ){
        paddingValues ->
        Box(modifier = Modifier.padding(paddingValues))
    }

}

@Preview(showBackground = true, backgroundColor = 0xFF1E1E1E, name = "PlacePreviewCard - Dark")
@Composable
private fun MapTemplateDarkPreview() {
    RumboTheme(darkTheme = true) {
        MapTemplate(sampleUser)
    }
}