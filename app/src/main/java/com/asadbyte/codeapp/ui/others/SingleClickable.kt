package com.asadbyte.codeapp.ui.others

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.singleClickable(
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    var lastClickTime by remember { mutableStateOf(0L) }

    clickable(
        enabled = enabled,
        onClick = {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime > 500L) { // 500ms debounce window
                lastClickTime = currentTime
                onClick()
            }
        }
    )
}

/**
 * This modifier disables multi-touch input for its content.
 * Once a first pointer goes down, all subsequent pointers that go down will be consumed,
 * preventing them from reaching the child composables.
 */
fun Modifier.disableMultiTouch() = this.then(
    Modifier.pointerInput(Unit) {
        awaitPointerEventScope {
            // This outer loop restarts after all pointers are up.
            while (true) {
                // 1. Wait for the first pointer to press down.
                val firstDown = awaitFirstDown(requireUnconsumed = true)

                // 2. Now that one pointer is down, consume all subsequent down events from other pointers.
                while (true) {
                    val event = awaitPointerEvent()

                    // If a different pointer goes down, consume the change.
                    event.changes.forEach {
                        if (it.id != firstDown.id && it.pressed) {
                            it.consume()
                        }
                    }

                    // If all pointers are up, break this inner loop to await a new first press.
                    if (event.changes.none { it.pressed }) {
                        break
                    }
                }
            }
        }
    }
)