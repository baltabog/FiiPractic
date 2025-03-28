package com.fii.practic.mes.admin;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.jboss.logging.Logger;

@QuarkusMain
public class Main {
    public static void main(String... args) {
        Quarkus.run(MyApp.class, args);
    }

    public static class MyApp implements QuarkusApplication {

        private static final Logger log = Logger.getLogger(MyApp.class.getName());

        @Override
        public int run(String... args) {
            log.info("Server is started");
            Quarkus.waitForExit();
            return 0;
        }
    }
}
