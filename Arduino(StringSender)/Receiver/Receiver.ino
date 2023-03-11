// receiver code for ESP32 board
const int inputPin = 4; // set the digital input pin for receiving the character
char receivedChar = 'A'; // initialize a variable to store the received character
const byte syncPattern = 0b10101010; // set the 8-bit synchronization pattern
byte characterWithParity = 0; // initialize the variable to store the characterWithParity
int parityBit = 0;

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
      delay(10); // wait for 1 ms before checking the next bit
    }
    if (syncFound) { // if True, leaves while(true) cycle 
      // Serial.println("Sync");
      break;
    }
  }

  delay(10);  // delay of 10 ms
  for (int i = 0; i < 9; i++) { // iterate over all 9 bits (8 bits of character + 1 bit of parity bit) of the character

    if (digitalRead(inputPin) == HIGH) { // check if the input pin is still HIGH
      // Serial.print("1");
      bitSet(receivedChar, i); // set the i-th bit of the character to 1
      characterWithParity |= (0 << i);

    } else if(digitalRead(inputPin) == LOW) {
      // Serial.print("0");
      bitClear(receivedChar, i); // set the i-th bit of the character to 0
      characterWithParity |= (1 << i);
        
    }
    delay(10); // wait for 10 ms before reading the next bit
  }

  parityBit = checkParity(characterWithParity);
  if (parityBit == 1) { // if parity bit is wrong there's an error

    Serial.println("ParityBit Error!");
    delay(10);

  } else if(parityBit == 0) { // if parity bit is correct there's no error

    byte characterReceived = receivedChar >> 1;
    Serial.print(receivedChar); // print the received character to the Serial monitor
    delay(10);
  }
}


int checkParity(char charReceive) { // receives the character received to check the parity

  int parity = charReceive & 0x01; // executes a AND operation to get the parity value
  bool errorDetected = false;

  for (int i = 0; i < 8; i++) {
    parity ^= bitRead(charReceive, i); // executes a XOR with the bit
  }
  if (parity != 0) { // If the parity doesn't match, an error is detected
    errorDetected = true;
    return 1; // returns '1' to indicate an error
  }

  return 0; // returns '0' to indicate no error
}


