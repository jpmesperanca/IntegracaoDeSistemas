import time, json
import numpy as np
from Owner import Owner
from Pet import Pet


def generateData():
    pass


def getJsonWriteTime(owner, datasetNumber, testNumber):

    with open("results.txt", "a") as f:
        with open("temp.json", "w") as temp:

            start = time.perf_counter()

            json.dump(owner, temp, indent=4, default=vars)

            elapsed = time.perf_counter() - start
            f.write(f"W Json\t{datasetNumber}\t{testNumber}\t{elapsed:.7f}\n")

    return elapsed


def getJsonLoadTime(datasetNumber, testNumber):

    with open("results.txt", "a") as f:
        with open("temp.json", "r") as temp:

            start = time.perf_counter()

            json.load(temp)

            elapsed = time.perf_counter() - start
            f.write(f"R Json\t{datasetNumber}\t{testNumber}\t{elapsed:.7f}\n")

    return elapsed


def getMPackWriteTime(owner, datasetNumber, testNumber):
    pass


def getMPackReadTime(datasetNumber, testNumber):
    pass


def main():

    pet = Pet(1, "Kiko", "Cat", "Male", 5, "Sept2018", "Gato do Ze")
    owner = Owner(1, "Jospy", "Ago2000", "912345678", "AvenidaEM", [pet])

    with open("stats.txt", "a") as f:

        # Correr isto bué vezes tipo num while qq coisa
        generateData()

        JsonWriteRes = []
        JsonReadRes = []
        MPackWriteRes = []
        MPackReadRes = []

        # Testar várias vezes para o mesmo dataset
        for i in range(1, 11):

            # De notar que este 1 seria o counter do while
            JsonWriteRes.append(getJsonWriteTime(owner, 1, i))
            JsonReadRes.append(getJsonLoadTime(1, i))

            MPackWriteRes.append(getMPackWriteTime(owner, 1, i))
            MPackReadRes.append(getMPackReadTime(1, i))

        # Estes 1s também
        f.write(f"Avg W Json time for Dataset 1: {np.average(JsonWriteRes):.9f}\n")
        f.write(f"Std W Json time for Dataset 1: {np.std(JsonWriteRes):.9f}\n")

        f.write(f"Avg R Json time for Dataset 1: {np.average(JsonReadRes):.9f}\n")
        f.write(f"Std R Json time for Dataset 1: {np.std(JsonReadRes):.9f}\n")

        # f.write(f"Avg W MPack time for Dataset 1: {np.average(MPackWriteRes)}\n")
        # f.write(f"Std W MPack time for Dataset 1: {np.std(MPackWriteRes)}\n")

        # f.write(f"Avg R MPack time for Dataset 1: {np.average(MPackReadRes)}\n")
        # f.write(f"Std R MPack time for Dataset 1: {np.std(MPackReadRes)}\n")

        f.write("\n\n")


if __name__ == "__main__":
    main()
