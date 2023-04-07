# import packages
import re
import math
import sys

uniquewords = 0
wordcount = 0
linecount = 0
stoplist = []

dfdict = {}
tfdict = {}
tfidfdict = {}

# input file 
f = open("data/sentences", encoding='utf-8')
# f = open(sys.argv[2], encoding='utf-8')
# stop list
s = open("data/stoplist", encoding='utf-8')
# s = open(sys.argv[1], encoding='utf-8')

# while loop ADD STOPLIST WORDS TO STOPLIST ARRAY VAR
while True:
    line = s.readline()
    # splits into word and ""
    splitLine = line.split("\n")

    if not line:
        break
    
    # take first elt of splitLine (the word we want)
    stoplist.append(splitLine[0])
s.close()

#PREPROCESS THINGS - BEFORE CONTEXT GATHERING
# param line - given line string from f.readLine()
def preprocess(line):
    '''Given a line, returns an array of elts in the line that are purely based of alphabetical characters.'''
    global wordcount
    # split line into words by whitespace
    words = line.split(" ")
    lowerWords = []
    # converting words to lowercase
    for word in words:
        lword = word.lower()
    
        if re.match(r"^[a-z]+$", word) and word not in stoplist:
            lowerWords.append(lword)
            wordcount += 1     

    return lowerWords

def calcTF(word, lineArr, index):
    '''Calculates the term frequency of a given word'''
    # create context list
    if word not in tfdict:
        tfdict[word] = {}

    #check if this is the first occurrence of the word
    if index == lineArr.index(word):
        # add to the document frequency dictionary
        if word not in dfdict:
            dfdict[word] = 1
        else: 
            dfdict[word] += 1

    # find context to the left of word
    curIndex = index - 1
    leftContext = 0
    while curIndex >= 0 and leftContext < 2:
        if lineArr[curIndex] in tfdict[word]:
            tfdict[word][lineArr[curIndex]] = tfdict[word][lineArr[curIndex]] + 1
        else:
            tfdict[word][lineArr[curIndex]] = 1
        curIndex -= 1
        leftContext += 1

    
    # find context to the right of word
    curIndex = index + 1
    rightContext = 0

    while curIndex < len(lineArr) and rightContext < 2:
        if lineArr[curIndex] in tfdict[word]:
            tfdict[word][lineArr[curIndex]] = tfdict[word][lineArr[curIndex]] + 1
        else:
            tfdict[word][lineArr[curIndex]] = 1
        curIndex += 1
        rightContext += 1


def calcIDF(N, w1):
    '''Calculates the inverse document frequency.'''
    return math.log10(N / dfdict[w1])
    

def calcTFIDF(N):
    '''Calculates the TF-IDF.'''
    for outerkey in tfdict:
        for innerkey in tfdict[outerkey]:
            if outerkey in tfidfdict:
                tfidfdict[outerkey][innerkey] = tfdict[outerkey][innerkey] * calcIDF(N, innerkey)
            else:
                tfidfdict[outerkey] = {innerkey : tfdict[outerkey][innerkey] * calcIDF(N, innerkey)}
    


def calcL1(w1dict, w2dict):
    '''Calculates the result of the L1 similarity function given two words'''
    length = 0
    for key in w1dict:
        # case where key in both
        if key in w2dict:
            length += abs(w1dict[key] - w2dict[key])
        #case where key in first dict only
        else:
            length += w1dict[key]
    #case where key in second dict only
    for key in w2dict:
        if key not in w1dict:
            length += w2dict[key]

    return length

def calcL2(w1dict, w2dict):
    '''Calculates the result of the L2 (Euclidean) similarity function given two words'''
    length = 0
    for key in w1dict:
        # case where key in both
        if key in w2dict:
            length += (w1dict[key] - w2dict[key]) * (w1dict[key] - w2dict[key])
        #case where key in first dict only
        else:
            length += w1dict[key] * w1dict[key]
    #case where key in second dict only
    for key in w2dict:
        if key not in w1dict:
            length += w2dict[key] * w2dict[key]
    
    length = math.sqrt(length)
    return length

def calcCosine(w1dict, w2dict):
    '''Calculates the result of the Cosine similarity function given two words'''
    numerator = 0
    adenom = 0
    bdenom = 0
    for key in w1dict:
        adenom += w1dict[key] * w1dict[key]
        if key in w2dict:
            numerator += w1dict[key] * w2dict[key]
  
    #case where key in second dict only
    for key in w2dict:
            bdenom += w2dict[key] * w2dict[key]
    
    adenom = math.sqrt(adenom)
    bdenom = math.sqrt(bdenom)
    length = numerator / (adenom * bdenom)
    return length

def normalize(wdict):
    '''Conducts length normalization for a given word vector.'''
    length = 0
    normalized = {}
    for key in wdict:
        length += wdict[key] * wdict[key]

    length = math.sqrt(length)

    for key in wdict:
        normalized[key] = wdict[key] / length

    return normalized

# Loop that goes thru the whole input file
while True:
    line = f.readline()
    
    if not line:
        break

    linecount += 1

    lineArray = preprocess(line)

    for i in range(len(lineArray)):
        calcTF(lineArray[i], lineArray, i)

    processedline = ""
    for word in lineArray:
        processedline += word + " "
    

# Print num unique words, num word occurrences, and num sentences/lines
print("The number of unique words is: {}\nThe number of word occurrences: {}\nThe number of lines is: {}".format(len(tfdict),wordcount,linecount))
print()

thechosenword = "dog"
weightmode = "TFIDF"
distmode = "COSINE"

distances = []


match weightmode:
    case "TF":
        for key in tfdict:
            tfdict[key] = normalize(tfdict[key])
        
        match distmode:
            case "L1":
                for key in tfdict:
                    if key != thechosenword:
                        distances.append((calcL1(tfdict[thechosenword], tfdict[key]), key))                
            case "EUCLIDEAN":
                for key in tfdict:
                    if key != thechosenword:
                        distances.append((calcL2(tfdict[thechosenword], tfdict[key]), key))
            case "COSINE":
                
                for key in tfdict:
                    if key != thechosenword and tfdict[key] != {}:
                        distances.append((calcCosine(tfdict[thechosenword], tfdict[key]), key))
            case _:
                print("Invalid distance mode.")
        
                    
    case "TFIDF":
        calcTFIDF(linecount)
        for key in tfidfdict:
            tfdict[key] = normalize(tfdict[key])
            tfidfdict[key] = normalize(tfidfdict[key])
        
        match distmode:
            case "L1":
                for key in tfidfdict:
                    if key != thechosenword:
                        distances.append((calcL1(tfidfdict[thechosenword], tfidfdict[key]), key))                
            case "EUCLIDEAN":
                for key in tfidfdict:
                    if key != thechosenword:
                        distances.append((calcL2(tfidfdict[thechosenword], tfidfdict[key]), key))
            case "COSINE":
                for key in tfidfdict:
                    if key != thechosenword:
                        distances.append((calcCosine(tfidfdict[thechosenword], tfidfdict[key]), key))
            case _:
                print("Invalid distance mode.")
    case _:
        print("Invalid weighting measure.")



print("SIM: {}\t{}\t{}\n".format(thechosenword, weightmode, distmode))

if distmode == "COSINE":
    distances = sorted(distances, reverse=True)
else:
    distances = sorted(distances)
for i in range(10):
    print("{} \t {}".format(distances[i][1], distances[i][0]))