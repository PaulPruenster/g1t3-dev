import random


def read_name() -> str:
    """
    Read name from command line.
    :return: Name entered by user.
    """
    return input('Enter your name: ')


def roll_dice() -> int:
    """
    Roll a die and return its value.
    :return: The roll of the die.
    """
    return random.randint(1, 6)


if __name__ == '__main__':
    name = read_name()
    print(f"Hello {name}!")

    dice = roll_dice()
    print(f'You rolled a {dice}')
