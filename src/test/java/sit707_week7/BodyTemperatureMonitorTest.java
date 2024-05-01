package sit707_week7;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class BodyTemperatureMonitorTest {

    @Test
    public void testStudentIdentity() {
        String studentId = "222332999";
        Assert.assertNotNull("Student ID is null", studentId);
    }

    @Test
    public void testStudentName() {
        String studentName = "Ameet";
        Assert.assertNotNull("Student name is null", studentName);
    }

    @Test
    public void testReadTemperatureNegative() {
        // Mocking TemperatureSensor
        TemperatureSensor temperatureSensor = Mockito.mock(TemperatureSensor.class);
        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(-1.0);

        BodyTemperatureMonitor monitor = new BodyTemperatureMonitor(temperatureSensor, null, null);
        double temperature = monitor.readTemperature();
        Assert.assertEquals("Temperature should be negative", -1, temperature, 0);
    }

    @Test
    public void testReadTemperatureZero() {
        // Mocking TemperatureSensor
        TemperatureSensor temperatureSensor = Mockito.mock(TemperatureSensor.class);
        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(0.0);

        BodyTemperatureMonitor monitor = new BodyTemperatureMonitor(temperatureSensor, null, null);
        double temperature = monitor.readTemperature();
        Assert.assertEquals("Temperature should be zero", 0, temperature, 0);
    }

    @Test
    public void testReadTemperatureNormal() {
        // Mocking TemperatureSensor
        TemperatureSensor temperatureSensor = Mockito.mock(TemperatureSensor.class);
        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(37.0);

        BodyTemperatureMonitor monitor = new BodyTemperatureMonitor(temperatureSensor, null, null);
        double temperature = monitor.readTemperature();
        Assert.assertEquals("Temperature should be normal", 37, temperature, 0);
    }

    @Test
    public void testReadTemperatureAbnormallyHigh() {
        // Mocking TemperatureSensor
        TemperatureSensor temperatureSensor = Mockito.mock(TemperatureSensor.class);
        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(42.0);

        BodyTemperatureMonitor monitor = new BodyTemperatureMonitor(temperatureSensor, null, null);
        double temperature = monitor.readTemperature();
        Assert.assertEquals("Temperature should be abnormally high", 42, temperature, 0);
    }

    @Test
    public void testReportTemperatureReadingToCloud() {
        // Mocking CloudService
        CloudService cloudService = Mockito.mock(CloudService.class);
        BodyTemperatureMonitor monitor = new BodyTemperatureMonitor(null, cloudService, null);

        TemperatureReading temperatureReading = new TemperatureReading();
        monitor.reportTemperatureReadingToCloud(temperatureReading);
        Mockito.verify(cloudService, Mockito.times(1)).sendTemperatureToCloud(temperatureReading);
    }

    @Test
    public void testInquireBodyStatusNormalNotification() {
        // Mocking NotificationSender
        NotificationSender notificationSender = Mockito.mock(NotificationSender.class);
        CloudService cloudService = Mockito.mock(CloudService.class);
        Mockito.when(cloudService.queryCustomerBodyStatus(Mockito.any(Customer.class))).thenReturn("NORMAL");

        BodyTemperatureMonitor monitor = new BodyTemperatureMonitor(null, cloudService, notificationSender);
        monitor.inquireBodyStatus();

        Mockito.verify(notificationSender, Mockito.times(1)).sendEmailNotification(Mockito.any(Customer.class), Mockito.anyString());
    }

    @Test
    public void testInquireBodyStatusAbnormalNotification() {
        // Mocking NotificationSender
        NotificationSender notificationSender = Mockito.mock(NotificationSender.class);
        CloudService cloudService = Mockito.mock(CloudService.class);
        Mockito.when(cloudService.queryCustomerBodyStatus(Mockito.any(Customer.class))).thenReturn("ABNORMAL");

        BodyTemperatureMonitor monitor = new BodyTemperatureMonitor(null, cloudService, notificationSender);
        monitor.inquireBodyStatus();

        Mockito.verify(notificationSender, Mockito.times(1)).sendEmailNotification(Mockito.any(FamilyDoctor.class), Mockito.anyString());
    }
}
