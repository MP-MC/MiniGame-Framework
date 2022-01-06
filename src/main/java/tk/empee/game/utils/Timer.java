package tk.empee.game.utils;

public abstract class Timer implements Runnable {

    private final int totalCycles;
    private int cycles = 0;

    public Timer(int totalCycles) {
        this.totalCycles = totalCycles;
    }

    @Override
    public final void run() {
        cycles++;

        if(totalCycles == cycles) {
            onLastCycle();
        } else {
            onCycle(cycles);
        }

    }

    public abstract void onLastCycle();

    /**
     * @param cycles since the timer was started
     */
    public abstract void onCycle(int cycles);

}
