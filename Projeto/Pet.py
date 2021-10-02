class Pet:
    def __init__(self, id, name, species, gender, weight, birthdate, description):
        self.id = id
        self.name = name
        self.species = species
        self.gender = gender
        self.weight = weight
        self.birthdate = birthdate
        self.description = description

    def printId(pet):
        print(f"Hey, my pet id is {pet.id}!")
