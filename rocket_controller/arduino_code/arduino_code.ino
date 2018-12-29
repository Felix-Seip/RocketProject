#include <ArduinoJson.h>
#include <Adafruit_BMP085.h>
#include <SoftwareSerial.h>
#include "TotalRGB.h"
#include <Servo.h>

Adafruit_BMP085 bmp;
CTotalRGB rgbControl(13, 12, 11);
//Servo servo;

SoftwareSerial mySerial(0, 1); // RX, TX

void setup() {
  rgbControl.SetColor(PURPLE);
  mySerial.begin(9600);  
  bmp.begin();  
  
  // pinMode(10, OUTPUT);
  // servo.attach(9);
}

void loop() {

  CheckSensors();

  if (mySerial.available()) {
    rgbControl.SetColor(BLUE);
    mySerial.read();
    //TODO: If a certain character comes in, start the launch sequence
  }

  SerialFlush();  
  SendData("EngineTemperature", bmp.readTemperature(), 0);
  rgbControl.SetColor(PURPLE);
  delay(1000);
}

void SerialFlush(){
  while(mySerial.available() > 0) {
    char t = mySerial.read();
  }
}

void SendData(const String type, const float value, const long collectionTime){
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& data  = jsonBuffer.createObject();
  data["type"] = type;
  data["value"] = value;
  data["collectionTime"] = collectionTime;
  data.printTo(mySerial);
}

void CheckSensors(){
  while(!bmp.begin()){
    rgbControl.Blink(BLACK, RED, 100);
    digitalWrite(10, HIGH); // send high signal to buzzer 
  }
  digitalWrite(10, LOW);
  rgbControl.SetColor(GREEN);
}