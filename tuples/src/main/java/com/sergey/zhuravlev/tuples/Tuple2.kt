package com.sergey.zhuravlev.tuples

import java.io.Serializable
import java.util.*
import java.util.function.Function

open class Tuple2<T1 : Any?, T2 : Any?> internal constructor(val t1: T1, val t2: T2) : Iterable<Any?>,
  Serializable {

  open fun <R : Any?> mapT1(mapper: Function<T1, R>): Tuple2<R, T2> {
    return Tuple2(mapper.apply(t1), t2)
  }

  open fun <R : Any?> mapT2(mapper: Function<T2, R>): Tuple2<T1, R> {
    return Tuple2(t1, mapper.apply(t2))
  }

  open operator fun get(index: Int): Any? {
    return when (index) {
      0 -> t1
      1 -> t2
      else -> null
    }
  }

  open fun toList(): List<Any?> {
    return listOf(t1, t2)
  }

  open fun toArray(): Array<Any?> {
    return arrayOf(t1, t2)
  }

  override fun iterator(): MutableIterator<Any?> {
    return Collections.unmodifiableList(toList()).iterator()
  }

  override fun equals(o: Any?): Boolean {
    if (this === o) {
      return true
    }
    if (o == null || javaClass != o.javaClass) {
      return false
    }
    val tuple2 = o as Tuple2<*, *>
    return t1 == tuple2.t1 && t2 == tuple2.t2
  }

  override fun hashCode(): Int {
    var result = size()
    result = 31 * result + t1.hashCode()
    result = 31 * result + t2.hashCode()
    return result
  }

  open fun size(): Int {
    return 2
  }

  override fun toString(): String {
    return Tuples.tupleStringRepresentation(*toArray()).insert(0, '[').append(']')
      .toString()
  }

  companion object {
    private const val serialVersionUID = -3518082018884860684L
  }
}