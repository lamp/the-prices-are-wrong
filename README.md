# Usage Instructions

## Dependencies

To run the script Babashka is preferred, you can find installation instructions [here](https://github.com/babashka/babashka#installation)

To run the tests I opted for clojure/kaocha as Babshka was fiddly to get working with the tests, installation instructions are [here](https://clojure.org/guides/install_clojure)

The data is required to be in the `data` directory otherwise the script will just fail

## Usage

To run the script, from the project root run:

```
bb src/calculate_price_differential.clj
```

To run the tests, from the project roo run:
```
./bin/kaocha
```

For a REPL, I used [vim-iced](https://github.com/liquidz/vim-iced) and to start that up (with kaocha) I used:
```
iced repl --dependency=kaocha-nrepl:0.1.1 --middleware=kaocha-nrepl.core/wrap-kaocha
```


## Notes
- I spent far too long trying to get the data into a format that was easy to output, resulting in _many_ iterations through the entire data set
- I avoided using regexes for no real reason, which led to parsing being pretty easy to break
- With a larger data set this script would not scale well, however in a real setting you are most likely to only improve the script
  when required, so I felt this more adequately reflected real world situations
- Lack of confiurability of location of the data, this would be useful, but again would likely be added later
- Wasted far too much time trying to get tests working in Babashka
- It would have been good to have kept track of the receipt in which the line item was, as it became hard to manually test the script
- The tests should really test edge cases and invalid data, but as I was using them as a design tool I found it hard to add that on later
