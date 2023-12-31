package com.example.ledbillboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ledbillboard.enum.Direction
import com.example.ledbillboard.enum.ToastType
import com.example.ledbillboard.extension.toast

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

                        var direction: Direction by remember { mutableStateOf(Direction.STOP) }

                        val maxChar: Int = 30

                        val minFontSize: Int = 60

                        val maxFontSize: Int = 140

                        BillBoard(text = text, fontSize = fontSize, direction = direction)
                        TextField(
                            value = text,
                            onValueChange = { newText ->
                                if(newText.length <= maxChar) text = newText
                            },
                            modifier = Modifier.fillMaxWidth()
                            ,
                            maxLines = 1,

                            singleLine = true
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                            ,
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(onClick = {
                                if(fontSize <= maxFontSize) fontSize += 2
                                else this@MainActivity.toast("최대 사이즈 입니다.", ToastType.SHORT)
                            }) {
                                Text(text = "+", textAlign = TextAlign.Center, fontSize = 25.sp)
                            }
                            Button(onClick = {
                                if(fontSize >= minFontSize) fontSize -= 2
                                else this@MainActivity.toast("최소 사이즈 입니다.", ToastType.SHORT)
                            }) {
                                Text(text = "-", textAlign = TextAlign.Center, fontSize = 25.sp)
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                            ,
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(onClick = { direction = Direction.LEFT }) {
                                Text(text = "←", textAlign = TextAlign.Center, fontSize = 25.sp)
                            }
                            Button(onClick = { direction = Direction.STOP }) {
                                Text(text = "STOP", textAlign = TextAlign.Center, fontSize = 25.sp)
                            }
                            Button(onClick = { direction = Direction.RIGHT }) {
                                Text(text = "→", textAlign = TextAlign.Center, fontSize = 25.sp)
                            }

                        }
                    }
                }
            }
        }
    }
}

