package com.save.continuesave.save

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.save.continuesave.R

@Composable
fun DropDownFolders(nameFolders: List<String>){
    var expancion by remember { mutableStateOf(false) }
    val contexto = LocalContext.current
    var returnNameButton by remember { mutableStateOf("") }
    var returnNameButtonState by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box {
            Surface() {
                IconButton(onClick = { expancion = true }) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
                }

            }
        }

        Box(Modifier.padding(end = 45.dp)) {
            DropdownMenu(
                expanded = expancion,
                onDismissRequest = { expancion = false },
                modifier = Modifier
                    .width(130.dp)
                    .background(Color(0xFF6E7B8B))
            ) {

                Column(Modifier.fillMaxHeight()) {
                    nameFolders.distinct().forEach { name ->

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 10.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.folder),
                                contentDescription = null
                            )
                            DropdownMenuItem(text = {
                                Text(
                                    text = name,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }, onClick = {
                                Toast.makeText(contexto, name, Toast.LENGTH_SHORT).show()
                                returnNameButton = name
                                returnNameButtonState = true
                            })
                        }
                    }
                }
            }
        }
    }
    if (returnNameButtonState){
        CreateFoldersAndFiles().listFoldersAndFilesInDocuments(returnNameButton)
        returnNameButtonState = false
    }
}