public class ProductionLine implements Runnable {
    private final CarFactory.ProductType productType;
    private final int capacity;

    public ProductionLine(CarFactory.ProductType productType, int capacity) {
        this.productType = productType;
        this.capacity = capacity;
    }

    private void onLineStart() {
        switch (productType) {
            case ENGINE:
                Actions.startEngineProductionLine();
                break;
            case WHEEL:
                Actions.startWheelProductionLine();
                break;
            case GLASS:
                Actions.startGlassProductionLine();
                break;
            default:
                break;
        }
    }

    private void onLineStop() {
        switch (productType) {
            case ENGINE:
                Actions.stopEngineProductionLine();
                break;
            case WHEEL:
                Actions.stopWheelProductionLine();
                break;
            case GLASS:
                Actions.stopGlassProductionLine();
                break;
            default:
                break;
        }

        try {
            CarFactory.lock.lock();
            CarFactory.semaphore.release();
            CarFactory.assembled.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            CarFactory.lock.unlock();
        }
    }

    private void onLineProduce() {
        switch (productType) {
            case ENGINE:
                Actions.produceEngine();
                break;
            case WHEEL:
                Actions.produceWheel();
                break;
            case GLASS:
                Actions.produceGlass();
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        while (true) {
            onLineStart();
            for (int i = 0; i < capacity; i++) {
                onLineProduce();
            }
            onLineStop();
        }
    }
}
