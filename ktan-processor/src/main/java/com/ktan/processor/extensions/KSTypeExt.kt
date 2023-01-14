package com.ktan.processor.extensions

import com.google.devtools.ksp.symbol.KSType

fun Sequence<KSType>.isActivityPresent(): Boolean = firstOrNull {
    it.declaration.simpleName.getShortName() == "AppCompatActivity" ||
        it.declaration.simpleName.getShortName() == "ComponentActivity" ||
        it.declaration.simpleName.getShortName() == "Activity"
} != null

fun Sequence<KSType>.isFragmentPresent(): Boolean = firstOrNull {
    it.declaration.simpleName.getShortName() == "Fragment"
} != null