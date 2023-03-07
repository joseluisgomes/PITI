// sender code for ESP32 board
const int outputPin = 13; // set the digital output pin for transmitting the character
char characterToSend = 'a'; // set the character to send

void setup() {
  Serial.begin(9600); // initialize serial communication for debugging
  pinMode(outputPin, OUTPUT); // set the output pin as an output
}

void loop() {
  Serial.print("Sending character: ");
  Serial.println(characterToSend); // print the character to send to the Serial monitor
  digitalWrite(outputPin, LOW); // set the output pin to LOW to signal the start of the character transmission
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
  delay(1000); // wait for 1 second before sending the next character
}