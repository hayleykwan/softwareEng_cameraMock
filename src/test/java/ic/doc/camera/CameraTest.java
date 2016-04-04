package ic.doc.camera;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class CameraTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    Sensor sensor = context.mock(Sensor.class);
    MemoryCard mCard = context.mock(MemoryCard.class);
    Camera camera = new Camera(sensor, mCard);
    byte[] data = new byte[4];

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
            ignoring(sensor).powerUp();
            exactly(1).of(sensor).powerDown();
        }});
        camera.powerOn();
        camera.powerOff();

    }

    @Test
    public void pressingTheShutterWhenPowerOffDoesNothing() {

        context.checking(new Expectations() {{
            never(sensor).powerDown();
            never(mCard);
        }});
        camera.pressShutter();

    }

    @Test
    public void pressingTheShutterWhenPowerOnCopiesDataFromSensorToMemoryCard(){

        context.checking(new Expectations(){{
            ignoring(sensor).powerUp();
            exactly(1).of(sensor).readData(); will(returnValue(data));
            exactly(1).of(mCard).write(data);
        }});

        camera.powerOn();
        camera.pressShutter();
    }

    @Test
    public void switchingCameraOffDoesNotPowerDownSensorIfDataBeingWritten() {

        context.checking(new Expectations(){{
            ignoring(sensor).powerUp();
            exactly(1).of(sensor).readData(); will(returnValue(data));
            exactly(1).of(mCard).write(data);
            never(sensor).powerDown();
        }});

        camera.powerOn();
        camera.pressShutter();
        camera.powerOff();

    }

    @Test
    public void poweringSensorDownAfterDataWritingComplete() {

        context.checking(new Expectations(){{
            ignoring(sensor).powerUp();
            exactly(1).of(sensor).readData(); will(returnValue(data));
            exactly(1).of(mCard).write(data);
        }});

        camera.powerOn();
        camera.pressShutter();
        camera.powerOff();

        context.checking(new Expectations(){{
            exactly(1).of(sensor).powerDown();}
        });
        camera.writeComplete();

    }

}
