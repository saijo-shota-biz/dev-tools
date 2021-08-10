package com.systemya_saijo

object TestConfig {

  val configMap = java.util.Map.of(
    "db.default.url", "jdbc:postgresql://localhost:15432/local",
    "db.default.username", "test",
    "db.default.password", "pass"
  )
}
