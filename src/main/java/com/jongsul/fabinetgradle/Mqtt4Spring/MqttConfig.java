package com.jongsul.fabinetgradle.Mqtt4Spring;

import com.jongsul.fabinetgradle.Service.CabinetService;
import com.jongsul.fabinetgradle.Service.CabinetServiceImpl;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;


@Configuration
public class MqttConfig {
    private static final String MQTT_USERNAME = "username";
    private static final String MQTT_PASSWORD = "password";

    private static final String BROKER_URL = "tcp://3.34.255.198:1883";
    private static final String MQTT_CLIENT_ID = MqttAsyncClient.generateClientId();
    private static final String TOPIC_FILTER = "heum/username";

    private static String inputUserName = "";
    @Autowired
    private static CabinetService cabinetservice;

    private MqttConnectOptions connectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setUserName(MQTT_USERNAME);
        options.setPassword(MQTT_PASSWORD.toCharArray());
        return options;
    }

    @Bean
    public DefaultMqttPahoClientFactory  defaultMqttPahoClientFactory() {
        DefaultMqttPahoClientFactory  clientFactory = new DefaultMqttPahoClientFactory();
        clientFactory.setConnectionOptions(connectOptions());
        return clientFactory;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inboundChannel() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(BROKER_URL, MQTT_CLIENT_ID, TOPIC_FILTER);
        adapter.setCompletionTimeout(50000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(0);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler inboundMessageHandler() {
        return message -> {
            String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
            System.out.println("Topic:" + topic);
            System.out.println("Payload:" + message.getPayload());
            inputUserName = message.getPayload().toString();
            searchDB(inputUserName);
            System.out.println("inputUserName = message.getPayload().toString() 완료");
        };
    }

    public String searchDB(String userName){
        return inputUserName;
    }

    public String getInputUserName(){
        return inputUserName;
    }
}
