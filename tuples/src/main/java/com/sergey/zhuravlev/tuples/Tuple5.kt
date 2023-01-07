package com.sergey.zhuravlev.tuples

import java.util.function.Function

open class Tuple5<T1, T2, T3, T4, T5> internal constructor(
  t1: T1,
  t2: T2,
  t3: T3,
  t4: T4,
  val t5: T5
) : Tuple4<T1, T2, T3, T4>(t1, t2, t3, t4) {

  override fun <R> mapT1(mapper: Function<T1, R>): Tuple5<R, T2, T3, T4, T5> {
    return Tuple5(mapper.apply(t1), t2, t3, t4, t5)
  }

  override fun <R> mapT2(mapper: Function<T2, R>): Tuple5<T1, R, T3, T4, T5> {
    return Tuple5(t1, mapper.apply(t2), t3, t4, t5)
  }

  override fun <R> mapT3(mapper: Function<T3, R>): Tuple5<T1, T2, R, T4, T5> {
    return Tuple5(t1, t2, mapper.apply(t3), t4, t5)
  }

  override fun <R> mapT4(mapper: Function<T4, R>): Tuple5<T1, T2, T3, R, T5> {
    return Tuple5(t1, t2, t3, mapper.apply(t4), t5)
  }

  open fun <R> mapT5(mapper: Function<T5, R>): Tuple5<T1, T2, T3, T4, R> {
    return Tuple5(t1, t2, t3, t4, mapper.apply(t5))
  }

  override fun get(index: Int): Any? {
    return when (index) {
      0 -> t1
      1 -> t2
      2 -> t3
      3 -> t4
      4 -> t5
      else -> null
    }
  }

  override fun toArray(): Array<Any?> {
    return arrayOf(t1, t2, t3, t4, t5)
  }

  override fun equals(o: Any?): Boolean {
    if (this === o) return true
    if (o !is Tuple5<*, *, *, *, *>) return false
    if (!super.equals(o)) return false
    return t5 == o.t5
  }

  override fun hashCode(): Int {
    var result = super.hashCode()
    result = 31 * result + t5.hashCode()
    return result
  }

  override fun size(): Int {
    return 5
  }

  companion object {
    private const val serialVersionUID = 3541548454198133275L
  }

}