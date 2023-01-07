package com.sergey.zhuravlev.tuples

import java.util.function.Function

class Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> internal constructor(
  t1: T1,
  t2: T2,
  t3: T3,
  t4: T4,
  t5: T5,
  t6: T6,
  t7: T7,
  val t8: T8
) : Tuple7<T1, T2, T3, T4, T5, T6, T7>(t1, t2, t3, t4, t5, t6, t7) {

  override fun <R> mapT1(mapper: Function<T1, R>): Tuple8<R, T2, T3, T4, T5, T6, T7, T8> {
    return Tuple8(mapper.apply(t1), t2, t3, t4, t5, t6, t7, t8)
  }

  override fun <R> mapT2(mapper: Function<T2, R>): Tuple8<T1, R, T3, T4, T5, T6, T7, T8> {
    return Tuple8(t1, mapper.apply(t2), t3, t4, t5, t6, t7, t8)
  }

  override fun <R> mapT3(mapper: Function<T3, R>): Tuple8<T1, T2, R, T4, T5, T6, T7, T8> {
    return Tuple8(t1, t2, mapper.apply(t3), t4, t5, t6, t7, t8)
  }

  override fun <R> mapT4(mapper: Function<T4, R>): Tuple8<T1, T2, T3, R, T5, T6, T7, T8> {
    return Tuple8(t1, t2, t3, mapper.apply(t4), t5, t6, t7, t8)
  }

  override fun <R> mapT5(mapper: Function<T5, R>): Tuple8<T1, T2, T3, T4, R, T6, T7, T8> {
    return Tuple8(t1, t2, t3, t4, mapper.apply(t5), t6, t7, t8)
  }

  override fun <R> mapT6(mapper: Function<T6, R>): Tuple8<T1, T2, T3, T4, T5, R, T7, T8> {
    return Tuple8(t1, t2, t3, t4, t5, mapper.apply(t6), t7, t8)
  }

  override fun <R> mapT7(mapper: Function<T7, R>): Tuple8<T1, T2, T3, T4, T5, T6, R, T8> {
    return Tuple8(t1, t2, t3, t4, t5, t6, mapper.apply(t7), t8)
  }

  fun <R> mapT8(mapper: Function<T8, R>): Tuple8<T1, T2, T3, T4, T5, T6, T7, R> {
    return Tuple8(t1, t2, t3, t4, t5, t6, t7, mapper.apply(t8))
  }

  override fun get(index: Int): Any? {
    return when (index) {
      0 -> t1
      1 -> t2
      2 -> t3
      3 -> t4
      4 -> t5
      5 -> t6
      6 -> t7
      7 -> t8
      else -> null
    }
  }

  override fun toArray(): Array<Any?> {
    return arrayOf(t1, t2, t3, t4, t5, t6, t7, t8)
  }

  override fun equals(o: Any?): Boolean {
    if (this === o) return true
    if (o !is Tuple8<*, *, *, *, *, *, *, *>) return false
    if (!super.equals(o)) return false
    return t8 == o.t8
  }

  override fun hashCode(): Int {
    var result = super.hashCode()
    result = 31 * result + t8.hashCode()
    return result
  }

  override fun size(): Int {
    return 8
  }

  companion object {
    private const val serialVersionUID = -8746796646535446242L
  }
}