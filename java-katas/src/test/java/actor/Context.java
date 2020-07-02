package actor;

import java.util.List;
import java.util.function.Consumer;

class Context {
    private final List<IActor> actors;
    private final StandardLifecycle lifecycle = new StandardLifecycle();

    Context(List<IActor> actors) {
        this.actors = actors;
    }

    void eachActor(Consumer<? super IActor> consumer) {
        actors.forEach(consumer);
    }

    void submit(Message message) {
        this.eachActor(a -> a.onMessage(this, message));
    }

    void run() {
        lifecycle.run(this);
    }


}
