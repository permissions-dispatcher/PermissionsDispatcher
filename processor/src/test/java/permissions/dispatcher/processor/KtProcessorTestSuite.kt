package permissions.dispatcher.processor

import org.hamcrest.CoreMatchers.containsString
import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.cli.common.ExitCode
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import permissions.dispatcher.processor.kotlin.KotlinCompilerCall
import java.io.File
import javax.annotation.processing.Processor

class KtProcessorTestSuite {
    @Rule
    @JvmField
    val temporaryFolder = TemporaryFolder()

    @Test
    fun noPermissionActivity() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Activity
        import permissions.dispatcher.RuntimePermissions

        @RuntimePermissions
        class MyActivity: Activity()
        """.trimMargin()
        call.addKt(source = source)
        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertThat(result.systemErr, containsString("Annotated class 'MyActivity' doesn't have any method annotated with '@NeedsPermission'"))
    }

    @Test
    fun permissionWithNonVoidReturnType() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Activity
        import permissions.dispatcher.RuntimePermissions
        import permissions.dispatcher.NeedsPermission

        @RuntimePermissions
        class MyActivity: Activity() {
            @NeedsPermission(Manifest.permission.CAMERA)
            fun showCamera(): Int {
                return 0
            }
        }
        """.trimMargin()
        call.addKt(source = source)
        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertThat(result.systemErr, containsString("Method 'showCamera()' must specify return type 'void', not 'int'"))
    }

    @Test
    fun rationaleWithNonVoidReturnType() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Activity
        import permissions.dispatcher.RuntimePermissions
        import permissions.dispatcher.NeedsPermission
        import permissions.dispatcher.OnShowRationale

        @RuntimePermissions
        class MyActivity: Activity() {
            @NeedsPermission(Manifest.permission.CAMERA)
            fun showCamera() {
            }
            @OnShowRationale(Manifest.permission.CAMERA)
            fun cameraRationale(): Int {
                return 0
            }
        }
        """.trimMargin()
        call.addKt(source = source)
        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertThat(result.systemErr, containsString("Method 'cameraRationale()' must specify return type 'void', not 'int'"))
    }

    @Test
    fun deniedWithNonVoidReturnType() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Activity
        import permissions.dispatcher.RuntimePermissions
        import permissions.dispatcher.NeedsPermission
        import permissions.dispatcher.OnPermissionDenied

        @RuntimePermissions
        class MyActivity: Activity() {
            @NeedsPermission(Manifest.permission.CAMERA)
            fun showCamera() {
            }
            @OnPermissionDenied(Manifest.permission.CAMERA)
            fun cameraDenied(): Int {
                return 0
            }
        }
        """.trimMargin()
        call.addKt(source = source)
        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertThat(result.systemErr, containsString("Method 'cameraDenied()' must specify return type 'void', not 'int'"))
    }

    @Test
    fun neverAskWithNonVoidReturnType() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Activity
        import permissions.dispatcher.RuntimePermissions
        import permissions.dispatcher.NeedsPermission
        import permissions.dispatcher.OnNeverAskAgain

        @RuntimePermissions
        class MyActivity: Activity() {
            @NeedsPermission(Manifest.permission.CAMERA)
            fun showCamera() {
            }
            @OnNeverAskAgain(Manifest.permission.CAMERA)
            fun cameraNeverAskAgain(): Int {
                return 0
            }
        }
        """.trimMargin()
        call.addKt(source = source)
        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertThat(result.systemErr, containsString("Method 'cameraNeverAskAgain()' must specify return type 'void', not 'int'"))
    }

    @Test
    fun rationaleWithWrongParameters() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Activity
        import permissions.dispatcher.RuntimePermissions
        import permissions.dispatcher.NeedsPermission
        import permissions.dispatcher.OnShowRationale

        @RuntimePermissions
        class MyActivity: Activity() {
            @NeedsPermission(Manifest.permission.CAMERA)
            fun showCamera() {
            }
            @OnShowRationale(Manifest.permission.CAMERA)
            fun cameraRationale(value: Int) {
            }
        }
        """.trimMargin()
        call.addKt(source = source)
        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertThat(result.systemErr, containsString("Method 'cameraRationale()' must has less than or equal to 1 size parameter and type of it is supposed to be 'PermissionRequest'"))
    }

    @Test
    fun rationaleWithOneMoreParameters() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Activity
        import permissions.dispatcher.RuntimePermissions
        import permissions.dispatcher.NeedsPermission
        import permissions.dispatcher.OnShowRationale
        import permissions.dispatcher.PermissionRequest

        @RuntimePermissions
        class MyActivity: Activity() {
            @NeedsPermission(Manifest.permission.CAMERA)
            fun showCamera() {
            }
            @OnShowRationale(Manifest.permission.CAMERA)
            fun cameraRationale(request: PermissionRequest, request2: PermissionRequest) {
            }
        }
        """.trimMargin()
        call.addKt(source = source)
        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertThat(result.systemErr, containsString("Method 'cameraRationale()' must has less than or equal to 1 size parameter and type of it is supposed to be 'PermissionRequest'"))
    }

    @Test
    fun deniedWithParameters() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Activity
        import permissions.dispatcher.RuntimePermissions
        import permissions.dispatcher.NeedsPermission
        import permissions.dispatcher.OnPermissionDenied

        @RuntimePermissions
        class MyActivity: Activity() {
            @NeedsPermission(Manifest.permission.CAMERA)
            fun showCamera() {
            }
            @OnPermissionDenied(Manifest.permission.CAMERA)
            fun cameraDenied(value: Int) {
            }
        }
        """.trimMargin()
        call.addKt(source = source)
        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertThat(result.systemErr, containsString("Method 'cameraDenied()' must not have any parameters"))
    }

    @Test
    fun neverAskWithParameters() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Activity
        import permissions.dispatcher.RuntimePermissions
        import permissions.dispatcher.NeedsPermission
        import permissions.dispatcher.OnNeverAskAgain

        @RuntimePermissions
        class MyActivity: Activity() {
            @NeedsPermission(Manifest.permission.CAMERA)
            fun showCamera() {
            }
            @OnNeverAskAgain(Manifest.permission.CAMERA)
            fun cameraNeverAskAgain(value: Int) {
            }
        }
        """.trimMargin()
        call.addKt(source = source)
        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertThat(result.systemErr, containsString("Method 'cameraNeverAskAgain()' must not have any parameters"))
    }

    @Test
    fun privatePermission() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Activity
        import permissions.dispatcher.RuntimePermissions
        import permissions.dispatcher.NeedsPermission

        @RuntimePermissions
        class MyActivity: Activity() {
            @NeedsPermission(Manifest.permission.CAMERA)
            private fun showCamera() {
            }
        }
        """.trimMargin()
        call.addKt(source = source)
        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertThat(result.systemErr, containsString("Method 'showCamera()' annotated with '@NeedsPermission' must not be private"))
    }

    @Test
    fun privateRationale() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Activity
        import permissions.dispatcher.RuntimePermissions
        import permissions.dispatcher.NeedsPermission
        import permissions.dispatcher.OnShowRationale

        @RuntimePermissions
        class MyActivity: Activity() {
            @NeedsPermission(Manifest.permission.CAMERA)
            fun showCamera() {
            }
            @OnShowRationale(Manifest.permission.CAMERA)
            private fun cameraRationale() {
            }
        }
        """.trimMargin()
        call.addKt(source = source)
        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertThat(result.systemErr, containsString("Method 'cameraRationale()' annotated with '@OnShowRationale' must not be private"))
    }

    @Test
    fun privateDenied() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Activity
        import permissions.dispatcher.RuntimePermissions
        import permissions.dispatcher.NeedsPermission
        import permissions.dispatcher.OnPermissionDenied

        @RuntimePermissions
        class MyActivity: Activity() {
            @NeedsPermission(Manifest.permission.CAMERA)
            fun showCamera() {
            }
            @OnPermissionDenied(Manifest.permission.CAMERA)
            private fun cameraDenied(value: Int) {
            }
        }
        """.trimMargin()
        call.addKt(source = source)
        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertThat(result.systemErr, containsString("Method 'cameraDenied()' annotated with '@OnPermissionDenied' must not be private"))
    }

    @Test
    fun privateNeverAskAgain() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Activity
        import permissions.dispatcher.RuntimePermissions
        import permissions.dispatcher.NeedsPermission
        import permissions.dispatcher.OnNeverAskAgain

        @RuntimePermissions
        class MyActivity: Activity() {
            @NeedsPermission(Manifest.permission.CAMERA)
            fun showCamera() {
            }
            @OnNeverAskAgain(Manifest.permission.CAMERA)
            private fun cameraNeverAskAgain() {
            }
        }
        """.trimMargin()
        call.addKt(source = source)
        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertThat(result.systemErr, containsString("Method 'cameraNeverAskAgain()' annotated with '@OnNeverAskAgain' must not be private"))
    }

    @Test
    fun wrongAnnotatedClass() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Service
        import android.content.Intent
        import android.os.IBinder
        import permissions.dispatcher.RuntimePermissions
        import permissions.dispatcher.NeedsPermission

        @RuntimePermissions
        class MyService: Service() {
            override fun onBind(intent: Intent?): IBinder? = null
            @NeedsPermission(Manifest.permission.CAMERA)
            fun showCamera() {
            }
        }
        """.trimMargin()
        call.addKt(source = source)
        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertThat(result.systemErr, containsString("Class 'MyService' can't be annotated with '@RuntimePermissions'"))
    }

    @Test
    fun duplicatedRationale() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Activity
        import permissions.dispatcher.RuntimePermissions
        import permissions.dispatcher.NeedsPermission
        import permissions.dispatcher.OnShowRationale

        @RuntimePermissions
        class MyActivity: Activity() {
            @NeedsPermission(Manifest.permission.CAMERA)
            fun showCamera() {
            }
            @OnShowRationale(Manifest.permission.CAMERA)
            fun cameraRationale() {
            }
            @OnShowRationale(Manifest.permission.CAMERA)
            fun cameraRationale2() {
            }
        }
        """.trimMargin()
        call.addKt(source = source)
        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertThat(result.systemErr, containsString("[android.permission.CAMERA] is duplicated in 'cameraRationale2()' annotated with '@OnShowRationale'"))
    }

    @Test
    fun duplicatedDenied() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Activity
        import permissions.dispatcher.RuntimePermissions
        import permissions.dispatcher.NeedsPermission
        import permissions.dispatcher.OnPermissionDenied

        @RuntimePermissions
        class MyActivity: Activity() {
            @NeedsPermission(Manifest.permission.CAMERA)
            fun showCamera() {
            }
            @OnPermissionDenied(Manifest.permission.CAMERA)
            fun cameraDenied(value: Int) {
            }
            @OnPermissionDenied(Manifest.permission.CAMERA)
            fun cameraDenied2(value: Int) {
            }
        }
        """.trimMargin()
        call.addKt(source = source)
        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertThat(result.systemErr, containsString("[android.permission.CAMERA] is duplicated in 'cameraDenied2()' annotated with '@OnPermissionDenied'"))
    }

    @Test
    fun duplicatedNeverAsk() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Activity
        import permissions.dispatcher.RuntimePermissions
        import permissions.dispatcher.NeedsPermission
        import permissions.dispatcher.OnNeverAskAgain

        @RuntimePermissions
        class MyActivity: Activity() {
            @NeedsPermission(Manifest.permission.CAMERA)
            fun showCamera() {
            }
            @OnNeverAskAgain(Manifest.permission.CAMERA)
            private fun cameraNeverAskAgain() {
            }
            @OnNeverAskAgain(Manifest.permission.CAMERA)
            private fun cameraNeverAskAgain2() {
            }
        }
        """.trimMargin()
        call.addKt(source = source)
        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertThat(result.systemErr, containsString("[android.permission.CAMERA] is duplicated in 'cameraNeverAskAgain2()' annotated with '@OnNeverAskAgain'"))
    }

    @Test
    fun needsPermissionMethodOverload() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Activity
        import permissions.dispatcher.RuntimePermissions
        import permissions.dispatcher.NeedsPermission
        import permissions.dispatcher.OnNeverAskAgain

        @RuntimePermissions
        class MyActivity: Activity() {
            @NeedsPermission(Manifest.permission.CAMERA)
            fun showCamera() {
            }
            @NeedsPermission(Manifest.permission.CAMERA)
            fun showCamera(value: Int) {
            }
        }
        """.trimMargin()
        call.addKt(source = source)
        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertThat(result.systemErr, containsString("'showCamera()' has duplicated '@NeedsPermission' method. The method annotated with '@NeedsPermission' must has the unique name."))
    }

    @Test
    fun systemAlertWindowOnNerverAskAgain() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Activity
        import permissions.dispatcher.RuntimePermissions
        import permissions.dispatcher.NeedsPermission
        import permissions.dispatcher.OnNeverAskAgain

        @RuntimePermissions
        class MyActivity: Activity() {
            @NeedsPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
            fun showCamera() {
            }
            @OnNeverAskAgain(Manifest.permission.SYSTEM_ALERT_WINDOW)
            fun cameraNeverAskAgain() {
            }
        }
        """.trimMargin()
        call.addKt(source = source)
        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertThat(result.systemErr, containsString("'@NeverAskAgain' annotated method never being called with 'WRITE_SETTINGS' or 'SYSTEM_ALERT_WINDOW' permission."))
    }

    @Test
    fun writeSettingsWithOnNeverAskAgain() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Activity
        import permissions.dispatcher.RuntimePermissions
        import permissions.dispatcher.NeedsPermission
        import permissions.dispatcher.OnNeverAskAgain

        @RuntimePermissions
        class MyActivity: Activity() {
            @NeedsPermission(Manifest.permission.WRITE_SETTINGS)
            fun showCamera() {
            }
            @OnNeverAskAgain(Manifest.permission.WRITE_SETTINGS)
            fun cameraNeverAskAgain() {
            }
        }
        """.trimMargin()
        call.addKt(source = source)
        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertThat(result.systemErr, containsString("'@NeverAskAgain' annotated method never being called with 'WRITE_SETTINGS' or 'SYSTEM_ALERT_WINDOW' permission."))
    }

    @Test
    fun compileValidCodeSuccessful() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
