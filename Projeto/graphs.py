import numpy as np
from matplotlib import pyplot as plt


def main():
    filename="./stats.txt"
    
    with open(filename) as f:
        text = f.read()
        
    sets = text.split("\n\n")
    
    
    nOwnersList = []
    JSONAvgWList = []
    
    for sete in sets:
        sete = sete.split("\n")
        nOwners = sete[0].split(" ")[2]
        nOwnersList.append(nOwners)
        
        JSONAvgW = sete[1].split(" ")[4]
        JSONAvgWList.append(JSONAvgW)
        
        JSONStdW = sete[2].split(" ")[4]
        JSONStdWList.append(JSONStdW)

        
        

if __name__ == "__main__":
    main()
    
