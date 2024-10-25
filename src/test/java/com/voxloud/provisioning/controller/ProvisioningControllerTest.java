package com.voxloud.provisioning.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.voxloud.provisioning.UtilsTest;
import com.voxloud.provisioning.exception.DeviceNotFoundException;
import com.voxloud.provisioning.service.ProvisioningService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProvisioningController.class)
class ProvisioningControllerTest {
    private final String macAddress = UtilsTest.MAC_ADDRESS;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProvisioningService provisioningService;

    @Test
    void testGetProvisioningFile_Success() throws Exception {
        String provisioningFileContent = "username=user\npassword=pass\n";
        given(provisioningService.getProvisioningFile(macAddress)).willReturn(provisioningFileContent);

        mockMvc.perform(get("/api/v1/provisioning/" + macAddress)
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(provisioningFileContent));
    }

    @Test
    void testGetProvisioningFile_DeviceNotFoundException() throws Exception {
        String macAddress1 = "test1";

        when(provisioningService.getProvisioningFile(macAddress1))
                .thenThrow(new DeviceNotFoundException("Device with MAC address " + macAddress1 + " not found"));

        mockMvc.perform(get("/api/v1/provisioning/" + macAddress1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Device with MAC address " + macAddress1 + " not found"))
                .andExpect(jsonPath("$.path").value("uri=/api/v1/provisioning/" + macAddress1));
    }
}
