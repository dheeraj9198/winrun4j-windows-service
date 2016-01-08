/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Peter Smith
 *******************************************************************************/
package org.boris.winrun4j.test.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.boris.winrun4j.AbstractService;
import org.boris.winrun4j.Launcher;
import org.boris.winrun4j.Log;
import org.boris.winrun4j.Log.Level;
import org.boris.winrun4j.ServiceException;
import org.boris.winrun4j.test.framework.TestHelper;

public class Service1 extends AbstractService
{
    public static void main(String[] args) throws Exception {
        register();
        // launcher();
        // unregister();
    }

    public int serviceMain(String[] args) throws ServiceException {
        Log.info(String.format("running: %s", Arrays.asList(args)));
        int count = 0;
        while (!shutdown) {
            TestHelper.sleep(1000);
            Log.info("still running...");
            if (count++ == 10)
                System.exit(1);
        }
        return 0;
    }

    static void register() throws Exception {
        TestHelper.run(launcher(), "--WinRun4J:RegisterService");
    }

    static void unregister() throws Exception {
        TestHelper.run(launcher(), "--WinRun4J:UnregisterService");
    }

    static Launcher launcher() throws IOException {
        File temp = new File(System.getProperty("java.io.tmpdir"));
        Launcher l = new Launcher(TestHelper.LAUNCHER).
                arg("-this").
                arg("-that").
                vmVersion("1.6", null, null).
                debug(8787, true, false).
                service(Service1.class, "Service1", "Testing service for w4j").
                log(new File(temp, "Service1.log").toString(), Level.INFO, true, true);
        l.vmarg("-Xcheck:jni");
        TestHelper.testcp(l);
        return l.createAt(new File(temp, "Service1.exe"));
    }
}
