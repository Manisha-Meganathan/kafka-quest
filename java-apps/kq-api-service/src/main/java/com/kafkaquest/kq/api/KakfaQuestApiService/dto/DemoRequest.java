package com.kafkaquest.kq.api.KESAApiService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemoRequest {

    @NotBlank(message = "FirstName can not be blank")
    private String firstName;

    @NotBlank(message = "LastName can not be blank")
    private String lastName;

    @NotBlank(message = "Email can not be blank")
    @Email(message = "Not a valid email")
    private String email;

    @NotBlank(message = "Company Name can not be blank")
    private String companyName;

    @NotBlank(message = "Company Role can not be blank")
    private String companyRole;

    @NotBlank(message = "Company Size can not be blank")
    private String companySize;

    @NotBlank(message = "Country can not be blank")
    private String country;

    @NotBlank(message = "Contact Number can not be blank")
    private String contactNumber;

    @NotBlank(message = "Message can not be blank")
    private String message;

    public Map<String, Object> getEmailTemplateKeyValues() {
        Map<String, Object> propertyValueMap = new HashMap<>();

        propertyValueMap.put(EmailTemplateKey.NAME.name(), "%s %s".formatted(this.firstName, this.lastName));
        propertyValueMap.put(EmailTemplateKey.EMAIL.name(), this.email);
        propertyValueMap.put(EmailTemplateKey.COMPANY.name(), this.companyName);
        propertyValueMap.put(EmailTemplateKey.ROLE.name(), this.companyRole);
        propertyValueMap.put(EmailTemplateKey.SIZE.name(), this.companySize);
        propertyValueMap.put(EmailTemplateKey.COUNTRY.name(), this.country);
        propertyValueMap.put(EmailTemplateKey.CONTACT_NUMBER.name(), this.contactNumber);
        propertyValueMap.put(EmailTemplateKey.MESSAGE.name(), this.message);

        return propertyValueMap;
    }
}
