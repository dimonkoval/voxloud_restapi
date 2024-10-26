package com.voxloud.provisioning;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.voxloud.provisioning.entity.Device;
import org.junit.jupiter.api.Test;

public class UtilsTest {
    public static final String MAC_ADDRESS = "00:1A:2B:3C:4D:5E";
    public static Device createTestDevice() {
        Device testDevice = new Device();
        testDevice.setUsername("testUser");
        testDevice.setPassword("testPass");
        testDevice.setMacAddress(MAC_ADDRESS);
        testDevice.setModel(Device.DeviceModel.DESK);
        testDevice.setOverrideFragment("domain=sip.mydomain.com\n"
                + "port=5161\n"
                + "timeout=3");
        return testDevice;
    }

    @Test
    void testCreateTestDevice() {

        Device testDevice = createTestDevice();

        assertEquals("testUser", testDevice.getUsername());
        assertEquals("testPass", testDevice.getPassword());
        assertEquals(MAC_ADDRESS, testDevice.getMacAddress());
        assertEquals(Device.DeviceModel.DESK, testDevice.getModel());
        assertEquals("domain=sip.mydomain.com\nport=5161\ntimeout=3", testDevice.getOverrideFragment());
    }
}
