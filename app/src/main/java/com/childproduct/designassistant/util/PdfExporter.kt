package com.childproduct.designassistant.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * PDF 导出工具类
 *
 * 用于将设计方案导出为PDF文档
 * 修复：使用应用专属目录，支持协程，添加错误处理
 */
object PdfExporter {

    private const val TAG = "PdfExporter"
    private const val PAGE_WIDTH = 595  // A4 宽度 (pt)
    private const val PAGE_HEIGHT = 842 // A4 高度 (pt)
    private const val MARGIN_LEFT = 40
    private const val MARGIN_RIGHT = 40
    private const val MARGIN_TOP = 60
    private const val MARGIN_BOTTOM = 60
    private const val LINE_HEIGHT = 25
    private const val CONTENT_WIDTH = PAGE_WIDTH - MARGIN_LEFT - MARGIN_RIGHT

    /**
     * 导出设计方案为PDF（协程函数，在IO线程执行）
     *
     * @param context 应用上下文
     * @param markdownContent Markdown格式的内容
     * @param fileName 文件名（不含扩展名）
     * @return Result<File> 成功返回PDF文件，失败返回异常
     */
    suspend fun exportDesignProposal(
        context: Context,
        markdownContent: String,
        fileName: String = "儿童安全座椅设计方案"
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            // 1. 校验数据
            if (markdownContent.isBlank()) {
                return@withContext Result.failure(IllegalArgumentException("设计方案内容为空，无法生成PDF"))
            }

            // 2. 获取应用专属目录（Android 10+无需权限）
            val exportDir = getPdfExportDirectory(context)

            // 3. 创建PDF文档
            val pdfDocument = PdfDocument()

            // 4. 过滤文件名非法字符
            val safeFileName = fileName.replace(Regex("[\\\\/:*?\"<>|]"), "_")

            // 5. 生成文件名（带时间戳）
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val pdfFileName = "${safeFileName}_$timestamp.pdf"
            val pdfFile = File(exportDir, pdfFileName)

            // 6. 创建第一页
            val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
            var page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            val paint = Paint()

            // 7. 绘制标题
            drawTitle(canvas, paint, fileName)

            // 8. 解析和绘制Markdown内容
            var yPosition = (MARGIN_TOP + 80).toFloat()
            yPosition = drawMarkdownContent(canvas, paint, markdownContent, yPosition)

            // 9. 完成页面
            pdfDocument.finishPage(page)

            // 10. 写入文件
            val outputStream = FileOutputStream(pdfFile)
            pdfDocument.writeTo(outputStream)
            outputStream.close()
            pdfDocument.close()

            Log.d(TAG, "PDF导出成功: ${pdfFile.absolutePath}")
            Result.success(pdfFile)
        } catch (e: Exception) {
            Log.e(TAG, "PDF导出失败: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * 绘制标题
     */
    private fun drawTitle(canvas: Canvas, paint: Paint, title: String) {
        paint.color = Color.parseColor("#1976D2")
        paint.textSize = 28f
        paint.isFakeBoldText = true
        canvas.drawText(title, MARGIN_LEFT.toFloat(), (MARGIN_TOP + 40).toFloat(), paint)
        
        // 绘制下划线
        paint.color = Color.parseColor("#2196F3")
        paint.strokeWidth = 4f
        canvas.drawLine(
            MARGIN_LEFT.toFloat(),
            (MARGIN_TOP + 50).toFloat(),
            (PAGE_WIDTH - MARGIN_RIGHT).toFloat(),
            (MARGIN_TOP + 50).toFloat(),
            paint
        )
    }

    /**
     * 绘制Markdown内容
     */
    private fun drawMarkdownContent(
        canvas: Canvas,
        paint: Paint,
        content: String,
        startY: Float
    ): Float {
        var y = startY
        val lines = content.lines()

        for (line in lines) {
            if (y > (PAGE_HEIGHT - MARGIN_BOTTOM).toFloat()) {
                break // 页面已满
            }

            when {
                // 标题1 (#)
                line.startsWith("# ") -> {
                    paint.color = Color.parseColor("#1976D2")
                    paint.textSize = 24f
                    paint.isFakeBoldText = true
                    canvas.drawText(line.substring(2), MARGIN_LEFT.toFloat(), y, paint)
                    y += LINE_HEIGHT * 1.5f
                }
                // 标题2 (##)
                line.startsWith("## ") -> {
                    paint.color = Color.parseColor("#1976D2")
                    paint.textSize = 20f
                    paint.isFakeBoldText = true
                    canvas.drawText(line.substring(3), MARGIN_LEFT.toFloat(), y, paint)
                    y += LINE_HEIGHT * 1.3f
                }
                // 标题3 (###)
                line.startsWith("### ") -> {
                    paint.color = Color.parseColor("#1976D2")
                    paint.textSize = 18f
                    paint.isFakeBoldText = true
                    canvas.drawText(line.substring(4), MARGIN_LEFT.toFloat(), y, paint)
                    y += LINE_HEIGHT * 1.2f
                }
                // 标签 (🔵)
                line.startsWith("🔵") -> {
                    paint.color = Color.parseColor("#1976D2")
                    paint.textSize = 14f
                    paint.isFakeBoldText = false
                    canvas.drawText(line, (MARGIN_LEFT + 20).toFloat(), y, paint)
                    y += LINE_HEIGHT.toFloat()
                }
                // 列表项 (-)
                line.startsWith("- ") -> {
                    paint.color = Color.BLACK
                    paint.textSize = 14f
                    paint.isFakeBoldText = false
                    canvas.drawText("•", (MARGIN_LEFT + 20).toFloat(), y, paint)
                    // 简单的自动换行处理
                    val text = line.substring(2)
                    val remainingWidth = CONTENT_WIDTH - 40f
                    drawWrappedText(canvas, paint, text, (MARGIN_LEFT + 40).toFloat(), y, remainingWidth)
                    y += LINE_HEIGHT.toFloat()
                }
                // 空行
                line.isBlank() -> {
                    y += (LINE_HEIGHT * 0.5).toFloat()
                }
                // 普通文本
                else -> {
                    paint.color = Color.BLACK
                    paint.textSize = 14f
                    paint.isFakeBoldText = false
                    canvas.drawText(line, MARGIN_LEFT.toFloat(), y, paint)
                    y += LINE_HEIGHT.toFloat()
                }
            }
        }

        return y
    }

    /**
     * 绘制自动换行的文本
     */
    private fun drawWrappedText(
        canvas: Canvas,
        paint: Paint,
        text: String,
        x: Float,
        y: Float,
        maxWidth: Float
    ) {
        val words = text.split(" ")
        var currentLine = ""
        var currentX = x

        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
            val textWidth = paint.measureText(testLine)

            if (textWidth > maxWidth) {
                if (currentLine.isNotEmpty()) {
                    canvas.drawText(currentLine, currentX, y, paint)
                    currentLine = word
                    currentX = x
                } else {
                    canvas.drawText(word, currentX, y, paint)
                }
            } else {
                currentLine = testLine
            }
        }

        if (currentLine.isNotEmpty()) {
            canvas.drawText(currentLine, currentX, y, paint)
        }
    }

    /**
     * 获取PDF导出目录（应用专属目录，Android 10+无需权限）
     *
     * 路径说明：
     * - Android 10+：/Android/data/包名/files/Documents/DesignProposals/
     * - Android 9及以下：/data/data/包名/files/Documents/DesignProposals/
     *
     * @param context 应用上下文
     * @return PDF导出目录
     */
    fun getPdfExportDirectory(context: Context): File {
        return File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "DesignProposals").also {
            if (!it.exists()) {
                it.mkdirs()
            }
        }
    }
}
