package permissions.dispatcher.processor.util

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class ExtensionsTest {
    @Test
    fun `java Byte being converted into kotlin Byte`() {
        val typeName = mock(TypeName::class.java)
        `when`(typeName.toString()).thenReturn("java.lang.Byte")
        val expected = typeName.correctJavaTypeToKotlinType().toString()
        assertEquals(expected, "kotlin.Byte")
    }

    @Test
    fun `java Double being converted into kotlin Double`() {
        val typeName = mock(TypeName::class.java)
        `when`(typeName.toString()).thenReturn("java.lang.Double")
        val expected = typeName.correctJavaTypeToKotlinType().toString()
        assertEquals(expected, "kotlin.Double")
    }

    @Test
    fun `java Object being converted into kotlin Any`() {
        val typeName = mock(TypeName::class.java)
        `when`(typeName.toString()).thenReturn("java.lang.Object")
        val expected = typeName.correctJavaTypeToKotlinType().toString()
        assertEquals(expected, "kotlin.Any")
    }

    @Test
    fun `java String being converted into kotlin String`() {
        val typeName = mock(TypeName::class.java)
        `when`(typeName.toString()).thenReturn("java.lang.String")
        val expected = typeName.correctJavaTypeToKotlinType().toString()
        assertEquals(expected, "kotlin.String")
    }

    @Test
    fun `java String in parameter being converted into kotlin String`() {
        val parameterizedTypeName = mock(ParameterizedTypeName::class.java)
        val typeName = mock(TypeName::class.java)
        `when`(typeName.toString()).thenReturn("java.lang.String")
        `when`(parameterizedTypeName.rawType).thenReturn(ClassName.bestGuess("kotlin.collections.List"))
        `when`(parameterizedTypeName.typeArguments).thenReturn(listOf(typeName))

        val expected = parameterizedTypeName.correctJavaTypeToKotlinType() as ParameterizedTypeName
        expected.typeArguments.first().toString()
        assertEquals( expected.typeArguments.first().toString(), "kotlin.String")
    }

    @Test
    fun `kotlin ByteArray retains its type`() {
        val typeName = mock(TypeName::class.java)
        `when`(typeName.toString()).thenReturn("kotlin.ByteArray")
        val expected = typeName.correctJavaTypeToKotlinType().toString()
        assertEquals(expected, "kotlin.ByteArray")
    }

    @Test
    fun `kotlin List retains its type`() {
        val typeName = mock(TypeName::class.java)
        `when`(typeName.toString()).thenReturn("kotlin.collections.List")
        val expected = typeName.correctJavaTypeToKotlinType().toString()
        assertEquals(expected, "kotlin.collections.List")
    }
}