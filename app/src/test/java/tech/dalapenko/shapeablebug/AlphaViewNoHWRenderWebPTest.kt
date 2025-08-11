package tech.dalapenko.shapeablebug

import android.R
import android.app.Activity
import android.widget.LinearLayout
import com.dropbox.differ.SimpleImageComparator
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.LosslessWebPImageIoFormat
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.After
import org.junit.Before
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
class AlphaViewNoHWRenderWebPTest {

    init {
        // works with false or if add EmptyActivity to AndroidManifest.xml
        System.setProperty(PIXEL_COPY_RENDER_MODE, "software")
    }

    @Before
    fun setUp() {
        System.setProperty(WEBP_EXTENSION_PROPERTY, "webp")
    }

    @After
    fun tearDown() {
        System.clearProperty(WEBP_EXTENSION_PROPERTY)
    }

    @ExperimentalRoborazziApi
    @get:Rule
    val roborazziRule = RoborazziRule(
        options = RoborazziRule.Options(
            outputDirectoryPath = COLT_COMPONENTS_REFERENCE_IMAGES_PATH,
            outputFileProvider = (FileProvider::get),
            roborazziOptions = RoborazziOptions(
                compareOptions = RoborazziOptions.CompareOptions(
                    imageComparator = SimpleImageComparator(
                        maxDistance = 0.007F,
                        hShift = 2, // without this shift, the test fails on different OS
                        vShift = 2 // without this shift, the test fails on different OS
                    ),
                    changeThreshold = 0.01f
                ),
                recordOptions = RoborazziOptions.RecordOptions(
                    imageIoFormat = LosslessWebPImageIoFormat(),
                ),
            )
        )
    )

    @Test
    fun macTest() {
        testTemplate()
    }

    @Test
    fun ubuntuTest() {
        testTemplate()
    }

    @Test
    fun windowsTest() {
        testTemplate()
    }

    private fun testTemplate() {
        val activityController = Robolectric.buildActivity(Activity::class.java).apply {
            get().setTheme(R.style.Theme_Translucent)
        }
        val activity = activityController.setup().get()

        val viewGroup = LinearLayout(activity)

        val view = CoverWithTagView(activity)

        viewGroup.addView(view)

        activity.setContentView(viewGroup)
        activityController.start().resume().visible()

        view.captureRoboImage()
    }
}

private const val COLT_COMPONENTS_REFERENCE_IMAGES_PATH = "src/test/assets/screenshot_tests_refs"
private const val PIXEL_COPY_RENDER_MODE = "robolectric.pixelCopyRenderMode"
private const val WEBP_EXTENSION_PROPERTY = "roborazzi.record.image.extension"
