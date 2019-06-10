package permissions.dispatcher.processor

import kompile.testing.kotlinc
import org.intellij.lang.annotations.Language
import org.junit.Test

class KtProcessorTestSuite {
    @Test
    fun noPermissionActivity() {
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Activity
        import permissions.dispatcher.RuntimePermissions

        @RuntimePermissions
        class MyActivity: Activity()
        """.trimMargin()

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .failed()
                .withErrorContaining("Annotated class 'MyActivity' doesn't have any method annotated with '@NeedsPermission'")
    }

    @Test
    fun permissionWithNonVoidReturnType() {
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

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .failed()
                .withErrorContaining("Method 'showCamera()' must specify return type 'void', not 'int'")
    }

    @Test
    fun rationaleWithNonVoidReturnType() {
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

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .failed()
                .withErrorContaining("Method 'cameraRationale()' must specify return type 'void', not 'int'")
    }

    @Test
    fun deniedWithNonVoidReturnType() {
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

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .failed()
                .withErrorContaining("Method 'cameraDenied()' must specify return type 'void', not 'int'")
    }

    @Test
    fun neverAskWithNonVoidReturnType() {
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

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .failed()
                .withErrorContaining("Method 'cameraNeverAskAgain()' must specify return type 'void', not 'int'")
    }

    @Test
    fun rationaleWithWrongParameters() {
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

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .failed()
                .withErrorContaining("Method 'cameraRationale()' must has less than or equal to 1 size parameter and type of it is supposed to be 'PermissionRequest'")
    }

    @Test
    fun rationaleWithOneMoreParameters() {
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

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .failed()
                .withErrorContaining("Method 'cameraRationale()' must has less than or equal to 1 size parameter and type of it is supposed to be 'PermissionRequest'")
    }

    @Test
    fun deniedWithParameters() {
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

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .failed()
                .withErrorContaining("Method 'cameraDenied()' must not have any parameters")
    }

    @Test
    fun neverAskWithParameters() {
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

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .failed()
                .withErrorContaining("Method 'cameraNeverAskAgain()' must not have any parameters")
    }

    @Test
    fun privatePermission() {
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

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .failed()
                .withErrorContaining("Method 'showCamera()' annotated with '@NeedsPermission' must not be private")
    }

    @Test
    fun privateRationale() {
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

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .failed()
                .withErrorContaining("Method 'cameraRationale()' annotated with '@OnShowRationale' must not be private")
    }

    @Test
    fun privateDenied() {
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

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .failed()
                .withErrorContaining("Method 'cameraDenied()' annotated with '@OnPermissionDenied' must not be private")
    }

    @Test
    fun privateNeverAskAgain() {
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

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .failed()
                .withErrorContaining("Method 'cameraNeverAskAgain()' annotated with '@OnNeverAskAgain' must not be private")
    }

    @Test
    fun wrongAnnotatedClass() {
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

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .failed()
                .withErrorContaining("Class 'MyService' can't be annotated with '@RuntimePermissions'")
    }

    @Test
    fun duplicatedRationale() {
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

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .failed()
                .withErrorContaining("[android.permission.CAMERA] is duplicated in 'cameraRationale2()' annotated with '@OnShowRationale'")
    }

    @Test
    fun duplicatedDenied() {
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

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .failed()
                .withErrorContaining("[android.permission.CAMERA] is duplicated in 'cameraDenied2()' annotated with '@OnPermissionDenied'")
    }

    @Test
    fun duplicatedNeverAsk() {
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

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .failed()
                .withErrorContaining("[android.permission.CAMERA] is duplicated in 'cameraNeverAskAgain2()' annotated with '@OnNeverAskAgain'")
    }

    @Test
    fun needsPermissionMethodOverload() {
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

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .failed()
                .withErrorContaining("'showCamera()' has duplicated '@NeedsPermission' method. The method annotated with '@NeedsPermission' must has the unique name.")
    }

    @Test
    fun systemAlertWindowOnNerverAskAgain() {
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

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .failed()
                .withErrorContaining("'@NeverAskAgain' annotated method never being called with 'WRITE_SETTINGS' or 'SYSTEM_ALERT_WINDOW' permission.")
    }

    @Test
    fun writeSettingsWithOnNeverAskAgain() {
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

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .failed()
                .withErrorContaining("'@NeverAskAgain' annotated method never being called with 'WRITE_SETTINGS' or 'SYSTEM_ALERT_WINDOW' permission.")
    }

    @Test
    fun compileValidCodeSuccessful() {
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

        @Language("kotlin") val expected = """
        // This file was generated by PermissionsDispatcher. Do not modify!
        @file:JvmName("MyActivityPermissionsDispatcher")

        import androidx.core.app.ActivityCompat
        import kotlin.Array
        import kotlin.Int
        import kotlin.IntArray
        import kotlin.String
        import permissions.dispatcher.PermissionUtils

        private const val REQUEST_SHOWCAMERA: Int = 0

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

        """.trimIndent()

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .succeededWithoutWarnings()
                .generatedFile("MyActivityPermissionsDispatcher.kt")
                .hasSourceEquivalentTo(expected)
    }

    @Test
    fun validInternalFragment() {
        @Language("kotlin") val source = """
        import android.Manifest
        import androidx.fragment.app.Fragment
        import permissions.dispatcher.RuntimePermissions
        import permissions.dispatcher.NeedsPermission

        @RuntimePermissions
        internal class MyFragment : Fragment {
            @NeedsPermission(Manifest.permission.CAMERA)
            fun showCamera() {
            }

            @NeedsPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
            fun systemAlertWindow() {
            }
        }
        """.trimIndent()

        @Language("kotlin") val expected = """
        // This file was generated by PermissionsDispatcher. Do not modify!
        @file:JvmName("MyFragmentPermissionsDispatcher")

        import android.content.Intent
        import android.net.Uri
        import android.provider.Settings
        import kotlin.Array
        import kotlin.Int
        import kotlin.IntArray
        import kotlin.String
        import permissions.dispatcher.PermissionUtils

        private const val REQUEST_SHOWCAMERA: Int = 0

        private val PERMISSION_SHOWCAMERA: Array<String> = arrayOf("android.permission.CAMERA")

        private const val REQUEST_SYSTEMALERTWINDOW: Int = 1

        private val PERMISSION_SYSTEMALERTWINDOW: Array<String> =
            arrayOf("android.permission.SYSTEM_ALERT_WINDOW")

        internal fun MyFragment.showCameraWithPermissionCheck() {
          if (PermissionUtils.hasSelfPermissions(this.requireActivity(), *PERMISSION_SHOWCAMERA)) {
            showCamera()
          } else {
            this.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA)
          }
        }

