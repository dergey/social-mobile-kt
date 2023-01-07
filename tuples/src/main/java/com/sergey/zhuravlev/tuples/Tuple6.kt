package com.sergey.zhuravlev.tuples

import java.util.function.Function

open class Tuple6<T1, T2, T3, T4, T5, T6> internal constructor(
  t1: T1,
  t2: T2,
  t3: T3,
  t4: T4,
  t5: T5,
  val t6: T6
) : Tuple5<T1, T2, T3, T4, T5>(t1, t2, t3, t4, t5) {

  override fun <R> mapT1(mapper: Function<T1, R>): Tuple6<R, T2, T3, T4, T5, T6> {
    return Tuple6(mapper.apply(t1), t2, t3, t4, t5, t6)
  }

  override fun <R> mapT2(mapper: Function<T2, R>): Tuple6<T1, R, T3, T4, T5, T6> {
    return Tuple6(t1, mapper.apply(t2), t3, t4, t5, t6)
  }

  override fun <R> mapT3(mapper: Function<T3, R>): Tuple6<T1, T2, R, T4, T5, T6> {
    return Tuple6(t1, t2, mapper.apply(t3), t4, t5, t6)
  }

  override fun <R> mapT4(mapper: Function<T4, R>): Tuple6<T1, T2, T3, R, T5, T6> {
    return Tuple6(t1, t2, t3, mapper.apply(t4), t5, t6)
  }

  override fun <R> mapT5(mapper: Function<T5, R>): Tuple6<T1, T2, T3, T4, R, T6> {
    return Tuple6(t1, t2, t3, t4, mapper.apply(t5), t6)
  }

  open fun <R> mapT6(mapper: Function<T6, R>): Tuple6<T1, T2, T3, T4, T5, R> {
    return Tuple6(t1, t2, t3, t4, t5, mapper.apply(t6))
  }

  override fun get(index: Int): Any? {
    return when (index) {
      0 -> t1
      1 -> t2
      2 -> t3
      3 -> t4
      4 -> t5
      5 -> t6
      else -> null
    }
  }

  override fun toArray(): Array<Any?> {
    return arrayOf(t1, t2, t3, t4, t5, t6)
  }

  override fun equals(o: Any?): Boolean {
    if (this === o) return true
    if (o !is Tuple6<*, *, *, *, *, *>) return false
    if (!super.equals(o)) return false
    return t6 == o.t6
  }

  override fun hashCode(): Int {
    var result = super.hashCode()
    result = 31 * result + t6.hashCode()
    return result
  }

  override fun size(): Int {
    return 6
  }

  companion object {
    private const val serialVersionUID = 770306356087176830L
  }
}