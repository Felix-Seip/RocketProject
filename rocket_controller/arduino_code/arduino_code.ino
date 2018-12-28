#include <ArduinoJson.h>
#include <Adafruit_BMP085.h>
#include <SoftwareSerial.h>
#include "TotalRGB.h"

// #define HC06 Serial3
Adafruit_BMP085 bmp;
CTotalRGB rgbControl(13, 12, 11);
SoftwareSerial hc06(0, 1);

void setup() {
  // put your setup code here, to run once:

  Serial.begin(9600);
  hc06.begin(9600);
  rgbControl.SetColor(BLUE);

  //CheckSensors();
}

void loop() {

  if (!hc06.available()){
    Serial.write("Error");
  }

  //Write data from HC06 to Serial Monitor
  if (hc06.available()){
    Serial.write(hc06.read());
  }
  
  //Write from Serial Monitor to HC06
  if (Serial.available()){
    hc06.write(Serial.read());
  } 

  // CheckSensors();

  // SendData("EngineTemperature", bmp.readTemperature(), 0);
  // SendData("AtmosphericPressure", bmp.readPressure()/100, 0);
  // SendData("Altitude", 0, 0);
  // SendData("Angle", 0, 0);
  // SendData("WindSpeed", 0, 0);
  // SendData("Velocity", 0, 0);
    
  // delay(1000);
}

void SendData(const String type, const float value, const long collectionTime){
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& data  = jsonBuffer.createObject();
  data["type"] = type;
  data["value"] = value;
  data["collectionTime"] = collectionTime;
}

void CheckSensors(){
  while(!bmp.begin()){
    rgbControl.Blink(BLACK, RED, 100);
  }
  rgbControl.SetColor(GREEN);
}