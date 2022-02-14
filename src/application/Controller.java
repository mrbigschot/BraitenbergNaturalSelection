package application;

import fitness.Evaluable;

public class Controller extends Thread {

    private int speed = 10;

    private ControlPanel controls;
    public boolean running, stepping = false;
    private Environment environment;
    private BNSModel model;

    public Controller(Environment env, BNSModel model) {
        this.environment = env;
        this.controls = new ControlPanel(this);
        this.model = model;
    }

    @Override
    public void run() {
        for (;;) {
            if (running || stepping) {
//                if (model.getTime() < Util.getGenTime()) {
                    step();
                    delay();
//                } else {
//                    running = false;
//                    completeGeneration();
//                    running = true;
//                }
                stepping = false;
            }
            delay();
        }
    }

    private void delay() {
        try {
            Thread.sleep(speed);
        } catch (Exception ex) {
        }
    }

    public void toggleRunning() {
        running = !running;
    }

    public void toggleStepping() {
        stepping = !stepping;
    }

    private void step() {
        model.step();
        this.environment.display();
    }

    public void setStep() {
        running = false;
    }

    public BNSModel getModel() {
        return model;
    }

    public boolean getRunning() {
        return running;
    }

    public void reset() {
        running = false;
        model.reset();
        this.environment.display();
    }

    public void completeGeneration() {
        model.completeGeneration();
        this.environment.display();
    }

    public String fitOutput() {
        String returnMe = "Output:\n PREDATORS ";
        for (Evaluable v :model.getPredatorPop()) {
            returnMe += "\npred =" + v.getFitness() + v.toString();
        }
        returnMe += "\n PREY";
        for(Evaluable v : model.getPreyPop()) {
            returnMe += "\nprey =" + v.getFitness() + v.toString();
        }
        return returnMe;
    }

}
