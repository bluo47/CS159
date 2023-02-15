import re

f = open("assignment1_resources/normaloutput4.txt", encoding="utf8")

file = f.read()

paragraphs = file.split("\n\n")

#print(len(paragraphs))

sentences = []
#print(len([""]))

str = "hello, my name is"
#print(str.split(" "))

count = 0
for paragraph in paragraphs:
    sentences = re.split(r"[\.\?\!]", paragraph)
    for sentence in sentences:
        words = re.split(r" ", sentence)
        if re.match(r"[a-z].*", words[0]):
            count -= 1
        if len(words[-1]) <= 3 and re.match(r"[A-Z].*", words[-1]):
            count -= 1
        if len(words) == 1:
            count -= 1
totalWords = file.split(" ")
print(len(totalWords))
for paragraph in paragraphs:
    count += len(sentences)

wordDict = {}
for word in totalWords:
    lower = word.lower()
    if lower in wordDict:
        wordDict[lower] = wordDict[lower] + 1
    else:
        wordDict[lower] = 1

print(len(wordDict))

sortedDict = dict(sorted(wordDict.items(), key=lambda item: item[1]))
# print(sortedDict)
lowFreq = 0
for i in wordDict:
    if wordDict[i] == 1:
        lowFreq += 1

# print(lowFreq)

longWord = 0
for i in wordDict:
    if len(i) >= 10:
        # print(len(i))
        # print(i + "\n")
        longWord += 1

# print(longWord)

print(wordDict["ben"])
# print(count)
# print(sentences)
# print(len(sentences))
# numLines = 0
# myarray = []
# myarray = f.split("\n\n")
# for line in myarray:
#     numLines += 1
# def split_on_empty_lines(s):

#     # greedily match 2 or more new-lines
#     blank_line_regex = r"(?:\r?\n){2,}"

#     return re.split(blank_line_regex, s.strip())

# s = """

# hello
# world

# this is







# a test

# """

# print(split_on_empty_lines(f))