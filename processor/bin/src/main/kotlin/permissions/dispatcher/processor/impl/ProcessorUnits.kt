package permissions.dispatcher.processor.impl

import permissions.dispatcher.processor.impl.java.JavaActivityProcessorUnit
import permissions.dispatcher.processor.impl.java.JavaNativeFragmentProcessorUnit
import permissions.dispatcher.processor.impl.java.JavaSupportFragmentProcessorUnit
import permissions.dispatcher.processor.impl.kotlin.KotlinActivityProcessorUnit
import permissions.dispatcher.processor.impl.kotlin.KotlinNativeFragmentProcessorUnit
import permissions.dispatcher.processor.impl.kotlin.KotlinSupportFragmentProcessorUnit
import javax.annotation.processing.Messager

fun javaProcessorUnits(messager: Messager) = listOf(
        JavaActivityProcessorUnit(messager),
        JavaSupportFragmentProcessorUnit(messager),
        JavaNativeFragmentProcessorUnit(messager))

fun kotlinProcessorUnits(messager: Messager) = listOf(
        KotlinActivityProcessorUnit(messager),
        KotlinSupportFragmentProcessorUnit(messager),
        KotlinNativeFragmentProcessorUnit(messager))
