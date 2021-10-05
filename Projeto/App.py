import time, json, msgpack, random
import numpy as np
from faker import Faker
from Owner import Owner
from Pet import Pet


def generateData(nSet, nOwners, maxPetsPerOwner):

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

    owners = []
    pets = []

    idPet = 0

    for ownerId in range(nOwners):
        newOwner = Owner(
            ownerId,
            fake.name_nonbinary(),
            fake.date(),
            fake.phone_number(),
            fake.address(),
        )
        owners.append(vars(newOwner))

        for i in range(random.randint(1, maxPetsPerOwner)):
            newPet = Pet(
                idPet,
                fake.first_name(),
                random.choice(species),
                random.choice(["Male", "Female", "Other"]),
                random.randint(1, 20),
                fake.date(),
                fake.text(max_nb_chars=random.randint(150, 500)),
                ownerId,
            )
            pets.append(vars(newPet))
            idPet += 1

    return {"pets": pets, "owners": owners}


def getJsonWriteTime(data, nSetNumber, testNumber):

    with open("results.txt", "a") as f:
        with open("temp.json", "w") as temp:

            start = time.perf_counter()

            json.dump(data, temp)

            elapsed = time.perf_counter() - start
            f.write(f"W Json\t{nSetNumber}\t{testNumber}\t{elapsed:.7f}\n")

    return elapsed


def getJsonLoadTime(nSetNumber, testNumber):

    with open("results.txt", "a") as f:
        with open("temp.json", "r") as temp:

            start = time.perf_counter()

            json.load(temp)

            elapsed = time.perf_counter() - start
            f.write(f"R Json\t{nSetNumber}\t{testNumber}\t{elapsed:.7f}\n")

    return elapsed


def getMPackWriteTime(data, nSetNumber, testNumber):

    with open("results.txt", "a") as f:
        with open("temp.msgpack", "wb") as temp:

            start = time.perf_counter()

            packed = msgpack.packb(data)
            temp.write(packed)

            elapsed = time.perf_counter() - start
            f.write(f"W MPck\t{nSetNumber}\t{testNumber}\t{elapsed:.7f}\n")

    return elapsed


def getMPackReadTime(nSetNumber, testNumber):

    with open("results.txt", "a") as f:
        with open("temp.msgpack", "rb") as temp:

            start = time.perf_counter()

            byte_data = temp.read()
            msgpack.unpackb(byte_data)

            elapsed = time.perf_counter() - start

            f.write(f"R MPck\t{nSetNumber}\t{testNumber}\t{elapsed:.7f}\n")

    return elapsed


def main():

    ownersMultiplier = 100
    maxPetsPerOwner = 10

    for nSet in range(1, 101):

        totalOwners = nSet * ownersMultiplier

        data = generateData(nSet, totalOwners, maxPetsPerOwner)

        JsonWRes = []
        JsonRRes = []
        MPackWRes = []
        MPackRRes = []

        # Testar várias vezes para o mesmo set para diminuir a variância
        for i in range(1, 11):

            JsonWRes.append(getJsonWriteTime(data, nSet, i))
            JsonRRes.append(getJsonLoadTime(nSet, i))

            MPackWRes.append(getMPackWriteTime(data, nSet, i))
            MPackRRes.append(getMPackReadTime(nSet, i))

        with open("stats.txt", "a") as f:

            f.write(f"Set {nSet}: {totalOwners} Owners and {maxPetsPerOwner} Pets\n")

            f.write(f"Avg W Json time: {np.average(JsonWRes):.9f}\n")
            f.write(f"Std W Json time: {np.std(JsonWRes):.9f}\n")

            f.write(f"Avg R Json time: {np.average(JsonRRes):.9f}\n")
            f.write(f"Std R Json time: {np.std(JsonRRes):.9f}\n")

            f.write(f"Avg W MPck time: {np.average(MPackWRes):.9f}\n")
            f.write(f"Std W MPck time: {np.std(MPackWRes):.9f}\n")

            f.write(f"Avg R MPck time: {np.average(MPackRRes):.9f}\n")
            f.write(f"Std R MPck time: {np.std(MPackRRes):.9f}\n")

            f.write("\n")


if __name__ == "__main__":
    main()
