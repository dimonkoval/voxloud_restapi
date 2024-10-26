package com.voxloud.provisioning.service;

import com.voxloud.provisioning.UtilsTest;
import com.voxloud.provisioning.config.ProvisioningProperties;
import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.DeviceNotFoundException;
import com.voxloud.provisioning.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProvisioningServiceImplTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private ProvisioningProperties provisioningProperties;

    @InjectMocks
    private ProvisioningServiceImpl provisioningService;

    private Device testDevice;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testDevice = UtilsTest.createTestDevice();
        when(deviceRepository.findById(UtilsTest.MAC_ADDRESS)).thenReturn(Optional.of(testDevice));
    }

    @Test
    void testGetProvisioningFile_Success_DeskModel() throws Exception {
        when(deviceRepository.findById(testDevice.getMacAddress())).thenReturn(Optional.of(testDevice));
        when(provisioningProperties.getDomain()).thenReturn("sip.mydomain.com");
        when(provisioningProperties.getPort()).thenReturn("5161");

        String result = provisioningService.getProvisioningFile(testDevice.getMacAddress());

        String expectedConfig = "username=testUser\n" +
                "password=testPass\n" +
                "domain=sip.mydomain.com\n" +
                "port=5161\n" +
                "timeout=3\n";

        assertEquals(expectedConfig, result);
    }

    @Test
    void testGetProvisioningFile_DeviceNotFound() {
        when(deviceRepository.findById(testDevice.getMacAddress())).thenReturn(Optional.empty());

        DeviceNotFoundException exception = assertThrows(DeviceNotFoundException.class,
                () -> provisioningService.getProvisioningFile(testDevice.getMacAddress()));

        assertEquals("Device with MAC address " + testDevice.getMacAddress() + " not found", exception.getMessage());
    }
}
