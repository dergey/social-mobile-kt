package com.sergey.zhuravlev.mobile.social.data.datasource

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.sergey.zhuravlev.mobile.social.data.model.GlideCompressedImage
import java.io.ByteArrayOutputStream

class GlideDataSource(
  val context: Context
) {

  fun compressImage(fileUri: Uri, glideSignature: String): GlideCompressedImage {
    val bitmap: Bitmap = Glide.with(context)
      .asBitmap()
      .load(fileUri)
      .apply(
        RequestOptions()
          .override(1280)
          .downsample(DownsampleStrategy.AT_MOST)
          .signature(ObjectKey(glideSignature))
      )
      .submit()
      .get()

    val out = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
    val byteArray = out.toByteArray()
    out.close()

    return GlideCompressedImage(
      glideSignature,
      fileUri.lastPathSegment,
      byteArray
    )
  }

}