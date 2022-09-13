package net.montoyo.mcef.client;

import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.utilities.IProgressListener;
import net.montoyo.mcef.utilities.Log;
import net.montoyo.mcef.utilities.Util;

public class UpdateFrameMacOS implements IProgressListener {

    public UpdateFrameMacOS() {

    }

    @Override
    public void onProgressed(double d) {
        int val = (int) Util.clamp(d, 0.d, 100.d);
        MCEF.debug(String.valueOf(val));
    }

    @Override
    public void onTaskChanged(String name) {
        Log.info("Task changed to \"%s\"", name);
    }

    @Override
    public void onProgressEnd() {
    }

}
