package com.korbiak.service.messagesenders;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SmsSender {
    @Value("${twilioValue.sid}")
    public String accountSid;

    @Value("${twilioValue.token}")
    public String authToken;

    @Value("${twilioValue.number}")
    public String number;

    public void sendSms(String phone, String messageStr) {
        Twilio.init(accountSid, authToken);

        try {
            Message.creator(new PhoneNumber(phone), // to
                            new PhoneNumber(number), // from
                            messageStr)
                    .create();
        } catch (ApiException e) {
            log.error("Error sending sms alert: {}", e.getMessage());
        }
    }
}
