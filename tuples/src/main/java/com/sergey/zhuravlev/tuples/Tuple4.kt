package com.sergey.zhuravlev.tuples

import java.util.function.Function

open class Tuple4<T1 : Any?, T2 : Any?, T3 : Any?, T4 : Any?> internal constructor(t1: T1, t2: T2, t3: T3, val t4: T4) :
  Tuple3<T1, T2, T3>(t1, t2, t3) {

  override fun <R> mapT1(mapper: Function<T1, R>): Tuple4<R, T2, T3, T4> {
    return Tuple4(mapper.apply(t1), t2, t3, t4)
  }

  override fun <R> mapT2(mapper: Function<T2, R>): Tuple4<T1, R, T3, T4> {
    return Tuple4(t1, mapper.apply(t2), t3, t4)
  }

  override fun <R> mapT3(mapper: Function<T3, R>): Tuple4<T1, T2, R, T4> {
    return Tuple4(t1, t2, mapper.apply(t3), t4)
  }

  open fun <R> mapT4(mapper: Function<T4, R>): Tuple4<T1, T2, T3, R> {
    return Tuple4(t1, t2, t3, mapper.apply(t4))
  }

  override fun get(index: Int): Any? {
    return when (index) {
      0 -> t1
      1 -> t2
      2 -> t3
      3 -> t4
      else -> null
    }
  }

  override fun toArray(): Array<Any?> {
    return arrayOf(t1, t2, t3, t4)
  }

  override fun equals(o: Any?): Boolean {
    if (this === o) return true
    if (o !is Tuple4<*, *, *, *>) return false
    if (!super.equals(o)) return false
    return t4 == o.t4
  }

  override fun hashCode(): Int {
    var result = super.hashCode()
    result = 31 * result + t4.hashCode()
    return result
  }

  override fun size(): Int {
    return 4
  }

  companion object {
    private const val serialVersionUID = -4898704078143033129L
  }

}