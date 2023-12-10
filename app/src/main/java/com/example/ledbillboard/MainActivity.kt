package com.example.ledbillboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.ledbillboard.ui.component.BillBoard
import com.example.ledbillboard.ui.theme.LedBillboardTheme
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LedBillboardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        var text: String by rememberSaveable { mutableStateOf("") }

                        var fontSize: Int by remember { mutableStateOf(100) }

                        BillBoard(text = text, fontSize = fontSize)
                        TextField(
                            value = text,
                            onValueChange = { newText -> text = newText },
                            modifier = Modifier.fillMaxWidth()
                            ,
                            maxLines = 1,
                            singleLine = true
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .padding(top = 20.dp)
                            ,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Button(onClick = { fontSize += 2 }) {
                                Text(text = "+", textAlign = TextAlign.Center, fontSize = 25.sp)
                            }
                            Button(onClick = { fontSize -= 2 }) {
                                Text(text = "-", textAlign = TextAlign.Center, fontSize = 25.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

