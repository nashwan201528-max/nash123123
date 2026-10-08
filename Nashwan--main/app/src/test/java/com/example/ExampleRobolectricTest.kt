package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("مونتاج برو", appName)
  }

  @Test
  fun `verify video templates repository loaded`() {
    val templates = com.example.data.model.VideoTemplateRepository.templates
    org.junit.Assert.assertTrue(templates.isNotEmpty())
    val categories = com.example.data.model.VideoTemplateRepository.categories
    org.junit.Assert.assertTrue(categories.size >= 5)
  }

  @Test
  fun `verify filter presets loaded`() {
    val presets = com.example.data.model.FilterRepository.presets
    org.junit.Assert.assertTrue(presets.size >= 10)
  }
}
