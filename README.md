# AI Codenames Clue Giving Agent
Our project aims to create an AI to play the game “Codenames” in the role of the “spymaster”. In this word association game, the spymaster attempts to get their team to guess a set of words giving as few one word hints (clues) as possible. Additionally, there are some words that, if guessed, result in a loss of points (or even the game).

## Building the Project
A built `codenames.jar` file is already included in the repository.

If you’d like to build the project yourself, you can either build it using the provided `build.sh` script, the IntelliJ IDE, or the manually using the kotlin compiler:
```
kotlinc src/ -d codenames.jar -include-runtime
```

## Running the Project
Packaged in the repo is `codenames.jar` which can be executed using the JVM:
```
java -jar codenames.jar [options]
```
or via kotlin directly:
```
kotlin codenames.jar [options]
```

> Please note, the `codenames.jar` file *must executed from the same directory* as both `CodenameWords.txt` and the `WordAssocData/` folder, or required files will not be found.

### Options
The following (case sensitive) options can be applied to change the agents playing the game. The default for both teams is a human player.
```
-blueHinter [maxRepAI | weightedAI | human]
-redHinter [maxRepAI | weightedAI | human]
```

To play a game via the JVM with two AI hinters:
```
java -jar codenames.jar -redHinter maxRepAI -blueHinter weightedAI
```


## References
- [Kotlin Programming Language](https://kotlinlang.org)
- [Word Association Dataset](http://w3.usf.edu/FreeAssociation/AppendixA/index.html)
- [Codenames Official Site](http://czechgames.com/en/codenames/)