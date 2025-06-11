#include <AccelStepper.h>


#define MICROSTEPS 8
#define STEPS_PER_REV 200

#define STEPS_90_DEGREES (STEPS_PER_REV * MICROSTEPS/4)
// скорость двигателя
int SPEED = 1000000;
int acselerat = 150000;
bool crash = false;
int actual = 0;
struct DriverPins {
  int stepPin;
  int dirPin;
};
bool oneStepMode=false;
int i = 0;
String str;

// Массив из 6 драйверов A4988
DriverPins drivers[6] = {
  // Драйвер u 1: STEP=23, DIR=25
  { 23, 25 },
  // Драйвер f 2: STEP=29, DIR=27
  { 29, 27 },
  // Драйвер r 3: STEP=35, DIR=31
  { 35, 31 },
  // Драйвер l 4: STEP=43, DIR=45
  { 43, 45 },
  // Драйвер b 5: STEP=47, DIR=52
  { 47, 52 },
  // Драйвер d 6: STEP=48, DIR=50
  { 48, 50}
};

AccelStepper steppers[6];

void setup() {
  for (int i = 0; i < 6; i++) {
    steppers[i] = AccelStepper(AccelStepper::DRIVER, drivers[i].stepPin, drivers[i].dirPin);
    steppers[i].setMaxSpeed(SPEED * MICROSTEPS);
    steppers[i].setAcceleration(acselerat * MICROSTEPS);
  }
  Serial.begin(9600);
}

void loop() {
  //checkButt();
  if (Serial.available()) {
    checkSerial();
  }
  steppers[actual].run();
  if (!steppers[actual].isRunning() && i < str.length() &&! oneStepMode) {
    next();
  }
}
void next() {
  String numStr;
  int driver = 6;  //   0...5
  //Serial.print(i);
  char currentChar = str.charAt(i);
  //Serial.print(currentChar);

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
      for (int i = 0; i < 6; i++) {
        steppers[i].setMaxSpeed(SPEED * MICROSTEPS);
      }
      break;
    case 'o':
      //изменение задержки между поворотами
      i = str.length();
      numStr = str.substring(1); // Берём подстроку с позиции 1 до конца
      acselerat = numStr.toInt();
      for (int i = 0; i < 6; i++) {
        steppers[i].setAcceleration(acselerat * MICROSTEPS);
      }
      break;
case 'n':
     if(oneStepMode){
      next();
     }
      break;
      case 'a':
      oneStepMode=!oneStepMode;
      break;
      
  }
  bool rotate = false;
  if (str.charAt(i + 2) == str.charAt(i + 1) && str.charAt(i + 1) == currentChar) {
    rotate = true;
    i += 2;
  }
  if (driver != 6) {
    actual = driver;
    if (rotate) {
      steppers[driver].move(-STEPS_90_DEGREES);
    } else {
      steppers[driver].move(STEPS_90_DEGREES);
    }
  }
  i++;
}
void checkSerial() {
  // read the incoming byte:
  str = Serial.readString();
  //Serial.println(str)
  i = 0;
}
