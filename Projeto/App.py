import time, json, msgpack
import numpy as np
from faker import Faker
import random
from Owner import Owner
from Pet import Pet


def generateData(nOwners, maxPetsPerOwner):

    fake = Faker()
    species = [
        "Bearded Dragon",
        "Bird",
        "Burro",
        "Cat",
        "Chameleon",
        "Chicken",
        "Chinchilla",
        "Chinese Water Dragon",
        "Cow",
        "Dog",
        "Donkey",
        "Duck",
        "Ferret",
        "Fish",
        "Gecko",
        "Goose",
        "Gerbil",
        "Goat",
        "Guinea Fowl",
        "Guinea Pig",
        "Hamster",
        "Hedgehog",
        "Horse",
        "Iguana",
        "Llama",
        "Lizard",
        "Mice",
        "Mule",
        "Peafowl",
        "Pig",
        "Pigeon",
        "Ponie",
        "Pot Bellied Pig",
        "Rabbit",
        "Rat",
        "Sheep",
        "Skink",
        "Snake",
        "Stick Insect",
        "Sugar Glider",
        "Tarantula",
        "Turkey",
        "Turtle",
    ]
    data = []
    idOwner = 0
    idPet = 0

    for i in range(nOwners):
        newOwner = Owner(
            idOwner,
            fake.name_nonbinary(),
            fake.date(),
            fake.phone_number(),
            fake.address(),
            [],
        )
        for i in range(random.randint(1, maxPetsPerOwner)):
            newPet = Pet(
                idPet,
                fake.first_name(),
                random.choice(species),
                random.choice(["Male", "Female", "Other"]),
                random.randint(1, 20),
                fake.date(),
                fake.text(max_nb_chars=random.randint(150, 500)),
                idOwner,
            )
            newOwner.pets.append(vars(newPet))
            idPet += 1

        data.append(vars(newOwner))
        idOwner += 1

    return data


def getJsonWriteTime(data, datasetNumber, testNumber):

    with open("results.txt", "a") as f:
        with open("temp.json", "w") as temp:

            start = time.perf_counter()

            json.dump(data, temp, indent=4)

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


def getMPackWriteTime(data, datasetNumber, testNumber):

    with open("results.txt", "a") as f:
        with open("temp.msgpack", "wb") as temp:

            start = time.perf_counter()

            packed = msgpack.packb(data)
            temp.write(packed)

            elapsed = time.perf_counter() - start
            f.write(f"W MPck\t{datasetNumber}\t{testNumber}\t{elapsed:.7f}\n")

    return elapsed


def getMPackReadTime(datasetNumber, testNumber):

    with open("results.txt", "a") as f:
        with open("temp.msgpack", "rb") as temp:

            start = time.perf_counter()

            byte_data = temp.read()
            msgpack.unpackb(byte_data)

            elapsed = time.perf_counter() - start

            f.write(f"R MPck\t{datasetNumber}\t{testNumber}\t{elapsed:.7f}\n")

    return elapsed


def main():

    # pet = Pet(1, "Kiko", "Cat", "Male", 5, "Sept2018", "Gato do Ze", 1)
    # owner = Owner(1, "Jospy", "Ago2000", "912345678", "AvenidaEM", [pet])

    with open("stats.txt", "a") as f:

        # Correr isto bué vezes tipo num while qq coisa
        data = generateData(200, 10)

        JsonWriteRes = []
        JsonReadRes = []
        MPackWriteRes = []
        MPackReadRes = []

        # Testar várias vezes para o mesmo dataset
        for i in range(1, 11):

            # De notar que este 1 seria o counter do while
            JsonWriteRes.append(getJsonWriteTime(data, 1, i))
            JsonReadRes.append(getJsonLoadTime(1, i))

            MPackWriteRes.append(getMPackWriteTime(data, 1, i))
            MPackReadRes.append(getMPackReadTime(1, i))

        # Estes 1s também
        f.write(f"Avg W Json time for Dataset 1: {np.average(JsonWriteRes):.9f}\n")
        f.write(f"Std W Json time for Dataset 1: {np.std(JsonWriteRes):.9f}\n")

        f.write(f"Avg R Json time for Dataset 1: {np.average(JsonReadRes):.9f}\n")
        f.write(f"Std R Json time for Dataset 1: {np.std(JsonReadRes):.9f}\n")

        f.write(f"Avg W MPck time for Dataset 1: {np.average(MPackWriteRes):.9f}\n")
        f.write(f"Std W MPck time for Dataset 1: {np.std(MPackWriteRes):.9f}\n")

        f.write(f"Avg R MPck time for Dataset 1: {np.average(MPackReadRes):.9f}\n")
        f.write(f"Std R MPck time for Dataset 1: {np.std(MPackReadRes):.9f}\n")

        f.write("\n\n")


if __name__ == "__main__":
    main()
