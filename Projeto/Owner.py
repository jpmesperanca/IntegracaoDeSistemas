class Owner:
    def __init__(self, id, name, birthdate, phone, address, pets):
        self.id = id
        self.name = name
        self.birthdate = birthdate
        self.phone = phone
        self.address = address
        self.pets = pets

    def printId(owner):
        print(f"Hey, my owner id is {owner.id}!")
