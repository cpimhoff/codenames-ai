
# load associations file
filePath = ARGV[0]
contents = File.open(filePath, 'rb') { |f| f.read }

# count times word was associated to
wordCounts = Hash.new(0)	# init counter
contents.lines.each do |line|
	columns = line.split(",")
	word = columns[1]
	wordCounts[word] += 1
end

# grab keys (words) that have many hints
result = []
cutoff = 24
wordCounts.each do |key, value|
	if value > cutoff
		trimmedKey = key.delete "\" "
		result << trimmedKey
	end
end

result.sort!
result.each {|e| puts e}

puts "---"
puts "#{result.length} results found"