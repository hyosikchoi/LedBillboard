package com.hyosik.features.ui.portrait

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.hyosik.core.ui.state.UiState
import com.hyosik.model.Billboard
import com.hyosik.model.Direction
import com.hyosik.features.ui.intent.MainEffect
import com.hyosik.features.ui.intent.MainEvent
import com.hyosik.features.ui.intent.MainState
import com.hyosik.theme.LedBillboardTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PortraitScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // 테스트용 Success 상태 헬퍼 함수
    private fun testSuccessState(
        description: String = "",
        fontSize: Int = 100,
        direction: Direction = Direction.STOP,
        textColor: String = "FFFFFFFF",
        billboardTextWidth: Int = 1,
        isInitialText: Boolean = true
    ): UiState<MainState> {
        return UiState.success(
            MainState(
                isInitialText = isInitialText,
                billboard = Billboard(
                    key = "billboard_key",
                    description = description,
                    fontSize = fontSize,
                    direction = direction,
                    textColor = textColor,
                    billboardTextWidth = billboardTextWidth
                )
            )
        )
    }

    /**
     * REQ-PT-001: WHEN 텍스트 입력 THEN onEvent(MainEvent.Save(description=입력값)) 호출
     */
    @Test
    fun potraitScreen_textInput_triggersOnEventWithSaveEvent() {
        // Arrange
        var capturedEvent: MainEvent? = null
        val inputText = "Hello"

        // Act
        composeTestRule.setContent {
            LedBillboardTheme {
                PotraitScreen(
                    mainState = testSuccessState(),
                    requestOrientationProvider = {},
                    onColorChanged = {},
                    onEvent = { capturedEvent = it },
                    onSideEffect = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        // 텍스트 필드에 입력
        composeTestRule
            .onNode(hasSetTextAction())
            .performTextInput(inputText)

        composeTestRule.waitForIdle()

        // Assert: Save 이벤트가 발생하고 description 이 입력값이어야 함
        assertNotNull("onEvent 가 호출되어야 합니다", capturedEvent)
        assertTrue(
            "MainEvent.Save 이벤트여야 합니다",
            capturedEvent is MainEvent.Save
        )
        assertEquals(
            "Save 이벤트의 description 이 입력값과 일치해야 합니다",
            inputText,
            (capturedEvent as MainEvent.Save).billboard.description
        )
    }

    /**
     * REQ-PT-002: WHEN 30자 초과 입력 THEN onSideEffect(MainEffect.Toast) 호출
     */
    @Test
    fun potraitScreen_textInput_over30Chars_triggersToastSideEffect() {
        // Arrange
        var capturedEffect: MainEffect? = null
        // 31자 문자열
        val longText = "A".repeat(31)

        // Act
        composeTestRule.setContent {
            LedBillboardTheme {
                PotraitScreen(
                    mainState = testSuccessState(),
                    requestOrientationProvider = {},
                    onColorChanged = {},
                    onEvent = {},
                    onSideEffect = { capturedEffect = it }
                )
            }
        }

        composeTestRule.waitForIdle()

        // 30자 초과 텍스트 입력
        composeTestRule
            .onNode(hasSetTextAction())
            .performTextReplacement(longText)

        composeTestRule.waitForIdle()

        // Assert: Toast 사이드 이펙트가 발생해야 함
        assertNotNull("사이드 이펙트가 호출되어야 합니다", capturedEffect)
        assertTrue(
            "Toast 사이드 이펙트여야 합니다",
            capturedEffect is MainEffect.Toast
        )
    }

    /**
     * REQ-PT-003: WHEN "+" 탭 AND fontSize < 140 THEN onEvent(MainEvent.Save(fontSize+2))
     */
    @Test
    fun potraitScreen_plusButton_whenFontSizeLessThan140_increasesFontSize() {
        // Arrange
        var capturedEvent: MainEvent? = null
        val initialFontSize = 100

        // Act
        composeTestRule.setContent {
            LedBillboardTheme {
                PotraitScreen(
                    mainState = testSuccessState(fontSize = initialFontSize),
                    requestOrientationProvider = {},
                    onColorChanged = {},
                    onEvent = { capturedEvent = it },
                    onSideEffect = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        // "+" 버튼 클릭
        composeTestRule.onNodeWithText("+").performClick()
        composeTestRule.waitForIdle()

        // Assert: fontSize 가 2 증가된 Save 이벤트여야 함
        assertNotNull("onEvent 가 호출되어야 합니다", capturedEvent)
        assertTrue(
            "MainEvent.Save 이벤트여야 합니다",
            capturedEvent is MainEvent.Save
        )
        assertEquals(
            "fontSize 가 2 증가되어야 합니다",
            initialFontSize + 2,
            (capturedEvent as MainEvent.Save).billboard.fontSize
        )
    }

    /**
     * REQ-PT-004: WHEN "+" 탭 AND fontSize >= 140 THEN onSideEffect(Toast("최대 사이즈 입니다!"))
     */
    @Test
    fun potraitScreen_plusButton_whenFontSizeAtMax_triggersToastSideEffect() {
        // Arrange
        var capturedEffect: MainEffect? = null
        val maxFontSize = 140

        // Act
        composeTestRule.setContent {
            LedBillboardTheme {
                PotraitScreen(
                    mainState = testSuccessState(fontSize = maxFontSize),
                    requestOrientationProvider = {},
                    onColorChanged = {},
                    onEvent = {},
                    onSideEffect = { capturedEffect = it }
                )
            }
        }

        composeTestRule.waitForIdle()

        // "+" 버튼 클릭 (fontSize 가 이미 maxFontSize 이상)
        composeTestRule.onNodeWithText("+").performClick()
        composeTestRule.waitForIdle()

        // Assert: Toast 사이드 이펙트가 발생해야 함
        assertNotNull("사이드 이펙트가 호출되어야 합니다", capturedEffect)
        assertTrue(
            "Toast 사이드 이펙트여야 합니다",
            capturedEffect is MainEffect.Toast
        )
        assertEquals(
            "최대 사이즈 토스트 메시지여야 합니다",
            "최대 사이즈 입니다!",
            (capturedEffect as MainEffect.Toast).msg
        )
    }

    /**
     * REQ-PT-005: WHEN "-" 탭 AND fontSize > 60 THEN onEvent(MainEvent.Save(fontSize-2))
     */
    @Test
    fun potraitScreen_minusButton_whenFontSizeGreaterThan60_decreasesFontSize() {
        // Arrange
        var capturedEvent: MainEvent? = null
        val initialFontSize = 100

        // Act
        composeTestRule.setContent {
            LedBillboardTheme {
                PotraitScreen(
                    mainState = testSuccessState(fontSize = initialFontSize),
                    requestOrientationProvider = {},
                    onColorChanged = {},
                    onEvent = { capturedEvent = it },
                    onSideEffect = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        // "-" 버튼 클릭
        composeTestRule.onNodeWithText("-").performClick()
        composeTestRule.waitForIdle()

        // Assert: fontSize 가 2 감소된 Save 이벤트여야 함
        assertNotNull("onEvent 가 호출되어야 합니다", capturedEvent)
        assertTrue(
            "MainEvent.Save 이벤트여야 합니다",
            capturedEvent is MainEvent.Save
        )
        assertEquals(
            "fontSize 가 2 감소되어야 합니다",
            initialFontSize - 2,
            (capturedEvent as MainEvent.Save).billboard.fontSize
        )
    }

    /**
     * REQ-PT-006: WHEN "-" 탭 AND fontSize <= 60 THEN onSideEffect(Toast("최소 사이즈 입니다!"))
     */
    @Test
    fun potraitScreen_minusButton_whenFontSizeAtMin_triggersToastSideEffect() {
        // Arrange
        var capturedEffect: MainEffect? = null
        val minFontSize = 60

        // Act
        composeTestRule.setContent {
            LedBillboardTheme {
                PotraitScreen(
                    mainState = testSuccessState(fontSize = minFontSize),
                    requestOrientationProvider = {},
                    onColorChanged = {},
                    onEvent = {},
                    onSideEffect = { capturedEffect = it }
                )
            }
        }

        composeTestRule.waitForIdle()

        // "-" 버튼 클릭 (fontSize 가 이미 minFontSize 이하)
        composeTestRule.onNodeWithText("-").performClick()
        composeTestRule.waitForIdle()

        // Assert: Toast 사이드 이펙트가 발생해야 함
        assertNotNull("사이드 이펙트가 호출되어야 합니다", capturedEffect)
        assertTrue(
            "Toast 사이드 이펙트여야 합니다",
            capturedEffect is MainEffect.Toast
        )
        assertEquals(
            "최소 사이즈 토스트 메시지여야 합니다",
            "최소 사이즈 입니다!",
            (capturedEffect as MainEffect.Toast).msg
        )
    }

    /**
     * REQ-PD-001: WHEN "L" 탭 THEN onEvent(MainEvent.Save(direction=Direction.LEFT))
     */
    @Test
    fun potraitScreen_leftButton_triggersOnEventWithDirectionLeft() {
        // Arrange
        var capturedEvent: MainEvent? = null

        // Act
        composeTestRule.setContent {
            LedBillboardTheme {
                PotraitScreen(
                    mainState = testSuccessState(),
                    requestOrientationProvider = {},
                    onColorChanged = {},
                    onEvent = { capturedEvent = it },
                    onSideEffect = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        // "L" 버튼 클릭
        composeTestRule.onNodeWithText("L").performClick()
        composeTestRule.waitForIdle()

        // Assert: Direction.LEFT 로 Save 이벤트가 발생해야 함
        assertNotNull("onEvent 가 호출되어야 합니다", capturedEvent)
        assertTrue(
            "MainEvent.Save 이벤트여야 합니다",
            capturedEvent is MainEvent.Save
        )
        assertEquals(
            "direction 이 LEFT 여야 합니다",
            Direction.LEFT,
            (capturedEvent as MainEvent.Save).billboard.direction
        )
    }

    /**
     * REQ-PD-002: WHEN "STOP" 탭 THEN onEvent(MainEvent.Save(direction=Direction.STOP))
     */
    @Test
    fun potraitScreen_stopButton_triggersOnEventWithDirectionStop() {
        // Arrange
        var capturedEvent: MainEvent? = null

        // Act
        composeTestRule.setContent {
            LedBillboardTheme {
                PotraitScreen(
                    mainState = testSuccessState(direction = Direction.LEFT),
                    requestOrientationProvider = {},
                    onColorChanged = {},
                    onEvent = { capturedEvent = it },
                    onSideEffect = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        // "STOP" 버튼 클릭
        composeTestRule.onNodeWithText("STOP").performClick()
        composeTestRule.waitForIdle()

        // Assert: Direction.STOP 으로 Save 이벤트가 발생해야 함
        assertNotNull("onEvent 가 호출되어야 합니다", capturedEvent)
        assertTrue(
            "MainEvent.Save 이벤트여야 합니다",
            capturedEvent is MainEvent.Save
        )
        assertEquals(
            "direction 이 STOP 이어야 합니다",
            Direction.STOP,
            (capturedEvent as MainEvent.Save).billboard.direction
        )
    }

    /**
     * REQ-PD-003: WHEN "R" 탭 THEN onEvent(MainEvent.Save(direction=Direction.RIGHT))
     */
    @Test
    fun potraitScreen_rightButton_triggersOnEventWithDirectionRight() {
        // Arrange
        var capturedEvent: MainEvent? = null

        // Act
        composeTestRule.setContent {
            LedBillboardTheme {
                PotraitScreen(
                    mainState = testSuccessState(),
                    requestOrientationProvider = {},
                    onColorChanged = {},
                    onEvent = { capturedEvent = it },
                    onSideEffect = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        // "R" 버튼 클릭
        composeTestRule.onNodeWithText("R").performClick()
        composeTestRule.waitForIdle()

        // Assert: Direction.RIGHT 로 Save 이벤트가 발생해야 함
        assertNotNull("onEvent 가 호출되어야 합니다", capturedEvent)
        assertTrue(
            "MainEvent.Save 이벤트여야 합니다",
            capturedEvent is MainEvent.Save
        )
        assertEquals(
            "direction 이 RIGHT 여야 합니다",
            Direction.RIGHT,
            (capturedEvent as MainEvent.Save).billboard.direction
        )
    }

    /**
     * REQ-PD-004: WHEN "START" 탭 THEN requestOrientationProvider() 호출
     */
    @Test
    fun potraitScreen_startButton_callsRequestOrientationProvider() {
        // Arrange
        var orientationProviderCalled = false

        // Act
        composeTestRule.setContent {
            LedBillboardTheme {
                PotraitScreen(
                    mainState = testSuccessState(),
                    requestOrientationProvider = { orientationProviderCalled = true },
                    onColorChanged = {},
                    onEvent = {},
                    onSideEffect = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        // "START" 버튼 클릭
        composeTestRule.onNodeWithText("START").performClick()
        composeTestRule.waitForIdle()

        // Assert: requestOrientationProvider 가 호출되어야 함
        assertTrue(
            "START 버튼 클릭 시 requestOrientationProvider 가 호출되어야 합니다",
            orientationProviderCalled
        )
    }

    /**
     * REQ-PD-005: IF mainState Success THEN 모든 UI 요소 표시
     */
    @Test
    fun potraitScreen_whenStateIsSuccess_displaysAllUiElements() {
        // Arrange
        val successState = testSuccessState(description = "테스트 텍스트")

        // Act
        composeTestRule.setContent {
            LedBillboardTheme {
                PotraitScreen(
                    mainState = successState,
                    requestOrientationProvider = {},
                    onColorChanged = {},
                    onEvent = {},
                    onSideEffect = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        // Assert: Success 상태일 때 모든 UI 요소가 표시되어야 함
        // 텍스트 필드 (hasSetTextAction 으로 확인)
        composeTestRule
            .onNode(hasSetTextAction())
            .assertIsDisplayed()

        // 폰트 크기 조절 버튼
        composeTestRule.onNodeWithText("+").assertIsDisplayed()
        composeTestRule.onNodeWithText("-").assertIsDisplayed()

        // 방향 버튼
        composeTestRule.onNodeWithText("L").assertIsDisplayed()
        composeTestRule.onNodeWithText("STOP").assertIsDisplayed()
        composeTestRule.onNodeWithText("R").assertIsDisplayed()

        // START 버튼
        composeTestRule.onNodeWithText("START").assertIsDisplayed()
    }
}
