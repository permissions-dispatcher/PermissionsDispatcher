package permissions.dispatcher.processor.impl

import permissions.dispatcher.processor.impl.java.JavaActivityProcessorUnit
import permissions.dispatcher.processor.impl.java.JavaFragmentProcessorUnit
import permissions.dispatcher.processor.impl.kotlin.KotlinActivityProcessorUnit
import permissions.dispatcher.processor.impl.kotlin.KotlinFragmentProcessorUnit

val javaProcessorUnits = listOf(JavaActivityProcessorUnit(), JavaFragmentProcessorUnit())

val kotlinProcessorUnits = listOf(KotlinActivityProcessorUnit(), KotlinFragmentProcessorUnit())
