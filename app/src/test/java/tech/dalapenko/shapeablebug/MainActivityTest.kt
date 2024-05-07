package tech.dalapenko.shapeablebug

import android.app.Activity
import android.view.WindowManager
import android.widget.LinearLayout
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(
    qualifiers = RobolectricDeviceQualifiers.Pixel5,
    sdk = [34]
)
class MainActivityTest {

    init {
        // works with false or if add EmptyActivity to AndroidManifest.xml
        System.setProperty(USE_HARDWARE_RENDERER_NATIVE_ENV, "true")
    }

    @get:Rule
    val roborazziRule = RoborazziRule(
        options = RoborazziRule.Options(
            outputDirectoryPath = COLT_COMPONENTS_REFERENCE_IMAGES_PATH,
            outputFileProvider = (FileProvider::get)
        )
    )

    @Test
    fun shapeableImageTest() {
        val activityController = Robolectric.buildActivity(Activity::class.java).apply {
            get().window.setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
            )
            get().setTheme(android.R.style.Theme_Translucent)
        }
        val activity = activityController.setup().get()

        val viewGroup = LinearLayout(activity)
        val view = CoverView(activity).apply {
            setImage(R.drawable.cover)
        }

        viewGroup.addView(view)

        activity.setContentView(viewGroup)
        activityController.start().resume().visible()

        view.captureRoboImage()
    }
}

private const val COLT_COMPONENTS_REFERENCE_IMAGES_PATH = "src/test/assets/colt_components_refs"
private const val USE_HARDWARE_RENDERER_NATIVE_ENV = "robolectric.screenshot.hwrdr.native"
