package com.childproduct.designassistant.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * PDF 导出工具类
 * 
 * 用于将设计方案导出为PDF文档
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
     * 导出设计方案为PDF
     * 
     * @param context 应用上下文
     * @param markdownContent Markdown格式的内容
     * @param fileName 文件名（不含扩展名）
     * @return 导出的PDF文件，失败返回null
     */
    fun exportDesignProposal(
        context: Context,
        markdownContent: String,
        fileName: String = "儿童安全座椅设计方案"
    ): File? {
        return try {
            // 创建PDF文档
            val pdfDocument = PdfDocument()

            // 创建第一页
            val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
            var page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            val paint = Paint()

            // 绘制标题
            drawTitle(canvas, paint, fileName)
            
            // 解析和绘制Markdown内容
            var yPosition = (MARGIN_TOP + 80).toFloat()
            yPosition = drawMarkdownContent(canvas, paint, markdownContent, yPosition)

            // 如果内容超过一页，创建新页面
            while (yPosition > PAGE_HEIGHT - MARGIN_BOTTOM) {
                pdfDocument.finishPage(page)
                val newPageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pdfDocument.pages.size + 1).create()
                val newPage = pdfDocument.startPage(newPageInfo)
                val newCanvas = newPage.canvas
                yPosition = drawMarkdownContent(newCanvas, paint, markdownContent, MARGIN_TOP.toFloat())
                if (yPosition <= PAGE_HEIGHT - MARGIN_BOTTOM) {
                    page = newPage
                } else {
                    pdfDocument.finishPage(newPage)
                }
            }

            // 完成页面
            pdfDocument.finishPage(page)

            // 保存到Downloads目录
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs()
            }

            // 生成文件名（带时间戳）
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val pdfFileName = "${fileName}_$timestamp.pdf"
            val pdfFile = File(downloadsDir, pdfFileName)

            // 写入文件
            val outputStream = FileOutputStream(pdfFile)
            pdfDocument.writeTo(outputStream)
            outputStream.close()
            pdfDocument.close()

            Log.d(TAG, "PDF导出成功: ${pdfFile.absolutePath}")
            pdfFile
        } catch (e: Exception) {
            Log.e(TAG, "PDF导出失败", e)
            null
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
     * 获取PDF导出目录
     */
    fun getPdfExportDirectory(context: Context): File {
        return File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "DesignProposals").also {
            if (!it.exists()) {
                it.mkdirs()
            }
        }
    }
}
