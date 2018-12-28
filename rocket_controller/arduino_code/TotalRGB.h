
enum COLOR {
  BLUE,
  RED,
  GREEN,
  PURPLE,
  YELLOW,
  WHITE,
  BLACK
};

class CTotalRGB{
  public:
    CTotalRGB(const int rPin, const int gPin, const int bPin);
    void SetColor(COLOR color);
    void SetRGB(const int rValue, const int gValue, const int bValue);
    void Blink(const COLOR startColor, const COLOR endColor, const int blinkDelay);
  private:
    int R;
    int G;
    int B;
};
