#include <ArduinoJson.h>
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <PubSubClient.h>

#define LED D0
#define red D5
#define blue D6
#define green D7
#define triggerPin D4
#define echoPin D3
#define buzzer D8
#define SERIAL_BAUD 9600

const String parking_id = "1";
String parking_status = "Available";
String previous_status = "";
String isBooked = "no";

const char* ssid = "OPTUS_A75988";
const char* password = "tubergongs88107";
const char* mqttServer = "192.168.0.10";
const int mqttPort = 1883;
const char* mqttUser = "";
const char* mqttPassword = "";

WiFiClient espClient;
PubSubClient client(espClient);

void setup() {
  WiFi.begin(ssid, password); // connect to Wifi
  Serial.begin(SERIAL_BAUD); // setup serial communication
  Serial.println("Serial Communication Setup Complete");
  
  pinMode(LED, OUTPUT);
  pinMode(red, OUTPUT);
  pinMode(green, OUTPUT);
  pinMode(blue, OUTPUT);
  pinMode(triggerPin, OUTPUT);
  pinMode(echoPin, INPUT);
  pinMode(buzzer, OUTPUT);

  digitalWrite(triggerPin, LOW); // clean the trigger pin
  digitalWrite(LED, HIGH); // turn off the Internal LED
  noTone(buzzer);

  // check if wifi not connected
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting..");
  }

  client.setServer(mqttServer, mqttPort);
  client.setCallback(callback);

  // reconnect MQTT when connection lost  
  while (!client.connected()) {
    Serial.println("Connecting to MQTT...");
 
    if (client.connect("parking1", mqttUser, mqttPassword )) {
      Serial.println("connected");  
    } else {
      Serial.print("failed with state ");
      Serial.print(client.state());
      delay(2000);
    }
  }
  client.subscribe("parking/authentication");
  client.subscribe("parking/booking");
  client.subscribe("parking/paymentStatus");
}

void loop() {
  if (!client.connected()) {
    reconnect();  
  }
  ledReactToDistance(getDistance());
  client.loop();
  delay(1000);
}

void ledReactToDistance(int distance) {
  if (distance <= 3) {
    colorLed(255, 0, 0); // LED red
    previous_status = parking_status;
    parking_status = "Occupied";
  } else if (isBooked == "yes" & distance > 3) {
    colorLed(255,255,0); //LED yellow
    previous_status = parking_status;
    parking_status = "Available";
    noTone(buzzer);
  } else {
    colorLed(0, 255, 0); // lED green
    previous_status = parking_status;
    parking_status = "Available";
    noTone(buzzer);
  }
  
  StaticJsonBuffer<300> JSONbuffer;
  JsonObject& JSONencoder = JSONbuffer.createObject();

  JSONencoder["parking_id"] = parking_id;
  JSONencoder["parking_status"] = parking_status;

  char JSONmessageBuffer[100];
  JSONencoder.printTo(JSONmessageBuffer, sizeof(JSONmessageBuffer));
  
  if (parking_status != previous_status) {
    client.publish("parking/status", JSONmessageBuffer);
  }
}

// trigger the ultrasonic sensor and get the distance value
int getDistance() {
  digitalWrite(triggerPin, LOW); // make sure the trigger is clean
  delayMicroseconds(2);

  digitalWrite(triggerPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(triggerPin, LOW); // turn off trigger after a duration

  long duration = pulseIn(echoPin, HIGH); // read the duration value
  int distance = duration * 0.034 / 2; // calculate the distance
  Serial.print("Distance: ");
  Serial.print(distance);
  Serial.println(" cm");
  return distance;
}

// blink the internal led every 1 second
void blinkInternalLed() {
  digitalWrite(LED, HIGH); // ESP Internal LED is reversed
  Serial.println("HIGH");
  delay(1000);
  digitalWrite(LED, LOW); // ESP Internal LED is reversed
  Serial.println("LOW");
  delay(1000);    
}

// pass the rgb value to the function
void colorLed(int r, int g, int b) {
  analogWrite(red, r);
  analogWrite(green, g);
  analogWrite(blue, b);  
}

// reconnect method for MQTT
void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    if (client.connect("parking1")) {
      Serial.println("connected");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      delay(5000);
    }
  }
}

// mqtt callback method
void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived in topic: ");
  Serial.println(topic);
 
  Serial.print("Message:");
  String msg = "";
  for (int i = 0; i < length; i++) {
    msg = msg + ((char)payload[i]);
  }
  Serial.print(msg);

  Serial.println();
  Serial.println("-----------------------");

  if (strcmp(topic, "parking/booking") == 0) {
    DynamicJsonBuffer jsonBuffer;
    JsonObject& root = jsonBuffer.parseObject(msg);
    if (root[String("parking_id")] == parking_id) {
      String holder = root[String("status")];
      if (holder == "yes") {
        isBooked = "yes";
      } else if (holder == "no") {
        isBooked = "no";
      }
    }    
  }

  if (strcmp(topic, "parking/authentication") == 0) {
    DynamicJsonBuffer jsonBuffer;
    JsonObject& root = jsonBuffer.parseObject(msg);
    if (root[String("parking_id")] == parking_id) {
      String status = root[String("status")];
      if (status == "mismatch") {
        tone(buzzer, 10);
      } else {
        colorLed(0,0,255); // make led blue
        delay(2000);
        noTone(buzzer);  
      }
    }
  }

  if (strcmp(topic, "parking/paymentStatus") == 0) {
    DynamicJsonBuffer jsonBuffer;
    JsonObject& root = jsonBuffer.parseObject(msg);
    if (root[String("parking_id")] == parking_id) {
      String status = root[String("status")];
      if (status == "not paid") {
        tone(buzzer, 10);
        delay(5000);
      } else {
        noTone(buzzer);
      }
    }
  }
  
}

// convert String to char
char* convertToChar(String msg) {
  int msg_len = msg.length() + 1;
  char msg_arr[msg_len];
  msg.toCharArray(msg_arr, msg_len);
  return msg_arr; 
}
