package permissions.dispatcher.processor.util

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class ExtensionsTest {
    @Test
    fun `java Byte is converted into kotlin Byte`() {
        val typeName = Mockito.mock(TypeName::class.java)
        `when`(typeName.toString()).thenReturn("java.lang.Byte")
        val expected = typeName.correctJavaTypeToKotlinType().toString()
        assertEquals(expected, "kotlin.Byte")
    }

    @Test
    fun `java Double is converted into kotlin Double`() {
        val typeName = Mockito.mock(TypeName::class.java)
        `when`(typeName.toString()).thenReturn("java.lang.Double")
        val expected = typeName.correctJavaTypeToKotlinType().toString()
        assertEquals(expected, "kotlin.Double")
    }

    @Test
    fun `java Object is converted into kotlin Any`() {
        val typeName = Mockito.mock(TypeName::class.java)
        `when`(typeName.toString()).thenReturn("java.lang.Object")
        val expected = typeName.correctJavaTypeToKotlinType().toString()
        assertEquals(expected, "kotlin.Any")
    }

    @Test
    fun `java String is converted into kotlin String`() {
        val typeName = Mockito.mock(TypeName::class.java)
        `when`(typeName.toString()).thenReturn("java.lang.String")
        val expected = typeName.correctJavaTypeToKotlinType().toString()
        assertEquals(expected, "kotlin.String")
    }

    @Test
    fun `java String in List is converted into kotlin String`() {
        val parameterizedTypeName = Mockito.mock(ParameterizedTypeName::class.java)
        val typeName = Mockito.mock(TypeName::class.java)
        `when`(typeName.toString()).thenReturn("java.lang.String")
        `when`(parameterizedTypeName.rawType).thenReturn(ClassName.bestGuess("kotlin.collections.List"))
        `when`(parameterizedTypeName.typeArguments).thenReturn(listOf(typeName))

        val expected = parameterizedTypeName.correctJavaTypeToKotlinType() as ParameterizedTypeName
        expected.typeArguments.first().toString()
        assertEquals( expected.typeArguments.first().toString(), "kotlin.String")
    }
}