package com.sergey.zhuravlev.tuples

object Tuples {

  fun fromArray(list: Array<Any?>?): Tuple2<*, *> {
    require(!(list == null || list.size < 2)) { "null or too small array, need between 2 and 8 values" }
    when (list.size) {
      2 -> return of(
        list[0], list[1]
      )
      3 -> return of(
        list[0], list[1], list[2]
      )
      4 -> return of(
        list[0], list[1], list[2], list[3]
      )
      5 -> return of(
        list[0], list[1], list[2], list[3], list[4]
      )
      6 -> return of(
        list[0], list[1], list[2], list[3], list[4], list[5]
      )
      7 -> return of(
        list[0], list[1], list[2], list[3], list[4], list[5], list[6]
      )
      8 -> return of(
        list[0], list[1], list[2], list[3], list[4], list[5], list[6], list[7]
      )
    }
    throw IllegalArgumentException("too many arguments (" + list.size + "), need between 2 and 8 values")
  }

  fun <T1, T2> of(t1: T1, t2: T2): Tuple2<T1, T2> {
    return Tuple2(t1, t2)
  }

  fun <T1, T2, T3> of(t1: T1, t2: T2, t3: T3): Tuple3<T1, T2, T3> {
    return Tuple3(t1, t2, t3)
  }

  fun <T1, T2, T3, T4> of(t1: T1, t2: T2, t3: T3, t4: T4): Tuple4<T1, T2, T3, T4> {
    return Tuple4(t1, t2, t3, t4)
  }

  fun <T1, T2, T3, T4, T5> of(
    t1: T1,
    t2: T2,
    t3: T3,
    t4: T4,
    t5: T5
  ): Tuple5<T1, T2, T3, T4, T5> {
    return Tuple5(t1, t2, t3, t4, t5)
  }

  fun <T1, T2, T3, T4, T5, T6> of(
    t1: T1,
    t2: T2,
    t3: T3,
    t4: T4,
    t5: T5,
    t6: T6
  ): Tuple6<T1, T2, T3, T4, T5, T6> {
    return Tuple6(t1, t2, t3, t4, t5, t6)
  }

  fun <T1, T2, T3, T4, T5, T6, T7> of(
    t1: T1,
    t2: T2,
    t3: T3,
    t4: T4,
    t5: T5,
    t6: T6,
    t7: T7
  ): Tuple7<T1, T2, T3, T4, T5, T6, T7> {
    return Tuple7(t1, t2, t3, t4, t5, t6, t7)
  }

  fun <T1, T2, T3, T4, T5, T6, T7, T8> of(
    t1: T1,
    t2: T2,
    t3: T3,
    t4: T4,
    t5: T5,
    t6: T6,
    t7: T7,
    t8: T8
  ): Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> {
    return Tuple8(t1, t2, t3, t4, t5, t6, t7, t8)
  }

  fun tupleStringRepresentation(vararg values: Any?): StringBuilder {
    val sb = StringBuilder()
    for (i in values.indices) {
      val t = values[i]
      if (i != 0) {
        sb.append(',')
      }
      if (t != null) {
        sb.append(t)
      }
    }
    return sb
  }
}
