// sender code for ESP32 board
const int outputPin = 13; // set the digital output pin for transmitting the character
char characterToSend = 'r'; // set the character to send
const byte syncPattern = 0b10101010; // set the 8-bit synchronization pattern

void setup() {
  Serial.begin(9600); // initialize serial communication for debugging
  pinMode(outputPin, OUTPUT); // set the output pin as an output
}

void loop() {
  Serial.print("Sending character: ");
  Serial.println(characterToSend); // print the character to send to the Serial monitor
  
  // send the synchronization pattern to indicate the start of a character transmission
  for (int i = 0; i < 8; i++) {
    digitalWrite(outputPin, bitRead(syncPattern, i)); // set the output pin to the i-th bit of the synchronization pattern
    delay(10); // wait for 10 ms before transmitting the next bit
  }

  delay(10); // wait for 10 ms before transmitting the first bit
  for (int i = 0; i < 8; i++) { // iterate over all 8 bits of the character
    if (bitRead(characterToSend, i) == 1) { // check if the i-th bit of the character is 1
      digitalWrite(outputPin, HIGH); // set the output pin to HIGH to transmit a 1
    } else {
      digitalWrite(outputPin, LOW); // set the output pin to LOW to transmit a 0
    }
    delay(10); // wait for 10 ms before transmitting the next bit
  }
  digitalWrite(outputPin, HIGH); // set the output pin to HIGH to signal the end of the character transmission
  delay(5000); // wait for 5 second before sending the next character
}