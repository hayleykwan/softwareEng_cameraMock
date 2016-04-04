package ic.doc.camera;

public class Camera implements WriteListener {

    private final Sensor sensor;
    private final MemoryCard memoryCard;
    private boolean isOn;
    private boolean writing;


    public Camera(Sensor sensor, MemoryCard mCard) {
        this.sensor = sensor;
        this.memoryCard = mCard;
        this.isOn = false;
        this.writing = false;
    }

    public void pressShutter() {
        if (isOn) {
            byte[] data = sensor.readData();
            memoryCard.write(data);
            writing = true;
        }
    }

    public void powerOn() {
        isOn = true;
        sensor.powerUp();
    }

    public void powerOff() {
        if (!writing) {
            sensor.powerDown();
        }
        isOn = false;
    }

    @Override
    public void writeComplete() {
        writing = false;
        if (!isOn) {
            powerOff();
        }
    }

 
}

