package com.hyosik.features.ui.viewmodel

import app.cash.turbine.test
import com.hyosik.core.ui.state.UiState
import com.hyosik.domain.usecase.GetBillboardUseCase
import com.hyosik.domain.usecase.PostBillboardUseCase
import com.hyosik.features.ui.intent.MainEvent
import com.hyosik.features.ui.intent.MainState
import com.hyosik.model.Billboard
import com.hyosik.model.Direction
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

// @MX:NOTE: MainDispatcherRule - Dispatchers.Main 을 테스트용 UnconfinedTestDispatcher 로 교체
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: UnconfinedTestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getBillboardUseCase: GetBillboardUseCase
    private lateinit var postBillboardUseCase: PostBillboardUseCase
    private lateinit var viewModel: MainViewModel

    // 테스트용 기본 Billboard
    private val testBillboard = Billboard(
        key = "billboard_key",
        description = "테스트 텍스트",
        fontSize = 100,
        direction = Direction.STOP,
        textColor = "FFFFFFFF",
        billboardTextWidth = 1
    )

    @Before
    fun setUp() {
        getBillboardUseCase = mockk()
        postBillboardUseCase = mockk(relaxed = true)
        // onStart 에서 호출되는 getBillboardUseCase 가 즉시 값을 방출하도록 설정
        every { getBillboardUseCase(any()) } returns flowOf(testBillboard)
        viewModel = MainViewModel(getBillboardUseCase, postBillboardUseCase)
    }

    /**
     * REQ-VM-001: WHEN MainEvent.Initial(billboard) THEN state.isInitialText=true
     */
    @Test
    fun `REQ-VM-001 - Initial 이벤트 전송 시 isInitialText 가 true 로 설정된다`() = runTest {
        // Arrange
        val billboard = testBillboard.copy(description = "초기 텍스트")

        // Act
        viewModel.state.test {
            // onStart 에서 Initial 이벤트가 자동으로 발생하므로 최신 상태를 확인
            val currentState = awaitItem()

            // Assert: onStart 에서 Initial 이벤트가 처리되어 isInitialText=true 여야 함
            // 초기 UiState 는 isInitialText=false 이지만 getBillboardUseCase 가 즉시 방출하므로
            // Initial 이벤트가 처리된 상태가 됨
            assertTrue(
                "Initial 이벤트 처리 후 isInitialText 는 true 여야 합니다",
                currentState.data?.isInitialText == true
            )

            cancelAndConsumeRemainingEvents()
        }
    }

    /**
     * REQ-VM-002: WHEN MainEvent.Edit(billboard) THEN state.billboard 업데이트
     */
    @Test
    fun `REQ-VM-002 - Edit 이벤트 전송 시 state의 billboard 가 업데이트된다`() = runTest {
        // Arrange
        val updatedBillboard = testBillboard.copy(description = "수정된 텍스트")

        // Act & Assert
        viewModel.state.test {
            // 초기 상태 소비
            awaitItem()

            // Edit 이벤트 전송
            viewModel.setEvent(MainEvent.Edit(billboard = updatedBillboard))

            // 업데이트된 상태 확인
            val updatedState = awaitItem()
            assertEquals(
                "Edit 이벤트 후 billboard description 이 업데이트되어야 합니다",
                "수정된 텍스트",
                updatedState.data?.billboard?.description
            )

            cancelAndConsumeRemainingEvents()
        }
    }

    /**
     * REQ-VM-003: WHEN MainEvent.Save(billboard) THEN postBillboardUseCase 호출
     */
    @Test
    fun `REQ-VM-003 - Save 이벤트 전송 시 postBillboardUseCase 가 호출된다`() = runTest {
        // Arrange
        val billboardToSave = testBillboard.copy(description = "저장할 텍스트")

        // Act
        viewModel.state.test {
            // 초기 상태 소비
            awaitItem()

            // Save 이벤트 전송
            viewModel.setEvent(MainEvent.Save(billboard = billboardToSave))

            // 상태 업데이트 소비
            awaitItem()

            cancelAndConsumeRemainingEvents()
        }

        // Assert: postBillboardUseCase 가 호출되었는지 검증
        coVerify { postBillboardUseCase(billboard = billboardToSave) }
    }

    /**
     * REQ-VM-004: WHEN MainEvent.SetTextWidth(width) THEN state.billboard.billboardTextWidth=width
     */
    @Test
    fun `REQ-VM-004 - SetTextWidth 이벤트 전송 시 billboardTextWidth 가 업데이트된다`() = runTest {
        // Arrange
        val newTextWidth = 500

        // Act & Assert
        viewModel.state.test {
            // 초기 상태 소비
            awaitItem()

            // SetTextWidth 이벤트 전송
            viewModel.setEvent(MainEvent.SetTextWidth(textWidth = newTextWidth))

            // 업데이트된 상태 확인
            val updatedState = awaitItem()
            assertEquals(
                "SetTextWidth 이벤트 후 billboardTextWidth 가 업데이트되어야 합니다",
                newTextWidth,
                updatedState.data?.billboard?.billboardTextWidth
            )

            cancelAndConsumeRemainingEvents()
        }
    }
}
