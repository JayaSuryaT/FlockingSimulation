package com.digitalcrafts.flockingsimulation.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.digitalcrafts.flockingsimulation.R
import com.digitalcrafts.flockingsimulation.logic.FlockGenerator
import com.digitalcrafts.flockingsimulation.logic.defintions.FlockBehavior
import com.digitalcrafts.flockingsimulation.logic.processor.FlockBehaviorProcessor
import com.digitalcrafts.flockingsimulation.models.Boid
import com.digitalcrafts.flockingsimulation.models.FlockSnapshot

class FlockBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val boidPaint: Paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.white)
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val flockBehaviorProcessor: FlockBehavior = FlockBehaviorProcessor()

    private var flockSnapshot: FlockSnapshot = FlockGenerator(
        numberOfBoids = 25,
        width = 1000f,
        height = 1000f,
    ).generate()

    fun setAlignmentCoefficient(value: Float) {
        flockSnapshot = flockSnapshot.copy(alignmentCoefficient = value)
    }

    fun setCohesionCoefficient(value: Float) {
        flockSnapshot = flockSnapshot.copy(cohesionCoefficient = value)
    }

    fun setSeparationCoefficient(value: Float) {
        flockSnapshot = flockSnapshot.copy(separationCoefficient = value)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        flockSnapshot.height = h.toFloat()
        flockSnapshot.width = w.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        flockSnapshot.draw(canvas)
        updateSnapshot()
    }

    private fun updateSnapshot() {
        flockSnapshot = flockBehaviorProcessor.nextFrameFor(flockSnapshot)
        invalidate()
    }

    private fun FlockSnapshot.draw(canvas: Canvas): Unit =
        this.boids.forEach { it.draw(canvas) }

    private fun Boid.draw(canvas: Canvas): Unit =
        canvas.drawCircle(this.position.x, this.position.y, BOID_RADIUS, boidPaint)

    companion object {

        private const val BOID_RADIUS: Float = 10f
    }
}