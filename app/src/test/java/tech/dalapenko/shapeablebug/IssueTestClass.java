package tech.dalapenko.shapeablebug;

import static org.hamcrest.MatcherAssert.assertThat;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.PixelCopy;
import android.view.View;
import android.view.Window;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.GraphicsMode;
import org.robolectric.shadows.ShadowPackageManager;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class CustomActivity extends Activity {
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = new View(this);
        view.setBackgroundColor(0xffff0000);
        view.layout(0, 0, 100, 100);
        setContentView(view);
    }
};

// From issue https://github.com/robolectric/robolectric/issues/8081#issuecomment-1968280166
@RunWith(RobolectricTestRunner.class)
public class IssueTestClass {

    @GraphicsMode(GraphicsMode.Mode.NATIVE)
    @Test
    public void hasPixelWhenUsingCustomAddingActivityHardwareRendering() {
        // **If we use false here, the test will pass.**
        System.setProperty("robolectric.screenshot.hwrdr.native", "true");
        Application application = ApplicationProvider.getApplicationContext();
        ((ShadowPackageManager) Shadows.shadowOf(
                application.getPackageManager())).addActivityIfNotPresent(
                new Intent(application, CustomActivity.class).resolveActivity(
                        application.getPackageManager()));

        try (ActivityScenario<CustomActivity> activityScenario = ActivityScenario.launch(
                CustomActivity.class)) {
            activityScenario.onActivity(activity -> {
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                Espresso.onIdle();
                Bitmap viewDrawBitmap = Bitmap.createBitmap(decorView.getWidth(), decorView.getHeight(),
                        Bitmap.Config.ARGB_8888);
                decorView.draw(new Canvas(viewDrawBitmap));

                Bitmap pixelCopyBitmap = Bitmap.createBitmap(decorView.getWidth(), decorView.getHeight(),
                        Bitmap.Config.ARGB_8888);
                CountDownLatch latch = new CountDownLatch(1);
                PixelCopy.request(window, pixelCopyBitmap, copyResult -> latch.countDown(),
                        new Handler(Looper.getMainLooper()));
                try {
                    latch.await(1, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                int pixelCopyBitmapPixel = pixelCopyBitmap.getPixel(0, 0);

                int viewDrawBitmapPixel = viewDrawBitmap.getPixel(0, 0);
                System.out.println("viewDrawBitmapPixel: " + Integer.toHexString(viewDrawBitmapPixel));
                System.out.println("pixelCopyBitmapPixel: " + Integer.toHexString(pixelCopyBitmapPixel));
                assert pixelCopyBitmapPixel == viewDrawBitmapPixel;
                pixelCopyBitmap.recycle();
            });
        }
    }
}
