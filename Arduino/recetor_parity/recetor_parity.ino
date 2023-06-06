// receiver code for ESP32 board
const int inputPin = 4; // set the digital input pin for receiving the character
char receivedChar = 'A'; // initialize a variable to store the received character
const byte syncPattern = 0b10101010; // set the 8-bit synchronization pattern
int parity = 0; // initialize the variable to store the characterWithParity
int parityBit = 0;
int input = 0;

void setup() {
  Serial.begin(9600); // initialize serial communication for debugging
  pinMode(inputPin, INPUT); // set the inputPin as an input
  Serial.println();
}

void loop() {


  // look for the synchronization pattern to indicate the start of a character transmission
  while (true) {
    bool syncFound = true;
    for (int i = 0; i < 8; i++) {
      if (digitalRead(inputPin) != bitRead(syncPattern, i)) { // check if the i-th bit of the synchronization pattern matches the input pin state
        syncFound = false; // if not, breaks from cycle and repeats process
        break;
      }

      delayMicroseconds(188); // wait for 1 ms before checking the next bit
    }
    if (syncFound) { // if True, leaves while(true) cycle 
      // Serial.println(" Sync! ");
      break;
    }
  }

  delayMicroseconds(188);  // delay of 1 ms

  for (int i = 0; i < 9; i++) { // iterate over all 9 bits (8 bits of character + 1 bit of parity bit) of the character

    if (digitalRead(inputPin) == HIGH) { // check if the input pin is still HIGH
      // input = read_input();
      // Serial.print(input);

      if (i == 8) {
        parity = 1;
      }


      // Serial.print("1 - ");

      bitSet(receivedChar, i); // set the i-th bit of the character to 1



    } else if(digitalRead(inputPin) == LOW) {
      // input = read_input();
      // Serial.print(input);

      if (i == 8) {
        parity = 0;
      }

      // Serial.print("0 -");

      bitClear(receivedChar, i); // set the i-th bit of the character to 1



    } 

    delayMicroseconds(188); // wait for 1 ms before reading the next bit
  }


  parityBit = checkParity(receivedChar, parity);
  if (parityBit == 1) { // if parity bit is wrong there's an error

    // Serial.println("ParityBit Error!\n");

  } else { // if parity bit is correct there's no error

    byte characterReceived = receivedChar >> 1;
    Serial.print(receivedChar); // print the received character to the Serial monitor
  }

  delayMicroseconds(188); // Wait 105 microseconds before receiving the next character

}


int checkParity(char charReceive, int parity) { // receives the character received to check the parity

  int parityBitEmpty = 0;
  bool errorDetected = false;

  for (int i = 0; i < 8; i++) {
      if (bitRead(charReceive, i) == 1) {
        parityBitEmpty = !parityBitEmpty;
    }
  }
  
  if (parityBitEmpty != parity) { // If the parity doesn't match, an error is detected
    errorDetected = true;
    return 1; // returns '1' to indicate an error
  }

  return 0; // returns '0' to indicate no error
}


int read_input() {
  int index_1 = 0;
  int index_0 = 0;

  for (int i = 0; i < 9; i++) {

    if (digitalRead(inputPin) == HIGH) { 
       index_1 = index_1 + 1;
    } else if(digitalRead(inputPin) == LOW) {
       index_0 = index_0 + 1;
    }
    delayMicroseconds(188);
  }

  if (index_1 < index_0) {
    return 0;
  } else {
    return 1;
  }

}

