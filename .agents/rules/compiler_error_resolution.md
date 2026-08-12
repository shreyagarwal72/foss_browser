# Compiler Error Resolution & API Signature Rules

## 1. No Guessing Signatures When Compiler Output Provides Candidates
- When Kotlin/Java/build compiler outputs list candidate function signatures, **do NOT guess** or attempt trial-and-error across multiple syntax variants.
- Inspect the exact candidate signatures from the compiler output log.
- Match the exact parameter names and parameter types (`Stroke` vs `Dp`, `() -> Float` vs `Float`, etc.) directly from the compiler diagnostic.

## 2. Compose Wavy Progress Indicators Signature
- `CircularWavyProgressIndicator` accepts `stroke: Stroke` (an `androidx.compose.ui.graphics.drawscope.Stroke` object), NOT `strokeWidth: Dp`.
- `progress` parameter takes a lambda `() -> Float` (e.g. `{ animatedProgress }`).
- Proper usage for explicit stroke width:
  ```kotlin
  CircularWavyProgressIndicator(
      progress = { animatedProgress },
      modifier = Modifier.size(36.dp),
      stroke = Stroke(width = with(LocalDensity.current) { 3.dp.toPx() })
  )
  ```
