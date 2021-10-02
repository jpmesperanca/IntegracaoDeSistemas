import time, json
from Owner import Owner
from Pet import Pet


def generateData():
    pass


def getJsonWriteTime(owner, datasetNumber, testNumber):

    with open("results.txt", "a") as f:
        with open("temp.txt", "w") as temp:

            start = time.perf_counter()

            json.dump(owner, temp, indent=4, default=vars)

            elapsed = time.perf_counter() - start
            f.write(f"W Json\t{datasetNumber}\t{testNumber}\t{elapsed:.7f}\n")


def getJsonLoadTime(datasetNumber, testNumber):

    with open("results.txt", "a") as f:
        with open("temp.txt", "r") as temp:

            start = time.perf_counter()

            json.load(temp)

            elapsed = time.perf_counter() - start
            f.write(f"R Json\t{datasetNumber}\t{testNumber}\t{elapsed:.7f}\n")


def getMPackWriteTime(owner, datasetNumber, testNumber):
    pass


def getMPackReadTime(datasetNumber, testNumber):
    pass


def main():

    pet = Pet(1, "Kiko", "Cat", "Male", 5, "Sept2018", "Gato do Ze")
    owner = Owner(1, "Jospy", "Ago2000", "912345678", "AvenidaEM", [pet])

    # Temos de ver como geramos os dados e que caracteristicas queremos que tenham
    # E correr isto bué vezes tipo num while qq coisa
    generateData()

    # Testar várias vezes para o mesmo dataset
    for i in range(10):
        # De notar que este 1 seria o counter do while
        getJsonWriteTime(owner, 1, i)
        getJsonLoadTime(1, i)

        getMPackWriteTime(owner, 1, i)
        getMPackReadTime(1, i)


if __name__ == "__main__":
    main()
