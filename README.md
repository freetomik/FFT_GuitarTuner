# MUL
FFT guitar tuner - school project for course Multimedia

## TODO
- ~~whole tuning mechanism~~ **TH**
  - ~~sampling cca 20-40ms of live input signal into buffer~~
  - ~~FFT over buffer to get frequency spectrum~~
  - ~~finding fundamental frequency by examining peaks in spectrum:~~
    - ~~highest peak~~
    - ~~Harmonic Product Spectrum method~~
- GUI **MD**
  - ~~buttons for tones generating~~
  - ~~slider for setting relative frequency~~
  - tuning indicator - tone name and arrow showing deviation in cents
- calculation with relative frequency for tuning ~~and generating~~ **MD**
- ~~tone generation~~ **MD**

### Documentation
  - describe theory behind tuning (FFT, HPS) **TH**
  - describe application GUI and controls **MD**

Czech task:
Výsledkem projektu by měla být aplikace pracující jako ladička zvoleného hudebního nástroje (kytary, houslí apod.) Aplikace by měla v reálném čase vzorkovat vstupní zvukový signál a zjišťovat jeho frekvenci, indikovat ji a indikovat též nejbližší ladicí tón a odchylku. Aplikace by měla mít i možnost kalibrace na "standardní stupnici" i "relativní ladění" se zadaným/zahraným vztažným tónem. Pro snadné používání programu by aplikace měla mít intuitivní grafické uživatelské rozhraní. Program by měl umět též generovat/přehrávat referenční tóny.