import android.Manifest
import android.app.Activity
import permissions.dispatcher.RuntimePermissions
import permissions.dispatcher.NeedsPermission
@RuntimePermissions
class MyActivity: Activity() {
    @NeedsPermission(Manifest.permission.CAMERA)
    fun showCamera() {
    }
}
        """.trimMargin()
        call.addKt(source = source)
        val result = call.execute()
        assertEquals(ExitCode.OK, result.exitCode)
        val kaptSourceDir = File(temporaryFolder.root, "generated/source/kapt")
        val generatedFile = File(kaptSourceDir, "MyActivityPermissionsDispatcher.kt")
        assertTrue(generatedFile.exists())
        @Language("kotlin") val expected = """
// This file was generated by PermissionsDispatcher. Do not modify!
@file:JvmName("MyActivityPermissionsDispatcher")

import androidx.core.app.ActivityCompat
import kotlin.Array
import kotlin.Int
import kotlin.IntArray
import kotlin.String
import permissions.dispatcher.PermissionUtils

private val REQUEST_SHOWCAMERA: Int = 0

private val PERMISSION_SHOWCAMERA: Array<String> = arrayOf("android.permission.CAMERA")

fun MyActivity.showCameraWithPermissionCheck() {
    if (PermissionUtils.hasSelfPermissions(this, *PERMISSION_SHOWCAMERA)) {
        showCamera()
    } else {
        ActivityCompat.requestPermissions(this, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA)
    }
}

fun MyActivity.onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
    when (requestCode) {
        REQUEST_SHOWCAMERA ->
         {
            if (PermissionUtils.verifyPermissions(*grantResults)) {
                showCamera()
            }
        }
    }
}

        """.trimMargin()
        assertEquals(expected, generatedFile.readText())
    }
}
