// пины для подключения контактов STEP, DIR

// количество шагов на 1 оборот
#define STEP_ROUND 50

// скорость двигателя
int SPEED = 900;
int delayRotate = 400;
bool crash = false;

struct DriverPins {
  int stepPin;
  int dirPin;
  int sensorPin;
};

// Массив из 6 драйверов A4988
DriverPins drivers[6] = {
  // Драйвер u 1: STEP=23, DIR=25
  { 23, 25, 2 },
  // Драйвер f 2: STEP=29, DIR=27
  { 29, 27, 3 },
  // Драйвер r 3: STEP=35, DIR=31
  { 35, 31, 4 },
  // Драйвер l 4: STEP=43, DIR=45
  { 43, 45, 5 },
  // Драйвер b 5: STEP=47, DIR=52
  { 47, 52, 6 },
  // Драйвер d 6: STEP=48, DIR=50
  { 48, 50, 7 }
};

void setup() {
  for (int i = 0; i < 6; i++) {
    pinMode(drivers[i].stepPin, OUTPUT);
    pinMode(drivers[i].dirPin, OUTPUT);
    pinMode(drivers[i].sensorPin, INPUT);

    digitalWrite(drivers[i].dirPin, 1);
    digitalWrite(drivers[i].stepPin, 0);
  }
  Serial.begin(9600);
}

void loop() {
  //checkButt();
  if (Serial.available()) {
    checkSerial();
  }
}

void checkSerial() {
  String str;
  String numStr;
  // read the incoming byte:
  str = Serial.readString();
  Serial.println(str);
  for (int i = 0; i < str.length(); i++) {
    int driver = 6;  //   0...5
    Serial.print(i);
    char currentChar = str.charAt(i);
    Serial.print(currentChar);

    switch (currentChar) {
      case 'u':
        // поворот верхнего двигателя
        driver = 0;
        break;

      case 'f':
        //поворот переднего двигателя
        driver = 1;
        break;

      case 'r':
        // поворот правого двигателя
        driver = 2;
        break;

      case 'l':
        //поворот левого двигателя
        driver = 3;
        break;

      case 'b':
        // поворот заднего двигателя
        driver = 4;
        break;

      case 'd':
        // поворот нижнего двигателя
        driver = 5;
        break;
      case 's':
        //изменение скорости
        i = str.length();
        numStr = str.substring(1); // Берём подстроку с позиции 1 до конца
        SPEED = numStr.toInt();
        break;
      case 'o':
        //изменение задержки между поворотами
        i = str.length();
        numStr = str.substring(1); // Берём подстроку с позиции 1 до конца
        delayRotate = numStr.toInt();
        break;
    }
    bool rotate = false;
    if (str.charAt(i + 2) == str.charAt(i + 1) && str.charAt(i + 1) == currentChar) {
      rotate = true;
      i += 2;
    }
    if (driver != 6) {
      rotateS(drivers[driver].stepPin, drivers[driver].dirPin, drivers[driver].sensorPin, rotate);
    }
    delay(delayRotate);
  }

}

void rotateS(int step, int dir, int stop, bool rot) {
  digitalWrite(dir, rot);
  for (int j = 0; j < STEP_ROUND; j++) {
    digitalWrite(step, HIGH);
    delayMicroseconds(SPEED);
    digitalWrite(step, LOW);
    delayMicroseconds(SPEED);
  }

}
