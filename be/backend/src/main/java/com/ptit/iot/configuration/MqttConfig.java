package com.ptit.iot.configuration;

import com.ptit.iot.model.Data;
import com.ptit.iot.dto.DataDto;
import com.ptit.iot.handler.DataSocketHandler;
import com.ptit.iot.service.DataService;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Date;

@Configuration
@Log4j2
public class MqttConfig
{
    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory()
    {
//        try {
//            Process p = Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"cd C:\\Mosquitto && mosquitto -v -c broker.conf\"");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        try {
            String serverUri = String.format("tcp://%s:1883", Inet4Address.getLocalHost().getHostAddress());
            options.setServerURIs(new String[]{serverUri});
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        options.setUserName("sonln");
        String password = "12345678";
        options.setPassword(password.toCharArray());
        options.setCleanSession(true);

        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel()
    {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound()
    {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("serverIn", mqttPahoClientFactory(), "#");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler()
    {
        return new MessageHandler()
        {
            @Autowired
            private DataService dataService;
            @Autowired
            private DataSocketHandler dataSocketHandler;
            @Override
            public void handleMessage(Message<?> message) throws MessagingException
            {
                String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
                if(topic.equals("dataSensorOutput"))
                {
                    System.out.println("nhận dữ liệu thành công");
                    Gson gson = new Gson();
                    Data data = gson.fromJson(message.getPayload().toString(), Data.class);
                    data.setTime(new Date());
                    log.info("data = {}", data);
                    this.dataService.saveData(data);

                    //publish data mới này tới tất cả client
                    this.dataSocketHandler.publishData(new DataDto(data));
//                    log.info("payload message = {}", message.getPayload().toString());
                }
                if(topic.equals("ledControlInput"))
                {
                    System.out.println("client gui du lieu");
                }
                System.out.println(message.getPayload());
            }
        };
    }

    @Bean
    public MessageChannel mqttOutBoundChannel()
    {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutBoundChannel")
    public MessageHandler mqttOutBound()
    {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("serverOut", mqttPahoClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("inTopic");
        return messageHandler;
    }
}

