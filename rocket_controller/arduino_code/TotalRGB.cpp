#include "TotalRGB.h"
#include "Arduino.h"

CTotalRGB::CTotalRGB(const int rPin, const int gPin, const int bPin){
  pinMode(R, OUTPUT);
  pinMode(G, OUTPUT);
  pinMode(B, OUTPUT);
  
  this->R = rPin;
  this->G = gPin;
  this->B = bPin;
}

void CTotalRGB::SetColor(COLOR color){
  switch(color){
    case RED:
      this->SetRGB(0, 255, 255);
      break;
    case BLUE:
      this->SetRGB(255, 255, 0);
      break;
    case GREEN:
      this->SetRGB(255, 0, 255);
      break;
    case PURPLE:
      this->SetRGB(0, 255, 0);
      break;
    case YELLOW:
      this->SetRGB(0, 0, 255);
      break;
    case WHITE:
      this->SetRGB(0, 0, 0);
      break;
    case BLACK:
      this->SetRGB(255, 255, 255);
      break;
  }
}

void CTotalRGB::SetRGB(const int rValue, const int gValue, const int bValue){
  analogWrite(this->R, rValue);
  analogWrite(this->G, gValue);
  analogWrite(this->B, bValue);
}

void CTotalRGB::Blink(const COLOR startColor, const COLOR endColor, const int blinkDelay){
  this->SetColor(startColor);
  delay(blinkDelay);
  this->SetColor(endColor);
  delay(blinkDelay);
}
