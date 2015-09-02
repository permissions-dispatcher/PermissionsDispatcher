package permissions.dispatcher.processor

import com.google.common.truth.Truth.ASSERT
import com.google.testing.compile.JavaFileObjects.forSourceLines
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource
import org.junit.Test
import permissions.dispatcher.processor.data.NewV13
import javax.tools.JavaFileObject

public class V13Test {

//    Rule public var expectedException: ExpectedException = ExpectedException.none()
    public val DEFAULT_CLASS: String = "MyFragment"

    private fun assertJavaSource(actual: JavaFileObject, expect: JavaFileObject) {
        ASSERT
                .about(javaSource())
                .that(actual)
                .processedWith(PermissionsProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expect)
    }

    Test fun newTest() {
        assertJavaSource(
                forSourceLines(DEFAULT_CLASS, NewV13.ACTUAL),
                forSourceLines(DEFAULT_CLASS + "PermissionsDispatcher", NewV13.EXPECT)
        )
    }

}