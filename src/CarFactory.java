import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

public class CarFactory {

    public enum ProductType {
        ENGINE("Engine"),
        WHEEL("Wheel"),
        GLASS("Glass");

        final String name;

        ProductType(String name) {
            this.name = name;
        }
    }

    static final Lock lock = new ReentrantLock();
    static final Semaphore semaphore = new Semaphore(0);
    static final Condition assembled = lock.newCondition();

    public static void main(String[] args) {
        Stream.of(
                new ProductionLine(ProductType.ENGINE, 1),
                new ProductionLine(ProductType.WHEEL, 4),
                new ProductionLine(ProductType.GLASS, 6),
                new AssemblyLine()
        ).map(Thread::new).forEach(Thread::start);
    }
}