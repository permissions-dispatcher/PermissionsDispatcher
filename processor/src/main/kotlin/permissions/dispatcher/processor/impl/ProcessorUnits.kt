package permissions.dispatcher.processor.impl

import permissions.dispatcher.processor.impl.java.JavaActivityProcessorUnit
import permissions.dispatcher.processor.impl.java.JavaFragmentProcessorUnit
import permissions.dispatcher.processor.impl.kotlin.KotlinActivityProcessorUnit
import permissions.dispatcher.processor.impl.kotlin.KotlinFragmentProcessorUnit
import javax.annotation.processing.Messager

fun javaProcessorUnits(messager: Messager) = listOf(
        JavaActivityProcessorUnit(messager),
        JavaFragmentProcessorUnit(messager))

fun kotlinProcessorUnits(messager: Messager) = listOf(
        KotlinActivityProcessorUnit(messager),
        KotlinFragmentProcessorUnit(messager))
