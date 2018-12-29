#include <ArduinoJson.h>
#include <Adafruit_BMP085.h>
#include <SoftwareSerial.h>
#include "TotalRGB.h"
#include <Servo.h>

// #define HC06 Serial3
Adafruit_BMP085 bmp;
CTotalRGB rgbControl(13, 12, 11);
Servo servo;

SoftwareSerial mySerial(0, 1); // RX, TX

void setup() {
  rgbControl.SetColor(PURPLE);
  Serial.begin(9600);
  Serial.println("Type AT commands!"); // put your setup code here, to run once:
  mySerial.begin(9600);
  pinMode(10, OUTPUT);
  servo.attach(9);
}

void loop() {

  CheckSensors();

  if (mySerial.available()) {
    rgbControl.SetColor(BLUE);
    Serial.println(mySerial.read());
  }

  mySerial.write("Hello World");

  rgbControl.SetColor(PURPLE);
  //delay(1000);
}

void CheckSensors(){
  while(!bmp.begin()){
    rgbControl.Blink(BLACK, RED, 100);
    digitalWrite(10, HIGH); // send high signal to buzzer 
  }
  digitalWrite(10, LOW);
  rgbControl.SetColor(WHITE);
}