package org.boris.winrun4j.test;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.boris.winrun4j.AbstractService;
import org.boris.winrun4j.EventLog;
import org.boris.winrun4j.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * A basic service.
 */
public class ServiceTest extends AbstractService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTest.class);
    private SocketIOServer server = null;
 /*   public static void main(String[] args) throws ServiceException{
        ServiceTest serviceTest = new ServiceTest();
        serviceTest.serviceMain(args);
    }*/

    public int serviceMain(String[] args) throws ServiceException {
        //This is the root logger provided by log4j
        org.apache.log4j.Logger rootLogger = org.apache.log4j.Logger.getRootLogger();
        rootLogger.setLevel(Level.DEBUG);

//Define log pattern layout
        PatternLayout layout = new PatternLayout("%d{ISO8601} [%t] %-5p %c %x - %m%n");

//Add console appender to root logger
        rootLogger.addAppender(new ConsoleAppender(layout));
        try {
//Define file appender with layout and output log file name
            RollingFileAppender fileAppender = new RollingFileAppender(layout, "C:\\Users\\Aurus\\Desktop\\demoApplication.log");

//Add the appender to root logger
            rootLogger.addAppender(fileAppender);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.error("started");
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092);

        try {
            server = new SocketIOServer(config);
            server.addEventListener("ackevent1", ChatObject.class, new DataListener<ChatObject>() {
                @Override
                public void onData(final SocketIOClient client, ChatObject data, final AckRequest ackRequest) {

                    // check is ack requested by client,
                    // but it's not required check
                    if (ackRequest.isAckRequested()) {
                        // send ack response with data to client
                        LOGGER.error("data received = "+data.getMessage());
                        ackRequest.sendAckData("client message was delivered to server!", "yeah!");
                    }

                    // send message back to client with ack callback WITH data
                    ChatObject ackChatObjectData = new ChatObject(data.getUserName(), "message with ack data");
                    client.sendEvent("ackevent2", new AckCallback<String>(String.class) {
                        @Override
                        public void onSuccess(String result) {
                            LOGGER.info("ack from client: " + client.getSessionId() + " data: " + result);
                        }
                    }, ackChatObjectData);

                    ChatObject ackChatObjectData1 = new ChatObject(data.getUserName(), "message with void ack");
                    client.sendEvent("ackevent3", new VoidAckCallback() {

                        protected void onSuccess() {
                            LOGGER.info("void ack from: " + client.getSessionId());
                        }

                    }, ackChatObjectData1);
                }
            });

            server.start();
        } catch (Exception e) {
            LOGGER.error("error starting server", e);
        }
        int count = 0;
        while (!shutdown) {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
            }

            if (++count % 10 == 0)
                EventLog.report("WinRun4J Test Service", EventLog.INFORMATION, "Ping");
        }
        try {
            if (server != null) {
                server.stop();
            }
        } catch (Exception e) {
            LOGGER.error("error stopping server", e);
        }
        return 0;
    }

    public static class ChatObject {

        private String userName;
        private String message;

        public ChatObject() {
        }

        public ChatObject(String userName, String message) {
            super();
            this.userName = userName;
            this.message = message;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

 /*   public static class AckChatLauncher {

        public static void main(String[] args) throws InterruptedException {

            Configuration config = new Configuration();
            config.setHostname("localhost");
            config.setPort(9092);

            final SocketIOServer server = new SocketIOServer(config);
            server.addEventListener("ackevent1", ChatObject.class, new DataListener<ChatObject>() {
                @Override
                public void onData(final SocketIOClient client, ChatObject data, final AckRequest ackRequest) {

                    // check is ack requested by client,
                    // but it's not required check
                    if (ackRequest.isAckRequested()) {
                        // send ack response with data to client
                        ackRequest.sendAckData("client message was delivered to server!", "yeah!");
                    }

                    // send message back to client with ack callback WITH data
                    ChatObject ackChatObjectData = new ChatObject(data.getUserName(), "message with ack data");
                    client.sendEvent("ackevent2", new AckCallback<String>(String.class) {
                        @Override
                        public void onSuccess(String result) {
                            System.out.println("ack from client: " + client.getSessionId() + " data: " + result);
                        }
                    }, ackChatObjectData);

                    ChatObject ackChatObjectData1 = new ChatObject(data.getUserName(), "message with void ack");
                    client.sendEvent("ackevent3", new VoidAckCallback() {

                        protected void onSuccess() {
                            System.out.println("void ack from: " + client.getSessionId());
                        }

                    }, ackChatObjectData1);
                }
            });

            server.start();

            Thread.sleep(Integer.MAX_VALUE);

            server.stop();
        }

    }*/


    @Override
    public int serviceRequest(int control) throws ServiceException {
        LOGGER.error("**********************************************");
        LOGGER.error("service Request : "+control);
        return super.serviceRequest(control);
    }
}
