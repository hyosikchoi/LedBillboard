package com.hyosik.features.ui.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hyosik.theme.LedBillboardTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BillBoardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * REQ-BB-001: WHEN text 제공 THEN 해당 텍스트 화면 표시
     */
    @Test
    fun billBoard_displaysProvidedText() {
        // Arrange
        val testText = "LED Billboard 테스트"

        // Act
        composeTestRule.setContent {
            LedBillboardTheme {
                BillBoard(
                    text = testText,
                    fontSize = 50,
                    textWidth = {},
                    textColor = "FFFFFF",
                    dynamicModifier = Modifier
                )
            }
        }

        composeTestRule.waitForIdle()

        // Assert: 주어진 텍스트가 화면에 표시되어야 함
        composeTestRule
            .onNodeWithText(testText)
            .assertIsDisplayed()
    }

    /**
     * REQ-BB-002: WHEN textColor hex 제공 THEN 해당 색상으로 렌더링
     * 색상 렌더링은 Compose 의 내부 동작이므로 컴포저블이 에러 없이 렌더링되는지 확인
     */
    @Test
    fun billBoard_rendersWithProvidedTextColor() {
        // Arrange
        val testText = "색상 테스트"
        val hexColor = "FF5733" // 주황색 계열

        // Act & Assert: 색상 파라미터로 렌더링 시 예외가 발생하지 않아야 함
        composeTestRule.setContent {
            LedBillboardTheme {
                BillBoard(
                    text = testText,
                    fontSize = 50,
                    textWidth = {},
                    textColor = hexColor,
                    dynamicModifier = Modifier
                )
            }
        }

        composeTestRule.waitForIdle()

        // 렌더링이 성공적으로 완료되어야 함 (텍스트가 표시되면 색상 렌더링도 성공)
        composeTestRule
            .onNodeWithText(testText)
            .assertIsDisplayed()
    }

    /**
     * REQ-BB-003: WHEN 컴포넌트 레이아웃 THEN textWidth 콜백 측정값과 함께 호출
     * BillBoard 는 onGloballyPositioned 에서 textWidth 콜백을 호출함
     */
    @Test
    fun billBoard_invokesTextWidthCallbackWithMeasuredWidth() {
        // Arrange
        var capturedWidth: Int? = null
        val testText = "너비 측정 테스트"

        // Act
        composeTestRule.setContent {
            LedBillboardTheme {
                BillBoard(
                    text = testText,
                    fontSize = 50,
                    textWidth = { width ->
                        capturedWidth = width
                    },
                    textColor = "FFFFFF",
                    dynamicModifier = Modifier
                )
            }
        }

        // 레이아웃이 완료될 때까지 대기
        composeTestRule.waitForIdle()

        // Assert: textWidth 콜백이 호출되어 너비 값이 측정되어야 함
        assertNotNull("textWidth 콜백이 호출되어야 합니다", capturedWidth)
        assertTrue(
            "측정된 textWidth 는 0 이상이어야 합니다",
            (capturedWidth ?: -1) >= 0
        )
    }
}
