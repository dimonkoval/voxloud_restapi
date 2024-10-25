package com.voxloud.provisioning;

import com.voxloud.provisioning.entity.Device;

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
}
