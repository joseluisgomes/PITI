// sender code for ESP32 board
const int outputPin = 13; // set the digital output pin for transmitting the character
const byte syncPattern = 0b10101010; // set the 8-bit synchronization pattern
int parityBit = 0;

void setup() {
  Serial.begin(9600); // initialize serial communication for debugging
  pinMode(outputPin, OUTPUT); // set the output pin as an output
}

void loop() {
  while (Serial.available() == 0) {}

  String stringToSend = Serial.readString();
  // stringToSend.trim();

  for (int j = 0; j < stringToSend.length(); j++) {
    // send the synchronization pattern to indicate the start of a character transmission
    for (int i = 0; i < 8; i++) {
      digitalWrite(outputPin, bitRead(syncPattern, i));
      delayMicroseconds(188); // wait for 188 microseconds before transmitting the next bit
    }

    delayMicroseconds(188); // wait for 267 microseconds before transmitting the first bit

    char characterToSend = stringToSend.charAt(j);

    parityBit = parity(characterToSend);
    for (int i = 0; i < 9; i++) {
      if (i == 8) {
        digitalWrite(outputPin, parityBit);
      } else {
        if (bitRead(characterToSend, i) == 1) {
          digitalWrite(outputPin, HIGH);
          // Serial.print("1 - ");
        } else if (bitRead(characterToSend, i) == 0) {
          digitalWrite(outputPin, LOW);
          // Serial.print("0 - ");
        }
      }
      delayMicroseconds(188); // wait for 188 microseconds before transmitting the next bit
    }

    delayMicroseconds(188); // wait for 188 microseconds before sending the next character
    Serial.println("");
  }
  Serial.println("");
  delayMicroseconds(188); // wait for 1 millisecond before sending the next character
}

int parity(char charSend) {
  int parity = 0;
  for (int i = 0; i < 8; i++) {
    if (bitRead(charSend, i) == 1) {
      parity = !parity;
    }
  }
  return parity;
}
