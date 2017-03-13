# NOTE: You may have to change the filepath strings depending on where
#       your CSVs are located.

# Read all word association CSVs (organized by letter range)
ab = read.csv("~/Desktop/WordAssocData/ab.csv", stringsAsFactors = F)
c = read.csv("~/Desktop/WordAssocData/c.csv", stringsAsFactors = F)
df = read.csv("~/Desktop/WordAssocData/df.csv", stringsAsFactors = F)
gk = read.csv("~/Desktop/WordAssocData/gk.csv", stringsAsFactors = F)
lo = read.csv("~/Desktop/WordAssocData/lo.csv", stringsAsFactors = F)
pr = read.csv("~/Desktop/WordAssocData/pr.csv", stringsAsFactors = F)
s = read.csv("~/Desktop/WordAssocData/s.csv", stringsAsFactors = F)
tz = read.csv("~/Desktop/WordAssocData/tz.csv", stringsAsFactors = F)

# Some of the header names have the Target Frequency column
#  named "TFR" and others have it incorrectly as "TRF".
#  Let's correct that so we can rbind() them together.
names(c) = names(ab) # ab has correct names
names(df) = names(ab)
names(gk) = names(ab)
names(lo) = names(ab)
names(pr) = names(ab)
names(s) = names(ab)
names(tz) = names(ab)

# Bind all the datasets into one
word.associations = rbind(ab, c, df, gk, lo, pr, s, tz)

# Remove unused columns
word.associations = word.associations[, c(1,2,6,7,18,27)]

# Let P(d|h) = P(hint | word) = P(target | cue), which we'll call ptc.
# Proportional to FSG

# ^ turn FSGs into probabilities by normalizing:
word.associations$ptc = 0

for (row in 1:length(word.associations$CUE)) {
  cue = word.associations$CUE[row]
  fsg.sum = sum(word.associations[which(word.associations$CUE == cue), 3])
  fsg = word.associations$FSG[row]
  
  word.associations$ptc[row] = fsg / fsg.sum
}

# Export this dataframe to a CSV
write.csv(word.associations, "~/Desktop/WordAssocData/word.associations.csv")

