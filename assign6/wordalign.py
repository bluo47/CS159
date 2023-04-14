english_sentences = "assign6/data/class_test.en"
foreign_sentences = "assign6/data/class_test.es"
iterations = 1
probability_threshold = 0.0



prob = {}

# open english sentences 
eng_sent = open(english_sentences, encoding='utf-8')

# open foreign sentences
forn_sent = open(foreign_sentences, encoding='utf-8')

for i in range(iterations):
    count = {}
    while True:
        eline = eng_sent.readline()
        fline = forn_sent.readline()
        
        if not eline:
            break

        englishSplitLine = eline.split()
        foreignSplitLine = fline.split()

        
        # array of dictionaries so each iteration can calculate pairs seperately!
        # pairs[i] = {}
        # adding a dictionary into the array every iteration
        
        for e in englishSplitLine:
            for f in foreignSplitLine:
                if not f in prob:
                    # if the given f word is not in prob, add it
                    prob[f] = {}
                    if not e in prob[f]:
                        prob[f][e] = 0.01


        for e in englishSplitLine:
            pairs = {}
            for f in foreignSplitLine:
                # check if f in pairs
                # if it is, ive already calculated the sum, just say pair_demoninator = pairs[f]
                # 
                if f in pairs:
                    pair_denominator = pairs[f]

                # temp_prob = prob
                else:
                # calculate the denominator of the pair
                    pair_denominator = 0.0
                    for e2 in englishSplitLine:
                        pair_denominator += prob[f][e2]
                    
                    if not f in pairs:
                        pairs[f] = {}

                    pairs[f] = pair_denominator

                if not e in count:
                    count[e] = {"<COUNT>" : 0}
                if not f in count[e]:
                    count[e][f] = 0

                count[e][f] += prob[f][e]/pairs[f]
                count[e]["<COUNT>"] += prob[f][e]/pairs[f]
        
        for e in englishSplitLine:
            for f in foreignSplitLine:
                prob[f][e] = count[e][f] / count[e]["<COUNT>"]
    # print(pairs)
    print(count)
    # print(prob)


# THE ALGORITHM
# -------------
# for (E,F) in corpus:
    # for e in E:
        # for f in F:
            # p(f -> e) = p(f|e) / [p(f|e1) + p(f|e2) + ... + p(f|e_last)]
            # count(e,f) += p(f -> e)
            # count(e) += p(f -> e)
# -------------
    
eng_sent.close()
forn_sent.close()
