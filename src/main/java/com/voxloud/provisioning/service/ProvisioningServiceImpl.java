package com.voxloud.provisioning.service;

import com.voxloud.provisioning.config.ProvisioningProperties;
import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.DeviceNotFoundException;
import com.voxloud.provisioning.exception.UnsupportedDeviceException;
import com.voxloud.provisioning.repository.DeviceRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ProvisioningServiceImpl implements ProvisioningService {

    private final DeviceRepository deviceRepository;
    private final ProvisioningProperties provisioningProperties;

    @Autowired
    public ProvisioningServiceImpl(DeviceRepository deviceRepository, ProvisioningProperties provisioningProperties) {
        this.deviceRepository = deviceRepository;
        this.provisioningProperties = provisioningProperties;
    }

    @Override
    public String getProvisioningFile(String macAddress) throws DeviceNotFoundException, UnsupportedDeviceException {
        Optional<Device> deviceOptional = deviceRepository.findById(macAddress);

        if (deviceOptional.isEmpty()) {
            throw new DeviceNotFoundException("Device with MAC address " + macAddress + " not found");
        }

        Device device = deviceOptional.get();

        if (device.getModel() == Device.DeviceModel.DESK) {
            return buildDeskConfiguration(device);
        } else if (device.getModel() == Device.DeviceModel.CONFERENCE) {
            return buildConferenceConfiguration(device);
        } else {
            throw new UnsupportedDeviceException("Unsupported device model: " + device.getModel());
        }
    }

    private String buildDeskConfiguration(Device device) {
        StringBuilder config = new StringBuilder();
        config.append("username=").append(device.getUsername()).append("\n");
        config.append("password=").append(device.getPassword()).append("\n");

        if (device.getOverrideFragment() == null) {
            config.append("domain=").append(provisioningProperties.getDomain()).append("\n");
            config.append("port=").append(provisioningProperties.getPort()).append("\n");
            config.append("codecs=").append(provisioningProperties.getCodecs()).append("\n");
        }else {
            config.append(device.getOverrideFragment()).append("\n");
        }
        return config.toString();
    }

    private String buildConferenceConfiguration(Device device) {
        Map<String, Object> jsonMap = new LinkedHashMap<>();
        jsonMap.put("username", device.getUsername());
        jsonMap.put("password", device.getPassword());

        if (device.getOverrideFragment() == null) {
            jsonMap.put("domain", provisioningProperties.getDomain());
            jsonMap.put("port", provisioningProperties.getPort());
            jsonMap.put("codecs", provisioningProperties.getCodecs().split(","));
        } else {
            JSONObject overrideJson = new JSONObject(device.getOverrideFragment());
            for (String key : overrideJson.keySet()) {
                jsonMap.put(key, overrideJson.get(key));
            }
        }
        return new JSONObject(jsonMap).toString(4);
    }
}
