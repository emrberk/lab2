public class AssemblyLine implements Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                CarFactory.semaphore.acquire(3);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Actions.startAssemblyLine();
            Actions.assemble();
            Actions.stopAssemblyLine();

            CarFactory.lock.lock();
            CarFactory.assembled.signalAll();
            CarFactory.lock.unlock();
        }
    }
}
