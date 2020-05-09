package com.csrapp.csr.datastructure

import kotlin.math.min

class FuzzySet {
    private val data = mutableMapOf<Int, Double>()

    fun add(key: Int, degree: Double) {
        if (degree < 0 || degree > 1) {
            throw IllegalArgumentException("Degree of membership must be in inclusive range of 0.0-1.0")
        } else if (key in data.keys) {
            throw IllegalArgumentException("Key already present in the fuzzy set")
        }

        data[key] = degree
    }

    fun remove(key: Int) {
        if (key !in data.keys) {
            throw IllegalArgumentException("Key not present in the fuzzy set")
        }

        data.remove(key)
    }

    fun intersect(otherSet: FuzzySet): FuzzySet {
        // Throw exception if sets are not compatible.
        compatibleSet(otherSet)

        val resultSet = FuzzySet()
        for (key in data.keys) {
            resultSet.add(key, min(data[key]!!, otherSet.data[key]!!))
        }

        return resultSet
    }

    fun divide(otherSet: FuzzySet): FuzzySet {
        // Throw exception if sets are not compatible.
        compatibleSet(otherSet)

        val resultSet = FuzzySet()
        for (key in data.keys) {
            if (otherSet.data[key] == 0.0) {
                resultSet.add(key, 0.0)
            } else {
                resultSet.add(key, data[key]!! / otherSet.data[key]!!)
            }
        }

        return resultSet
    }

    fun arithmeticMean(): Double {
        // Mean of empty set is zero.
        if (data.keys.size == 0)
            return 0.0

        var sum = 0.0
        for (key in data.keys) {
            sum += data[key]!!
        }

        return sum / data.keys.size
    }

    private fun compatibleSet(otherSet: FuzzySet): Boolean {
        // Check if other set has all the keys of this fuzzy set.
        for (key in data.keys) {
            if (otherSet.data[key] == null) {
                throw IllegalArgumentException("Both the fuzzy sets must have same members")
            }
        }

        // Check if this set has all the keys of other fuzzy set.
        for (key in otherSet.data.keys) {
            if (data[key] == null) {
                throw IllegalArgumentException("Both the fuzzy sets must have same members")
            }
        }

        return true
    }

    override
    fun toString(): String {
        val builder = StringBuilder()
        for (key in data.keys) {
            builder.append("$key=${data[key]}\n")
        }

        return builder.toString()
    }
}