package ic.doc.camera;

public class Camera implements WriteListener {

    private final Sensor sensor;
    private final MemoryCard memoryCard;
    private boolean isOn;
    private boolean writingDone;


    public Camera(Sensor sensor, MemoryCard mCard) {
        this.sensor = sensor;
        this.memoryCard = mCard;
        this.isOn = false;
        this.writingDone = true;
    }

    public void pressShutter() {
        if (isOn) {
            byte[] data = sensor.readData();
            writingDone = false;
            memoryCard.write(data);
        }

    }

    public void powerOn() {
        sensor.powerUp();
        isOn = true;
    }

    public void powerOff() {
        if (writingDone) {
            sensor.powerDown();
        }
        isOn = false;
    }

    @Override
    public void writeComplete() {
        writingDone = true;
        if (!isOn) {
            sensor.powerDown();
        }
    }

 
}

