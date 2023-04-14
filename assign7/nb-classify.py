import math

training_data = "assign7/data/movies.data"
pos = {"<COUNT>" : 0}
neg = {"<COUNT>" : 0}
numlabelp = 0
numlabeln = 0
vocabcount = 0
td = open(training_data, encoding='utf-8')

while True:
    line = td.readline()
    # split by whitespace
    splitLine = line.split()

    if not line:
        break

    # retrieve the label "positive" or "negative" from the line
    label = splitLine[0]
    splitLine.remove(label)

    if label == "positive":
        numlabelp += 1
        for word in splitLine:
            if word in pos:
                pos[word] += 1
            else:
                pos[word] = 1
                vocabcount += 1
            pos["<COUNT>"] += 1
    else:
        numlabeln += 1
        for word in splitLine:
            if word in neg:
                neg[word] += 1
            else:
                neg[word] = 1
                vocabcount += 1
            neg["<COUNT>"] += 1

sen = {"<COUNT>" : 0}

ppos = numlabelp / (numlabelp + numlabeln)
pneg = 1 - ppos
lamb = 0

def inputSentence(sentence):
    sentenceSplit = sentence.split()
    possum = 0
    negsum = 0
    
    # calculate the sum
    for word in sentenceSplit:
        if word in neg and word in pos:
            negsum += math.log10((neg[word] + lamb) / (neg["<COUNT>"] + lamb * vocabcount))
            possum += math.log10((pos[word] + lamb)/ (pos["<COUNT>"] + lamb * vocabcount))

    probability_pos = math.log10(ppos) + possum
    probability_neg = math.log10(pneg) + negsum

    # QUESTION 1
    if probability_pos >= probability_neg:
        print(sentence)
        print("positive\t{}".format(probability_pos))
        print()
    else:
        print(sentence)
        print("negative\t{}".format(probability_neg))
        print()

# inputSentence("i loved it")
# inputSentence("i thought i hated it but loved it")

# for word in sentence:
#     if word in sen:
#         sen[word] += 1
#     else:
#         sen[word] = 1
#     sen["<COUNT>"] += 1

# print("\n")
# # QUESTION 2
# print("p(negative): {} | p(positive): {}".format(pneg,ppos))
# print()
# for word in pos:
#     if not word == "<COUNT>":
#         print("p({} | positive) = {}".format(word,pos[word]/pos["<COUNT>"]))
# print()
# for word in neg:
#     if not word == "<COUNT>":
#         print("p({} | negative) = {}".format(word,neg[word]/neg["<COUNT>"]))

# QUESTION 3
# bestword = {}
# for word in pos:
#     if word in neg and not word == "<COUNT>":
#         bestword[word] = pos[word] / neg[word]

# bestwordp = sorted(bestword.items(), key=lambda x:x[1], reverse=True)
# bestwordn = sorted(bestword.items(), key=lambda x:x[1])

# for i in range(10):
#     print("word: {}\t\tpos/neg ratio: {}".format(bestwordp[i][0], bestwordp[i][1]))
# print()

# for i in range(10):
#     print("word: {}\t\tpos/neg ratio: {}".format(bestwordn[i][0], bestwordn[i][1]))

# QUESTION 4
# inputSentence("i hate this movie so much the movie should kill itself")
# inputSentence("i wish i spent my money elsewhere")
# inputSentence("this movie has the best actors ever")
# inputSentence("this movie earns five stars")
# inputSentence("i like cotton candy and this movie too")

td.close()