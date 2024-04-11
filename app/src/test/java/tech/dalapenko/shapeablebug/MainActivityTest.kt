package tech.dalapenko.shapeablebug

import android.content.Context
import android.content.pm.ActivityInfo
import android.widget.LinearLayout
import androidx.test.core.app.ApplicationProvider
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel5)
class MainActivityTest {

    private val applicationContext: Context by lazy {
        ApplicationProvider.getApplicationContext()
    }

    init {
        // works with false or if add EmptyActivity to AndroidManifest.xml
        System.setProperty(USE_HARDWARE_RENDERER_NATIVE_ENV, "true")
    }

    @Test
    fun shapeableImageTest() {
        val activityInfo = ActivityInfo().apply {
            name = EmptyActivity::class.java.name
            packageName = applicationContext.packageName
        }

        shadowOf(applicationContext.packageManager)
            .addOrUpdateActivity(activityInfo)

        val activityController = Robolectric.buildActivity(EmptyActivity::class.java)
        val activity = activityController.create().get()

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

private const val USE_HARDWARE_RENDERER_NATIVE_ENV = "robolectric.screenshot.hwrdr.native"
