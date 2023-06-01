// sender code for ESP32 board
const int outputPin = 13; // set the digital output pin for transmitting the character
// char characterToSend = 'H'; // set the character to send
// String stringToSend = "Hello";
const byte syncPattern = 0b10101010; // set the 8-bit synchronization pattern
int parityBit = 0;

void setup() {
  Serial.begin(9600); // initialize serial communication for debugging
  pinMode(outputPin, OUTPUT); // set the output pin as an output
}

void loop() {
  // Serial.print("Sending character: ");
  // Serial.println(stringToSend); // print the character to send to the Serial monitor

  while(Serial.available() == 0){}
  String stringToSend = Serial.readString();
  stringToSend.trim();  
  
  delay(1);

  for (int j = 0; j < stringToSend.length(); j++) { // iterate over all characters of the string

    // send the synchronization pattern to indicate the start of a character transmission
    for (int i = 0; i < 8; i++) {
      digitalWrite(outputPin, bitRead(syncPattern, i)); // set the output pin to the i-th bit of the synchronization pattern
      delay(1); // wait for 10 ms before transmitting the next bit
    }

    delay(1); // wait for 10 ms before transmitting the first bit
    char characterToSend = stringToSend.charAt(j); // get the j-th character of the string
    parityBit = parity(characterToSend);
    //byte charWithParity = (characterToSend << 1) | parityBit; // Add the parity bit to the end of the char
    for (int i = 0; i < 9; i++) { // iterate over all 8 bits of the 
      if (i == 8) {
        digitalWrite(outputPin, parityBit);
      } else {
        if (bitRead(characterToSend, i) == 1) { // check if the i-th bit of the character is 1
          digitalWrite(outputPin, HIGH); // set the output pin to HIGH to transmit a 1
          Serial.print("1");
        } else if(bitRead(characterToSend, i) == 0) {
          digitalWrite(outputPin, LOW); // set the output pin to LOW to transmit a 0
          Serial.print("0");
        }
      }
      delay(1); // wait for 10 ms before transmitting the next bit
    }
    delay(1); // wait for 10 milisecond before sending the next character
  }
  Serial.println("");
  //digitalWrite(outputPin, HIGH); // set the output pin to HIGH to signal the end of the character transmission
  delay(1); // wait for 1 second before sending the next character
}


int parity(char charSend) {

  int parity = 0;
  for (int i = 0; i < 8; i++) {
    if(bitRead(charSend, i) == 1) {
      parity = !parity;
    }
  }

  Serial.println(parity);

  return parity;
}