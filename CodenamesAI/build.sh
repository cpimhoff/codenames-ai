rm codenames.jar 2>/dev/null
kotlinc src/ -d codenames.jar -include-runtime
kotlin codenames.jar -redHinter maxRepAI -blueHinter weightedAI
