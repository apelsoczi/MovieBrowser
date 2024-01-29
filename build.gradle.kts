@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

// TODO: KTIJ-19369
//      suppressing DSL SCOPE VIOLATION expects an expression
//      remove this printlln() or comment out the @Suppress to see the errors.
println()
