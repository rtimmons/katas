package actor;

import java.util.concurrent.CountDownLatch;

public class StandardLifecycle {
    private final CountDownLatch done = new CountDownLatch(1);

    public void run(Context context) {
        context.eachActor(a -> a.onBeforeTest(context));
        context.eachActor(a -> a.onTest(context));

        try {
            done.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        context.eachActor(a -> a.onAfterTest(context));
    }
}
