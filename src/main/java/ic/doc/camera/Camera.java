package ic.doc.camera;

public class Camera implements WriteListener {

    private final Sensor sensor;
    private final MemoryCard memoryCard;
    private boolean isOn;


    public Camera(Sensor sensor, MemoryCard mCard) {
        this.sensor = sensor;
        this.memoryCard = mCard;
        this.isOn = false;
    }

    public void pressShutter() {
        if (isOn) {
            byte[] data = sensor.readData();
            memoryCard.write(data);
        }

    }

    public void powerOn() {
        sensor.powerUp();
        isOn = true;
    }

    public void powerOff() {
        sensor.powerDown();
        isOn = false;
    }

    @Override
    public void writeComplete() {

    }

 
}

