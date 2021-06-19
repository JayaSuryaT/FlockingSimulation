package com.digitalcrafts.flockingsimulation.logic.behaviors

import com.digitalcrafts.flockingsimulation.logic.defintions.FlockBehavior
import com.digitalcrafts.flockingsimulation.models.Boid
import com.digitalcrafts.flockingsimulation.models.FlockSnapshot
import com.digitalcrafts.flockingsimulation.models.Vector2d

class Alignment : FlockBehavior {

    override fun nextFrameFor(flockSnapshot: FlockSnapshot): FlockSnapshot =
        alignBoids(flockSnapshot)

    private fun alignBoids(source: FlockSnapshot): FlockSnapshot = source.copy(
        boids = source.boids.map { boid ->
            val newAcceleration = boid.align(source.boids)
            val multiplied = newAcceleration * source.alignmentCoefficient
            val final = boid.acceleration + multiplied
            boid.copy(acceleration = final)
        }
    )

    private fun Boid.align(flock: List<Boid>): Vector2d {

        val boid = this
        var total = 0
        var steering = Vector2d(0f, 0f)

        flock.forEach { other ->

            val distance = boid.position.distanceFrom(other.position)

            if (distance < PERCEPTION_RADIUS && boid != other) {
                total++
                steering += other.speed
            }
        }

        return if (total == 0) steering
        else {
            steering /= total.toFloat()
            steering = steering.withMagnitude(boid.maxSpeed)
            steering - boid.speed
            steering = steering.withLimit(boid.maxAcceleration)
            steering
        }
    }

    companion object {

        private const val PERCEPTION_RADIUS: Float = 100f
    }
}