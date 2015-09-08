/**
 *
 */
package com.brc.whitemercurydg;

import generator.engine.ProgressUpdateObserver;

/**
 * @author tim
 *
 */
public class WhiteMercuryProgressObserver implements ProgressUpdateObserver {

    int maxProgress;
    int currProgress;
    Boolean isInterrupted = false;

    /* (non-Javadoc)
     * @see generator.engine.ProgressUpdateObserver#dataGenStarted()
     */
    @Override
    public void dataGenStarted() {
        System.out.println("Data generation started...");
    }

    /* (non-Javadoc)
     * @see generator.engine.ProgressUpdateObserver#dataGenMaxProgressValue(int)
     */
    @Override
    public void dataGenMaxProgressValue(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    /* (non-Javadoc)
     * @see generator.engine.ProgressUpdateObserver#dataGenProgressContinue(java.lang.String, int)
     */
    @Override
    public boolean dataGenProgressContinue(String msg, int progress) {
        System.out.println(msg);
        this.currProgress = progress;

        return this.isInterrupted;
    }

    /* (non-Javadoc)
     * @see generator.engine.ProgressUpdateObserver#dataGenEnd()
     */
    @Override
    public void dataGenEnd() {
        if (this.isInterrupted)
            System.out.println("Data generation process was interrupted by user.");
        else
            System.out.println("Data generation done.");
    }

    /* (non-Javadoc)
     * @see generator.engine.ProgressUpdateObserver#datageGenError(java.lang.String)
     */
    @Override
    public void datageGenError(String msg) {
        System.err.println(msg);
    }

}
