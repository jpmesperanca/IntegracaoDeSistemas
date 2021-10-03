class Pet:
    def __init__(self, id, name, species, gender, weight, birthdate, description, ownerId):
        self.id = id
        self.name = name
        self.species = species
        self.gender = gender
        self.weight = weight
        self.birthdate = birthdate
        self.description = description
        self.ownerId = ownerId

    def printId(pet):
        print(f"Hey, my name is {pet.name}, I'm a {pet.species}, my pet id is {pet.id}!")
