package com.voxloud.provisioning.repository;

import com.voxloud.provisioning.UtilsTest;
import com.voxloud.provisioning.entity.Device;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class DeviceRepositoryTest {
    private final String macAddress = "00:1A:2B:3C:4D:5E";
    @Autowired
    private DeviceRepository deviceRepository;
    private Device testDevice;

    @BeforeEach
    void setUp() {
        testDevice = UtilsTest.createTestDevice();
        deviceRepository.save(testDevice);
    }

    @Test
    void testFindById() {
        Optional<Device> foundDevice = deviceRepository.findById(macAddress);
        assertThat(foundDevice).isPresent();
        assertThat(foundDevice.get().getUsername()).isEqualTo(testDevice.getUsername());
        assertThat(foundDevice.get().getPassword()).isEqualTo(testDevice.getPassword());
        assertThat(foundDevice.get().getMacAddress()).isEqualTo(testDevice.getMacAddress());
        assertThat(foundDevice.get().getModel()).isEqualTo(testDevice.getModel());
        assertThat(foundDevice.get().getOverrideFragment()).isEqualTo(testDevice.getOverrideFragment());
    }

    @Test
    void testDeleteDevice() {
        deviceRepository.delete(testDevice);
        Optional<Device> foundDevice = deviceRepository.findById(macAddress);
        assertThat(foundDevice).isNotPresent();
    }

    @Test
    void testSaveDevice() {
        Device newDevice = new Device();
        newDevice.setMacAddress("test");
        deviceRepository.save(newDevice);

        Optional<Device> foundDevice = deviceRepository.findById("test");
        assertThat(foundDevice).isPresent();
        assertThat(foundDevice.get().getMacAddress()).isEqualTo(newDevice.getMacAddress());
    }
}
