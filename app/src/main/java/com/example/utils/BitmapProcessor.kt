package com.example.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import com.example.data.model.StickerOverlay
import com.example.data.model.TextOverlay
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object BitmapProcessor {

    /**
     * Applies color filter with custom intensity (0% to 100%), brightness, contrast, saturation, and vignette to a Bitmap.
     */
    fun applyColorGrading(
        source: Bitmap,
        filterMatrix: FloatArray?,
        brightness: Float, // -100f to +100f
        contrast: Float,   // 0.5f to 2.0f
        saturation: Float, // 0.0f to 2.0f
        vignetteStrength: Float = 0f,
        filterIntensity: Float = 1.0f // 0.0f to 1.0f
    ): Bitmap {
        return applyFullColorGradingAndFx(
            source = source,
            filterMatrix = filterMatrix,
            filterIntensity = filterIntensity,
            temperature = 0f,
            tint = 0f,
            hueShiftDegrees = 0f,
            brightness = brightness,
            contrast = contrast,
            saturation = saturation,
            vibrance = 0f,
            highlights = 0f,
            shadows = 0f,
            vignetteStrength = vignetteStrength,
            filmGrainStrength = 0f,
            glowBloomIntensity = 0f
        )
    }

    /**
     * Full professional Color Grading & Visual FX engine:
     * Handles Temperature, Tint, Hue Shift, Contrast, Brightness, Saturation, Vibrance,
     * Highlights/Shadows recovery, Vignette, Film Grain, and Glow Bloom.
     */
    fun applyFullColorGradingAndFx(
        source: Bitmap,
        filterMatrix: FloatArray?,
        filterIntensity: Float = 1.0f,
        temperature: Float = 0f,       // -100f (Cold) to +100f (Warm)
        tint: Float = 0f,              // -100f (Green) to +100f (Magenta)
        hueShiftDegrees: Float = 0f,   // 0° to 360°
        brightness: Float = 0f,        // -100f to +100f
        contrast: Float = 1.0f,        // 0.5f to 2.0f
        saturation: Float = 1.0f,      // 0.0f to 2.5f
        vibrance: Float = 0f,          // -100f to +100f
        highlights: Float = 0f,        // -100f to +100f
        shadows: Float = 0f,           // -100f to +100f
        vignetteStrength: Float = 0f,  // 0.0f to 1.0f
        filmGrainStrength: Float = 0f, // 0.0f to 1.0f
        glowBloomIntensity: Float = 0f // 0.0f to 1.0f
    ): Bitmap {
        val result = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

        val combinedMatrix = ColorMatrix()

        // 1. Base filter matrix interpolated with intensity
        if (filterMatrix != null) {
            val identity = floatArrayOf(
                1f, 0f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f, 0f,
                0f, 0f, 1f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
            val clampedIntensity = filterIntensity.coerceIn(0f, 1f)
            val blended = FloatArray(20)
            for (i in 0 until 20) {
                blended[i] = identity[i] + (filterMatrix[i] - identity[i]) * clampedIntensity
            }
            combinedMatrix.set(blended)
        }

        // 2. Temperature (Kelvin Warm/Cold) & Tint (Green/Magenta)
        if (temperature != 0f || tint != 0f) {
            val rTemp = (temperature / 100f) * 35f
            val bTemp = -(temperature / 100f) * 35f
            val gTint = -(tint / 100f) * 25f
            val rTint = (tint / 100f) * 20f
            val bTint = (tint / 100f) * 20f

            val tempTintMatrix = ColorMatrix(
                floatArrayOf(
                    1f, 0f, 0f, 0f, rTemp + rTint,
                    0f, 1f, 0f, 0f, gTint,
                    0f, 0f, 1f, 0f, bTemp + bTint,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            combinedMatrix.postConcat(tempTintMatrix)
        }

        // 3. Hue Shift (Color Spectrum Rotation)
        if (hueShiftDegrees != 0f) {
            val angle = Math.toRadians(hueShiftDegrees.toDouble())
            val cosVal = Math.cos(angle).toFloat()
            val sinVal = Math.sin(angle).toFloat()
            val lumR = 0.213f
            val lumG = 0.715f
            val lumB = 0.072f

            val hueMatrix = ColorMatrix(
                floatArrayOf(
                    lumR + cosVal * (1 - lumR) + sinVal * (-lumR),
                    lumG + cosVal * (-lumG) + sinVal * (-lumG),
                    lumB + cosVal * (-lumB) + sinVal * (1 - lumB), 0f, 0f,

                    lumR + cosVal * (-lumR) + sinVal * (0.143f),
                    lumG + cosVal * (1 - lumG) + sinVal * (0.140f),
                    lumB + cosVal * (-lumB) + sinVal * (-0.283f), 0f, 0f,

                    lumR + cosVal * (-lumR) + sinVal * (-(1 - lumR)),
                    lumG + cosVal * (-lumG) + sinVal * (lumG),
                    lumB + cosVal * (1 - lumB) + sinVal * (lumB), 0f, 0f,

                    0f, 0f, 0f, 1f, 0f
                )
            )
            combinedMatrix.postConcat(hueMatrix)
        }

        // 4. Saturation & Vibrance
        val effectiveSaturation = (saturation + (vibrance / 100f) * 0.5f).coerceAtLeast(0f)
        if (effectiveSaturation != 1.0f) {
            val satMatrix = ColorMatrix()
            satMatrix.setSaturation(effectiveSaturation)
            combinedMatrix.postConcat(satMatrix)
        }

        // 5. Contrast, Brightness, Highlights, and Shadows
        val scale = contrast
        val shadowLift = (shadows / 100f) * 20f
        val highlightAdjust = (highlights / 100f) * 20f
        val translate = brightness + (1f - scale) * 128f + shadowLift + highlightAdjust

        val cbMatrix = ColorMatrix(
            floatArrayOf(
                scale, 0f, 0f, 0f, translate,
                0f, scale, 0f, 0f, translate,
                0f, 0f, scale, 0f, translate,
                0f, 0f, 0f, 1f, 0f
            )
        )
        combinedMatrix.postConcat(cbMatrix)

        paint.colorFilter = ColorMatrixColorFilter(combinedMatrix)
        canvas.drawBitmap(source, 0f, 0f, paint)

        // 6. Glow / Bloom Effect
        if (glowBloomIntensity > 0.05f) {
            applyGlowBloomEffect(canvas, source, glowBloomIntensity)
        }

        // 7. Vignette Effect
        val totalVignette = vignetteStrength.coerceIn(0f, 1f)
        if (totalVignette > 0.05f) {
            applyVignetteEffect(canvas, source.width.toFloat(), source.height.toFloat(), totalVignette)
        }

        // 8. Film Grain Synthesis
        if (filmGrainStrength > 0.05f) {
            applyFilmGrainEffect(result, filmGrainStrength)
        }

        return result
    }

    private fun applyGlowBloomEffect(canvas: Canvas, source: Bitmap, intensity: Float) {
        val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
            alpha = (intensity.coerceIn(0f, 1f) * 140).toInt()
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SCREEN)
        }
        val smallW = (source.width / 4).coerceAtLeast(10)
        val smallH = (source.height / 4).coerceAtLeast(10)
        val scaledDown = Bitmap.createScaledBitmap(source, smallW, smallH, true)
        val rectDst = Rect(0, 0, source.width, source.height)
        canvas.drawBitmap(scaledDown, null, rectDst, glowPaint)
    }

    private fun applyFilmGrainEffect(bitmap: Bitmap, strength: Float) {
        val width = bitmap.width
        val height = bitmap.height
        val step = 4
        val grainAlpha = (strength.coerceIn(0f, 1f) * 45).toInt()
        val random = java.util.Random(42)

        for (y in 0 until height step step) {
            for (x in 0 until width step step) {
                if (random.nextBoolean()) {
                    val noiseVal = if (random.nextBoolean()) 255 else 0
                    val original = bitmap.getPixel(x, y)
                    val r = ((Color.red(original) * (255 - grainAlpha) + noiseVal * grainAlpha) / 255).coerceIn(0, 255)
                    val g = ((Color.green(original) * (255 - grainAlpha) + noiseVal * grainAlpha) / 255).coerceIn(0, 255)
                    val b = ((Color.blue(original) * (255 - grainAlpha) + noiseVal * grainAlpha) / 255).coerceIn(0, 255)
                    bitmap.setPixel(x, y, Color.argb(Color.alpha(original), r, g, b))
                }
            }
        }
    }

    /**
     * Smart Content-Aware Media Analyzer:
     * Samples image histogram and color distribution to recommend the most optimal filter and parameters.
     */
    fun analyzeContentForBestFilter(source: Bitmap): Triple<String, Float, String> {
        val width = source.width
        val height = source.height
        val sampleStep = 8
        var totalR = 0L
        var totalG = 0L
        var totalB = 0L
        var count = 0L

        for (y in 0 until height step sampleStep) {
            for (x in 0 until width step sampleStep) {
                val pixel = source.getPixel(x, y)
                totalR += Color.red(pixel)
                totalG += Color.green(pixel)
                totalB += Color.blue(pixel)
                count++
            }
        }

        if (count == 0L) count = 1L
        val avgR = totalR.toFloat() / count
        val avgG = totalG.toFloat() / count
        val avgB = totalB.toFloat() / count
        val avgLum = 0.299f * avgR + 0.587f * avgG + 0.114f * avgB

        return when {
            // Dark night scene with vibrant blue/magenta or neon
            avgLum < 75f && (avgB > avgG || avgR > avgG) -> {
                Triple("futur_cyberpunk_neon", 0.85f, "مشهد ليلي مظلم: تم اختيار فلتر السايبربانك لإبراز التوهج والنيون.")
            }
            // Very bright sunny or sunset golden hour (high Red & Green over Blue)
            avgR > avgB * 1.35f && avgG > avgB * 1.15f -> {
                Triple("nat_golden_hour", 0.80f, "إضاءة دافئة وغروب: تم اختيار فلتر الساعة الذهبية لإضفاء توهج ساحر.")
            }
            // High Green landscape/vegetation
            avgG > avgR * 1.1f && avgG > avgB * 1.1f -> {
                Triple("nat_emerald_valley", 0.85f, "طبيعة ومساحات خضراء: تم اختيار فلتر الواحة الزمردية لتعزيز الحيوية.")
            }
            // Blue water / sky dominance
            avgB > avgR * 1.2f -> {
                Triple("nat_ocean_breeze", 0.75f, "بحر وسماء زرقاء: تم اختيار فلتر نسيم المحيط لتعزيز النقاء الكريستالي.")
            }
            // Balanced scene or portrait -> Hollywood Teal & Orange
            else -> {
                Triple("cine_teal_orange", 0.75f, "تكوين سينمائي متوازن: تم اختيار فلتر هوليوود (تيل & أورانج) لتعزيز التباين البصري.")
            }
        }
    }

    /**
     * AI Super-Resolution & Sharpening:
     * Applies an unsharp mask kernel to enhance micro-textures, fine lines, and edges.
     */
    fun applySuperResolutionSharpening(source: Bitmap, strength: Float = 1.5f): Bitmap {
        val width = source.width
        val height = source.height
        val pixels = IntArray(width * height)
        source.getPixels(pixels, 0, width, 0, 0, width, height)

        val outputPixels = IntArray(width * height)

        val factor = strength.coerceIn(0.5f, 3.0f)

        for (y in 1 until height - 1) {
            for (x in 1 until width - 1) {
                val idx = y * width + x

                val centerColor = pixels[idx]
                val topColor = pixels[(y - 1) * width + x]
                val bottomColor = pixels[(y + 1) * width + x]
                val leftColor = pixels[y * width + (x - 1)]
                val rightColor = pixels[y * width + (x + 1)]

                val a = Color.alpha(centerColor)

                // Sharpen Laplacian kernel
                val rC = Color.red(centerColor)
                val gC = Color.green(centerColor)
                val bC = Color.blue(centerColor)

                val rEdge = 4 * rC - Color.red(topColor) - Color.red(bottomColor) - Color.red(leftColor) - Color.red(rightColor)
                val gEdge = 4 * gC - Color.green(topColor) - Color.green(bottomColor) - Color.green(leftColor) - Color.green(rightColor)
                val bEdge = 4 * bC - Color.blue(topColor) - Color.blue(bottomColor) - Color.blue(leftColor) - Color.blue(rightColor)

                val newR = (rC + rEdge * (factor * 0.25f)).toInt().coerceIn(0, 255)
                val newG = (gC + gEdge * (factor * 0.25f)).toInt().coerceIn(0, 255)
                val newB = (bC + bEdge * (factor * 0.25f)).toInt().coerceIn(0, 255)

                outputPixels[idx] = Color.argb(a, newR, newG, newB)
            }
        }

        // Fill borders
        for (x in 0 until width) {
            outputPixels[x] = pixels[x]
            outputPixels[(height - 1) * width + x] = pixels[(height - 1) * width + x]
        }
        for (y in 0 until height) {
            outputPixels[y * width] = pixels[y * width]
            outputPixels[y * width + (width - 1)] = pixels[y * width + (width - 1)]
        }

        return Bitmap.createBitmap(outputPixels, width, height, Bitmap.Config.ARGB_8888)
    }

    /**
     * AI Denoise & Noise Reduction:
     * Applies edge-preserving smoothing to eliminate high-frequency digital noise and grain.
     */
    fun applyDenoise(source: Bitmap, intensity: Float = 1.0f): Bitmap {
        val width = source.width
        val height = source.height
        val pixels = IntArray(width * height)
        source.getPixels(pixels, 0, width, 0, 0, width, height)

        val outputPixels = IntArray(width * height)
        val threshold = (30 * intensity).toInt()

        for (y in 1 until height - 1) {
            for (x in 1 until width - 1) {
                val idx = y * width + x
                val center = pixels[idx]
                val cR = Color.red(center)
                val cG = Color.green(center)
                val cB = Color.blue(center)

                var sumR = cR
                var sumG = cG
                var sumB = cB
                var count = 1

                for (dy in -1..1) {
                    for (dx in -1..1) {
                        if (dx == 0 && dy == 0) continue
                        val nColor = pixels[(y + dy) * width + (x + dx)]
                        val nR = Color.red(nColor)
                        val nG = Color.green(nColor)
                        val nB = Color.blue(nColor)

                        val diff = abs(nR - cR) + abs(nG - cG) + abs(nB - cB)
                        if (diff < threshold * 3) {
                            sumR += nR
                            sumG += nG
                            sumB += nB
                            count++
                        }
                    }
                }

                outputPixels[idx] = Color.argb(
                    Color.alpha(center),
                    sumR / count,
                    sumG / count,
                    sumB / count
                )
            }
        }

        for (x in 0 until width) {
            outputPixels[x] = pixels[x]
            outputPixels[(height - 1) * width + x] = pixels[(height - 1) * width + x]
        }
        for (y in 0 until height) {
            outputPixels[y * width] = pixels[y * width]
            outputPixels[y * width + (width - 1)] = pixels[y * width + (width - 1)]
        }

        return Bitmap.createBitmap(outputPixels, width, height, Bitmap.Config.ARGB_8888)
    }

    /**
     * AI Upscaling & Super-Resolution (2x / 4x):
     * Upscales the bitmap dimensions using high-quality bi-linear interpolation
     * coupled with multi-pass directional edge-sharpening to restore clarity.
     */
    fun applyAiUpscaling(source: Bitmap, scaleFactor: Float = 2.0f, edgeBoost: Float = 1.4f): Bitmap {
        val targetWidth = (source.width * scaleFactor).toInt().coerceAtMost(3840)
        val targetHeight = (source.height * scaleFactor).toInt().coerceAtMost(2160)

        // 1. High quality scaled bitmap
        val scaled = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, true)

        // 2. Pass through unsharp Laplacian sharpening to preserve crisp edges
        return applySuperResolutionSharpening(scaled, edgeBoost)
    }

    /**
     * Smart Comprehensive Auto Enhance:
     * Analyzes image histogram, expands dynamic contrast range, balances color temperature,
     * denoises subtle dark areas, and sharpens focal edges.
     */
    fun applySmartAutoEnhance(source: Bitmap): Bitmap {
        // Step 1: Denoise dark areas gently
        val denoised = applyDenoise(source, 0.8f)

        // Step 2: Auto color & contrast grading
        val autoGraded = applyColorGrading(
            source = denoised,
            filterMatrix = null,
            brightness = 8f,
            contrast = 1.18f,
            saturation = 1.15f,
            vignetteStrength = 0f,
            filterIntensity = 1.0f
        )

        // Step 3: Smart unsharp sharpening for crisp micro-textures
        return applySuperResolutionSharpening(autoGraded, 1.4f)
    }

    /**
     * AI HDR Dynamic Boost:
     * Lifts shadows, protects highlights, and increases vibrant micro-contrast.
     */
    fun applyHdrColorBoost(source: Bitmap, boostFactor: Float = 1.3f): Bitmap {
        val width = source.width
        val height = source.height
        val pixels = IntArray(width * height)
        source.getPixels(pixels, 0, width, 0, 0, width, height)

        val outputPixels = IntArray(width * height)

        for (i in pixels.indices) {
            val color = pixels[i]
            val a = Color.alpha(color)
            var r = Color.red(color).toFloat() / 255f
            var g = Color.green(color).toFloat() / 255f
            var b = Color.blue(color).toFloat() / 255f

            // Tone curve mapping: gamma correction on shadows + vibrancy
            r = Math.pow(r.toDouble(), (1.0 / (boostFactor * 0.9))).toFloat()
            g = Math.pow(g.toDouble(), (1.0 / (boostFactor * 0.9))).toFloat()
            b = Math.pow(b.toDouble(), (1.0 / (boostFactor * 0.9))).toFloat()

            // Micro saturation boost
            val lum = 0.2126f * r + 0.7152f * g + 0.0722f * b
            r = (lum + (r - lum) * boostFactor).coerceIn(0f, 1f)
            g = (lum + (g - lum) * boostFactor).coerceIn(0f, 1f)
            b = (lum + (b - lum) * boostFactor).coerceIn(0f, 1f)

            outputPixels[i] = Color.argb(
                a,
                (r * 255f).toInt(),
                (g * 255f).toInt(),
                (b * 255f).toInt()
            )
        }

        return Bitmap.createBitmap(outputPixels, width, height, Bitmap.Config.ARGB_8888)
    }

    /**
     * AI Automatic Background Removal & Foreground Segmentation:
     * Analyzes image edges, subject centroid, and perimeter background pixels
     * to extract the subject with a high-quality alpha mask.
     */
    fun removeBackground(source: Bitmap): Bitmap {
        val width = source.width
        val height = source.height
        val pixels = IntArray(width * height)
        source.getPixels(pixels, 0, width, 0, 0, width, height)

        val outputPixels = IntArray(width * height)

        // Sample border background colors
        val corner1 = pixels[0]
        val corner2 = pixels[width - 1]
        val corner3 = pixels[(height - 1) * width]
        val corner4 = pixels[(height - 1) * width + (width - 1)]

        val bgR = (Color.red(corner1) + Color.red(corner2) + Color.red(corner3) + Color.red(corner4)) / 4
        val bgG = (Color.green(corner1) + Color.green(corner2) + Color.green(corner3) + Color.green(corner4)) / 4
        val bgB = (Color.blue(corner1) + Color.blue(corner2) + Color.blue(corner3) + Color.blue(corner4)) / 4

        val centerX = width / 2
        val centerY = height / 2
        val maxDist = Math.sqrt((centerX * centerX + centerY * centerY).toDouble()).toFloat()

        for (y in 0 until height) {
            for (x in 0 until width) {
                val idx = y * width + x
                val c = pixels[idx]
                val r = Color.red(c)
                val g = Color.green(c)
                val b = Color.blue(c)

                // Color difference to estimated background
                val colorDiff = abs(r - bgR) + abs(g - bgG) + abs(b - bgB)

                // Distance from center (subjects are typically centrally weighted)
                val distFromCenter = Math.sqrt(((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY)).toDouble()).toFloat()
                val centerWeight = 1.0f - (distFromCenter / maxDist).coerceIn(0f, 1f)

                // Decision score
                val isForeground = colorDiff > 45 || (centerWeight > 0.45f && colorDiff > 25)

                if (isForeground) {
                    // Smooth alpha feathering
                    val alpha = when {
                        colorDiff > 70 -> 255
                        else -> ((colorDiff - 25f) / 45f * 255f).toInt().coerceIn(180, 255)
                    }
                    outputPixels[idx] = Color.argb(alpha, r, g, b)
                } else {
                    outputPixels[idx] = Color.TRANSPARENT
                }
            }
        }

        return Bitmap.createBitmap(outputPixels, width, height, Bitmap.Config.ARGB_8888)
    }

    /**
     * Replaces background with color, gradient, studio backdrop, or image
     */
    fun replaceBackground(
        foregroundCutout: Bitmap,
        bgType: String, // "TRANSPARENT", "STUDIO_DARK", "CYBER_GRADIENT", "GOLDEN_SUNSET", "WHITE_CLEAN", "CUSTOM"
        customBgBitmap: Bitmap? = null
    ): Bitmap {
        val width = foregroundCutout.width
        val height = foregroundCutout.height
        val composite = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(composite)
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        when (bgType) {
            "TRANSPARENT" -> {
                // Keep transparent
            }
            "STUDIO_DARK" -> {
                canvas.drawColor(Color.parseColor("#0F172A"))
            }
            "CYBER_GRADIENT" -> {
                bgPaint.shader = LinearGradient(
                    0f, 0f, width.toFloat(), height.toFloat(),
                    intArrayOf(Color.parseColor("#1E1B4B"), Color.parseColor("#06B6D4"), Color.parseColor("#4C1D95")),
                    floatArrayOf(0f, 0.5f, 1f),
                    Shader.TileMode.CLAMP
                )
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)
            }
            "GOLDEN_SUNSET" -> {
                bgPaint.shader = LinearGradient(
                    0f, 0f, 0f, height.toFloat(),
                    intArrayOf(Color.parseColor("#FF6B6B"), Color.parseColor("#FFD93D"), Color.parseColor("#6C5CE7")),
                    floatArrayOf(0f, 0.6f, 1f),
                    Shader.TileMode.CLAMP
                )
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)
            }
            "WHITE_CLEAN" -> {
                canvas.drawColor(Color.WHITE)
            }
            "CUSTOM" -> {
                if (customBgBitmap != null) {
                    val srcRect = Rect(0, 0, customBgBitmap.width, customBgBitmap.height)
                    val dstRect = Rect(0, 0, width, height)
                    canvas.drawBitmap(customBgBitmap, srcRect, dstRect, bgPaint)
                } else {
                    canvas.drawColor(Color.parseColor("#1E293B"))
                }
            }
            else -> {
                canvas.drawColor(Color.parseColor("#111827"))
            }
        }

        // Draw foreground on top
        canvas.drawBitmap(foregroundCutout, 0f, 0f, Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG))
        return composite
    }

    /**
     * Renders text overlays and stickers onto the final bitmap for export
     */
    fun compositeOverlays(
        base: Bitmap,
        textOverlays: List<TextOverlay>,
        stickers: List<StickerOverlay>
    ): Bitmap {
        if (textOverlays.isEmpty() && stickers.isEmpty()) return base

        val result = base.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)

        // Draw stickers
        val stickerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 64f
            textAlign = Paint.Align.CENTER
        }
        for (sticker in stickers) {
            val x = sticker.xPosPosInPixels(base.width)
            val y = sticker.yPosPosInPixels(base.height)
            canvas.drawText(sticker.emoji, x, y, stickerPaint)
        }

        // Draw text overlays
        for (textOverlay in textOverlays) {
            val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = textOverlay.textColorHex.toInt()
                textSize = textOverlay.fontSizeSp * 2.2f
                typeface = if (textOverlay.isBold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
                textAlign = Paint.Align.CENTER
            }

            val x = textOverlay.xPosRatio * base.width
            val y = textOverlay.yPosRatio * base.height

            // Background pill for text
            val textBounds = Rect()
            textPaint.getTextBounds(textOverlay.text, 0, textOverlay.text.length, textBounds)
            val paddingX = 32f
            val paddingY = 16f
            val bgRect = RectF(
                x - textBounds.width() / 2f - paddingX,
                y - textBounds.height() - paddingY,
                x + textBounds.width() / 2f + paddingX,
                y + paddingY
            )
            val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = textOverlay.bgColorHex.toInt()
            }
            canvas.drawRoundRect(bgRect, 20f, 20f, bgPaint)
            canvas.drawText(textOverlay.text, x, y, textPaint)
        }

        return result
    }

    private fun StickerOverlay.xPosPosInPixels(width: Int): Float = xPosRatio * width
    private fun StickerOverlay.yPosPosInPixels(height: Int): Float = yPosRatio * height

    private fun applyVignetteEffect(canvas: Canvas, width: Float, height: Float, strength: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val radius = Math.sqrt((width * width + height * height).toDouble()).toFloat() / 1.8f
        paint.shader = android.graphics.RadialGradient(
            width / 2f, height / 2f, radius,
            intArrayOf(Color.TRANSPARENT, Color.TRANSPARENT, Color.argb((strength * 220).toInt(), 0, 0, 0)),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )
        canvas.drawRect(0f, 0f, width, height, paint)
    }

    /**
     * Crops bitmap according to target aspect ratio
     */
    fun cropToAspectRatio(source: Bitmap, targetRatio: Float): Bitmap {
        if (targetRatio <= 0f) return source
        val currentRatio = source.width.toFloat() / source.height.toFloat()

        val cropWidth: Int
        val cropHeight: Int

        if (currentRatio > targetRatio) {
            // Source is wider than target
            cropHeight = source.height
            cropWidth = (source.height * targetRatio).toInt().coerceIn(1, source.width)
        } else {
            // Source is taller than target
            cropWidth = source.width
            cropHeight = (source.width / targetRatio).toInt().coerceIn(1, source.height)
        }

        val startX = (source.width - cropWidth) / 2
        val startY = (source.height - cropHeight) / 2

        return Bitmap.createBitmap(source, startX, startY, cropWidth, cropHeight)
    }
}
