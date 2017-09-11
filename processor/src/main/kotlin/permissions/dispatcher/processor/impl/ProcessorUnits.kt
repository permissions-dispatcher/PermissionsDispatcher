package permissions.dispatcher.processor.impl

import permissions.dispatcher.processor.impl.java.JavaActivityProcessorUnit
import permissions.dispatcher.processor.impl.java.JavaNativeFragmentProcessorUnit
import permissions.dispatcher.processor.impl.java.JavaSupportFragmentProcessorUnit
import permissions.dispatcher.processor.impl.kotlin.KotlinActivityProcessorUnit
import permissions.dispatcher.processor.impl.kotlin.KotlinNativeFragmentProcessorUnit
import permissions.dispatcher.processor.impl.kotlin.KotlinSupportFragmentProcessorUnit

fun javaProcessorUnits() = listOf(
        JavaActivityProcessorUnit(),
        JavaSupportFragmentProcessorUnit(),
        JavaNativeFragmentProcessorUnit())

fun kotlinProcessorUnits() = listOf(
        KotlinActivityProcessorUnit(),
        KotlinSupportFragmentProcessorUnit(),
        KotlinNativeFragmentProcessorUnit())
