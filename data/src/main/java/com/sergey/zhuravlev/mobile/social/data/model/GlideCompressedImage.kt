package com.sergey.zhuravlev.mobile.social.data.model

data class GlideCompressedImage(
  val glideSignature: String? = null,
  val filename: String? = null,
  val bytearray: ByteArray
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as GlideCompressedImage

    if (glideSignature != other.glideSignature) return false
    if (filename != other.filename) return false
    if (!bytearray.contentEquals(other.bytearray)) return false

    return true
  }

  override fun hashCode(): Int {
    var result = glideSignature?.hashCode() ?: 0
    result = 31 * result + (filename?.hashCode() ?: 0)
    result = 31 * result + bytearray.contentHashCode()
    return result
  }
}