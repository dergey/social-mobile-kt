package com.sergey.zhuravlev.tuples

import java.util.function.Function

open class Tuple3<T1 : Any?, T2 : Any?, T3 : Any?> internal constructor(t1: T1, t2: T2, val t3: T3) :
  Tuple2<T1, T2>(t1, t2) {

  override fun <R> mapT1(mapper: Function<T1, R>): Tuple3<R, T2, T3> {
    return Tuple3(mapper.apply(t1), t2, t3)
  }

  override fun <R> mapT2(mapper: Function<T2, R>): Tuple3<T1, R, T3> {
    return Tuple3(t1, mapper.apply(t2), t3)
  }

  open fun <R> mapT3(mapper: Function<T3, R>): Tuple3<T1, T2, R> {
    return Tuple3(t1, t2, mapper.apply(t3))
  }

  override fun get(index: Int): Any? {
    return when (index) {
      0 -> t1
      1 -> t2
      2 -> t3
      else -> null
    }
  }

  override fun toArray(): Array<Any?> {
    return arrayOf(t1, t2, t3)
  }

  override fun equals(o: Any?): Boolean {
    if (this === o) return true
    if (o !is Tuple3<*, *, *>) return false
    if (!super.equals(o)) return false
    return t3 == o.t3
  }

  override fun size(): Int {
    return 3
  }

  override fun hashCode(): Int {
    var result = super.hashCode()
    result = 31 * result + t3.hashCode()
    return result
  }

  companion object {
    private const val serialVersionUID = -4430274211524723033L
  }

}