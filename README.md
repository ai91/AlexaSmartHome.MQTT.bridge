# AlexaSmartHome.MQTT.bridge

This project represents a bridge between your Alexa AWS Lambda and your MQTT broker.

![Architecture](README/architecture.png)

The Project provides a REST API, secured via [OAuth2 JSON Web Tokens](https://oauth.net/2/jwt/), and provides access to MQTT topics.

For ease setup, the project is also provided as a Docker image: [ai91/alexamqttbridge](https://hub.docker.com/r/ai91/alexamqttbridge).

To enable integration of Alexa into your IoT MQTT, you need to setup:
- Install/configure **MQTT Broker**
- Install or use some third-party **JWT Auth Server** 
- Install **Alexa.MQTT.Bridge** *(this project)*
- Create/setup own **Alexa Smart Home Skill** and install in in DEV mode into your Alexa account
- Create/setup own **AWS Lambda**, and configure it to interact with your instance of the Alexa.MQTT.Bridge and your Alexa Smart Skill.

## MQTT Broker and security questions
As soon as we need to communicate between your home network, and outer-internets, for security purposes I would recommend to not let connect directly to your internal network with MQTT broker and Home Automation system. 
Though the MQTT configuration is out of scope of this project, I recommend to install a new, external, MQTT broker, and configure your internal one as a bridge:

![MQTT Security](README/mqtt_security.png)



Here is an example as I did in my case. I'm hosting a docker infrastructure on VPS:
 
![Dockerized Security](README/docker_reverse_proxy.png)

Of course it's up to you how you configure the infrastructure: one can use DynamicDNS and host everything at home network, use no docker, etc.


## Configuration and startup of Alexa.MQTT.Bridge
The project is shipped either as a docker image, or as a Spring Boot "fat jar". Alternatively, of course, it's possible to build and start from sources.
### Docker image
The image: [ai91/alexamqttbridge](https://hub.docker.com/r/ai91/alexamqttbridge) on hub.docker.
Sample docker-compose config:
```yaml
version: '3.3'

services:

# ....
  alexa_mqtt_bridge:
    container_name: alexa_mqtt_bridge
    image: ai91/alexamqttbridge:latest
    volumes:
      - "./config/:/workspace/config/"
    restart: always
    networks:
# ....
```

The sample config directory content can be found [here](config).

### Fat jar
The application can also be started as a "fat jar". You need to prepare a JRE 11, and execute following command:
`java -jar alexamqttbridge.jar`
Same [here](config) should be placed in the directory with jar.

### Configuration
The basic configuration is provided in the [config/application.properties](config/application.properties). 
Properties in the file are mandatory, though the file itself is optional. Configuration can also be provided as a YAML file, command-line arguments, or as environment variables. 
Details and other options are described in the [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/2.1.9.RELEASE/reference/html/boot-features-external-config.html).
For example for troubleshooting, one can lower logging levels with environment variable `LOGGING_LEVEL_ROOT=trace`

The basic configuration is responsible for communication and application itself. 
Meanwhile, there is an extended configuration, which describes your smart home configuration, rules, and value mappings between your home automation system and Alexa Smart Home Skill. 
The extended configuration is takes place in the [config/devices.json](config/devices.json).

## AWS Lambda and Alexa Smart Home Skill
The **AWS Lambda** function is a proxy between Alexa Smart Home Skill, and Alexa.MQTT.Bridge. 
The Lambda function gets triggered by Alexa, gets JWT token from parameters, and passes request as-is to the Alexa.MQTT.Bridge REST API. Then returns back a response from the API. Simple as it is.

Detailed information on configuration can be found on [official documentation](https://developer.amazon.com/en-US/docs/alexa/smarthome/steps-to-build-a-smart-home-skill.html).
The proxy-function is provided here: [lambda\lambda_function.py](lambda\lambda_function.py) (written in Python 3). 
**Note:** don't forget to modify `url = 'https://example.com/alexamqttbridge/api/events'` with your instance of Alexa.MQTT.Bridge. 
