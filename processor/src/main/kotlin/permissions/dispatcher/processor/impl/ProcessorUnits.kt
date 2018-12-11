package permissions.dispatcher.processor.impl

import permissions.dispatcher.processor.impl.java.JavaActivityProcessorUnit
import permissions.dispatcher.processor.impl.java.JavaFragmentProcessorUnit
import permissions.dispatcher.processor.impl.kotlin.KotlinActivityProcessorUnit
import permissions.dispatcher.processor.impl.kotlin.KotlinFragmentProcessorUnit

fun javaProcessorUnits() = listOf(
        JavaActivityProcessorUnit(),
        JavaFragmentProcessorUnit())

fun kotlinProcessorUnits() = listOf(
        KotlinActivityProcessorUnit(),
        KotlinFragmentProcessorUnit())
