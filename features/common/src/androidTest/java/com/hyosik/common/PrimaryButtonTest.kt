package com.hyosik.common

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hyosik.theme.LedBillboardTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PrimaryButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * REQ-BTN-001: WHEN PrimaryButton 렌더링 THEN buttonText 화면 표시
     */
    @Test
    fun primaryButton_displaysText() {
        // Arrange
        val buttonText = "확인"

        // Act
        composeTestRule.setContent {
            LedBillboardTheme {
                PrimaryButton(
                    buttonText = buttonText,
                    onClick = {}
                )
            }
        }

        // Assert: buttonText 가 화면에 표시되어야 함
        composeTestRule
            .onNodeWithText(buttonText)
            .assertIsDisplayed()
    }

    /**
     * REQ-BTN-002: WHEN PrimaryButton 클릭 AND enabled=true THEN onClick 호출
     */
    @Test
    fun primaryButton_click_whenEnabled_invokesCallback() {
        // Arrange
        var clickCount = 0
        val buttonText = "클릭 가능"

        // Act
        composeTestRule.setContent {
            LedBillboardTheme {
                PrimaryButton(
                    buttonText = buttonText,
                    enabled = true,
                    onClick = { clickCount++ }
                )
            }
        }

        composeTestRule.onNodeWithText(buttonText).performClick()
        composeTestRule.waitForIdle()

        // Assert: onClick 이 호출되어야 함
        assertEquals("enabled=true 일 때 클릭 시 onClick 이 호출되어야 합니다", 1, clickCount)
    }

    /**
     * REQ-BTN-003: WHEN PrimaryButton 클릭 AND enabled=false THEN onClick 미호출
     */
    @Test
    fun primaryButton_click_whenDisabled_doesNotInvokeCallback() {
        // Arrange
        var clickCount = 0
        val buttonText = "비활성화"

        // Act
        composeTestRule.setContent {
            LedBillboardTheme {
                PrimaryButton(
                    buttonText = buttonText,
                    enabled = false,
                    onClick = { clickCount++ }
                )
            }
        }

        composeTestRule.onNodeWithText(buttonText).performClick()
        composeTestRule.waitForIdle()

        // Assert: onClick 이 호출되지 않아야 함
        assertEquals("enabled=false 일 때 클릭 시 onClick 이 호출되지 않아야 합니다", 0, clickCount)
    }

    /**
     * REQ-BTN-004: WHEN 500ms 이내 2회 클릭 THEN onClick 1번만 호출 (throttle)
     * PrimaryButton 은 기본 throttleTime=500ms 로 빠른 연속 클릭을 방지함
     */
    @Test
    fun primaryButton_throttle_preventsDuplicateClicks() {
        // Arrange
        var clickCount = 0
        val buttonText = "스로틀 테스트"

        // Act
        composeTestRule.setContent {
            LedBillboardTheme {
                PrimaryButton(
                    buttonText = buttonText,
                    throttleTime = 500L,
                    enabled = true,
                    onClick = { clickCount++ }
                )
            }
        }

        // 첫 번째 클릭
        composeTestRule.onNodeWithText(buttonText).performClick()
        composeTestRule.waitForIdle()

        // 100ms 내에 두 번째 클릭 (throttle 시간 이내)
        Thread.sleep(100)
        composeTestRule.onNodeWithText(buttonText).performClick()
        composeTestRule.waitForIdle()

        // Assert: 두 번 클릭했지만 throttle 로 인해 1번만 호출되어야 함
        assertEquals("500ms throttle 내 2회 클릭 시 onClick 은 1번만 호출되어야 합니다", 1, clickCount)

        // 600ms 대기 후 다시 클릭 가능한지 확인
        Thread.sleep(600)
        composeTestRule.onNodeWithText(buttonText).performClick()
        composeTestRule.waitForIdle()

        // Assert: throttle 시간 후 클릭은 다시 동작해야 함
        assertEquals("throttle 시간 경과 후 클릭은 호출되어야 합니다", 2, clickCount)
    }
}