        internal fun MyFragment.systemAlertWindowWithPermissionCheck() {
          if (PermissionUtils.hasSelfPermissions(this.requireActivity(), *PERMISSION_SYSTEMALERTWINDOW) ||
              Settings.canDrawOverlays(this.requireActivity())) {
            systemAlertWindow()
          } else {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" +
                this.requireActivity().getPackageName()))
            this.startActivityForResult(intent, REQUEST_SYSTEMALERTWINDOW)
          }
        }

        internal fun MyFragment.onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
          when (requestCode) {
            REQUEST_SHOWCAMERA ->
             {
              if (PermissionUtils.verifyPermissions(*grantResults)) {
                showCamera()
              }
            }
          }
        }

        internal fun MyFragment.onActivityResult(requestCode: Int) {
          when (requestCode) {
            REQUEST_SYSTEMALERTWINDOW -> {
              if (PermissionUtils.hasSelfPermissions(this.requireActivity(), *PERMISSION_SYSTEMALERTWINDOW)
                  || Settings.canDrawOverlays(this.requireActivity())) {
                systemAlertWindow()
              }
            }
          }
        }

        """.trimIndent()

        kotlinc()
                .withProcessors(PermissionsProcessor())
                .addKotlin("sources.kt", source)
                .compile()
                .succeededWithoutWarnings()
                .generatedFile("MyFragmentPermissionsDispatcher.kt")
                .hasSourceEquivalentTo(expected)
    }
}
