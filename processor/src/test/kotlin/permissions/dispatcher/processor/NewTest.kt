package permissions.dispatcher.processor

import com.google.common.truth.Truth.ASSERT
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource
import com.google.testing.compile.JavaFileObjects.forSourceLines
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import permissions.dispatcher.processor.data.New
import javax.tools.JavaFileObject

public class NewTest {

//    Rule public var expectedException: ExpectedException = ExpectedException.none()
    public val DEFAULT_CLASS: String = "MyActivity"

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
                forSourceLines(DEFAULT_CLASS, New.ACTUAL),
                forSourceLines(DEFAULT_CLASS + "PermissionsDispatcher", New.EXPECT)
        )
    }

}