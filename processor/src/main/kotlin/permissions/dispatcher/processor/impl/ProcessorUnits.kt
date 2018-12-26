package permissions.dispatcher.processor.impl

import permissions.dispatcher.processor.impl.java.JavaActivityProcessorUnit
import permissions.dispatcher.processor.impl.java.JavaConductorProcessorUnit
import permissions.dispatcher.processor.impl.java.JavaFragmentProcessorUnit
import permissions.dispatcher.processor.impl.kotlin.KotlinActivityProcessorUnit
import permissions.dispatcher.processor.impl.kotlin.KotlinConductorProcessorUnit
import permissions.dispatcher.processor.impl.kotlin.KotlinFragmentProcessorUnit

val javaProcessorUnits = listOf(JavaActivityProcessorUnit(), JavaFragmentProcessorUnit(), JavaConductorProcessorUnit())
val kotlinProcessorUnits = listOf(KotlinActivityProcessorUnit(), KotlinFragmentProcessorUnit(), KotlinConductorProcessorUnit())
