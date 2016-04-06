package org.blueskywalker.java.utils.thread;

/**
 * Created by kkim on 1/2/16.
 */
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author kkim
 */
public class StreamThreadService<I, O> extends ThreadService {

     ConcurrentLinkedQueue<I> inputQueue;
     ConcurrentLinkedQueue<O> outputQueue;

    public static abstract class StreamThreadRunner<I, O> implements Runnable {

        protected I input;
        protected O output;

        void setInput(I msg) {
            input = msg;
        }

        O getOutput() {
            return output;
        }

        public I getInput() {
            return input;
        }

        public void setOutput(O msg) {
            output=msg;
        }
    }

    public StreamThreadService(Runnable[] runners) {
        super(runners);
    }

    @Override
    protected void setPriority(int priority) {
        inputQueue = new ConcurrentLinkedQueue<>();
        outputQueue = new ConcurrentLinkedQueue<>();

        for (int i = 0; i < service.length; i++) {
            service[i] = new StreamThread(this,runners[i]);
            service[i].setPriority(priority);
        }
    }


    public ConcurrentLinkedQueue<I> getInputQueue() {
        return inputQueue;
    }

    public ConcurrentLinkedQueue<O> getOutputQueue() {
        return outputQueue;
    }

    public void send(I msg) {
        inputQueue.add(msg);
    }

    public O receive() {
        return outputQueue.poll();
    }

    static class StreamThread<I, O> extends ServiceThread {

        final ConcurrentLinkedQueue<I> inputQueue;
        final ConcurrentLinkedQueue<O> outputQueue;

        public StreamThread(StreamThreadService service, Runnable runner) {
            super(runner, null);
            this.inputQueue = service.inputQueue;
            this.outputQueue = service.outputQueue;
        }

        @Override
        public void run()  {
            StreamThreadRunner<I, O> worker = (StreamThreadRunner<I, O>) runner;
            while (!done.get()) {
                I input = inputQueue.poll();
                if (input == null) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        System.exit(-1);
                    }
                    continue;
                }
                worker.setInput(input);
                worker.run();

                O output = worker.getOutput();
                if(output!=null)
                    outputQueue.add(output);
            }
        }
    }
}
