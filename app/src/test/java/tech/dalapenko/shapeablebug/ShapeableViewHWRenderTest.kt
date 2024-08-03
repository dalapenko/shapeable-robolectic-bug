package tech.dalapenko.shapeablebug

import android.app.Activity
import android.widget.LinearLayout
import com.dropbox.differ.SimpleImageComparator
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.RoborazziOptions
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
class ShapeableViewHWRenderTest {

    init {
        // works with false or if add EmptyActivity to AndroidManifest.xml
        System.setProperty(PIXEL_COPY_RENDER_MODE, "hardware")
    }

    @ExperimentalRoborazziApi
    @get:Rule
    val roborazziRule = RoborazziRule(
        options = RoborazziRule.Options(
            outputDirectoryPath = COLT_COMPONENTS_REFERENCE_IMAGES_PATH,
            outputFileProvider = (FileProvider::get),
            roborazziOptions = RoborazziOptions(
                compareOptions = RoborazziOptions.CompareOptions(
                    imageComparator = SimpleImageComparator(maxDistance = 0.007F)
                )
            )
        )
    )

    @Test
    fun shapeableImageMacTest() {
        shapeableTestTemplate()
    }

    @Test
    fun shapeableImageUbuntuTest() {
        shapeableTestTemplate()
    }

    @Test
    fun shapeableImageWindowsTest() {
        shapeableTestTemplate()
    }

    private fun shapeableTestTemplate() {
        val activityController = Robolectric.buildActivity(Activity::class.java).apply {
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

private const val COLT_COMPONENTS_REFERENCE_IMAGES_PATH = "src/test/assets/screenshot_tests_refs"
private const val PIXEL_COPY_RENDER_MODE = "robolectric.pixelCopyRenderMode"
