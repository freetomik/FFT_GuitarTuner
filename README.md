# Guitar Tuner

## Description
This JavaFX application listens via microphone for music tone and displays its frequency and deviation from nearest musical note. It also generates guitar tones for tuning by ear. For both tuning and generating referential frequency can be set.

Note: Application uses ASIO sound driver from Steinberg company. Drivers supported OS is Windows, so whole application is dedicated to Windows.

## Usage
- download ASIO4ALL sound driver from http://asio4all.com/ and install it
- download GuitarTuner.jar and run it

## Development
The GuitarTuner directory contains some files from NetBeans 8.2 project using Maven which can be opened in NetBeans.

## 3-rd party stuff
GuitarTuner is based on ChordRecognizer created as [Bachelor Thesis](https://www.vutbr.cz/studium/zaverecne-prace?zp_id=88462) at BUT FIT.

It uses
- [JTransforms](https://github.com/wendykierp/JTransforms) for Fast Fourier Transform,
- [JAsioHost](https://github.com/mhroth/jasiohost) for communication with ASIO driver and
- [JLargeArrays](https://github.com/IcmVis/JLargeArrays), which are used internally by JTransforms.

## Authors
### Tomáš Hudziec
- tuning mechanism
  - sampling cca 20-40ms of live input signal into buffer
  - FFT over buffer to get frequency spectrum
  - finding fundamental frequency by examining peaks in spectrum:
    - highest peak
    - Harmonic Product Spectrum method
### Matúš Dobrotka
- GUI
  - buttons for tones generating
  - slider for setting relative frequency
  - tuning indicator - tone name and arrow showing deviation in cents
- calculation with relative frequency for tuning and generating
- tone generation

## Documentation
### Tomáš Hudziec
  - describe theory behind tuning (FFT, HPS)
### Matúš Dobrotka
  - describe application GUI and controls

## License
MIT License

Czech task:
Výsledkem projektu by měla být aplikace pracující jako ladička zvoleného hudebního nástroje (kytary, houslí apod.) Aplikace by měla v reálném čase vzorkovat vstupní zvukový signál a zjišťovat jeho frekvenci, indikovat ji a indikovat též nejbližší ladicí tón a odchylku. Aplikace by měla mít i možnost kalibrace na "standardní stupnici" i "relativní ladění" se zadaným/zahraným vztažným tónem. Pro snadné používání programu by aplikace měla mít intuitivní grafické uživatelské rozhraní. Program by měl umět též generovat/přehrávat referenční tóny.
