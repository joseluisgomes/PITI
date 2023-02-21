import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class MyPortListener implements SerialPortEventListener {
    private final SerialPort port;
    private String messageReceived;

    public MyPortListener(SerialPort port) {
        this.port = port;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if(serialPortEvent.isRXCHAR()){ // data is available
            // read data, if there's bytes available
            if(serialPortEvent.getEventValue() > 0) {
                try {
                    byte[] buffer = port.readBytes();
                    messageReceived = new String(buffer); // Decode the received message
                } catch (SerialPortException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else if(serialPortEvent.isCTS()) { // CTS line has changed state
            if(serialPortEvent.getEventValue() == 1) { // line is ON
                System.out.println("CTS - ON");
            } else {
                System.out.println("CTS - OFF");
            }
        } else if(serialPortEvent.isDSR()){ // DSR line has changed state
            if(serialPortEvent.getEventValue() == 1) { // line is ON
                System.out.println("DSR - ON");
            } else {
                System.out.println("DSR - OFF");
            }
        }
    }

    public String getMessageReceived() {
        return messageReceived;
    }
}
