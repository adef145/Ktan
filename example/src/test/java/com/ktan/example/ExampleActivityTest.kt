package com.ktan.example

import android.os.Looper
import android.widget.EditText
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

class ExampleActivityTest {

    private lateinit var activity: ExampleActivity

    @MockK
    lateinit var extrasBinding: ExampleExtrasBinding

    @MockK
    lateinit var store: Store

    @Before
    fun before() {
        MockKAnnotations.init(this, relaxed = true, relaxUnitFun = true)

        every { extrasBinding.store } returns store

        mockkStatic(Looper::class)
        every { Looper.getMainLooper() } returns mockk<Looper>(
            relaxed = true,
            relaxUnitFun = true
        ).apply {
            every { thread } returns Thread.currentThread()
        }
        activity = spyk(ExampleActivity())

        every { activity.extrasBinding } returns extrasBinding
    }

    @Test
    fun init_test() {
        val editText: EditText = mockk(relaxed = true, relaxUnitFun = true)

        every { store.id } returns 1
        activity.initId(editText)

        verify { editText.setText("1") }
    }
}