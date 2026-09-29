package id.kasirsaku.app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun LineIcon(name: String, modifier: Modifier = Modifier, color: Color = Color(0xFF315C45)) {
    Canvas(modifier) {
        val unit = size.minDimension / 24f
        val stroke = Stroke(width = 1.8f * unit)
        fun pt(x: Float, y: Float) = Offset(x * unit, y * unit)
        fun line(x1: Float, y1: Float, x2: Float, y2: Float) = drawLine(color, pt(x1, y1), pt(x2, y2), stroke.width)
        when (name) {
            "cashier" -> {
                drawRoundRect(color, pt(4f, 6f), Size(16f * unit, 14f * unit), CornerRadius(2f * unit), style = stroke)
                line(8f, 6f, 8f, 4f); line(8f, 4f, 16f, 4f); line(16f, 4f, 16f, 6f)
                line(7f, 11f, 17f, 11f); line(8f, 15f, 10f, 15f); line(13f, 15f, 16f, 15f)
            }
            "box" -> {
                val path = Path().apply { moveTo(4f*unit,8f*unit); lineTo(12f*unit,4f*unit); lineTo(20f*unit,8f*unit); lineTo(12f*unit,12f*unit); close() }
                drawPath(path, color, style = stroke)
                line(4f,8f,4f,17f); line(4f,17f,12f,21f); line(12f,21f,20f,17f); line(20f,17f,20f,8f); line(12f,12f,12f,21f)
            }
            "history" -> {
                drawCircle(color, 8.5f*unit, pt(12f,12f), style = stroke)
                line(12f,7f,12f,12f); line(12f,12f,15.5f,14f); line(4f,5f,4f,9f); line(4f,9f,8f,9f)
            }
            "chart" -> {
                line(4f,4f,4f,20f); line(4f,20f,21f,20f)
                val path = Path().apply { moveTo(7f*unit,16f*unit); lineTo(11f*unit,12f*unit); lineTo(14f*unit,14f*unit); lineTo(19f*unit,7f*unit) }
                drawPath(path, color, style = stroke)
                line(15f,7f,19f,7f); line(19f,7f,19f,11f)
            }
            "drink" -> {
                drawRoundRect(color, pt(5f,8f), Size(12f*unit,12f*unit), CornerRadius(1.5f*unit), style = stroke)
                line(17f,10f,19f,10f); line(19f,10f,19f,14f); line(19f,14f,17f,14f)
                line(8f,5f,8f,4f); line(11f,5f,11f,4f); line(14f,5f,14f,4f)
            }
            "bread" -> {
                val path = Path().apply { moveTo(5f*unit,20f*unit); lineTo(5f*unit,10f*unit); quadraticTo(5f*unit,5f*unit,9f*unit,6f*unit); quadraticTo(12f*unit,3f*unit,15f*unit,6f*unit); quadraticTo(20f*unit,5f*unit,20f*unit,10f*unit); lineTo(20f*unit,20f*unit); close() }
                drawPath(path, color, style = stroke); line(8f,11f,8f,15f); line(12f,10f,12f,15f); line(16f,11f,16f,15f)
            }
            "lemon" -> {
                val path = Path().apply { moveTo(5f*unit,13f*unit); quadraticTo(7f*unit,5f*unit,15f*unit,7f*unit); quadraticTo(21f*unit,9f*unit,19f*unit,14f*unit); quadraticTo(16f*unit,20f*unit,9f*unit,18f*unit); quadraticTo(4f*unit,17f*unit,5f*unit,13f*unit); close() }
                drawPath(path, color, style = stroke); line(11f,13f,16f,10f); line(14f,7f,18f,4f); line(18f,4f,20f,7f)
            }
            "water" -> {
                val path = Path().apply { moveTo(12f*unit,3f*unit); quadraticTo(9f*unit,8f*unit,6f*unit,12f*unit); quadraticTo(3f*unit,18f*unit,9f*unit,20f*unit); quadraticTo(16f*unit,23f*unit,19f*unit,17f*unit); quadraticTo(21f*unit,12f*unit,12f*unit,3f*unit); close() }
                drawPath(path, color, style = stroke); line(9f,16f,11f,18f)
            }
            "dessert" -> {
                val path = Path().apply { moveTo(4f*unit,19f*unit); lineTo(12f*unit,5f*unit); lineTo(20f*unit,19f*unit); close() }
                drawPath(path, color, style = stroke); line(7f,14f,17f,14f); line(9f,10f,15f,10f); line(6f,20f,18f,20f)
            }
            else -> {
                drawCircle(color, 7f*unit, pt(10f,10f), style = stroke)
                line(15f,15f,20f,20f)
            }
        }
    }
}
