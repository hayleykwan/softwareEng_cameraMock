package ic.doc.camera;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class CameraTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    Sensor sensor = context.mock(Sensor.class);
    MemoryCard mCard = context.mock(MemoryCard.class);
    Camera camera = new Camera(sensor, mCard);

    @Test
    public void switchingTheCameraOnPowersUpTheSensor() {

        context.checking(new Expectations() {{
            exactly(1).of(sensor).powerUp();
        }});

        camera.powerOn();

    }

    @Test
    public void switchingTheCameraOffPowersDownTheSensor() {

        context.checking(new Expectations() {{
            exactly(1).of(sensor).powerDown();
        }});

        camera.powerOff();

    }

    @Test
    public void pressingTheShutterWhenPowerOffDoesNothing() {

        context.checking(new Expectations() {{
            exactly(1).of(sensor).powerDown();
            never(mCard).write(with(any(byte[].class)));
        }});

        camera.powerOff();
        camera.pressShutter();

    }

    @Test
    public void pressingTheShutterWhenPowerOnCopiesDataFromSensorToMemoryCard(){

        context.checking(new Expectations(){{
            exactly(1).of(sensor).powerUp();
            exactly(1).of(sensor).readData();
            exactly(1).of(mCard).write(with(any(byte[].class)));
        }});

        camera.powerOn();
        camera.pressShutter();
    }

    @Test
    public void switchingCameraOffDoesNotPowerDownSensorIfDataBeingWritten() {

        context.checking(new Expectations(){{
            exactly(1).of(sensor).powerUp();
            exactly(1).of(sensor).readData();
            exactly(1).of(mCard).write(with(any(byte[].class)));
            never(sensor).powerDown();
        }});

        camera.powerOn();
        camera.pressShutter();
        camera.powerOff();

    }

    @Test
    public void poweringSensorDownAfterDataWritingComplete() {

        context.checking(new Expectations(){{
            exactly(1).of(sensor).powerUp();
            exactly(1).of(sensor).readData();
            exactly(1).of(mCard).write(with(any(byte[].class)));
            exactly(1).of(sensor).powerDown();
        }});

        camera.powerOn();
        camera.pressShutter();
        camera.powerOff();
        camera.writeComplete();

    }

}
