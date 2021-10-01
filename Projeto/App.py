from Owner import Owner
from Pet import Pet


def main():
    pet = Pet(1, "Kiko", "Cat", "Male", 5, "Sept2018", "Gato do Zé")
    owner = Owner(1, "Jospy", "Ago2000", "912345678", "AvenidaEM")

    pet.printId()
    owner.printId()


if __name__ == "__main__":
    main()
