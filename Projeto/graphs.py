import numpy as np
from matplotlib import pyplot as plt


def main():
    filename="./stats.txt"
    
    with open(filename) as f:
        text = f.read()
        
    sets = text.split("\n\n")
    
    
    nOwnersList = []
    JSONAvgWList = []
    JSONStdWList = []
    JSONAvgRList = []
    JSONStdRList = []
    
    MPackAvgWList = []
    MPackStdWList = []
    MPackAvgRList = []
    MPackStdRList = []
    
    
    for sete in sets:
        sete = sete.split("\n")
        nOwners = sete[0].split(" ")[2]
        nOwnersList.append(float(nOwners))
        
        JSONAvgW = sete[1].split(" ")[4]
        JSONAvgWList.append(float(JSONAvgW))
        
        JSONStdW = sete[2].split(" ")[4]
        JSONStdWList.append(float(JSONStdW))
        
        JSONAvgR = sete[3].split(" ")[4]
        JSONAvgRList.append(float(JSONAvgR))
        
        JSONStdR = sete[4].split(" ")[4]
        JSONStdRList.append(float(JSONStdR))
        
        MPackAvgW = sete[5].split(" ")[4]
        MPackAvgWList.append(float(MPackAvgW))
        
        MPackStdW = sete[6].split(" ")[4]
        MPackStdWList.append(float(MPackStdW))
        
        MPackAvgR = sete[7].split(" ")[4]
        MPackAvgRList.append(float(MPackAvgR))
        
        MPackStdR = sete[8].split(" ")[4]
        MPackStdRList.append(float(MPackStdR))
        
        

    plt.plot(JSONAvgWList)
    plt.legend()
    plt.show()
            
        

if __name__ == "__main__":
    main()
    
