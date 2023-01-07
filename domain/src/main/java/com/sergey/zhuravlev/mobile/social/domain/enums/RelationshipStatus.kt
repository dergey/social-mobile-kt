package com.sergey.zhuravlev.mobile.social.domain.enums

enum class RelationshipStatus {
  NONE,
  SINGLE,
  IN_RELATIONSHIPS,
  ENGAGED,
  MARRIED,
  IN_CIVIL_MARRIAGE,
  IN_LOVE;

  fun toCode(): String {
    return "${ENUM_PREFIX}.${name.lowercase()}"
  }

  companion object {
    private const val ENUM_PREFIX = "relationship_status"
  }
}