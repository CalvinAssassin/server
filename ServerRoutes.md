# Server Routes

# Profile Operations

  /profile/create (POST)
    - First name
    - Last name
    - Residence
    - Picture
    - GameID

    Returns:
    - UserID

  /profile/query (GET)
    - UserID


# Game Operations

  /game/list
    Returns:
      - [
          - GameID
          - GameName
          - StartDate
          - inPlay
          - Players
        ]

  /game/join
    - PlayerID

    Returns:
      - Success
