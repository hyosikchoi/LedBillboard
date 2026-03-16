package com.hyosik.features.ui.portrait

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.hyosik.core.ui.state.UiState
import com.hyosik.features.extension.orZero
import com.hyosik.features.ui.component.BillBoard
import com.hyosik.features.ui.intent.MainEffect
import com.hyosik.features.ui.intent.MainEvent
import com.hyosik.features.ui.intent.MainState
import com.hyosik.model.BILLBOARD_KEY
import com.hyosik.model.Billboard
import com.hyosik.model.Direction
import com.hyosik.theme.buttonText
import com.hyosik.theme.color.DarkBackground
import com.hyosik.theme.color.DarkCard
import com.hyosik.theme.color.DarkSurface
import com.hyosik.theme.color.NeonPurple
import com.hyosik.theme.color.NeonRed
import com.hyosik.theme.color.OutlineGray
import com.hyosik.theme.color.TextHint
import com.hyosik.utils.getColor

@Composable
fun PotraitScreen(
    mainState: UiState<MainState>,
    requestOrientationProvider: () -> Unit,
    onColorChanged: (ColorEnvelope) -> Unit,
    onEvent: (MainEvent) -> Unit,
    onSideEffect: (MainEffect) -> Unit
) {
    val controller = rememberColorPickerController()
    val infiniteTransition = rememberInfiniteTransition(label = "scroll")

    val maxChar: Int = 30
    val minFontSize: Int = 60
    val maxFontSize: Int = 140

    val scroll by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = -1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ), label = "scrollAnim"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (mainState.isSuccess) {

            // ── Billboard 영역 ──────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(DarkSurface, Color(0xFF0D0D1A))
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = OutlineGray,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                BillBoard(
                    text = mainState.data?.billboard?.description.orEmpty(),
                    fontSize = mainState.data?.billboard?.fontSize.orZero(),
                    textWidth = { textWidth ->
                        onEvent(MainEvent.SetTextWidth(textWidth = textWidth))
                    },
                    textColor = mainState.data?.billboard?.textColor ?: "BB86FC",
                    dynamicModifier = getModifier(
                        direction = mainState.data!!.billboard.direction,
                        billboardTextWidth = mainState.data!!.billboard.billboardTextWidth,
                        scrollProvider = { scroll }
                    )
                )
            }

            // ── 텍스트 입력 필드 ────────────────────────────────────────
            OutlinedTextField(
                value = mainState.data?.billboard?.description.orEmpty(),
                onValueChange = { newText ->
                    if (newText.length <= maxChar) {
                        mainState.data?.billboard?.let {
                            onEvent(
                                MainEvent.Save(
                                    it.copy(
                                        key = BILLBOARD_KEY,
                                        description = newText,
                                    )
                                )
                            )
                        }
                    } else {
                        onSideEffect(MainEffect.Toast("최대 길이 입니다!"))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                singleLine = true,
                placeholder = {
                    Text(
                        text = "텍스트를 입력하세요",
                        color = TextHint,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "edit",
                        tint = TextHint
                    )
                },
                textStyle = MaterialTheme.typography.buttonText.copy(
                    color = Color.White,
                    fontSize = 16.sp
                ),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonPurple,
                    unfocusedBorderColor = OutlineGray,
                    focusedContainerColor = DarkCard,
                    unfocusedContainerColor = DarkCard,
                    cursorColor = NeonPurple,
                )
            )

            // ── START 행 (+, START, -) ──────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // + 버튼
                BillboardIconButton(
                    label = "+",
                    onClick = {
                        if (mainState.data?.billboard?.fontSize.orZero() <= maxFontSize) {
                            mainState.data?.billboard?.let {
                                onEvent(MainEvent.Save(it.copy(fontSize = it.fontSize + 2)))
                            }
                        } else onSideEffect(MainEffect.Toast("최대 사이즈 입니다!"))
                    }
                )

                // START 버튼
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    onClick = { requestOrientationProvider() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonPurple,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = "START",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }

                // - 버튼
                BillboardIconButton(
                    label = "−",
                    onClick = {
                        if (mainState.data?.billboard?.fontSize.orZero() >= minFontSize) {
                            mainState.data?.billboard?.let {
                                onEvent(MainEvent.Save(it.copy(fontSize = it.fontSize - 2)))
                            }
                        } else onSideEffect(MainEffect.Toast("최소 사이즈 입니다!"))
                    }
                )
            }

            // ── STOP 행 (L, STOP, R) ────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // L 버튼
                BillboardIconButton(
                    label = "L",
                    onClick = {
                        mainState.data?.billboard?.let {
                            onEvent(MainEvent.Save(it.copy(direction = Direction.LEFT)))
                        }
                    }
                )

                // STOP 버튼
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    onClick = {
                        mainState.data?.billboard?.let {
                            onEvent(MainEvent.Save(it.copy(direction = Direction.STOP)))
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(width = 1.5.dp, color = NeonRed),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = NeonRed,
                        containerColor = Color.Transparent
                    )
                ) {
                    Text(
                        text = "STOP",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }

                // R 버튼
                BillboardIconButton(
                    label = "R",
                    onClick = {
                        mainState.data?.billboard?.let {
                            onEvent(MainEvent.Save(it.copy(direction = Direction.RIGHT)))
                        }
                    }
                )
            }

            // ── Color Picker ────────────────────────────────────────────
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Color",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )

                HsvColorPicker(
                    modifier = Modifier
                        .size(240.dp)
                        .aspectRatio(1f),
                    controller = controller,
                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                        onColorChanged(colorEnvelope)
                    },
                    initialColor = if (mainState.data?.billboard?.textColor != null)
                        mainState.data?.billboard?.textColor?.getColor()
                    else null
                )
            }
        }
    }
}

// ── 공통 아웃라인 아이콘 버튼 ──────────────────────────────────────────────
@Composable
private fun BillboardIconButton(
    label: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = Modifier.size(52.dp),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(0.dp),
        border = BorderStroke(width = 1.5.dp, color = NeonPurple),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = NeonPurple,
            containerColor = Color.Transparent
        )
    ) {
        Text(
            text = label,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun getModifier(
    direction: Direction,
    billboardTextWidth: Int,
    scrollProvider: () -> Float
): Modifier = when (direction) {
    Direction.LEFT -> Modifier
        .fillMaxWidth()
        .horizontalScroll(state = ScrollState(0), enabled = false)
        .graphicsLayer {
            translationX = billboardTextWidth * scrollProvider()
        }

    Direction.STOP -> Modifier
        .fillMaxWidth()
        .horizontalScroll(state = ScrollState(0), enabled = true)

    Direction.RIGHT -> Modifier
        .fillMaxWidth()
        .horizontalScroll(state = ScrollState(0), enabled = false)
        .graphicsLayer {
            translationX = -billboardTextWidth * scrollProvider()
        }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D0D1A)
@Composable
fun PotraitScreenPreview() {
    PotraitScreen(
        mainState = UiState(
            data = MainState(
                billboard = Billboard()
            )
        ),
        requestOrientationProvider = {},
        onColorChanged = {},
        onEvent = {},
        onSideEffect = {}
    )
}
